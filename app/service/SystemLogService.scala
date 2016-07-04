package com.getgua.service

import javax.inject.{Inject, Singleton}

import com.getgua.dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 21/6/2016.
  */
@Singleton
class SystemLogService @Inject() (systemLogDao: SystemLogDao) {
  def submit(ls: Seq[(Long, String, String)]) = systemLogDao.submit(ls)
}
