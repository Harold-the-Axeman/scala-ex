package controllers

import javax.inject.Inject

import play.api._
import dao._
import service._
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._
import utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

@Singleton
class QidianProxy @Inject() (configuration: Configuration, wSClient: WSClient) {
  val proxy_url = "http://127.0.0.1:9000/proxy"
  val code = "woshixiaolu"

  def getResponse(response: WSResponse) = {
    (response.json \ "data").as[JsValue]
  }

  def sendRequest(request: ProxyRequest):Future[WSResponse] = {
    wSClient.url(proxy_url).post(Json.toJson(request))
  }
  /**
    *
    * @param url
    * @param headers
    * @param queryString
    * @return
    */
  def get(url: String, headers: Seq[(String, String)] = Seq(), queryString: Seq[(String, String)] = Seq()) = {
    val request = ProxyRequest(code, "GET", url, headers, queryString, JsNull)
    sendRequest(request)
  }

  /**
    *
    * @param url
    * @param headers
    * @param queryString
    * @param body
    * @return
    */
  def post(url: String, headers: Seq[(String, String)] = Seq(), queryString: Seq[(String, String)] = Seq(), body: JsValue = JsNull) = {
    val request = ProxyRequest(code, "POST", url, headers, queryString, body)
    sendRequest(request)
  }
}

case class ProxyRequest(code: String, method: String, url: String, headers: Seq[(String, String)], queryString: Seq[(String, String)],  body: JsValue)

/**
  * Created by likaili on 30/6/2016.
  */
class ProxyController @Inject() (wSClient: WSClient, qidianProxy: QidianProxy) extends Controller {
  def proxy = Action.async(parse.json[ProxyRequest]) { implicit request =>
    val data = request.body

    data.code == qidianProxy.code match {
      case true => {
        val request = wSClient.url(data.url).withQueryString(data.queryString: _*).withHeaders(data.headers:_*)

        data.method match {
          case "GET" => request.get().map(r => JsonOk(r.json))
          case "POST" => request.post(data.body).map(r => JsonOk(r.json))
          case _ => Future.successful(JsonError)
        }
      }
      case false => Future.successful(JsonError)
    }
  }
}
