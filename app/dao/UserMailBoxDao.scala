package dao

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 6/20/16.
  */



/**
  *
  * @param dbConfigProvider
  */
@Singleton
class UserMailBoxDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  def create(user_id: Long, message_type: Int, message: String) = {
    val query = UserMailboxTable.map(m => (m.user_id, m.message_type, m.message)) += (user_id, message_type, message)

    db.run(query)
  }

  def list(user_id: Long) = {
    val query = UserMailboxTable.filter(_.user_id === user_id).result

    db.run(query)
  }

 /* def delete(user_id: Long) = {
    val query = UserMailboxTable.filter(_.user_id === user_id).delete

    db.run(query)
  }*/
}
