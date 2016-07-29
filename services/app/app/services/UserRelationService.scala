package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import com.getgua.utils.ws.QidianWebService
/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserRelationService @Inject()(userRelationDao: UserRelationDao, userDao: UserDao, qidianWebService: QidianWebService) {

  def add(from: Long, to: Long) = {

    for {
      _ <- userRelationDao.add(from, to)
      c <- userDao.like_count(from, 1)

      // send message to user
      user <- userDao.get(from)
      data_message = Json.stringify(Json.obj("user" -> Json.toJson(user)))

      _ <- qidianWebService.sendMessage(from, to, user.name, "user-like", data_message)
    } yield c
  }

  def delete(from: Long, to: Long) = {
    for {
      _ <- userRelationDao.delete(from, to)
      c <- userDao.like_count(from, -1)
    } yield c
  }

  def list(user_id: Long) = userRelationDao.list(user_id).map(l => l.map(u => UserWrapper(u)))
}
