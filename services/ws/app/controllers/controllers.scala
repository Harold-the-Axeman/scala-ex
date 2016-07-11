

/**
  * Created by likaili on 10/7/2016.
  */

package com.getgua.ws {

  import javax.inject.Inject

  import com.google.inject.Singleton
  import controllers.MessageSubmit
  import play.api.Configuration
  import play.api.libs.json.Json

  package object controllers {
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

    implicit val proxyRequestFormat = Json.format[ProxyRequest]

    /**
      * Message
      */
    implicit val messageSubmitFormat = Json.format[MessageSubmit]
  }
}
