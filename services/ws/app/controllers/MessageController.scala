package com.getgua.ws.controllers

import javax.inject.Inject

import com.getgua.utils.JsonFormat._
import play.api.libs.json._
import play.api.mvc._
import com.getgua.ws.services._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.getgua.utils.ws._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by likaili on 11/7/2016.
  */
class MessageController @Inject() (uMengPushService: UMengPushService, userMailboxService: UserMailboxService) extends Controller{
  def token(user_id: Long, token: String, token_type: String) = Action.async { implicit request =>
    //val id = request.session.get("id").get.toLong
    uMengPushService.device_token(user_id, token, token_type).map(r => JsonOk(Json.obj("ret" -> r)))
  }

  def get_token(user_id: Long) = Action.async {
    uMengPushService.get_token(user_id).map(t => JsonOk(Json.obj("token"->t)))
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

    Logger.info(message.message_type)

    //send message to mailbox
    userMailboxService.create(message.user_id, mi.message_index, message.message)

    //push message
    if (mi.has_push == true)  {
        uMengPushService.unicast(message.user_id, message.sender + mi.push_text, message.message, message.message_type)
    }

    Future.successful(JsonOk)
  }
}
