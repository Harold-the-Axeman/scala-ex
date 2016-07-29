package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import com.getgua.utils.ws.QidianWebService

import scala.concurrent.Future

/**
  * Created by likaili on 30/6/2016.
  */
@Singleton
class UrlLikeService @Inject()(urlLikeDao: UrlLikeDao, uRLDao: URLDao,  userDao: UserDao, qidianWebService: QidianWebService) {
  def add(user_id: Long, url_id: Long) = {
    for {
      ret <- urlLikeDao.add(user_id, url_id)
      _ <- uRLDao.like_count(url_id, 1)

      // send message to user
      url <- uRLDao.get(url_id)
      to_user_id = url.owner_id
      user <- userDao.get(user_id)

      text_message = s"${user.name}喜欢了你的推荐"
      data_message = Json.stringify(Json.toJson(UrlUser(url, user)))
      push_message_type = "user-url-like"

      _ <- qidianWebService.sendMessage(user_id, to_user_id, user.name, "user-url-like", data_message)

    } yield ret
  }

  def delete(user_id: Long, url_id: Long) = {
    for {
      ret <- urlLikeDao.delete(user_id, url_id)
      _ <- uRLDao.like_count(url_id, -1)
    } yield ret
  }

  def list(url_id: Long) = urlLikeDao.list(url_id).map(l => l.map(u => UserWrapper(u)))
}
