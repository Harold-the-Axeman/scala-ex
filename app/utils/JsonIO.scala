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

trait JsonFormat {
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
}

object JsonFormat extends JsonFormat {
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
  def JsonOk() = Ok(responseJson(0, "Ok", JsNull))
  def JsonOk(data: JsValue) = Ok(responseJson(0, "Ok", data)).as("application/json; charset=utf-8")
  def JsonError() = Ok(responseJson(-1, "Error", JsNull))
  def JsonError(data: JsValue) = Ok(responseJson(-1, "Error", data)).as("application/json; charset=utf-8")
  /**
   * Return Json
   */
  def responseJson(status: Int, message: String = "Ok", data: JsValue = JsNull) = Json.obj(
    "status" -> JsNumber(status),
    "message" -> JsString(message),
    "data" -> data
  )
}
