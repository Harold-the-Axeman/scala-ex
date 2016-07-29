/**
  * Created by likaili on 13/6/2016.
  */

package com.getgua {

  import play.api.Configuration

  package object controllers {

    import javax.inject.Inject

    import com.google.inject.Singleton
    import play.api.Configuration
    import play.api.libs.json.Json

    implicit val authFormat = Json.format[Auth]
    implicit val urlSubmitFormat = Json.format[UrlSubmit]
    implicit val commentSubmitFormat = Json.format[CommentSubmit]
    implicit val urlCollectionFormat = Json.format[UrlCollection]
    implicit val submitLogFormat = Json.format[SubmitLog]
    implicit val submitLogsFormat = Json.format[SubmitLogs]


    case class Auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name: Option[String], avatar: Option[String])
    case class UrlSubmit(user_id: Option[Long], url: String, title: String, description: String, anonymous: Int, cover_url: String)
    case class CommentSubmit(url_id: Long, content: String, user_id: Option[Long], at_user_id: Option[Long])
    case class UrlCollection(user_id: Option[Long], url: String, title: Option[String])
    case class SubmitLog(user_id: Option[Long], log_type: String, meta_data: String)
    case class SubmitLogs(logs: Seq[SubmitLog])

    /**
      * Server Info
      */
    import scala.collection.immutable.TreeMap

    @Singleton
    class ServerInfo @Inject()(configuration: Configuration) {
      val code = configuration.getString("qidian.server.code").getOrElse("woshixiaolu")
      val version = configuration.getString("qidian.server.version").getOrElse("unknown version")
      val hosts = configuration.getStringSeq("qidian.server.hosts").getOrElse(Seq("127.0.0.1"))

      val parameters = TreeMap(
        "app.db" -> "slick.dbs.default.db.url",
        "cms.db" -> "slick.dbs.cms.db.url",
        "push.app.key" -> "push.app.key",
        "push.url.send" -> "push.url.send",
        "wechat.app.id" -> "wechat.app.id",
        "wechat.grant.type" -> "wechat.grant.type",
        "proxy.url" -> "proxy.url",
        //"proxy.code" -> "proxy.code",
        "sms.account.sid" -> "sms.account.sid",
        "sms.app.id" -> "sms.app.id",
        "sms.host" -> "sms.host"
      )

      val push_user_id = configuration.getLong("qidian.server.push.user").getOrElse(1026L)
      val telephone = configuration.getString("qidian.server.sms.user").getOrElse("18610150806")
    }

 /*   @Singleton
    class WSConfig @Inject() (configuration: Configuration) {
      val ws_url = configuration.getString("qidian.ws.url").get //"http://127.0.0.1:9000"
    }*/
  }
}





