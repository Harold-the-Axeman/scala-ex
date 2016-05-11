package dao

import java.sql.Timestamp

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 6/3/15.
  */

case class Auth(name: String, password: String)

case class CreateUser (
                        name: String,
                        password: String,
                        show_name: String,
                        gender: String,
                        age: Int,
                        work: Option[String] = None,
                        hobby: Option[String] = None,
                        confession: Option[String] = None,
                        want_to_do: Option[String] = None
                      )



object AuthDao extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  //import driver.api._
  import slick.driver.MySQLDriver.api._

/*  def create(user: CreateUser):Future[Long] = {
    val createStatement =  (UserTable.map(u => (u.name, u.password, u.show_name, u.gender, u.age, u.work, u.hobby, u.confession, u.want_to_do))
      returning UserTable.map(_.id)) += (user.name, user.password, user.show_name, user.gender, user.age, user.work,
      user.hobby, user.confession, user.want_to_do)

    //TODO: name exist check. actually not need
    /*    val query = (for{
          name <- UserTable.filter(_.name === user.name).map(_.name).result.headOption
          id <- name match {
            case None => createStatement
            case Some(_) => DBIO.successful(-1L)
          }

        } yield id).transactionally*/

    db.run(createStatement)
  }

  def delete(name: String):Future[Int] = {
    //val deleteStatement =
    val deleteStatement = UserTable.filter( _.name === name).delete

    db.run(deleteStatement)
  }


  def password(name: String, password: String): Future[Option[Long]] = {

    val query = (for{
      id <- UserTable.filter(_.name === name).map(_.id).result.headOption
      _ <- id match {
        case Some(id) => UserTable.filter(_.id === id).map(_.password).update(password)
        case None => DBIO.successful()
      }
    } yield id).transactionally

    db.run(query)
  }

  def login(name: String, password: String): Future[Option[User]] = {

    // TODO: Logging Information
    val query = (for {
      user <- UserTable.filter(u => u.name === name && u.password === password).result.headOption
      _ <- user match {
        case Some(u) => UserLoginTable.map(l => (l.user_id, l.`type`)) += (u.id, 0)
        case None => DBIO.successful()
      }
    } yield user).transactionally


    db.run(query)
  }

  def logout(id: Long): Future[Int] = {
    val query = UserLoginTable.map(l => (l.user_id, l.`type`)) += (id, 1)

    db.run(query)
  }


  def exists(name: String): Future[Int] = {
    val query = UserTable.filter(_.name === name).map(_.id).result.headOption

    db.run(query).map( idOption =>
      idOption match {
        case Some(id) => -1
        case None => 0
      }
    )
  }*/


}

