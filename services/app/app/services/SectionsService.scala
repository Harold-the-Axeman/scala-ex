package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._

/**
  * Created by likaili on 21/6/2016.
  */
@Singleton
class SectionsService @Inject()(sectionsDao: SectionsDao) {

  def list(category: String) = {
    sectionsDao.list(category)
  }
}
