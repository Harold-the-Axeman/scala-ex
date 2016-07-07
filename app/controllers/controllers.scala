/**
  * Created by likaili on 13/6/2016.
  */

package com.getgua {

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

    implicit val proxyRequestFormat = Json.format[ProxyRequest]

    case class Auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name: Option[String], avatar: Option[String])

    case class UrlSubmit(user_id: Option[Long], url: String, title: String, description: String, anonymous: Int, cover_url: String)

    case class CommentSubmit(url_id: Long, content: String, user_id: Option[Long], at_user_id: Option[Long])

    case class UrlCollection(user_id: Option[Long], url: String, title: Option[String])

    case class SubmitLog(user_id: Option[Long], log_type: String, meta_data: String)

    /**
      * CMS
      */

    case class SubmitLogs(logs: Seq[SubmitLog])

    /**
      * Wechat
      */
    @Singleton
    class WeichatConfig @Inject()(configuration: Configuration) {
      val appid = configuration.getString("wechat.app.id").getOrElse("wx0ab15104e2a02d6a")
      val app_secret = configuration.getString("wechat.app.secret").getOrElse("a1a637316fc3a71295a2d9109d19d3dc")
      val grant_type = configuration.getString("wechat.grant.type").getOrElse("authorization_code")
    }



    /**
      * Proxy
      */

    import play.api.libs.json.{JsNull, JsValue}
    import play.api.libs.ws.{WSClient, WSResponse}

    import scala.concurrent.Future

    @Singleton
    class QidianProxy @Inject()(configuration: Configuration, wSClient: WSClient) {
      val url = configuration.getString("proxy.url").get
      //"http://127.0.0.1:9001/proxy"
      val code = configuration.getString("proxy.code").get //"33d60800441663873b190641607fe978"

      def getResponse(response: WSResponse) = {
        (response.json \ "data").as[JsValue]
      }

      def get(url: String, headers: Map[String, String] = Map(), queryString: Map[String, String] = Map()) = {
        val request = ProxyRequest(code, "GET", url, headers, queryString, JsNull)
        sendRequest(request)
      }

      def sendRequest(request: ProxyRequest): Future[WSResponse] = {
        wSClient.url(url).post(Json.toJson(request))
      }

      def post(url: String, headers: Map[String, String] = Map(), queryString: Map[String, String] = Map(), body: JsValue = JsNull) = {
        val request = ProxyRequest(code, "POST", url, headers, queryString, body)
        sendRequest(request)
      }
    }

    case class ProxyRequest(code: String, method: String, url: String, headers: Map[String, String], queryString: Map[String, String], body: JsValue)

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
  }
}





