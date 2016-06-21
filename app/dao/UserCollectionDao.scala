package dao

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 20/6/15.
  */

@Singleton
class UserCollectionDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def add(user_id: Long, url: String, title: String) = {
    val query = UserCollectionTable.map(u => (u.user_id, u.url, u.title)) += (user_id, url, title)

    db.run(query)
  }

  def delete(user_id: Long, url: String) = {
    val query = UserCollectionTable.filter(u => (u.user_id === user_id && u.url === url)).delete

    db.run(query)
  }

  def list(user_id: Long) = {
    val query = UserCollectionTable.filter(_.user_id === user_id).result

    db.run(query)
  }
}
