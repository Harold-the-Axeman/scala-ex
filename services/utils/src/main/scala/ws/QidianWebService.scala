package com.getgua.utils.ws

import javax.inject.{Inject, Singleton}

import play.api.libs.json.{JsNull, JsValue, Json}
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by likaili on 28/7/2016.
  */
@Singleton
class QidianWebService @Inject() (wSConfig: WSConfig, wSClient: WSClient) {

  def sendMessage(from: Long, to: Long, sender: String, message_type: String, data_message: String):Future[JsValue] = {
    from == to match {
      case false =>
        val submit = MessageSubmit(to, sender, message_type, data_message)
        wSClient.url(wSConfig.ws_url + "/ws/message").post(Json.toJson(submit)).map(_.json)
      case true =>
        Future.successful(JsNull)
    }
  }
}
