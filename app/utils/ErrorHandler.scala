package utils

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
class ErrorHandler @Inject() (
   env: Environment,
   config: Configuration,
   sourceMapper: OptionalSourceMapper,
   router: Provider[Router]) extends DefaultHttpErrorHandler(env, config, sourceMapper, router){

  override def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful {
      JsonServerError("Server Error", Json.obj("path" -> request.path, "query" -> request.queryString.toString, "error" -> exception.getMessage))
    }
  }

  override def onBadRequest(request: RequestHeader, message: String) = {
    Future.successful{
      JsonServerError("BadRequest",Json.obj("path" -> request.path, "query" -> request.queryString.toString, "error" -> message))
    }
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful{
      JsonServerError("Client Error", Json.obj("path" -> request.path, "query" -> request.queryString.toString, "status" -> statusCode, "error" -> message))
    }
  }

  override def onForbidden(request: RequestHeader, message: String) = {
    Future.successful{
      JsonServerError("Forbidden", Json.obj("path" -> request.path, "query" -> request.queryString.toString, "error" -> message))
    }
  }

  override def onNotFound(request: RequestHeader, message: String) = {
    Future.successful{
      JsonServerError("Not Found", Json.obj("path" -> request.path, "query" -> request.queryString.toString, "error" -> message))
    }
  }
}


