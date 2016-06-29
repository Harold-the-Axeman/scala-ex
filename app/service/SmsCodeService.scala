package service

import java.util.Base64
import javax.inject.{Inject, Singleton}

import dao._
import play.api.{Configuration, Logger}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import utils.JsonFormat._
import org.apache.commons.codec.digest.DigestUtils
import play.api.libs.ws.WSClient
import utils.JsonFormat._

@Singleton
class SMSConfig @Inject() (configuration: Configuration){
  val accountSid = "aaf98f894d328b13014d6b4a88b0295e"
  val token = "437c2c92e5be4639b7033391caa66ff8"

  val appId = "8a48b5514db9e13d014dbd1171d101f4"
  val templateId = "22164"
  //val
  val message_url_pre = "https://app.cloopen.com:8883"

  val proxy_url = configuration.getString("http://192.168.1.2:9000/sms/send").get
  //"http://127.0.0.1:9000/sms/send"
}

case class SMSBody(to: String, datas: Seq[String], appId: String, templateId: String)

/**
  * Created by likaili on 29/6/2016.
  */
@Singleton
class SmsCodeService @Inject()(smsCodeDao: SmsCodeDao, wSClient: WSClient, sMSConfig: SMSConfig) {

  def generate_code = {
    import scala.util.Random

    val r = Random.nextLong % 10000
    r > 0 match {
      case true => r.toString
      case false => (r + 10000).toString
    }
  }

  def create(telephone: String):Future[Int] = {
    val code = generate_code
    // if exists
    smsCodeDao.exists(telephone).flatMap(r => r match {
      case true => {
        //TODO: check the frequency in the future, and send status check
        remote_send(telephone, code).flatMap( r => r match {
          case "000000" => smsCodeDao.update(telephone, code)
          case _ => Future.successful(0)
        })
      }
      case false => {
        remote_send(telephone, code).flatMap( r => r match {
          case "000000" => smsCodeDao.create(telephone, code)
          case _ => Future.successful(0)
        })
      }
     })
  }

  def remote_send(telephone: String, code: String): Future[String] = {
    wSClient.url(sMSConfig.proxy_url).withQueryString("telephone" -> telephone, "code" -> code).get().map(
      r => (r.json \ "statusCode").as[String]
    )
  }

  def send(telephone: String, code: String) ={
    import java.util.Calendar
    import java.text.SimpleDateFormat

    val now = Calendar.getInstance.getTime
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val datetimeFormat = new SimpleDateFormat("yyyyMMddHHmmss")

    val date_now = dateFormat.format(now)
    val timestamp = datetimeFormat.format(now)

    val url = sMSConfig.message_url_pre + s"/2013-12-26/Accounts/${sMSConfig.accountSid}/SMS/TemplateSMS"
    val body = SMSBody(telephone, Seq(code), sMSConfig.appId, sMSConfig.templateId)

    val sig = DigestUtils.md5Hex(sMSConfig.accountSid + sMSConfig.token + timestamp).toUpperCase
    val authorization = Base64.getEncoder.encodeToString((sMSConfig.accountSid + ":" + timestamp).getBytes)

    wSClient.url(url).withQueryString("sig" -> sig).withHeaders(
      "Accept" -> "application/json",
      "Content-Type" -> "application/json;charset=utf-8",
      "Authorization" -> authorization
    ).post(Json.toJson(body))
  }

 /* def send(telephone: String, code: String) = {

  }*/

  def validate(telephone: String, code: String) = {
    smsCodeDao.check(telephone, code).map(r => r match {
      case Some(c) => {
        smsCodeDao.set_pass(r.get.id)
        true
      }
      case None => false
    })
  }
}
