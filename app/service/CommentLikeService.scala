package com.getgua.service

import javax.inject.{Inject, Singleton}

import com.getgua.daos._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import com.getgua.utils.JsonFormat._

/**
  * Created by likaili on 20/6/2016.
  */
@Singleton
class CommentLikeService @Inject() (commentLikeDao: CommentLikeDao, commentDao: CommentDao, userMailBoxDao: UserMailBoxDao, userDao: UserDao, uMengPushService: UMengPushService) {
  def add(user_id: Long, comment_id: Long) = {
    for {
      ret <- commentLikeDao.add(user_id, comment_id)
      _ <- commentDao.like_count(comment_id, 1)

      // send message to user
      url <- commentDao.get_url(comment_id)
      to_user_id <- commentDao.get_owner_id(comment_id)
      user <- userDao.get(user_id)
      comment <- commentDao.get(comment_id)

      data_message = Json.stringify(Json.toJson(CommentUrlUser(comment, url, user)))
      _ <- userMailBoxDao.create(user_id, to_user_id, 3, data_message)
    } yield ret
  }

  def delete(user_id: Long, comment_id: Long) = {
    for {
      ret <- commentLikeDao.delete(user_id, comment_id)
      _ <- commentDao.like_count(comment_id, -1)
    } yield ret
  }

  def list(comment_id: Long) = commentLikeDao.list(comment_id).map(l => l.map(u => UserWrapper(u)))
}
