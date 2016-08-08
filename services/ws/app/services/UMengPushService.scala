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
  val appkey = configuration.getString("push.app.key").getOrElse("5767c9bf67e58e9a0b0005f0")
  val app_master_secret = configuration.getString("push.app.secret").getOrElse("iyt4yttbudrk7gvx5pnw3qewtwfpakdz")
  val send_url = configuration.getString("push.url.send").getOrElse("http://msg.umeng.com/api/send")

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
        val message = UmengMessage(appkey = appkey, timestamp = now, `type` = "unicast", device_tokens = Some(token), payload = payload, description = message_type)
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
    userPushCounterService.get(user_id) match {
      case false =>
        userPushCounterService.set(user_id)
        unicast_ori(user_id, text_message, data_message, message_type, description)
      case true =>
        dataWatchLogger.info(s"Umeng User Too Much Push ${user_id}")
        Future.successful(JsNull)
    }
  }

  //
  def broadcast(text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {
    val apns = APNS(text_message)
    val payload = Json.toJson(iOSPayload(apns, message_type, data_message))
    val now = System.currentTimeMillis.toString
    val message = UmengMessage(appkey = appkey, timestamp = now, payload = payload, description = message_type)
    val message_json = Json.toJson(message)

    val url = send_url + "?sign=" + sign(send_url, Json.stringify(message_json))
    ws.url(url).post(message_json).map{ r =>
      dataWatchLogger.info(s"UMeng : ${r.json.toString}")
      r.json
    }
  }
}
