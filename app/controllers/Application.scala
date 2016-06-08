package controllers

import javax.inject.Inject

import play.api._
import org.apache.commons.codec.digest.DigestUtils
import dao._
import service._
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
    Ok(views.html.index("奇点浏览器--种瓜得瓜有限公司"))
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


class UserController @Inject() (userService: UserService) extends Controller {
 def profile(user_id: Long) = Action.async {
   userService.profile(user_id).map(r => JsonOk(Json.toJson(r)))
 }
 def other(user_id: Long, me_id: Long) = Action.async {
   userService.other_profile(user_id, me_id).map(r => JsonOk(Json.toJson(r)))
 }
}

class UrlController @Inject() (urlService: UrlService) extends Controller {
  def submit(user_id: Long, url: String, title: String, description: String, anonymous: Int) = Action.async{

    urlService.create(user_id, url, title, description, anonymous).map(r => JsonOk)
  }

  def list(user_id: Long) = Action.async {
    urlService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }

  def feeds = Action.async {
    urlService.feeds.map(l => JsonOk(Json.toJson(l)))
  }

  def comments(user_id: Long) = Action.async {
    urlService.comments(user_id).map(l => JsonOk(Json.toJson(l)))
  }
}

class CommentController @Inject() (commentService: CommentService) extends Controller {
  def add(url_id: Long, content: String, user_id:Long, at_user_id: Option[Long]) = Action.async {
    commentService.create(url_id, content, user_id, at_user_id).map(r => JsonOk)
  }

  def list(url_id: Long) = Action.async {
    commentService.list(url_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UserRelationControllor @Inject() (userRelationService: UserRelationService) extends Controller {
  def add(from_id: Long, to_id: Long) = Action.async {
    userRelationService.add(from_id, to_id).map(r => JsonOk())
  }

  def delete(from_id: Long, to_id: Long) = Action.async {
    userRelationService.delete(from_id, to_id).map(r => JsonOk())
  }

  def list(user_id: Long) = Action.async {
    userRelationService.list(user_id: Long).map(r => JsonOk(Json.toJson(r)))
  }
}

class NavigatorController @Inject() (navigatorDao: NavigatorDao) extends Controller {
  def info = Action.async {
    navigatorDao.info.map(r => JsonOk(Json.toJson(r)))
  }
}