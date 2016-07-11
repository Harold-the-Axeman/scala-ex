package com.getgua.controllers

import javax.inject.Inject

import com.getgua.daos._
import com.getgua.services._
import com.getgua.utils.JsonFormat._
import com.google.inject.Singleton
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}



/**
  * Created by likaili on 30/6/2016.
  */
class ServerStatusCheckController @Inject()(ws: WSClient, configuration: Configuration, navigatorDao: NavigatorDao, serverInfo: ServerInfo) extends Controller {
  def info(code: String) = Action {
    code == serverInfo.code match {
      case true => {
        val rs = serverInfo.hosts.map { h =>
          val status_url = s"http://$h:9000/status?code=$code"
          val r = ws.url(status_url).get()
          Await.result(r, 10 seconds).json
        }
        JsonOk(Json.toJson(rs))
      }
      case false => JsonError
    }
  }

  def status(code: String) = Action.async {
    import java.net._
    code == serverInfo.code match {
      case true => {
        val version = serverInfo.version
        val host = InetAddress.getLocalHost.getHostAddress
        val parameters = serverInfo.parameters.map(kv => (kv._1 -> configuration.getString(kv._2).get))

        val response = for {
        // db test
          dbr <- navigatorDao.info.map(x => x.toList(0).websites(0).url)
          // push test
          //pushr <- uMengPushService.unicast(serverInfo.push_user_id, s"Push测试:$host", "", "push_server_test")
          //smsr <- smsCodeService.send(serverInfo.telephone, serverInfo.push_user_id.toString)
        } yield (dbr)

        response.map { r =>
          Ok(Json.obj("version" -> version, "host" -> host, "parameters" -> Json.toJson(parameters)
            , "db_test_reulst" -> r))
        }
      }
      case false => Future.successful(JsonError)
    }
  }

  def check_token(code: String, id: Long) = Action.async {
    code == serverInfo.code match {
      case true => {
        //pushUserDao.get(id).map(r => JsonOk(Json.obj("t" -> r)))
        //TODO: ws call
        Future.successful(JsonOk)
      }
      case false => Future.successful(JsonError)
    }
  }
}
