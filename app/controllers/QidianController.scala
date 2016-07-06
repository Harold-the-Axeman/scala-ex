package com.getgua.controllers

import javax.inject.Inject

import play.api._
import org.apache.commons.codec.digest.DigestUtils
import com.getgua.daos._
import com.getgua.service._
import play.api.cache.Cache
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Security.{Authenticated, AuthenticatedBuilder, AuthenticatedRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by likaili on 14/6/2016.
  */


abstract  class QidianController extends Controller {

  //in a Security trait
  def user_id(request: RequestHeader) = request.session.get("id")
  def onUnauthenticated(request: RequestHeader) = JsonAuthError

  object AuthenticatedAction extends AuthenticatedBuilder(req => user_id(req), onUnauthenticated)

  def QidianAction = AuthenticatedAction

  def QidianAuthAction = Action
}
