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
    Ok(views.html.index("奇点浏览器 -- 深度阅读"))
  }

}

class AuthController @Inject() (authDao: AuthDao) extends Controller {
  def social_auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name:Option[String], avatar: Option[String]) = Action.async {
    if (auth_type == None) {
      authDao.uuid_login(client_id).map(r => Ok(responseJson(0, "Ok", Json.obj("id" -> r))))
    } else {
      authDao.auth_login(client_id, auth_type.get, third_party_id.get, name.get, avatar.get).map(r => Ok(responseJson(0, "Ok", Json.obj("id" -> r))))
    }
  }
}

class UrlController @Inject() (urlDao: URLDao) extends Controller {
  def submit(user_id: Long, url: String, title: String, description: String, anonymous: Int) = Action.async{

    urlDao.createURL(user_id, url, title, description, anonymous).map(r => Ok(responseJson(0, "Ok", JsNull)))
  }

  def list(user_id: Long) = Action.async {
    urlDao.list(user_id).map(r => Ok(responseJson(0, "Ok", Json.toJson(r))))
  }

  def feeds = Action.async {
    urlDao.feeds.map(l =>  Ok(responseJson(0, "Ok", Json.toJson(l))))
  }

  def comments(user_id: Long) = Action.async {
    //urlDao.comments(url_id).map(l =>  println(Json.toJson(l)))
    urlDao.comments(user_id).map(l =>  Ok(responseJson(0, "Ok", Json.toJson(l))).withHeaders(CONTENT_TYPE -> "application/json; charset=utf-8 "))
  }
}

class CommentController @Inject() (commentDao: CommentDao) extends Controller {
  def add(url_id: Long, content: String, user_id:Long, at_user_id: Option[Long]) = Action.async {
    commentDao.create(url_id, content, user_id, at_user_id).map(r => Ok(responseJson(0, "Ok", JsNull)))
  }

  def list(url_id: Long) = Action.async {
    commentDao.list(url_id).map(r => Ok(responseJson(0, "Ok", Json.toJson(r))))
  }
}

class NavigatorController @Inject() (navigatorDao: NavigatorDao) extends Controller {
  def info = Action.async {
    navigatorDao.info.map(r => Ok(responseJson(0, "Ok", Json.toJson(r))).withHeaders(CONTENT_TYPE -> "application/json; charset=utf-8 "))
  }
}