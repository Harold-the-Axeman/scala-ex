package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class CommentService @Inject()(commentDao: CommentDao, uRLDao: URLDao, userDao: UserDao) {
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]) = {
    //TODO: WS , userMailboxService: UserMailboxService, uMengPushService: UMengPushService
    for {
      id <- commentDao.create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
      _ <- uRLDao.comment_count(url_id)
      _ <- userDao.comment_count(user_id)

      // send message to user
   /*   comment <- commentDao.get(id)
      url <- uRLDao.get(url_id)
      user <- userDao.get(user_id)
      (to_user_id, message_type, push_message_type, text_message) = at_user_id match {
        case Some(u) => {
          (u, 2, "user-comment-user", String.format("%s回复了你", user.name))
        }
        case None => (url.owner_id, 1, "user-comment-url", String.format("%s评论了你", user.name))
      }
      data_message = Json.stringify(Json.toJson(CommentUrlUser(comment, url, user)))
      _ <- userMailboxService.create(user_id, to_user_id, message_type, data_message) //if user_id != to_user_id
      _ <- uMengPushService.unicast(to_user_id, text_message, data_message, push_message_type) //if user_id != to_user_id*/
    } yield id
  }

  def list(user_id: Long) = commentDao.list(user_id).map(_.sortWith(_.comment.id > _.comment.id))
}
