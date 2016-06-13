package service

import javax.inject.Inject

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._


/**
  * Created by kailili on 5/26/16.
  */
class AuthService @Inject()(authDao: AuthDao) {
  def auth_login(client_id: String, auth_type: String, third_party_id: String, name: String, avatar: String) = {
    authDao.auth_login(client_id, auth_type, third_party_id, name, avatar)
  }

  def uuid_login(client_id: String): Future[Long] = {
    authDao.uuid_login(client_id)
  }
}
