package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import com.getgua.controllers.WSConfig

import scala.concurrent.Future

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class CommentService @Inject()(commentDao: CommentDao, uRLDao: URLDao, userDao: UserDao, wSClient: WSClient, wsConfig: WSConfig) {
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]) = {
    for {
      id <- commentDao.create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
      _ <- uRLDao.comment_count(url_id)
      _ <- userDao.comment_count(user_id)

      // send message to user
      comment <- commentDao.get(id)
      url <- uRLDao.get(url_id)
      user <- userDao.get(user_id)
      (to_user_id,  message_type, text_message) = at_user_id match {
        case Some(u) => {
          (u,  "user-comment-user", String.format("%s回复了你", user.name))
        }
        case None => (url.owner_id,  "user-comment-url", String.format("%s评论了你", user.name))
      }
      data_message = Json.stringify(Json.toJson(CommentUrlUser(comment, url, user)))

      _ <- to_user_id == user_id match {
        case false =>
          val submit = MessageSubmit(to_user_id, user.name, message_type, data_message)
          wSClient.url(wsConfig.ws_url + "/ws/message").post(Json.toJson(submit))
        case true => Future.successful()
      }
      /*_ <- userMailboxService.create(user_id, to_user_id, message_type, data_message) //if user_id != to_user_id
      _ <- uMengPushService.unicast(to_user_id, text_message, data_message, push_message_type) //if user_id != to_user_id*/
    } yield id
  }

  def list(user_id: Long) = commentDao.list(user_id).map(_.sortWith(_.comment.id > _.comment.id))
}
