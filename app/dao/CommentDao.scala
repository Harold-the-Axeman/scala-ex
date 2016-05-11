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

object CommentDao extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  //import driver.api._
  import slick.driver.MySQLDriver.api._

  def create(url_id: Long, content: String, user: Long, at_user: Option[Long]) = {
    val at:Long = at_user.getOrElse(0) // not use null here
    val query = CommentTable.map(c => (c.url_id, c.content, c.comment_user, c.at_user)) += (url_id, content, user, at)

    db.run(query)
  }

  def list(url_id: Long) = {
    val query = CommentTable.filter(_.url_id === url_id).result

    db.run(query)
  }


}

