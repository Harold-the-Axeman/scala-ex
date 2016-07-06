package com.getgua.services

import java.util.Base64
import javax.inject.{Inject, Singleton}

import com.getgua.controllers.QidianProxy
import com.getgua.daos._
import org.apache.commons.codec.digest.DigestUtils
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future


/**
  * Created by likaili on 29/6/2016.
  */
@Singleton
class SmsCodeService @Inject()(smsCodeDao: SmsCodeDao, wSClient: WSClient, sMSConfig: SMSConfig, qidianProxy: QidianProxy) {

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
    // if exists
    smsCodeDao.exists(telephone).flatMap(r => r match {
      case true => {
        //TODO: check the frequency in the future, and send status check
        send(telephone, code).flatMap(r => r match {
          case "000000" => smsCodeDao.update(telephone, code)
          case _ => Future.successful(0)
        })
      }
      case false => {
        send(telephone, code).flatMap(r => r match {
          case "000000" => smsCodeDao.create(telephone, code)
          case _ => Future.successful(0)
        })
      }
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

    qidianProxy.post(sMSConfig.send_url, headers = Map(
      "Accept" -> "application/json",
      "Content-Type" -> "application/json;charset=utf-8",
      "Authorization" -> authorization),
      queryString = Map("sig" -> sig),
      body = Json.toJson(body)).map { x =>
      val r = qidianProxy.getResponse(x)
      (r \ "statusCode").as[String]
    }
  }

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
