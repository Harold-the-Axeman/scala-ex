package com.getgua.daos

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import com.getgua.models._

/**
  * Created by kailili on 6/20/15.
  */

@Singleton
class CommentLikeDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def add(user_id: Long, comment_id: Long) = {
    val query = CommentLikeTable.map(c => (c.user_id, c.comment_id)) += (user_id, comment_id)

    db.run(query)
  }

  def delete(user_id: Long, comment_id: Long) = {
    val query = CommentLikeTable.filter(c => (c.user_id === user_id && c.comment_id === comment_id)).delete

    db.run(query)
  }

  def list(comment_id: Long) = {
    val query = (for(
        c <- CommentLikeTable if c.comment_id === comment_id;
        user <- UserTable if user.id === c.user_id
    ) yield user ).result

    db.run(query)
  }

  def comment_list(user_id: Long): Future[Seq[Long]] = {
    val query = CommentLikeTable.filter(_.user_id === user_id).map(_.comment_id).result

    db.run(query)
  }
}
