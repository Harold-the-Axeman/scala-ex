package dao

import javax.inject.Inject

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 6/3/15.
  */

/**
  *
  * @param dbConfigProvider
  */
class AuthDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  //import slick.driver.MySQLDriver.api._

  /**
    *
    * @param client_id
    * @return
    */
  def uuid_login(client_id: String): Future[Long] = {
    val query = (for {
      //NOTE: auth_type and third_party_id == "", is an anonymous user
      idOpt <- UserTable.filter(u => u.client_id === client_id && u.auth_type === "" && u.third_party_id === "" ).map(_.id).result.headOption
      id <- idOpt match {
        case Some(i) => DBIO.successful(i)
        case None => (UserTable.map(u => (u.client_id)) returning UserTable.map(_.id)) += (client_id)
      }
    } yield id).transactionally

    db.run(query)
  }

  /**
    *
    * @param client_id
    * @param auth_type
    * @param third_party_id
    * @param name
    * @param avatar
    * @return
    */
  def auth_login(client_id: String, auth_type: String, third_party_id: String, name: String, avatar: String): Future[Long] = {
    val query = (for {
      idOpt <- UserTable.filter(u => u.auth_type === auth_type && u.third_party_id === third_party_id).map(_.id).result.headOption
      id <- idOpt match {
          // is a Qidian User, update client_id
        case Some(u) => for {
          _ <- UserTable.filter(u => u.auth_type === auth_type && u.third_party_id === third_party_id).map(_.client_id).update(client_id)
          i <- DBIO.successful(u)
        } yield i
          // a new Qidian User, try to merge with an anonymous user
        case None => for {
          // if client_id already exists,
          idOpt <- UserTable.filter(u => u.client_id === client_id && u.auth_type === "" && u.third_party_id === "" ).map(_.id).result.headOption
          idu: Long <- idOpt match {
            case Some(iu) => for {
              _ <- UserTable.filter(u => u.client_id === client_id && u.auth_type === "" && u.third_party_id === "" ).map(u => (u.auth_type, u.third_party_id)).update((auth_type, third_party_id))
              x <- DBIO.successful(iu)
            } yield x
            case None => (UserTable.map(u => (u.client_id, u.auth_type, u.third_party_id, u.name, u.avatar)) returning UserTable.map(_.id)) += (client_id, auth_type, third_party_id, name, avatar)
          }
        } yield idu
      }
    } yield id).transactionally

    db.run(query)
  }

  /**
    *
    * @param id
    * @return
    */
  def profile(id: Long): Future[Option[User]] = {
    val query = UserTable.filter(_.id === id).result.headOption

    db.run(query)
  }

 /* def list(user_id_list: List[Long]): Future[List[User]] = {

  }*/
}

