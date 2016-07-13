package com.getgua.cms.services

/**
  * Created by likaili on 13/7/2016.
  */
import javax.inject.{Inject, Singleton}

import com.getgua.cms.daos._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger

@Singleton
class FeedsProducerService @Inject()(urlDao: UrlDao, cMSService: CMSService, editorUrlDao: EditorUrlDao) {

  def submit = {
    Logger.info("service submit")
    editorUrlDao.list.map{ eul =>
      println(eul.length)
      eul.map { eu =>
        val user_id = 1026
        for {
          url_id <- cMSService.editor_submit(user_id, eu.url, eu.title, eu.description, eu.cover_url, eu.category, 1)
          _ <- editorUrlDao.submit(eu.id)
          _ <- urlDao.set_priority_property(url_id, eu.property, eu.priority)
        } yield url_id
      }
    }
  }
}
