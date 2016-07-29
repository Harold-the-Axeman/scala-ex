

/**
  * Created by likaili on 28/7/2016.
  */

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.libs.json.Json

package com.getgua.utils {
  package object ws {
    @Singleton
    class WSConfig @Inject() (configuration: Configuration) {
      val ws_url = configuration.getString("qidian.ws.url").get
    }
    /**
      * WS Push Message
      */
    case class MessageSubmit(user_id: Long, sender: String, message_type: String, message: String)
    implicit val messageSubmitFormat = Json.format[MessageSubmit]
  }
}
