package com.getgua.controllers

import javax.inject.{Inject, Singleton}

import play.api._
import com.getgua.daos._
import com.getgua.services._
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by likaili on 30/6/2016.
  */
class ProxyController @Inject() (wSClient: WSClient, qidianProxy: QidianProxy) extends Controller {
  def proxy = Action.async(parse.json[ProxyRequest]) { implicit request =>
    val data = request.body

    data.code == qidianProxy.code match {
      case true => {
        val request = wSClient.url(data.url).withQueryString(data.queryString.toSeq: _*).withHeaders(data.headers.toSeq:_*)

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
