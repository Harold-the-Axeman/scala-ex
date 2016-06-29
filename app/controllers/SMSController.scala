package controllers

import javax.inject.Inject

import play.api._
import dao._
import service._
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._
import utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


/**
  * Created by likaili on 29/6/2016.
  */
class SMSController @Inject() (smsCodeService: SmsCodeService) extends Controller {
  def send(telephone: String, code: String) = Action.async {
    smsCodeService.send(telephone, code).map(r => Ok(r.json))
  }

  def create(telephone: String) = Action.async {
    smsCodeService.create(telephone).map(r => r match {
      case 0 => JsonOk(Json.obj("ret" -> false))
      case _ => JsonOk(Json.obj("ret" -> true))
    })
  }

  def validate(telephone: String, code: String) = Action.async {
    smsCodeService.validate(telephone, code).map(r => JsonOk(Json.obj("ret" -> r)))
  }
}
