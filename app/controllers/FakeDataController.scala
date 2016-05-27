package controllers

import javax.inject.Inject

import play.api._
import play.api.mvc._
import org.apache.commons.codec.digest.DigestUtils
import dao._
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
  * Created by kailili on 5/26/16.
  */

class FakeDataController extends Controller {

  def FakeUser = Unit

  def FakeUrl = Unit

  def FakeSummit = Unit

  def FakeComment = Unit
}
