package utils

import java.sql.Timestamp
import java.util.Date

import play.api.libs.json.{Json, _}

import models.Tables._
import controllers._
import dao._
import play.api.mvc.Results._

/**
 * Created by kailili on 2/12/15.
 */

object JsonFormat  {
  /**
    * Special Handle for Timestamp
    */
  implicit val formatTimestamp = new Format[Timestamp] {
    //def writes(ts: Timestamp): JsValue = JsString(format.format(new Date(ts.getTime)))
    def writes(ts: Timestamp): JsValue = JsNumber(new Date(ts.getTime()).getTime)

    def reads(ts: JsValue): JsResult[Timestamp] = {
      try {
        //JsSuccess(Timestamp.valueOf(ts.as[String]))
        JsSuccess(new Timestamp(ts.as[Long]))
      } catch {
        case e: IllegalArgumentException => JsError("Unable to parse timestamp")
      }
    }
  }

  /**
   * Dao Case Class
   */
  implicit val urlFormat = Json.format[Url]
  implicit val userFormat = Json.format[User]
  implicit val userProfileFormat = Json.format[UserProfile]
  implicit val commentFormat = Json.format[Comment]
  implicit val urlWithUserFormat = Json.format[URLWithUser]
  implicit val commentWithUserFormat = Json.format[CommentWithUser]
  implicit val commentWithUrlFormat = Json.format[CommentWithUrl]
  implicit val navigatorFormat = Json.format[Navigator]
  implicit val navigatorWithTypeFormat = Json.format[NavigatorWithType]

  implicit val otherUserProfileFormat = Json.format[OtherUserProfile]


  /**
    * Controller Case Class
    */
  implicit val authFormat = Json.format[Auth]
  implicit val urlSubmitFormat = Json.format[UrlSubmit]
  implicit val commentSubmitFormat = Json.format[CommentSubmit]

  // Json Result Helper
  val QIDIAN_OK = 0
  val QIDIAN_LOGIC_ERROR = -1
  val QIDIAN_ERROR = -5
  def JsonOk() = Ok(responseJson(QIDIAN_OK, "Ok", JsNull))
  def JsonOk(data: JsValue) = Ok(responseJson(QIDIAN_OK, "Ok", data)).as("application/json; charset=utf-8")
  def JsonError() = Ok(responseJson(QIDIAN_LOGIC_ERROR, "Error", JsNull))
  def JsonError(data: JsValue) = Ok(responseJson(QIDIAN_LOGIC_ERROR, "Error", data)).as("application/json; charset=utf-8")
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
