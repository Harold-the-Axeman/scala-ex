package com.getgua.ws.services

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import com.getgua.ws.daos._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserMailboxService @Inject()(userMailBoxDao: UserMailBoxDao) {
  def list(user_id: Long) = {
    val ml = for {
      r <- userMailBoxDao.list(user_id)
      _ <- userMailBoxDao.status_set(user_id, 0)
    } yield r

    ml.map(_.sortWith(_.id > _.id))
  }

  def create(user_id: Long, message_type: Int, message: String) = {
    for{
      r <- userMailBoxDao.create(user_id: Long, message_type: Int, message: String)
      _ <- userMailBoxDao.status_set(user_id, 1)
    } yield r
  }

  def status(user_id: Long) = {
    userMailBoxDao.status_get(user_id)
  }
}
