package service

import javax.inject.Inject

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._


/**
  * Created by likaili on 8/6/2016.
  */
class CommentService @Inject() (commentDao: CommentDao, uRLDao: URLDao, userDao: UserDao) {
  def create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long]) = {
    for {
      id <- commentDao.create(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
      _ <- uRLDao.comment_count(url_id)
      _ <- userDao.comment_count(user_id)
    } yield id
  }

  def list(user_id: Long) = commentDao.list(user_id)
}
