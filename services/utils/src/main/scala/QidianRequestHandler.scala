package com.getgua.utils

import javax.inject.{Inject, Singleton}

import play.api.http._
import play.api.mvc.RequestHeader
import play.api.routing.Router

/**
  * Created by likaili on 14/6/2016.
  */

/**
  * One use case for a custom request handler may be that you want to delegate to a different router,
  * depending on what host the request is for. Here is an example of how this might be done
  */

//TODO: this function is not used. We seperate our services.
@Singleton
class QidianRequestHandler @Inject()(errorHandler: HttpErrorHandler,
                                     configuration: HttpConfiguration,
                                     filters: HttpFilters,
                                     router: Router
                                     //fooRouter: foo.Routes, barRouter: bar.Routes
                                    ) extends DefaultHttpRequestHandler(
  router, errorHandler, configuration, filters
) {

  override def routeRequest(request: RequestHeader) = {
    request.host match {
      //case "foo.example.com" => fooRouter.routes.lift(request)
      //case "bar.example.com" => barRouter.routes.lift(request)
      case _ => super.routeRequest(request)
    }
  }
}
