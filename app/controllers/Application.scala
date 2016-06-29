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

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


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
  def exists(telephone: String) = Action.async{
    authService.telephone_exists(telephone).map(r => JsonOk(Json.obj("ret" -> r)))
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
  def unicast = Action.async(parse.json[PushMessage]) { implicit request =>
    val data = request.body
    data.code == PushServerCheck.code match {
      case true => {
        uMengPushService.unicast(data.user_id.get, data.text, data.message, data.message_type).map(r => JsonOk(r))
      }
      case false => Future.successful(JsonError)
    }
 }

  def broadcast = Action.async(parse.json[PushMessage]) { implicit request =>
    val data = request.body
    data.code == PushServerCheck.code match {
      case true => {
        uMengPushService.broadcast(data.text, data.message, data.message_type).map(r => JsonOk(r))
      }
      case false => Future.successful(JsonError)
    }
  }
}

class ServerStatusCheckController @Inject() (ws: WSClient, configuration: Configuration, uMengPushService: UMengPushService, navigatorDao: NavigatorDao, pushUserDao: PushUserDao, serverInfo: ServerInfo) extends Controller {
  def info(code: String) = Action {
    code == serverInfo.code match {
      case true => {
        val rs = serverInfo.hosts.map{ h =>
          val status_url = s"http://$h:9000/status?code=$code"
          val r = ws.url(status_url).get()
          Await.result(r, 10 seconds).json
        }
        JsonOk(Json.toJson(rs))
      }
      case false => JsonError
    }
  }

  def status(code: String) = Action.async {
    import java.net._
    code == serverInfo.code match {
      case true => {
        val version = serverInfo.version
        val host = InetAddress.getLocalHost.getHostAddress.toString
        val parameters = serverInfo.parameters.map(kv => (kv._1 -> configuration.getString(kv._2).get))

        //val weixin = configuration.getString("weixin.server_url").get
        //val sms = configuration.getString("sms.server_url").get
        val response = for {
          // db test
          dbr <- navigatorDao.info
          pushr <- uMengPushService.remote_unicast(serverInfo.push_user_id, s"Push测试:$host", "", "push_server_test")
        } yield (dbr, pushr)

        response.map{ r =>
          Ok(Json.obj("version" -> version, "host" -> host, "parameters" -> Json.toJson(parameters)
            , "db_test_reulst" -> Json.toJson(r._1), "push_test_result" -> r._2.json))
        }
        // push test
      }
      case false => Future.successful(JsonError)
    }
  }

  def check_token(code: String, id: Long) = Action.async {
    code == serverInfo.code match {
      case true => {
        pushUserDao.get(id).map(r => JsonOk(Json.obj("t" -> r)))
      }
      case false => Future.successful(JsonError)
    }
  }
}