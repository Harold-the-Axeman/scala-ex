package com.getgua.utils.ws

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.libs.ws.WSClient

/**
  * Created by likaili on 28/7/2016.
  */
@Singleton
class QidianWebService @Inject() (wSConfig: WSConfig, wSClient: WSClient) {

  def sendMessage(from: Long, to: Long, sender: String, message_type: String, data_message: String) = {
    if (from != to) {
      val submit = MessageSubmit(to, sender, message_type, data_message)
      wSClient.url(wSConfig.ws_url + "/ws/message").post(Json.toJson(submit))
    }
  }
}
