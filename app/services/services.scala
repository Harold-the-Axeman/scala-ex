/**
  * Created by likaili on 22/6/2016.
  */
package com.getgua {

  package object services {

    import daos.{CommentUser, UrlUser}
    import models.{Url, User}
    import play.api.libs.json.Json

    case class CommentUserStatus(cu: CommentUser, status: Boolean)

    case class UrlUserStatus(uu: UrlUser, status: Boolean)

    case class UrlStatus(url: Url, status: Boolean)

    case class UserWrapper(user: User)

    implicit val commentWithStatusFormat = Json.format[CommentUserStatus]
    implicit val userWrapperFormat = Json.format[UserWrapper]
    implicit val urlUserStatusFormat = Json.format[UrlUserStatus]
    implicit val urlStatusFormat = Json.format[UrlStatus]

    /**
      * sms sub service
      */

    import javax.inject.{Inject, Singleton}

    import play.api.Configuration

    @Singleton
    class SMSConfig @Inject()(configuration: Configuration) {
      val accountSid = configuration.getString("sms.account.sid").getOrElse("aaf98f894d328b13014d6b4a88b0295e")
      val app_token = configuration.getString("sms.account.token").getOrElse("437c2c92e5be4639b7033391caa66ff8")

      val appId = configuration.getString("sms.app.id").getOrElse("8a48b5514db9e13d014dbd1171d101f4")
      val templateId = configuration.getString("sms.template.id").getOrElse("22164")
      //val
      val message_url_pre = configuration.getString("sms.host").getOrElse("https://app.cloopen.com:8883")

      val send_url = message_url_pre + s"/2013-12-26/Accounts/$accountSid/SMS/TemplateSMS"
    }

    case class SMSBody(to: String, datas: Seq[String], appId: String, templateId: String)

    implicit val smsBodyFormat = Json.format[SMSBody]

    /**
      * umeng push sub service
      */

    import play.api.libs.json.JsValue

    case class UmengMessage(
                             appkey: String,
                             timestamp: String,
                             `type`: String = "broadcast", //默认广播
                             device_tokens: Option[String] = None,
                             // alias_type
                             // alias
                             // file_id
                             // filter
                             payload: JsValue,
                             //policy:  //TODO: add policy logic
                             production_mode: String = "true",
                             description: String
                             //third_party_id:
                           )

    case class iOSPayload(
                           aps: APNS,
                           message_type: String,
                           message: String
                         )

    case class APNS(
                     alert: String, //消息文本
                     badge: Option[String] = None,
                     sound: Option[String] = Some("default"),
                     content_available: Option[String] = None,
                     category: Option[String] = None
                   )

    implicit val apnsFormat = Json.format[APNS]
    implicit val iOSPayloadFormat = Json.format[iOSPayload]
    implicit val umengMessageFormat = Json.format[UmengMessage]


  }

}




