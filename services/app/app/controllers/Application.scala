package com.getgua.controllers

import javax.inject.Inject

import com.getgua.daos._
import com.getgua.services._
import com.getgua.utils.JsonFormat._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.Future


class AuthController @Inject()(authService: AuthService) extends QidianController {
  val authLogger = Logger("auth")

  def social_auth = QidianAuthAction.async(parse.json[Auth]) { implicit request =>
    val data = request.body
    authLogger.info(s"auth_type: ${data.auth_type} client_id: ${data.client_id} name: ${data.name}")

    if (data.auth_type == None) {
      authService.uuid_login(data.client_id).map(r => JsonOk(Json.obj("id" -> r)).withSession("id" -> r.toString))
    } else {
      authService.auth_login(data.client_id, data.auth_type.get, data.third_party_id.get, data.name.get, data.avatar.get).map(
        r => JsonOk(Json.obj("id" -> r)).withSession("id" -> r.toString))
    }
  }

  def exists(telephone: String) = Action.async {
    authService.telephone_exists(telephone).map(r => JsonOk(Json.obj("ret" -> r)))
  }
}


class UserController @Inject()(userService: UserService, wSClient: WSClient, wSConfig: WSConfig) extends QidianController {
  def profile = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userService.profile(id).map(r => JsonOk(Json.toJson(r)))
  }

  def other(user_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userService.other_profile(user_id, id).map(r => JsonOk(Json.toJson(r)))
  }

  def token(token: String, token_type: String) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    //uMengPushService.device_token(id, token, token_type).map(r => JsonOk(Json.obj("ret" -> r)))
    val url = wSConfig.ws_url + s"/ws/message/token?user_id=$id&token=$token&token_type=$token_type"
    wSClient.url(url).get.map(r => Ok(r.json))
  }
}

class UrlController @Inject()(urlService: UrlService, feedsService: FeedsService) extends QidianController {
  def submit = QidianAction.async(parse.json[UrlSubmit]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    urlService.create(id, data.url, data.title, data.description, data.anonymous, data.cover_url).map(r => JsonOk(Json.obj("id" -> r)))
  }

  def list(user_id: Long) = QidianAction.async { implicit request =>
    //val id = request.session.get("id").get.toLong
    urlService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }

  def feeds(category: Option[String]) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    category match {
      case Some(c) => feedsService.feeds_category(id, c).map(l => JsonOk(Json.toJson(l)))
      case None => feedsService.feeds(id).map(l => JsonOk(Json.toJson(l)))
    }
  }

  def comments(url_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    urlService.comments(url_id, id).map { l => JsonOk(Json.toJson(l)) }
  }

  def get(url_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    urlService.get(url_id, id).map { l => JsonOk(Json.toJson(l)) }
  }

  def delete(url_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    urlService.delete(url_id, id).map { r => JsonOk(Json.obj("ret" -> r))}
  }
}

class CommentController @Inject()(commentService: CommentService) extends QidianController {
  def add = QidianAction.async(parse.json[CommentSubmit]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    commentService.create(data.url_id, data.content, id, data.at_user_id).map(r => JsonOk(Json.obj("id" -> r)))
  }

  def list(user_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    commentService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }

  def delete(comment_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    commentService.delete(comment_id, id).map(r => JsonOk(Json.obj("ret" -> r)))
  }
}

class UserRelationController @Inject()(userRelationService: UserRelationService) extends QidianController {
  def add(to_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userRelationService.add(id, to_id).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def delete(to_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userRelationService.delete(id, to_id).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def list = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userRelationService.list(id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UserMailboxController @Inject()(wSClient: WSClient, wSConfig: WSConfig) extends QidianController {
  def list = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    val url = wSConfig.ws_url + s"/ws/message/list?user_id=$id"
    //userMailboxService.list(id).map(r => JsonOk(Json.toJson(r)))
    wSClient.url(url).get.map(r => Ok(r.json))
  }

  def status = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    val url = wSConfig.ws_url + s"/ws/message/status?user_id=$id"
    //userMailboxService.status(id).map(r => JsonOk(Json.obj("status" -> r)))
    wSClient.url(url).get.map(r => Ok(r.json))
  }
}

class UserCollectionController @Inject()(userCollectionService: UserCollectionService) extends QidianController {
  def add = QidianAction.async(parse.json[UrlCollection]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    userCollectionService.add(id, data.url, data.title.getOrElse("")).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def delete = QidianAction.async(parse.json[UrlCollection]) { implicit request =>
    val data = request.body
    val id = request.session.get("id").get.toLong
    userCollectionService.delete(id, data.url).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def list = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    userCollectionService.list(id).map(r => JsonOk(Json.toJson(r)))
  }
}

class CommentLikeController @Inject()(commentLikeService: CommentLikeService) extends QidianController {
  def add(comment_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    commentLikeService.add(id, comment_id).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def delete(comment_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    commentLikeService.delete(id, comment_id).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def list(comment_id: Long) = QidianAction.async {
    commentLikeService.list(comment_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UrlLikeController @Inject()(urlLikeService: UrlLikeService) extends QidianController {
  def add(url_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    urlLikeService.add(id, url_id).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def delete(url_id: Long) = QidianAction.async { implicit request =>
    val id = request.session.get("id").get.toLong
    urlLikeService.delete(id, url_id).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def list(url_id: Long) = QidianAction.async {
    urlLikeService.list(url_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class SystemLogController @Inject()(systemLogService: SystemLogService) extends QidianController {
  def submit = QidianAction.async(parse.json[SubmitLogs]) { implicit request =>
    val id = request.session.get("id").get.toLong
    val data = request.body.logs.map(s => (id, s.log_type, s.meta_data))
    systemLogService.submit(data).map(r => JsonOk(Json.obj("count" -> r)))
  }
}

class UserRegisterTrackingController @Inject() (userRegisterTrackingService: UserRegisterTrackingService) extends Controller {

  def create(from: String) = Action.async { implicit request =>
    val idOpt = request.session.get("id")

    idOpt match {
      case Some(id) => userRegisterTrackingService.create(id.toLong, from).map(ret => JsonOk(Json.obj("ret" -> ret)))
      case None => Future.successful(Ok("Not Login"))
    }
  }

  def get_url(from: String) = Action {
    val url_pre = "http://101.201.33.198/tracking/"
    val url = url_pre + userRegisterTrackingService.get_url(from)
    JsonOk(Json.obj("url" -> url))
  }
}

//depreacated
class NavigatorController @Inject()(navigatorDao: NavigatorDao) extends QidianController {
  def info = QidianAction.async {
    navigatorDao.info.map(r => JsonOk(Json.toJson(r)))
  }
}


