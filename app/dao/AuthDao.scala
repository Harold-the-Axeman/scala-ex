package dao

import java.sql.Timestamp
import javax.inject.Inject

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

import play.api.Logger
/**
  * Created by kailili on 6/3/15.
  */

class AuthDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  //import slick.driver.MySQLDriver.api._

  //Deprecated.
  def login(auth_type: String, token: String): Future[Long] = {

    // TODO: Logging Information
    val query = (for {
      idOpt <- UserTable.filter(u => u.auth_type === auth_type && u.token === token).map(_.id).result.headOption
      id <- idOpt match {
        case Some(u) => DBIO.successful(u)
        case None => (UserTable.map(u => (u.auth_type, u.token)) returning UserTable.map(_.id)) += (auth_type, token)
      }
    } yield id).transactionally

    db.run(query)
  }

  def uuid_login(uuid: String): Future[Long] = {
    val query = (for {
      //NOTE: auth_type and token == "", is an anonymous user
      idOpt <- UserTable.filter(u => u.uuid === uuid && u.auth_type === "" && u.token === "" ).map(_.id).result.headOption
      id <- idOpt match {
        case Some(i) => DBIO.successful(i)
        case None => (UserTable.map(u => (u.uuid)) returning UserTable.map(_.id)) += (uuid)
      }
    } yield id).transactionally

    db.run(query)
  }

  //Only return id
  def auth_login(uuid: String, auth_type: String, token: String, name: String, avatar: String): Future[Long] = {
    val query = (for {
      idOpt <- UserTable.filter(u => u.auth_type === auth_type && u.token === token).map(_.id).result.headOption
      id <- idOpt match {
          // is a Qidian User, update uuid
        case Some(u) => for {
          _ <- UserTable.filter(u => u.auth_type === auth_type && u.token === token).map(_.uuid).update(uuid)
          i <- DBIO.successful(u)
        } yield i
          // a new Qidian User, try to merge with an anonymous user
        case None => for {
          // if uuid already exists,
          idOpt <- UserTable.filter(u => u.uuid === uuid && u.auth_type === "" && u.token === "" ).map(_.id).result.headOption
          idu: Long <- idOpt match {
            case Some(iu) => for {
              _ <- UserTable.filter(u => u.uuid === uuid && u.auth_type === "" && u.token === "" ).map(u => (u.auth_type, u.token)).update((auth_type, token))
              x <- DBIO.successful(iu)
            } yield x
            case None => (UserTable.map(u => (u.uuid, u.auth_type, u.token, u.name, u.avatar)) returning UserTable.map(_.id)) += (uuid, auth_type, token, name, avatar)
          }
        } yield idu
         // (UserTable.map(u => (u.uuid, u.auth_type, u.token)) returning UserTable.map(_.id)) += (uuid, auth_type, token)
      }
    } yield id).transactionally

    db.run(query)
  }

  def profile(uid: Long): Future[Option[User]] = {
    val query = UserTable.filter(_.id === uid).result.headOption

    db.run(query)
  }
}

