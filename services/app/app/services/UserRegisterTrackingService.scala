package com.getgua.services

/**
  * Created by likaili on 18/7/2016.
  */
import javax.inject.{Inject, Singleton}
import com.getgua.daos._
import org.apache.commons.codec.digest.DigestUtils

@Singleton
class UserRegisterTrackingService @Inject()(userRegisterTrackingDao: UserRegisterTrackingDao) {
  def create(user_id: Long, from: String) = {
    userRegisterTrackingDao.create(user_id, from)
  }

  def get_url(from: String) = {
     DigestUtils.sha1Hex(from)
  }
}
