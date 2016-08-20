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
  * Created by likaili on 20/8/2016.
  */
class PushController @Inject() (uMengPushService: UMengPushService) {
  //def unicast

  //text_message: String, data_message: String, message_type: String
  def broadcast = Action.async {
    uMengPushService.broadcast("奇点广播", "{\"json\":\"value\"}", "url-redirect").map(r => JsonOk(r))
  }
}
