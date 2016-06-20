package dao

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 6/3/15.
  */

@Singleton
class CommentDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  //import slick.driver.MySQLDriver.api._

  /**
    *
    * @param url_id
    * @param content
    * @param user_id
    * @param at_user_id
    * @return
    */
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]):Future[Long] = {
    val at:Long = at_user_id.getOrElse(0)

    val query = (CommentTable.map(c => (c.url_id, c.content, c.user_id, c.at_user_id)) returning CommentTable.map(_.id)) += (url_id, content, user_id, at)

    db.run(query)
  }

  def list(user_id: Long):Future[Seq[CommentWithUrl]] = {
    val query = ( for (
      c <- CommentTable if c.user_id === user_id;
      u <- UrlTable if c.url_id === u.id;
      user <- UserTable if user.id === u.owner_id
    ) yield (c, u, user)).result

    db.run(query).map(l => l.map{
      case (c, u, user) => CommentWithUrl(c, u, user)
    })
  }

  def get(id: Long) = {
    val query = CommentTable.filter(_.id === id).result.head

    db.run(query)
  }
}

