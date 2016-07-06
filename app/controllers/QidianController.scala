package com.getgua.controllers

import com.getgua.utils.JsonFormat._
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc._

/**
  * Created by likaili on 14/6/2016.
  */


abstract class QidianController extends Controller {

  //in a Security trait
  def user_id(request: RequestHeader) = request.session.get("id")

  def onUnauthenticated(request: RequestHeader) = JsonAuthError

  object AuthenticatedAction extends AuthenticatedBuilder(req => user_id(req), onUnauthenticated)

  def QidianAction = AuthenticatedAction

  def QidianAuthAction = Action
}
