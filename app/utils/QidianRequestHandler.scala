package utils

import javax.inject.Inject
import play.api.http._
import play.api.mvc.RequestHeader

/**
  * Created by likaili on 14/6/2016.
  */


/*class VirtualHostRequestHandler @Inject() (errorHandler: HttpErrorHandler,
                                           configuration: HttpConfiguration, filters: HttpFilters
                                           //fooRouter: foo.Routes, barRouter: bar.Routes
                                          ) extends DefaultHttpRequestHandler(
  errorHandler, configuration, filters
) {

  override def routeRequest(request: RequestHeader) = {
    request.host match {
      //case "foo.example.com" => fooRouter.routes.lift(request)
      //case "bar.example.com" => barRouter.routes.lift(request)
      case _ => super.routeRequest(request)
    }
  }
}*/
