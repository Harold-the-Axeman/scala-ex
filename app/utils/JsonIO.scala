package utils

import java.sql.Timestamp
import java.util.Date

import play.api.libs.json.{Json, _}

import models.Tables._
import dao._

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
   * User
   */
  //implicit val userInfoFormat = Json.format[UserInfo]
  //implicit val userImageFormat = Json.format[UserImage]

  implicit val urlFormat = Json.format[Url]
  implicit val userFormat = Json.format[User]
  implicit val commentFormat = Json.format[Comment]
  implicit val urlWithUserFormat = Json.format[URLWithUser]
  implicit val commentWithUserFormat = Json.format[CommentWithUser]
  implicit val commentWithUrlFormat = Json.format[CommentWithUrl]
  implicit val navigatorFormat = Json.format[Navigator]
  implicit val navigatorWithTypeFormat = Json.format[NavigatorWithType]


  /**
   * Return Json
   */
  def responseJson(status: Int, message: String, data: JsValue) = Json.obj(
    "status" -> JsNumber(status),
    "message" -> JsString(message),
    "data" -> data
  )
}
