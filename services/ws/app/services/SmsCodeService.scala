package com.getgua.ws.services

import java.util.Base64
import javax.inject.{Inject, Singleton}

import org.apache.commons.codec.digest.DigestUtils
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import com.getgua.ws.daos._

/**
  * Created by likaili on 29/6/2016.
  */
@Singleton
class SmsCodeService @Inject()(smsCodeDao: SmsCodeDao, wSClient: WSClient, sMSConfig: SMSConfig) {

  def generate_code = {
    import scala.util.Random

    val r = Random.nextInt % 10000
    //Random.next
    r > 0 match {
      case true => "%04d".format(r)
      case false => "%04d".format(r + 10000)
    }
  }

  def create(telephone: String): Future[Int] = {
    val code = generate_code

    //TODO: check the frequency in the future, and send status check
    send(telephone, code).flatMap(r => r match {
      case "000000" => smsCodeDao.create(telephone, code)
      case _ => Future.successful(0)
    })
  }

  def send(telephone: String, code: String) = {
    import java.text.SimpleDateFormat
    import java.util.Calendar

    val now = Calendar.getInstance.getTime
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val datetimeFormat = new SimpleDateFormat("yyyyMMddHHmmss")

    val date_now = dateFormat.format(now)
    val timestamp = datetimeFormat.format(now)

    val body = SMSBody(telephone, Seq(code), sMSConfig.appId, sMSConfig.templateId)

    val sig = DigestUtils.md5Hex(sMSConfig.accountSid + sMSConfig.app_token + timestamp).toUpperCase
    val authorization = Base64.getEncoder.encodeToString((sMSConfig.accountSid + ":" + timestamp).getBytes)

    wSClient.url(sMSConfig.send_url).withHeaders( "Accept" -> "application/json",
      "Content-Type" -> "application/json;charset=utf-8",
      "Authorization" -> authorization).withQueryString("sig" -> sig).post(Json.toJson(body)).map { x =>
      (x.json \ "statusCode").as[String]
    }
  }

  def validate(telephone: String, code: String) = {
    smsCodeDao.check(telephone, code).map(r => r match {
      case Some(c) => {
        smsCodeDao.set_pass(telephone)
        true
      }
      case None => false
    })
  }
}
