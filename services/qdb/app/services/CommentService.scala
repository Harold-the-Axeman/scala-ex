package com.getgua.qdb.services

import javax.inject.{Inject, Singleton}

import com.getgua.utils.ws.QidianWebService
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import com.getgua.qdb.daos._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class CommentService @Inject()(commentDao: CommentDao, uRLDao: URLDao, userDao: UserDao, qidianWebService: QidianWebService) {
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]) = {

    for {
      id <- commentDao.create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
      _ <- uRLDao.comment_count(url_id, 1)
      _ <- userDao.comment_count(user_id, 1)

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

      _ <- qidianWebService.sendMessage(user_id, to_user_id, user.name, message_type, data_message)
    } yield id
  }

  def list(user_id: Long) = commentDao.list(user_id).map(_.sortWith(_.comment.id > _.comment.id))

  def delete(comment_id: Long, user_id: Long) = {
   /* for {
      ret <- commentDao.delete(comment_id, user_id)
      url_id <- commentDao.get_url(comment_id).map(_.id)
      _ <- uRLDao.comment_count(url_id, -1)
      _ <- userDao.comment_count(user_id, -1)
    } yield ret*/

    commentDao.delete(comment_id, user_id).map{ ret =>
      if (ret == 1) {
        for {
          url_id <- commentDao.get_url(comment_id).map(_.id)
          _ <- uRLDao.comment_count(url_id, -1)
          _ <- userDao.comment_count(user_id, -1)
        } yield ()
      }
      ret
    }
  }
}
