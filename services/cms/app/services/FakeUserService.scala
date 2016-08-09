package com.getgua.cms.services

import javax.inject.{Inject, Singleton}

import com.getgua.cms.daos._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSCookie}
import play.api.mvc.Cookie

/**
  * Created by likaili on 8/8/2016.
  */
@Singleton
class FakeUserService @Inject() (userDao: UserDao, wSClient: WSClient) {

  val dataWatchLogger = Logger("data.watch")

  def doFake = {
    login.map{ c => like_users(c) }
  }

  def login = {
    userDao.get_fake_user.flatMap{ u =>
      val url = "http://101.201.33.198/login"

      val data = Json.obj(
        "client_id" -> u.client_id,
        "auth_type" -> u.auth_type,
        "third_party_id" -> u.third_party_id,
        "name" -> u.name,
        "avatar" -> u.avatar
      )

      wSClient.url(url).post(data).map{r =>
        //dataWatchLogger.info(Json.prettyPrint(r.json))
        dataWatchLogger.info(r.cookie("PLAY_SESSION").get.toString)
        r.cookie("PLAY_SESSION").get
      }
    }
  }

  def like_users(cookie: WSCookie) = {
    userDao.get_users.map( us =>
      us.map { u =>
        val url = s"http://101.201.33.198/relation/add?to_id=${u.id}"
        //NOTE: set update time here
        dataWatchLogger.info(s"CMS: user-like, $url")
        userDao.set_update_time(u.id)
        wSClient.url(url).withHeaders("Cookie" -> cookie.toString).get.map { r =>
          dataWatchLogger.info(Json.stringify(r.json))
          r.json
        }
      })
  }
}
