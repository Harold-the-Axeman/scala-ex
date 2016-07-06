package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 28/6/2016.
  */
@Singleton
class CMSService @Inject()(uRLDao: URLDao, urlPoolDao: UrlPoolDao) {
  def user_list = {
    uRLDao.unpass_list
  }

  def user_submit(url_id: Long, category: String, score: Int) = {
    uRLDao.submit_pass(url_id, category, score)
  }

  def system_list = {
    urlPoolDao.list
  }

  def system_submit(url_id: Long, user_id: Long, url: String, title: String, description: String, cover_url: String, category: String, score: Int) = {
    for {
      id <- uRLDao.create(user_id, url, title, description, 0, cover_url)
      _ <- uRLDao.submit_pass(id, category, score)
      _ <- urlPoolDao.submit(url_id, score)
    } yield id
  }

  def editor_submit(user_id: Long, url: String, title: String, description: String, cover_url: String, category: String, score: Int) = {
    for {
      id <- uRLDao.create(user_id, url, title, description, 0, cover_url)
      _ <- uRLDao.submit_pass(id, category, score)
    } yield id
  }

  def uncategory_list = {
    uRLDao.uncategory_list
  }

  def set_category(url_id: Long, category: String) = {
    uRLDao.update_category(url_id, category)
  }
}
