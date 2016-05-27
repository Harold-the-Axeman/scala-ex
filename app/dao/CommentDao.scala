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

case class CommentWithUrl(comment: Comment, url: Url)

class CommentDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  //import slick.driver.MySQLDriver.api._

  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]):Future[Long] = {
    val at:Long = at_user_id.getOrElse(0) // not use null here
    //val query1 = (CommentTable.map(c => (c.url_id, c.content, c.user_id, c.at_user_id)) returning CommentTable.map(_.id)) += (url_id, content, user_id, at)

    val query = (for {
      id <- (CommentTable.map(c => (c.url_id, c.content, c.user_id, c.at_user_id)) returning CommentTable.map(_.id)) += (url_id, content, user_id, at)
      c <- UrlTable.filter(_.id === url_id).map(_.comment_count).result.head
      - <- UrlTable.filter(_.id === url_id).map(_.comment_count).update(c + 1)
    } yield id).transactionally

    db.run(query)
  }

  def list(user_id: Long):Future[Seq[CommentWithUrl]] = {
    val query = ( for (
      c <- CommentTable if c.user_id === user_id;
      u <- UrlTable if c.url_id === u.id
    ) yield (c, u)).result

    db.run(query).map(l => l.map{
      case (c, u) => CommentWithUrl(c, u)
    })
  }
}

