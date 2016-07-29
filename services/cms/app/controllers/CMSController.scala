package com.getgua.cms.controllers

import javax.inject.Inject

import com.getgua.cms.services._
import com.getgua.utils.JsonFormat._
import play.api.cache.{CacheApi, NamedCache}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by likaili on 28/6/2016.
  */
class Application @Inject() (@NamedCache("push-cache") pushCache: CacheApi, cache: CacheApi) extends Controller {
  def test = Action {
    cache.set("item.key", "this is the item key")
    pushCache.set("qidian.push.key", "this is key from push-cache", 1 days)

    //Ok(cache.get[String]("item.key").getOrElse("nothing in cache"))
    Ok(pushCache.get[String]("qidian.push.key").getOrElse("nothing in push cache"))
  }
}

class CMSController @Inject()(cMSService: CMSService) extends Controller {
  def user_list(code: String) = Action.async {
    code == CMSConfig.code match {
      case true => {
        cMSService.user_list.map(r => JsonOk(Json.toJson(r)))
      }
      case false => Future.successful(JsonError)
    }
  }

  def user_submit(code: String) = Action.async(parse.json[CMSSubmit]) { implicit request =>
    code == CMSConfig.code match {
      case true => {
        val data = request.body
        cMSService.user_submit(data.url_id.get, data.category.get, data.score).map(r => JsonOk(Json.toJson(r)))
      }
      case false => Future.successful(JsonError)
    }
  }

  def system_list(code: String) = Action.async {
    code == CMSConfig.code match {
      case true => {
        cMSService.system_list.map(r => JsonOk(Json.toJson(r)))
      }
      case false => Future.successful(JsonError)
    }
  }

  def system_submit(code: String) = Action.async(parse.json[CMSSubmit]) { implicit request =>
    code == CMSConfig.code match {
      case true => {
        val data = request.body
        cMSService.system_submit(data.url_id.get, data.user_id.get, data.url.get,
          data.title.get, data.description.get, data.cover_url.get, data.category.get, data.score).map(r => JsonOk(Json.toJson(r)))
      }
      case false => Future.successful(JsonError)
    }
  }

  def editor_submit(code: String) = Action.async(parse.json[CMSSubmit]) { implicit request =>
    code == CMSConfig.code match {
      case true => {
        val data = request.body
        cMSService.editor_submit(data.user_id.get, data.url.get, data.title.get, data.description.get,
          data.cover_url.get, data.category.get, data.score).map(r => JsonOk(Json.toJson(r)))
      }
      case false => Future.successful(JsonError)
    }
  }

  def uncategory_list(code: String) = Action.async {
    code == CMSConfig.code match {
      case true => {
        cMSService.uncategory_list.map(r => JsonOk(Json.toJson(r)))
      }
      case false => Future.successful(JsonError)
    }
  }

  def set_category(code: String, url_id: Long, category: String) = Action.async {
    code == CMSConfig.code match {
      case true => {
        cMSService.set_category(url_id, category).map(r => JsonOk)
      }
      case false => Future.successful(JsonError)
    }
  }
}

