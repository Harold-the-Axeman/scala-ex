package service

import javax.inject.{Inject, Singleton}

import dao.PushUserDao

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsNull, JsValue, Json}
import play.api.libs.ws._
import utils.JsonFormat._
import org.apache.commons.codec.digest.DigestUtils
import controllers.PushMessage

/**
  * Created by likaili on 23/6/2016.
  */



case class UmengMessage (
                         appkey: String,
                         timestamp: String,
                         `type`: String = "broadcast", //默认广播
                         device_tokens: Option[String] = None,
                         // alias_type
                         // alias
                         // file_id
                         // filter
                         payload: JsValue,
                         //policy:  //TODO: add policy logic
                         production_mode: String = "true",
                         description: String
                         //third_party_id:
                     )


case class iOSPayload(
                     aps: APNS,
                     message_type: String,
                     message: String
                     )
case class APNS (
                alert: String,  //消息文本
                badge: Option[String] = None,
                sound: Option[String] = Some("default"),
                content_available: Option[String] = None,
                category: Option[String] = None
                )


/**
  * TODO: is only for iOS now
  */
@Singleton
class UMengPushService @Inject() (ws: WSClient, pushUserDao: PushUserDao){
  val appkey = "5767c9bf67e58e9a0b0005f0"
  val app_master_secret = "iyt4yttbudrk7gvx5pnw3qewtwfpakdz"

  //val push_server_url = "http://127.0.0.1:9000/push/"
  val push_server_url = "http://192.168.1.2:9000/push/"

  val send_url =  "http://msg.umeng.com/api/send"

  def sign(url: String, body: String, method: String = "POST") = {
    DigestUtils.md5Hex(method + url + body + app_master_secret)
  }

  // register user device token
  def device_token(user_id: Long, device_token: String, device_type: String) = {
    pushUserDao.add(user_id, device_token, device_type)
  }

  def remote_unicast(user_id: Long, text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {
      val url = push_server_url + "unicast"
      val data = PushMessage(Some(user_id), text_message, data_message, message_type)

      ws.url(url).post(Json.toJson(data))
  }

  def remote_broadcast(text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {
    val url = push_server_url + "broadcast"
    val data = PushMessage(None, text_message, data_message, message_type)

    ws.url(url).post(Json.toJson(data))
  }


  // unicast
  def unicast(user_id: Long, text_message: String, data_message: String, message_type: String, description: Option[String] = None) = {
    //val device_tokens = Some("a228f2d8fd9277d7a516fa56d64f8ded0b1aead2d0353c4c9d6f32986fccf370")

    pushUserDao.get(user_id).flatMap{
      case Some(token: String) =>
        val apns = APNS(text_message)
        val payload = Json.toJson(iOSPayload(apns, message_type, data_message))
        val now = System.currentTimeMillis.toString
        val message = UmengMessage(appkey = appkey, timestamp = now, `type` = "unicast", device_tokens = Some(token), payload = payload, description = message_type)
        val message_json = Json.toJson(message)

        val url = send_url + "?sign=" + sign(send_url, Json.stringify(message_json))
        ws.url(url).post(message_json).map(r => r.json)
      case None => Future.successful(JsNull)
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
    ws.url(url).post(message_json).map(r => r.json)
  }
}
