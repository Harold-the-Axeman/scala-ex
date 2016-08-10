package com.getgua.cms.controllers

import java.net.URL
import javax.inject._

import akka.actor.{ActorRef, ActorSystem}
import com.getgua.cms.daos.UrlDao
import com.getgua.cms.services._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._
import com.getgua.utils.aliyun.QidianOSS
import play.api.Logger

/**
  * Created by likaili on 10/8/2016.
  */
class AliyunController @Inject() (oss: QidianOSS, urlDao: UrlDao)extends Controller {
  def test = Action {
    val url = "http://pic.sc.chinaz.com/files/pic/pic9/201603/apic19657.jpg"

    Ok(oss.putNetworkObject(url))
    //Ok(configuration.getString("oss.end_point").get)
  }

  val dataWatchLogger = Logger("data.watch")

  def fixCoverImage = Action.async {
    urlDao.cover_image_list.map{ r =>
      r.map{ u =>
        try {
          val url = new URL("http://www.baidu.com")
          val in = url.openStream

          val key_name = oss.putNetworkObject(u.cover_url)
          val new_url = s"http://cdn.gotgua.com/$key_name@!large"
          urlDao.set_cover_image(u.id, new_url)
        } catch  {
          case e: Exception => {
            dataWatchLogger.info(s"/cms/oss/fix $u.id")
            urlDao.set_cover_image(u.id, "")
          }
        }
      }
      JsonOk(Json.obj("image_processed" -> r.length))
    }
  }

}
