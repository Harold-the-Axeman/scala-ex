package com.getgua.utils

import javax.inject.Inject

import akka.stream.Materializer
import com.google.inject.Singleton
import play.api.Logger
import play.api.http.HttpFilters
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

//import play.api.http.DefaultHttpFilters

/**
  * Created by likaili on 14/6/2016.
  */

@Singleton
class QidianFilters @Inject()(qidianFilter: QidianFilter) extends HttpFilters {
  def filters = Seq(qidianFilter)
}

@Singleton
class QidianFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>

      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      if (requestTime > 100) Logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")

      result.withHeaders("Request-Time" -> (requestTime.toString + "ms"))
    }
  }
}