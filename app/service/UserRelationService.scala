package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json


/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserRelationService @Inject() (userRelationDao: UserRelationDao, userDao: UserDao, userMailboxService: UserMailboxService) {

  def add(from: Long, to: Long) = {
    for {
      _ <- userRelationDao.add(from, to)
      c <- userDao.like_count(from, 1)

      // send message to user
      user <- userDao.get(from)
      _ <- userMailboxService.create(from, to, 4, Json.stringify(Json.toJson(user)))
    } yield c
  }

  def delete(from: Long, to: Long) = {
    for {
      _ <- userRelationDao.delete(from, to)
      c <- userDao.like_count(from, -1)
    } yield c
  }

  def list(user_id: Long) = userRelationDao.list(user_id)
}
