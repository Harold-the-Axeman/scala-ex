package com.getgua.utils

import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router

import scala.concurrent._
import JsonFormat._
import play.api.libs.json.Json

/**
  * Created by likaili on 14/6/2016.
  */
@Singleton
class QidianErrorHandler @Inject()(
   env: Environment,
   config: Configuration,
   sourceMapper: OptionalSourceMapper,
   router: Provider[Router]) extends DefaultHttpErrorHandler(env, config, sourceMapper, router){

  def error_response(request: RequestHeader, message: String) = {
      Json.obj("path" -> request.path, "query" -> request.rawQueryString, "error" -> message)
  }

  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful {
      Logger.info(s"${request.method} ${request.uri} took ${request}ms and error ${exception.getMessage}")
      JsonServerError("Server Error", error_response(request, exception.getMessage))
    }
  }

  override def onBadRequest(request: RequestHeader, message: String) = {
    Future.successful{
      JsonServerError("BadRequest", error_response(request, message))
    }
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful{
      JsonServerError("Client Error", Json.obj("path" -> request.path, "query" -> request.queryString.toString, "status" -> statusCode, "error" -> message))
    }
  }

  override def onForbidden(request: RequestHeader, message: String) = {
    Future.successful{
      JsonServerError("Forbidden", error_response(request, message))
    }
  }

  override def onNotFound(request: RequestHeader, message: String) = {
    Future.successful{
      JsonServerError("Not Found", error_response(request, message))
    }
  }
}


