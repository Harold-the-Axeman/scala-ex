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


class AuthController @Inject() (authService: AuthService) extends QidianController {
  def social_auth = QidianAction.async(parse.json[Auth]) { implicit request =>
    val data = request.body
    if (data.auth_type == None) {
      authService.uuid_login(data.client_id).map(r => JsonOk(Json.obj("id" -> r)))
    } else {
      authService.auth_login(data.client_id, data.auth_type.get, data.third_party_id.get, data.name.get, data.avatar.get).map(
        r => JsonOk(Json.obj("id" -> r)))
    }
  }
}


class UserController @Inject() (userService: UserService) extends QidianController {
 def profile(user_id: Long) = QidianAction.async {
   userService.profile(user_id).map(r => JsonOk(Json.toJson(r)))
 }
 def other(user_id: Long, me_id: Long) = QidianAction.async {
   userService.other_profile(user_id, me_id).map(r => JsonOk(Json.toJson(r)))
 }
}

class UrlController @Inject() (urlService: UrlService) extends QidianController {
  def submit = QidianAction.async(parse.json[UrlSubmit]){ implicit request =>
    val data = request.body
    urlService.create(data.user_id, data.url, data.title, data.description, data.anonymous, data.cover_url).map(r => JsonOk(Json.obj("id"->r)))
  }

  def list(user_id: Long) = QidianAction.async {
    urlService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }

  def feeds = QidianAction.async {
    urlService.feeds.map(l => JsonOk(Json.toJson(l)))
  }

  def comments(user_id: Long) = QidianAction.async {
    urlService.comments(user_id).map(l => JsonOk(Json.toJson(l)))
  }
}

class CommentController @Inject() (commentService: CommentService) extends QidianController {
  def add = QidianAction.async(parse.json[CommentSubmit]) { implicit request =>
    val data = request.body
    commentService.create(data.url_id, data.content, data.user_id, data.at_user_id).map(r => JsonOk)
  }

  def list(url_id: Long) = QidianAction.async {
    commentService.list(url_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UserRelationController @Inject() (userRelationService: UserRelationService) extends QidianController {
  def add(from_id: Long, to_id: Long) = QidianAction.async {
    userRelationService.add(from_id, to_id).map(r => JsonOk())
  }

  def delete(from_id: Long, to_id: Long) = QidianAction.async {
    userRelationService.delete(from_id, to_id).map(r => JsonOk())
  }

  def list(user_id: Long) = QidianAction.async {
    userRelationService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class UserMailboxController @Inject() (userMailboxService: UserMailboxService) extends QidianController {
  def list(user_id: Long) = QidianAction.async{
    userMailboxService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }
  def status(user_id: Long)= QidianAction.async {
    userMailboxService.status(user_id: Long).map(r => JsonOk(Json.obj("status" -> r)))
  }
}

class UserCollectionController @Inject() (userCollectionService: UserCollectionService) extends QidianController {
  def add = QidianAction.async(parse.json[UrlCollection]) { implicit request =>
    val data = request.body
    userCollectionService.add(data.user_id, data.url, data.title.getOrElse("")).map(r => JsonOk)
  }

  def delete = QidianAction.async(parse.json[UrlCollection]) { implicit request =>
    val data = request.body
    userCollectionService.delete(data.user_id, data.url).map(r => JsonOk)
  }

  def list(user_id: Long) = QidianAction.async {
    userCollectionService.list(user_id).map( r => JsonOk(Json.toJson(r)))
  }
}

class CommentLikeController @Inject() (commentLikeService: CommentLikeService) extends  QidianController {
  def add(user_id: Long, comment_id: Long) = QidianAction.async {
    commentLikeService.add(user_id, comment_id).map(r => JsonOk)
  }

  def delete(user_id: Long, comment_id: Long) = QidianAction.async {
    commentLikeService.delete(user_id, comment_id).map(r => JsonOk)
  }

  def list(comment_id: Long) = QidianAction.async {
    commentLikeService.list(comment_id).map(r => JsonOk(Json.toJson(r)))
  }
}

class SystemLogController @Inject() (systemLogService: SystemLogService) extends QidianController {
  def submit = QidianAction.async(parse.json[Seq[SubmitLog]]) { implicit  request =>
    val data = request.body.map(s => (s.user_id, s.log_type, s.meta_data))

    systemLogService.submit(data).map(r => JsonOk(Json.obj("count" -> r)))
  }
}

//depreacated
class NavigatorController @Inject() (navigatorDao: NavigatorDao) extends QidianController {
  def info = QidianAction.async {
    navigatorDao.info.map(r => JsonOk(Json.toJson(r)))
  }
}

