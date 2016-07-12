package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import com.getgua.controllers.WSConfig
import play.api.libs.ws.WSClient
/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserRelationService @Inject()(userRelationDao: UserRelationDao, userDao: UserDao, wsConfig: WSConfig, wSClient: WSClient) {

  def add(from: Long, to: Long) = {

    for {
      _ <- userRelationDao.add(from, to)
      c <- userDao.like_count(from, 1)

      // send message to user
      user <- userDao.get(from)
      data_message = Json.stringify(Json.obj("user" -> Json.toJson(user)))

      submit = MessageSubmit(to, user.name, "user-like", data_message)
      _ <- wSClient.url(wsConfig.ws_url + "/ws/message").post(Json.toJson(submit))
     /* _ <- userMailboxService.create(from, to, 4, data_message)
      _ <- uMengPushService.unicast(to, String.format("%s喜欢了你", user.name), data_message, "user_like") */

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
