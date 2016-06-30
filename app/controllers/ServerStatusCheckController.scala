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

import scala.collection.immutable.TreeMap
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

@Singleton
class ServerInfo @Inject() (configuration: Configuration)  {
  val code =  configuration.getString("qidian.server.code").getOrElse("woshixiaolu")
  val version = configuration.getString("qidian.server.version").getOrElse("unknown version")
  val hosts = configuration.getStringSeq("qidian.server.hosts").getOrElse(Seq("127.0.0.1"))

  val parameters =  TreeMap(
    "db_host" -> "slick.dbs.default.db.url",
    "push.app.key" -> "push.app.key",
    "push.url.send" -> "push.url.send",
    "wechat.app.id" -> "wechat.app.id",
    "wechat.grant.type" -> "wechat.grant.type",
    "proxy.url" -> "proxy.url",
    "proxy.code" -> "proxy.code",
    "sms.account.sid" -> "sms.account.sid",
    "sms.app.id" -> "sms.app.id",
    "sms.host" -> "sms.host"
  )

  val push_user_id = configuration.getLong("qidian.server.push.user").getOrElse(1026L)
  val telephone = configuration.getString("qidian.server.sms.user").getOrElse("18610150806")
}

/**
  * Created by likaili on 30/6/2016.
  */
class ServerStatusCheckController @Inject() (ws: WSClient, configuration: Configuration, uMengPushService: UMengPushService, navigatorDao: NavigatorDao, pushUserDao: PushUserDao, serverInfo: ServerInfo, smsCodeService: SmsCodeService) extends Controller {
  def info(code: String) = Action {
    code == serverInfo.code match {
      case true => {
        val rs = serverInfo.hosts.map{ h =>
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
          pushr <- uMengPushService.unicast(serverInfo.push_user_id, s"Push测试:$host", "", "push_server_test")
          smsr <- smsCodeService.send(serverInfo.telephone, serverInfo.push_user_id.toString)
        } yield (dbr, pushr, smsr)

        response.map{ r =>
          Ok(Json.obj("version" -> version, "host" -> host, "parameters" -> Json.toJson(parameters)
            , "db_test_reulst" -> r._1, "push_test_result" -> r._2, "sms_test_result" -> r._3))
        }
        // push test
      }
      case false => Future.successful(JsonError)
    }
  }

  def check_token(code: String, id: Long) = Action.async {
    code == serverInfo.code match {
      case true => {
        pushUserDao.get(id).map(r => JsonOk(Json.obj("t" -> r)))
      }
      case false => Future.successful(JsonError)
    }
  }
}
