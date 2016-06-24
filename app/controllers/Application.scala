package controllers

import javax.inject.Inject

import play.api._
import dao._
import service._
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._
import utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSClient

import scala.concurrent.Future


class AuthController @Inject() (authService: AuthService) extends QidianController {
  def social_auth = QidianAuthAction.async(parse.json[Auth]) { implicit request =>
    val data = request.body
    if (data.auth_type == None) {
      authService.uuid_login(data.client_id).map(r => JsonOk(Json.obj("id" -> r)).withSession("id" -> r.toString))
    } else {
      authService.auth_login(data.client_id, data.auth_type.get, data.third_party_id.get, data.name.get, data.avatar.get).map(
        r => JsonOk(Json.obj("id" -> r)).withSession("id" -> r.toString))
    }
  }
}


class UserController @Inject() (userService: UserService, uMengPushService: UMengPushService) extends QidianController {
 def profile = QidianAction.async { implicit request =>
   val id = request.session.get("id").get.toLong
   userService.profile(id).map(r => JsonOk(Json.toJson(r)))
 }
 def other(user_id: Long) = QidianAction.async { implicit request =>
   val id = request.session.get("id").get.toLong
   userService.other_profile(user_id, id).map(r => JsonOk(Json.toJson(r)))
 }

 def token(token: String, token_type: String) = QidianAction.async {  implicit request =>
   val id = request.session.get("id").get.toLong
   uMengPushService.device_token(id, token, token_type).map( r => JsonOk)
 }
}

class UrlController @Inject() (urlService: UrlService) extends QidianController {
  def submit = QidianAction.async(parse.json[UrlSubmit]){ implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    urlService.create(id, data.url, data.title, data.description, data.anonymous, data.cover_url).map(r => JsonOk(Json.obj("id"->r)))
  }

  def list(user_id: Long) = QidianAction.async { implicit  request =>
    val id = request.session.get("id").get.toLong
    urlService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }

  def feeds = QidianAction.async {
    urlService.feeds.map(l => JsonOk(Json.toJson(l)))
  }

  def comments(url_id: Long) = QidianAction.async { implicit  request =>
    val id = request.session.get("id").get.toLong
    urlService.comments(url_id, id).map{l => JsonOk(Json.toJson(l))}
  }

  def get(url_id: Long) = QidianAction.async (
    urlService.get(url_id).map(l => JsonOk(Json.toJson(l)))
  )
}

class CommentController @Inject() (commentService: CommentService) extends QidianController {
  def add = QidianAction.async(parse.json[CommentSubmit]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    commentService.create(data.url_id, data.content, id, data.at_user_id).map(r => JsonOk)
  }

  def list(user_id: Long) = QidianAction.async { implicit  request =>
    val id = request.session.get("id").get.toLong
    commentService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UserRelationController @Inject() (userRelationService: UserRelationService) extends QidianController {
  def add(to_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userRelationService.add(id, to_id).map(r => JsonOk())
  }

  def delete(to_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userRelationService.delete(id, to_id).map(r => JsonOk())
  }

  def list = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userRelationService.list(id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UserMailboxController @Inject() (userMailboxService: UserMailboxService) extends QidianController {
  def list = QidianAction.async{ implicit request =>
    val id = request.session.get("id").get.toLong
    userMailboxService.list(id).map(r => JsonOk(Json.toJson(r)))
  }
  def status = QidianAction.async { implicit  request =>
    val id = request.session.get("id").get.toLong
    userMailboxService.status(id).map(r => JsonOk(Json.obj("status" -> r)))
  }
}

class UserCollectionController @Inject() (userCollectionService: UserCollectionService) extends QidianController {
  def add = QidianAction.async(parse.json[UrlCollection]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    userCollectionService.add(id, data.url, data.title.getOrElse("")).map(r => JsonOk)
  }

  def delete = QidianAction.async(parse.json[UrlCollection]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    userCollectionService.delete(id, data.url).map(r => JsonOk)
  }

  def list = QidianAction.async {implicit request =>
    val id = request.session.get("id").get.toLong
    userCollectionService.list(id).map( r => JsonOk(Json.toJson(r)))
  }
}

class CommentLikeController @Inject() (commentLikeService: CommentLikeService) extends  QidianController {
  def add(comment_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    commentLikeService.add(id, comment_id).map(r => JsonOk)
  }

  def delete(comment_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    commentLikeService.delete(id, comment_id).map(r => JsonOk)
  }

  def list(comment_id: Long) = QidianAction.async {
    commentLikeService.list(comment_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class SystemLogController @Inject() (systemLogService: SystemLogService) extends QidianController {
  def submit = QidianAction.async(parse.json[SubmitLogs]) { implicit  request =>
    val id = request.session.get("id").get.toLong
    val data = request.body.logs.map(s => (id, s.log_type, s.meta_data))
    systemLogService.submit(data).map(r => JsonOk(Json.obj("count" -> r)))
  }
}

//depreacated
class NavigatorController @Inject() (navigatorDao: NavigatorDao) extends QidianController {
  def info = QidianAction.async {
    navigatorDao.info.map(r => JsonOk(Json.toJson(r)))
  }
}

class UMengPushController @Inject() (uMengPushService: UMengPushService) extends Controller {
  def unicast(code: String) = Action.async(parse.json[PushMessage]) { implicit request =>
    //33d60800441663873b190641607fe978
    code == "33d60800441663873b190641607fe978" match {
      case true => {
        val data = request.body
        uMengPushService.unicast(data.user_id.get, data.text,  data.message, data.message_type).map(r => JsonOk(r))
      }
      case false => Future.successful(JsonError)
    }
 }

  def broadcast(code: String) = Action.async(parse.json[PushMessage]) { implicit request =>
    code == "33d60800441663873b190641607fe978" match {
      case true => {
        val data = request.body
        uMengPushService.broadcast(data.text, data.message, data.message_type).map(r => JsonOk(r))
      }
      case false => Future.successful(JsonError)
    }
  }
}

class ServerStatusCheckController @Inject() (ws: WSClient, configuration: Configuration, pushUserDao: PushUserDao) extends Controller {
  def info(code: String, host: String) = Action.async {
    code == "woshixiaolu" match {
      case true => {
        val status_url = s"http://$host:9000/status?code=$code"
        ws.url(status_url).get().map(r => JsonOk(r.json))
      }
      case false => Future.successful(JsonError)
    }
  }

  def status(code: String) = Action {
    import java.net._
    code == "woshixiaolu" match {
      case true => {
        val v = "v1.4"
        val x = InetAddress.getLocalHost
        val y = configuration.getString("slick.dbs.default.db.url").get
        val z1  = configuration.getString("push.appkey").get
        val z2 = configuration.getString("push.server_url").get
        //= configuration.getString("").get
        Ok(Json.obj("v" -> v, "x" -> x.getHostAddress.toString, "y" -> y, "z1" -> z1, "z2" -> z2))

      }
      case false => Ok(Json.obj("x"->"error"))
    }
  }

  def check_token(code: String, id: Long) = Action.async {
    code == "woshixiaolu" match {
      case true => {
        pushUserDao.get(id).map(r => JsonOk(Json.obj("t" -> r)))
      }
      case false => Future.successful(Ok(Json.obj("x"->"error")))
    }
  }
}