package com.getgua.cms.task

import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

import akka.actor._
import play.api.Logger
import com.getgua.cms.services.FeedsProducerService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.getgua.cms.models._

/**
  * Created by likaili on 13/7/2016.
  */

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport


class FeedsModule extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[FeedsProducer]("feeds-actor")
  }
}

/*object FeedsProducer {
  def props = Props[FeedsProducer]
}*/

class FeedsProducer @Inject()(feedsProducerService: FeedsProducerService) extends  Actor{
  //import FeedsProducer._



  def receive = {
    case SubmitCommand(name: String) =>
      val now = Calendar.getInstance().getTime()
      val minuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val currentMinuteAsString = minuteFormat.format(now)
      //count = count + 1
      dataWatchLogger.info("Schedule Feeds Producer Task at: " + currentMinuteAsString)
      //sender() ! "Hello, " + name
      feedsProducerService.submit
      //ul.map( l => l.foreach(println(_)))
  }
}
