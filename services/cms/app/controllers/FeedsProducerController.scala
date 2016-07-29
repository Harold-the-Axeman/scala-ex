package com.getgua.cms.controllers

import javax.inject._

import akka.actor.{ActorRef, ActorSystem}
import com.getgua.cms.services._
import com.getgua.utils.JsonFormat._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future
import com.getgua.cms.task._
import akka.pattern.ask

import scala.concurrent.duration._
import akka.util.Timeout.durationToTimeout
import com.getgua.cms.services.FeedsProducerService

/**
  * Created by likaili on 13/7/2016.
  */
@Singleton
//You never want two actor with the same name.
class FeedsProducerController @Inject() (@Named("feeds-actor") feedsProducerActor: ActorRef, system: ActorSystem, feedsProducerService: FeedsProducerService, feedsSchedule: FeedsSchedule) extends Controller {
  
  implicit val timeout = durationToTimeout(5.seconds)

  def test = Action.async {
    (feedsProducerActor ? SubmitCommand("小鹿")).mapTo[String].map { message =>
      Ok(message)
    }
  }

  def test_service = Action.async {
    feedsProducerService.submit.map(r => Ok("work"))
  }

  def start = Action {
    //system.scheduler.schedule(0.minutes, 30.minutes, feedsProducerActor, SubmitCommand("Feeds Produce"))
    val fs = feedsSchedule.start
    Ok(s"Feeds Producer Actor Started: ${fs.toString}")
  }

  def stop = Action {
    val fs = feedsSchedule.stop
    Ok(s"Feeds Producer Actor Stop: ${fs.toString}")
  }

  def status = Action {
    val fs = feedsSchedule.status
    Ok(s"Feeds Producer Actor Status: ${fs.toString}")
  }
}
