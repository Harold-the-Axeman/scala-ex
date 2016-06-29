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
import play.api.libs.ws.WSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * AppID：wx765f0c7a2ea0c4b1
AppSecret：f0ffd4a3404676aa09259884955fb7ef
   */

object WeichatConfig  {
  val appid =  "wx0ab15104e2a02d6a"
  val app_secret = "a1a637316fc3a71295a2d9109d19d3dc"
  val authorization_code = "authorization_code"
  val token_url = "http://192.168.1.2:9000/oauth/redirect"
}

/**
  * Created by likaili on 28/6/2016.
  */
class WeichatController @Inject() (wSClient: WSClient) extends Controller{
  def redirect_url(code: String) = Action.async {
    val url = s"https://api.weixin.qq.com/sns/oauth2/access_token?appid=${WeichatConfig.appid}" +
          s"&secret=${WeichatConfig.app_secret}&code=$code&grant_type=${WeichatConfig.authorization_code}"
    wSClient.url(url).get().flatMap{ r => {
        val access_token = (r.json \ "access_token").as[String]
        val openid = (r.json \ "openid").as[String]
        val unionid = (r.json \ "unionid").as[String]
        val user_info_url = "https://api.weixin.qq.com/sns/userinfo"
        wSClient.url(user_info_url).withQueryString("access_token" -> access_token, "openid" -> openid).get().map(x =>
          Ok(Json.obj("openid" -> openid, "unionid" -> unionid, "user_info" -> x.json))
        )
      }
    }
  }
  def auth(code: Option[String], state: Option[String]) = Action.async {
    code match {
      case Some(c) => {
        wSClient.url(WeichatConfig.token_url).withQueryString("code" -> c).get().map{ r =>
          JsonOk(r.json)
        }
      }
      case None => Future.successful(JsonError)
    }
  }
}
