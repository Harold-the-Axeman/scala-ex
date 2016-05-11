package controllers

import play.api._
import play.api.mvc._

import org.apache.commons.codec.digest.DigestUtils
import dao._

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc._
import utils.JsonFormat._

import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import scala.concurrent.duration._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}

class AuthController extends Controller {

}

class UrlController extends Controller {
  def submit(url: String, user: Long) = Action.async{
    //val url = "http://www.baidu.com/index"
    //val owner = 1232

    URLDao.createURL(url, user).map(r => Ok(r.toString))
  }
}