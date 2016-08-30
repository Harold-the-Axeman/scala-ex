package com.getgua.qdb.services

import javax.inject.{Inject, Singleton}
import com.getgua.qdb.daos._

/**
  * Created by likaili on 21/6/2016.
  */
@Singleton
class SectionsService @Inject()(sectionsDao: SectionsDao) {

  def list(category: String) = {
    sectionsDao.list(category)
  }
}
