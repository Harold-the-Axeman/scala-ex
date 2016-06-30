package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import utils.JsonFormat._

/**
  * Created by likaili on 30/6/2016.
  */
@Singleton
class UrlLikeService @Inject() (urlLikeDao: UrlLikeDao, uRLDao: URLDao, userMailBoxDao: UserMailBoxDao, userDao: UserDao, uMengPushService: UMengPushService) {
  def add(user_id: Long, url_id: Long) = {
    for {
      _ <- urlLikeDao.add(user_id, url_id)
      _ <- uRLDao.like_count(url_id, 1)

      // send message to user
      url <- uRLDao.get(url_id)
      to_user_id = url.owner_id
      user <- userDao.get(user_id)

      text_message = s"${user.name}喜欢了你的推荐"
      data_message = Json.stringify(Json.toJson(URLWithUser(url, user)))
      push_message_type = "user-url-like"
      _ <- userMailBoxDao.create(user_id, to_user_id, 5, data_message)
      _ <- uMengPushService.unicast(to_user_id, text_message, data_message, push_message_type)
    } yield ()
  }

  def delete(user_id: Long, url_id: Long) = {
    for {
      _ <- urlLikeDao.delete(user_id, url_id)
      _ <- uRLDao.like_count(url_id, -1)
    } yield ()
  }

  def list(comment_id: Long) = urlLikeDao.list(comment_id)
}
