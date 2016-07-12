package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._

/**
  * Created by likaili on 21/6/2016.
  */
@Singleton
class SystemLogService @Inject()(systemLogDao: SystemLogDao) {
  def submit(ls: Seq[(Long, String, String)]) = systemLogDao.submit(ls)
}
