package controllers

import javax.inject.Inject

import com.google.inject.Singleton
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
@Singleton
class WeichatConfig @Inject() (configuration: Configuration) {
  val appid = configuration.getString("wechat.app.id").getOrElse("wx0ab15104e2a02d6a")
  val app_secret = configuration.getString("wechat.app.secret").getOrElse("a1a637316fc3a71295a2d9109d19d3dc")
  val grant_type = configuration.getString("wechat.grant.type").getOrElse("authorization_code")
}

/**
  * Created by likaili on 28/6/2016.
  */
class WeichatController @Inject() (wSClient: WSClient, weichatConfig: WeichatConfig, authService: AuthService, qidianProxy: QidianProxy) extends Controller{

  def auth(code: String, state: Option[String], client_id: String, redirect_urL: String) = Action.async {
    val url = s"https://api.weixin.qq.com/sns/oauth2/access_token?appid=${weichatConfig.appid}" +
      s"&secret=${weichatConfig.app_secret}&code=$code&grant_type=${weichatConfig.grant_type}"

    qidianProxy.get(url).flatMap{ x => {
      val r = qidianProxy.getResponse(x)
      val access_token = (r \ "access_token").as[String]
      val openid = (r \ "openid").as[String]
      val unionid = (r \ "unionid").as[String]

      val user_info_url = "https://api.weixin.qq.com/sns/userinfo"
      (for {
        //r <- wSClient.url(user_info_url).withQueryString("access_token" -> access_token, "openid" -> openid).get()
        x <- qidianProxy.get(user_info_url, queryString = Map("access_token" -> access_token, "openid" -> openid))
        r = qidianProxy.getResponse(x)
        name = (r \ "nickname").as[String]
        avatar = (r \ "headimgurl").as[String]
        id <- authService.auth_login(client_id, "wechat", unionid, name, avatar)
      } yield (r, id)).map( x =>
        //JsonOk(Json.obj("user_id" -> x._2, "unionid" -> unionid, "user_info" -> x._1)).withSession("id" -> r.toString)
        Redirect(redirect_urL + x._2.toString).withSession("id" -> r.toString)
      )
      }
    }
  }
}
