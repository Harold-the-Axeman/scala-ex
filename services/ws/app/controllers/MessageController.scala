package com.getgua.ws.controllers

import javax.inject.Inject

import com.getgua.utils.JsonFormat._
import play.api.libs.json._
import play.api.mvc._
import com.getgua.ws.services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


case class MessageSubmit(user_id: Long, sender: String, message_type: String, message: String)

case class MessageInfo(message_type: String, message_index: Int, push_text: String, has_push: Boolean)

object MessageInfos {
  val info = Map(
    "user_comment_url" -> MessageInfo("user_comment_url", 1, "评论了你的分享", true),
    "user_comment_user" -> MessageInfo("user_comment_user", 2, "评论了你的分享", true),
    "user_like" -> MessageInfo("user_like", 3, "评论了你的分享", true),
    "user_comment_url" -> MessageInfo("user_comment_url", 4, "评论了你的分享", true),
    "user_comment_url" -> MessageInfo("user_comment_url", 5, "评论了你的分享", true)
  )
}

/**
  * Created by likaili on 11/7/2016.
  */
class MessageController @Inject() (uMengPushService: UMengPushService, userMailboxService: UserMailboxService) extends Controller{
  def token(user_id: Long, token: String, token_type: String) = Action.async { implicit request =>
    //val id = request.session.get("id").get.toLong
    uMengPushService.device_token(user_id, token, token_type).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def list(user_id: Long) = Action.async { implicit request =>
    //val id = request.session.get("id").get.toLong
    userMailboxService.list(user_id).map(r => JsonOk(Json.toJson(r)))
  }

  def status(user_id: Long) = Action.async { implicit request =>
    //val id = request.session.get("id").get.toLong
    userMailboxService.status(user_id).map(r => JsonOk(Json.obj("status" -> r)))
  }

  def message = Action.async(parse.json[MessageSubmit]) { implicit request =>
    val message = request.body
    val mi = MessageInfos.info(message.message_type)

    //send message to mailbox
    userMailboxService.create(message.user_id, mi.message_index, message.message)

    //push message
    mi.has_push match {
      case true =>
        val r = uMengPushService.unicast(message.user_id, message.sender + mi.push_text, message.message, message.message_type)
        val h =Await.result(r, 10 seconds)
        println(h.toString)
    }

    Future.successful(JsonOk)
  }

}
