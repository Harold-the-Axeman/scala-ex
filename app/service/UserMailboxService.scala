package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserMailboxService @Inject() (userMailBoxDao: UserMailBoxDao, userDao: UserDao) {
  def list(user_id: Long) = {
    for {
      r <- userMailBoxDao.list(user_id)
      _ <- userDao.unread(user_id, 0)
    } yield r
  }

  def create(user_id: Long, message_type: Int, message: String) = {
    for {
      _ <- userMailBoxDao.create(user_id: Long, message_type: Int, message: String)
      _ <- userDao.unread(user_id, 1)
    } yield ()
  }

  def status(user_id: Long) = {
    userDao.get_unread(user_id)
  }
}