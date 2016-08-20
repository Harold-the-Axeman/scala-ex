package com.getgua.ws.services

import javax.inject.{Inject, Singleton}

import org.apache.commons.codec.digest.DigestUtils
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsNull, Json}
import play.api.libs.ws._
import com.getgua.ws.daos._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by likaili on 23/6/2016.
  */

/**
  * TODO: is only for iOS now
  */
@Singleton
class UMengPushService @Inject()(ws: WSClient, pushUserDao: PushUserDao, configuration: Configuration, userPushCounterService: UserPushCounterService) {
  val appkey = configuration.getString("push.app.key").get
  val app_master_secret = configuration.getString("push.app.secret").get
  val send_url = configuration.getString("push.url.send").get
  val mode = configuration.getString("push.production_mode").get

  def sign(url: String, body: String, method: String = "POST") = {
    DigestUtils.md5Hex(method + url + body + app_master_secret)
  }

  def get_token(user_id: Long) = pushUserDao.get(user_id)

  // register user device token
  def device_token(user_id: Long, device_token: String, device_type: String) = {
    pushUserDao.add(user_id, device_token, device_type)
  }

  // unicast
  def unicast_ori(user_id: Long, text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {

    pushUserDao.get(user_id).flatMap {
      case Some(token: String) =>
        val apns = APNS(text_message)
        val payload = Json.toJson(iOSPayload(apns, message_type, data_message))
        val now = System.currentTimeMillis.toString
        val message = UmengMessage(appkey = appkey, timestamp = now, `type` = "unicast", device_tokens = Some(token), payload = payload, description = message_type, production_mode = mode)
        val message_json = Json.toJson(message)

        val url = send_url + "?sign=" + sign(send_url, Json.stringify(message_json))

        ws.url(url).post(message_json).map{ r =>
          dataWatchLogger.info(s"UMeng : ${r.json.toString}")
          r.json
        }
      case None => Future.successful(JsNull)
    }
  }

  def unicast(user_id: Long, text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {
    if (mode == "true" && userPushCounterService.get(user_id) == true) {
      dataWatchLogger.info(s"Umeng User Too Much Push ${user_id}")
      Future.successful(JsNull)
    } else {
      userPushCounterService.set(user_id)
      unicast_ori(user_id, text_message, data_message, message_type, description)
    }
  }

  //
  def broadcast(text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {
    val apns = APNS(text_message)
    val payload = Json.toJson(iOSPayload(apns, message_type, data_message))
    val now = System.currentTimeMillis.toString
    val message = UmengMessage(appkey = appkey, timestamp = now, payload = payload, description = message_type, production_mode = mode)
    val message_json = Json.toJson(message)

    dataWatchLogger.info(s"Umeng Broadcast ${Json.stringify(message_json)}")
    val url = send_url + "?sign=" + sign(send_url, Json.stringify(message_json))
    ws.url(url).post(message_json).map{ r =>
      dataWatchLogger.info(s"UMeng : ${r.json.toString}")
      r.json
    }
  }
}
