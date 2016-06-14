package controllers

import javax.inject.Inject

import play.api._
import org.apache.commons.codec.digest.DigestUtils
import dao._
import service._
import play.api.cache.Cache
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc._
import utils.JsonFormat._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by likaili on 14/6/2016.
  */


abstract  class QidianController extends Controller {


  def QidianAction = Action
}
