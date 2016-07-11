package com.getgua.ws.controllers

import javax.inject.Inject

import play.api.libs.ws.WSClient
import play.api.mvc._
import com.getgua.ws.services._
import play.api.libs.json.Json
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 28/6/2016.
  */
class WeichatController @Inject()(wSClient: WSClient, weichatConfig: WeichatConfig) extends Controller {

  def auth(code: String, state: Option[String], client_id: String, redirect_urL: String) = Action.async {
    val url = s"https://api.weixin.qq.com/sns/oauth2/access_token?appid=${weichatConfig.appid}" +
      s"&secret=${weichatConfig.app_secret}&code=$code&grant_type=${weichatConfig.grant_type}"

    wSClient.url(url).get().flatMap { x => {
      val r = x.json
      val access_token = (r \ "access_token").as[String]
      val openid = (r \ "openid").as[String]
      val unionid = (r \ "unionid").as[String]

      val user_info_url = "https://api.weixin.qq.com/sns/userinfo"
      (for {
        x <- wSClient.url(user_info_url).withQueryString("access_token" -> access_token, "openid" -> openid).get()
        //x <- qidianProxy.get(user_info_url, queryString = Map("access_token" -> access_token, "openid" -> openid))
        //r = qidianProxy.getResponse(x)
        r = x.json
        //name = (r \ "nickname").as[String]
        //avatar = (r \ "headimgurl").as[String]
        //id <- authService.auth_login(client_id, "wechat", unionid, name, avatar)
      } yield r).map(x =>
        //JsonOk(Json.obj("user_id" -> x._2, "unionid" -> unionid, "user_info" -> x._1))//.withSession("id" -> r.toString)
        JsonOk(x)
        //Redirect(redirect_urL + x._2.toString).withSession("id" -> r.toString)
      )
    }
    }
  }
}
