package com.getgua.service

import javax.inject.{Inject, Singleton}

import com.getgua.dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserMailboxService @Inject() (userMailBoxDao: UserMailBoxDao, userDao: UserDao) {
  def list(user_id: Long) = {
    val ml = for {
      r <- userMailBoxDao.list(user_id)
      _ <- userDao.unread(user_id, 0)
    } yield r

    ml.map(_.sortWith(_.id > _.id))
  }

  def create(sender_id: Long, user_id: Long, message_type: Int, message: String) = {
    sender_id == user_id match {
      case true =>  for {
          _ <- userMailBoxDao.create(sender_id, user_id: Long, message_type: Int, message: String)
          _ <- userDao.unread(user_id, 1)
        } yield ()
      case false => Future.successful()
    }
  }

  def status(user_id: Long) = {
    userDao.get_unread(user_id)
  }
}
