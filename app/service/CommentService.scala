package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import utils.JsonFormat._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class CommentService @Inject() (commentDao: CommentDao, uRLDao: URLDao, userDao: UserDao, userMailboxService: UserMailboxService) {
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]) = {
    for {
      id <- commentDao.create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
      _ <- uRLDao.comment_count(url_id)
      _ <- userDao.comment_count(user_id)

      // send message to user
      comment <- commentDao.get(id)
      url <- uRLDao.get(url_id)
      (to_user_id, message_type) = at_user_id match {
        case Some(u) => (u, 2)
        case None => (url.owner_id, 1)
      }
      user <- userDao.get(user_id)
      _ <- userMailboxService.create(to_user_id, message_type, Json.stringify(Json.toJson(CommentWithUrl(comment, url, user))))
    } yield id
  }

  def list(user_id: Long) = commentDao.list(user_id).map(_.sortWith(_.comment.id > _.comment.id))
}
