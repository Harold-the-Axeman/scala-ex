package com.getgua.qdb.services

import javax.inject.{Inject, Singleton}
import com.getgua.qdb.daos._

/**
  * Created by likaili on 21/6/2016.
  */
@Singleton
class SystemLogService @Inject()(systemLogDao: SystemLogDao, locationLogDao: LocationLogDao) {
  def submit(ls: Seq[(Long, String, String)]) = systemLogDao.submit(ls)

  def location(user_id: Long, address: String) = locationLogDao.create(user_id, address)
}
