package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import utils.JsonFormat._

/**
  * Created by likaili on 20/6/2016.
  */
@Singleton
class CommentLikeService @Inject() (commentLikeDao: CommentLikeDao, commentDao: CommentDao) {
  def add(user_id: Long, comment_id: Long) = {
    for {
      _ <- commentLikeDao.add(user_id, comment_id)
      _ <- commentDao.like_count(comment_id, 1)
    } yield ()
  }

  def delete(user_id: Long, comment_id: Long) = {
    for {
      _ <- commentLikeDao.delete(user_id, comment_id)
      _ <- commentDao.like_count(comment_id, -1)
    } yield ()
  }

  def list(comment_id: Long) = commentLikeDao.list(comment_id)
}
