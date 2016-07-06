package com.getgua.controllers

import javax.inject.Inject

import play.api._
import com.getgua.daos._
import com.getgua.services._
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**  Submit
  *  user: url_id
  *  system: url_id, url, user_id, description, category
  *  editor: url, user_id, description, category
  */
case class CMSSubmit(url_id: Option[Long], url: Option[String], user_id: Option[Long], title: Option[String], cover_url: Option[String], description: Option[String], category: Option[String], score: Int)

object CMSConfig{
  val code = "411e3fa7c5dc344f84e97abe952190ee"
}

/**
  * Created by likaili on 28/6/2016.
  */
class CMSController @Inject() (cMSService: CMSService) extends Controller {
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

