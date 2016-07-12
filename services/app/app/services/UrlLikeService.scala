package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json

/**
  * Created by likaili on 30/6/2016.
  */
@Singleton
class UrlLikeService @Inject()(urlLikeDao: UrlLikeDao, uRLDao: URLDao,  userDao: UserDao) {
  def add(user_id: Long, url_id: Long) = {
    //TODO: userMailboxService: UserMailboxService, , uMengPushService: UMengPushService
    for {
      ret <- urlLikeDao.add(user_id, url_id)
      _ <- uRLDao.like_count(url_id, 1)

      // send message to user
   /*   url <- uRLDao.get(url_id)
      to_user_id = url.owner_id
      user <- userDao.get(user_id)

      text_message = s"${user.name}喜欢了你的推荐"
      data_message = Json.stringify(Json.toJson(UrlUser(url, user)))
      push_message_type = "user-url-like"
      _ <- userMailboxService.create(user_id, to_user_id, 5, data_message)
      _ <- uMengPushService.unicast(to_user_id, text_message, data_message, push_message_type)*/
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
