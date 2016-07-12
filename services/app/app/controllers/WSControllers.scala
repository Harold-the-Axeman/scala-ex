package com.getgua.controllers

import javax.inject.Inject

import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 11/7/2016.
  */
class SMSController @Inject()(wSClient: WSClient, wSConfig: WSConfig) extends Controller {
  def create(telephone: String) = Action.async {
    val url = wSConfig.ws_url + s"/ws/sms/create?telephone=$telephone"
    println(url)
    wSClient.url(url).get.map(r => Ok(r.json))
  }

  def validate(telephone: String, code: String) = Action.async {
    val url = wSConfig.ws_url + s"/ws/sms/validate?telephone=$telephone&code=$code"
    wSClient.url(url).get.map(r => Ok(r.json))
  }
}
