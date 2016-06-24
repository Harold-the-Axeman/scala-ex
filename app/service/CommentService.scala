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
class  CommentService @Inject() (commentDao: CommentDao, uRLDao: URLDao, userDao: UserDao, userMailboxService: UserMailboxService, uMengPushService: UMengPushService) {
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]) = {
    for {
      id <- commentDao.create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
      _ <- uRLDao.comment_count(url_id)
      _ <- userDao.comment_count(user_id)

      // send message to user
      comment <- commentDao.get(id)
      url <- uRLDao.get(url_id)
      user <- userDao.get(user_id)
      (to_user_id, message_type, push_message_type, text_message) = at_user_id match {
        case Some(u) => (u, 2, "user-comment-user", "$user.name回复了你")
        case None => (url.owner_id, 1, "user-comment-url", "$user.name评论了你")
      }
      data_message = Json.stringify(Json.toJson(CommentWithUrl(comment, url, user)))
      _ <- userMailboxService.create(user_id, to_user_id, message_type, data_message)
      _ <- uMengPushService.remote_unicast(to_user_id, text_message, data_message, push_message_type)
    } yield id
  }

  def list(user_id: Long) = commentDao.list(user_id).map(_.sortWith(_.comment.id > _.comment.id))
}
