package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._


/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserRelationService @Inject() (userRelationDao: UserRelationDao, userDao: UserDao) {

  def add(from: Long, to: Long) = {
    for {
      _ <- userRelationDao.add(from, to)
      c <- userDao.like_count(from, 1)
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
