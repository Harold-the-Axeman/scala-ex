package dao

import java.sql.Timestamp
import javax.inject.Inject

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 6/3/15.
  */

class AuthDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  //protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile]

  import driver.api._
  //import slick.driver.MySQLDriver.api._

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
}

