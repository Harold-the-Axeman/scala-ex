package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._

import scala.concurrent.Future


/**
  * Created by kailili on 5/26/16.
  */
@Singleton
class AuthService @Inject()(authDao: AuthDao) {
  def auth_login(client_id: String, auth_type: String, third_party_id: String, name: String, avatar: String) = {
    authDao.auth_login(client_id, auth_type, third_party_id, name, avatar)
  }

  def uuid_login(client_id: String): Future[Long] = {
    authDao.uuid_login(client_id)
  }

  def telephone_exists(telephone: String) = {
    authDao.telephone_exists(telephone)
  }
}
