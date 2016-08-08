package com.getgua.ws.services

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.cache.{CacheApi, _}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._

/**
  * Created by likaili on 8/8/2016.
  */
@Singleton
class UserPushCounterService @Inject() (@NamedCache("push-cache") pushCache: CacheApi, cache: CacheApi) {
  def get(user_id: Long) = {
    cache.get[Long](user_id.toString) match {
      case Some(s) => true
      case None => false
    }
  }

  def set(user_id: Long) = {
    val now = System.currentTimeMillis
    //NOTE: 12 hours, twice a day
    cache.set(user_id.toString, now, 12 hours)
  }
}
