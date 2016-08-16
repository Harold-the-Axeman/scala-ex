package com.getgua.utils

import javax.inject._

import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent._
import JsonFormat._

/**
  * Created by likaili on 14/6/2016.
  */
@Singleton
class QidianErrorHandler @Inject()(
                                    env: Environment,
                                    config: Configuration,
                                    sourceMapper: OptionalSourceMapper,
                                    router: Provider[Router]) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  val serverErrorLogger = Logger("error.server")
  val clientErrorLogger = Logger("error.client")


  def error_response(request: RequestHeader, message: String) = {
    Json.obj("path" -> request.path, "query" -> request.rawQueryString, "error" -> message)
  }

  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful {
      serverErrorLogger.error(s"${request.method} ${request.uri} and error ${exception.getMessage}")
      JsonServerError("Server Error", error_response(request, exception.getMessage))
    }
  }

  override def onBadRequest(request: RequestHeader, message: String) = {
    Future.successful {
      clientErrorLogger.error(s"${request.method} ${request.uri} and error 400  and message ${message}")
      JsonServerError("BadRequest", error_response(request, message))
    }
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful {
      clientErrorLogger.error(s"${request.method} ${request.uri} and error ${statusCode}  and message ${message}")
      JsonServerError("Client Error", Json.obj("path" -> request.path, "query" -> request.queryString.toString, "status" -> statusCode, "error" -> message))
    }
  }

  override def onForbidden(request: RequestHeader, message: String) = {
    Future.successful {
      clientErrorLogger.error(s"${request.method} ${request.uri} and error 403  and message ${message}")
      JsonServerError("Forbidden", error_response(request, message))
    }
  }

  override def onNotFound(request: RequestHeader, message: String) = {
    Future.successful {
      clientErrorLogger.error(s"${request.method} ${request.uri} and error 404 and message ${message}")
      JsonServerError("Not Found", error_response(request, message))
    }
  }
}


