package com.getgua.cms.task

import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.{Inject, Named, Singleton}

import akka.actor._
import play.api.Logger
import com.getgua.cms.services.{FakeUserService, FeedsProducerService}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.getgua.cms.models._
import com.getgua.cms.services.AliyunService

/**
  * Created by likaili on 13/7/2016.
  */

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import scala.concurrent.duration._

class FeedsModule extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[FeedsProducer]("feeds-actor")
  }
}

class FeedsProducer @Inject()(feedsProducerService: FeedsProducerService, fakeUserService: FakeUserService, aliyunService: AliyunService) extends  Actor{

  def receive = {
    case SubmitCommand(name: String) =>
      val now = Calendar.getInstance().getTime()
      val minuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val currentMinuteAsString = minuteFormat.format(now)
      //count = count + 1
      dataWatchLogger.info("Schedule Feeds Producer Task at: " + currentMinuteAsString)

      // Task 1
      feedsProducerService.submit

      // Task 2
      for (i <- 1 to 30) {
        fakeUserService.doFake
      }

      // Task 3
      aliyunService.fix_image
  }
}

//TODO: Make it a trait.
@Singleton
class FeedsSchedule @Inject() (@Named("feeds-actor") feedsProducerActor: ActorRef, system: ActorSystem){

  var cancellable: Cancellable = null

  def start: Cancellable = synchronized {
    if (cancellable == null) {
      //Logger.info("Restart Actor")
      cancellable = system.scheduler.schedule(0 minutes, 30 minutes, feedsProducerActor, SubmitCommand(""))
    }
    cancellable
  }

  def stop: Boolean = synchronized {
    if (cancellable != null) {
      cancellable.cancel()
    } else {
      false
    }
  }

  def status = {
    cancellable
  }
}
