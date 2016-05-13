package controllers

import javax.inject.Inject

import play.api._
import play.api.mvc._
import org.apache.commons.codec.digest.DigestUtils
import dao._
import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc._
import utils.JsonFormat._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future
import scala.concurrent.duration._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}

class AuthController @Inject() (authDao: AuthDao) extends Controller {
  def auth(auth_type: String, token: String) = Action.async {
    authDao.login(auth_type, token).map(r => Ok(responseJson(0, "Ok", Json.obj("id" -> r))))
  }

  def social_auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name:Option[String], avatar: Option[String]) = Action.async {
    if (auth_type == None) {
      authDao.uuid_login(client_id).map(r => Ok(responseJson(0, "Ok", Json.obj("id" -> r))))
    } else {
      authDao.auth_login(client_id, auth_type.get, third_party_id.get, name.get, avatar.get).map(r => Ok(responseJson(0, "Ok", Json.obj("id" -> r))))
    }
  }
}

class UrlController @Inject() (urlDao: URLDao) extends Controller {
  def submit(url: String, user: Long) = Action.async{

    urlDao.createURL(url, user).map(r => Ok(responseJson(0, "Ok", JsNull)))
  }

  def list(user_id: Long) = Action.async {
    urlDao.list(user_id).map(r => Ok(responseJson(0, "Ok", Json.toJson(r))))
  }
}

class CommentController @Inject() (commentDao: CommentDao) extends Controller {
  def add(url_id: Long, content: String, user:Long, at_user: Option[Long]) = Action.async {
    commentDao.create(url_id, content, user, at_user).map(r => Ok(responseJson(0, "Ok", JsNull)))
  }

  def list(url_id: Long) = Action.async {
    commentDao.list(url_id).map(r => Ok(responseJson(0, "Ok", Json.toJson(r))))
  }
}