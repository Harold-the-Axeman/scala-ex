package com.getgua.utils

import play.api.libs.json.JsValue
import play.api.libs.ws.WSResponse

/**
  * Created by likaili on 11/7/2016.
  */
object QidianWsHelper {
  def getResponse(response: WSResponse) = {
    (response.json \ "data").as[JsValue]
  }
}
