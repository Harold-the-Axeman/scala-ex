package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import dao.CommentWithUser
import play.api.Configuration


/**
  * Created by likaili on 13/6/2016.
  */


case class Auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name: Option[String], avatar: Option[String])

case class UrlSubmit(user_id: Option[Long], url: String, title: String, description: String, anonymous: Int, cover_url: String)
case class CommentSubmit(url_id: Long, content: String, user_id: Option[Long], at_user_id: Option[Long])

case class UrlCollection(user_id: Option[Long], url: String, title: Option[String])

case class SubmitLog(user_id: Option[Long], log_type: String, meta_data: String)

case class SubmitLogs(logs: Seq[SubmitLog])

//user_id: Long, text: String, message: String, message_type: String
case class PushMessage(user_id: Option[Long], text: String, message: String, message_type: String, code: String = PushServerCheck.code)

object PushServerCheck{
  val code = "33d60800441663873b190641607fe978"
}

@Singleton
class ServerInfo @Inject() (configuration: Configuration)  {
  val code =  configuration.getString("qidian.server.code").get
  val version = configuration.getString("qidian.server.version").get
  val hosts = configuration.getStringSeq("qidian.server.hosts").get


  val parameters =  Map(
    "db_host" -> "slick.dbs.default.db.url",
    "push_app" -> "push.appkey",
    "push_server" -> "push.server_url",
    "weixin" -> "weixin.server_url",
    "sms" -> "sms.server_url"
  )

  val push_user_id = configuration.getLong("qidian.server.push.user").get
}

