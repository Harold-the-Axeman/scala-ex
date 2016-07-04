package com.getgua.service

import javax.inject.{Inject, Singleton}

import com.getgua.dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._


/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserService @Inject() (userDao: UserDao, userRelationDao: UserRelationDao) {

  def profile(id: Long) = userDao.profile(id)

  def other_profile(user_id: Long, me: Long) = {
    for {
      u <- userDao.profile(user_id)
      il <- userRelationDao.is_like(me, user_id)
    } yield OtherProfile(u, il)
  }
}
