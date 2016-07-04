package com.getgua.utils

import java.sql.Timestamp
import java.util.Date

import play.api.libs.json.{Json, _}

import com.getgua.models.Tables._
import com.getgua.models.CMSTables._
import com.getgua.controllers._
import com.getgua.dao._
import com.getgua.service._
import play.api.mvc.Results._

/**
 * Created by kailili on 2/12/15.
 */

object JsonFormat  {
  /**
    * Special Handle for Timestamp
    */
  implicit val timestampFormat = new Format[Timestamp] {
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

/*  implicit val stringTupleFormat = new Format[(String, String)] {
    def writes(st: (String, String)): JsValue = Json.obj(st._1 -> st._2)

    def reads(st: JsValue): JsResult[(String, String)] = {
      st.toString.split("->")
      JsSuccess(st.toString
    }
  }*/

  implicit val userMailboxFormat = new Writes[UserMailbox] {
    def writes(um: UserMailbox): JsValue = {
      Json.obj(
        "id" -> um.id,
        "user_id" -> um.user_id,
        "message_type" -> um.message_type,
        "message" -> Json.parse(um.message),
        "create_time" -> um.create_time
      )
    }
  }

  /**
   * Dao Case Class
   */
  implicit val urlFormat = Json.format[Url]
  implicit val userFormat = Json.format[User]
  implicit val userProfileFormat = Json.format[UserProfile]
  implicit val commentFormat = Json.format[Comment]
  implicit val urlWithUserFormat = Json.format[UrlUser]
  implicit val commentWithUserFormat = Json.format[CommentUser]
  implicit val commentWithUrlFormat = Json.format[CommentUrlUser]
  implicit val navigatorFormat = Json.format[Navigator]
  implicit val navigatorWithTypeFormat = Json.format[NavigatorWithType]

  implicit val otherUserProfileFormat = Json.format[OtherProfile]

  implicit val systemLogFormat = Json.format[SystemLog]
  implicit val userCollectionFormat = Json.format[UserCollection]

  /**
    * Controller Case Class
    */
  implicit val authFormat = Json.format[Auth]
  implicit val urlSubmitFormat = Json.format[UrlSubmit]
  implicit val commentSubmitFormat = Json.format[CommentSubmit]
  implicit val urlCollectionFormat = Json.format[UrlCollection]
  implicit val submitLogFormat = Json.format[SubmitLog]
  implicit val submitLogsFormat = Json.format[SubmitLogs]

  /**
    * Service Case Class
    */

  implicit val commentWithStatusFormat = Json.format[CommentUserStatus]
  implicit val userWrapperFormat = Json.format[UserWrapper]
  implicit val urlUserStatusFormat = Json.format[UrlUserStatus]
  implicit val urlStatusFormat = Json.format[UrlStatus]

  /**
    * UMeng Push Service
    */
  implicit val apnsFormat = Json.format[APNS]
  implicit val iOSPayloadFormat = Json.format[iOSPayload]
  implicit val umengMessageFormat = Json.format[UmengMessage]

  /**
    * CMS Service
    */
  implicit val urlPoolFormat = Json.format[UrlPool]
  implicit val cMSSubmitFormat = Json.format[CMSSubmit]

  /**
    * SMS Service
    */
  implicit val smsBodyFormat = Json.format[SMSBody]

  /**
    * Proxy
    */
  implicit val proxyRequestFormat = Json.format[ProxyRequest]

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
