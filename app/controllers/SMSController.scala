package com.getgua.controllers

import javax.inject.Inject

import play.api._
import com.getgua.daos._
import com.getgua.services._
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


/**
  * Created by likaili on 29/6/2016.
  */
class SMSController @Inject() (smsCodeService: SmsCodeService) extends Controller {

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
