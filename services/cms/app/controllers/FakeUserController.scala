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
  * Created by likaili on 9/8/2016.
  */
class FakeUserController @Inject() (fakeUserService: FakeUserService) extends Controller{
  def login = Action.async {
    fakeUserService.login.map{ c =>
      fakeUserService.like_users(c)
      JsonOk
    }
  }
}
