package com.getgua.cms.utils

import play.api.libs.json._
import play.api.mvc.Results._

/**
  * Created by kailili on 2/12/15.
  */

object JsonFormat {

  // Json Result Helper
  val QIDIAN_OK = 0
  val QIDIAN_LOGIC_ERROR = -1
  val QIDIAN_AUTH_ERROR = -4
  val QIDIAN_ERROR = -5

  def JsonOk() = Ok(responseJson(QIDIAN_OK, "Ok", JsNull))

  def JsonOk(data: JsValue) = Ok(responseJson(QIDIAN_OK, "Ok", data)).as("application/json; charset=utf-8")

  def JsonError() = Ok(responseJson(QIDIAN_LOGIC_ERROR, "Error", JsNull))

  def JsonError(data: JsValue) = Ok(responseJson(QIDIAN_LOGIC_ERROR, "Error", data)).as("application/json; charset=utf-8")

  def JsonAuthError = Ok(responseJson(QIDIAN_AUTH_ERROR, "Not Login", JsNull)).as("application/json; charset=utf-8")

  def JsonServerError(message: String, data: JsValue) = Ok(responseJson(QIDIAN_ERROR, message, data)).as("application/json; charset=utf-8")

  /**
    * Return Json
    * Do not use this directly, it does not handle the Content-Type Header
    */
  def responseJson(status: Int, message: String = "Ok", data: JsValue = JsNull) = Json.obj(
    "status" -> JsNumber(status),
    "message" -> JsString(message),
    "data" -> data
  )
}
