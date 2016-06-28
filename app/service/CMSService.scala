package service

import javax.inject.{Inject, Singleton}

import dao._
import play.api.Logger

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import utils.JsonFormat._

/**
  * Created by likaili on 28/6/2016.
  */
@Singleton
class CMSService @Inject()(uRLDao: URLDao, urlPoolDao: UrlPoolDao) {
  def user_list = {
    uRLDao.unpass_list
  }

  def user_submit(url_id: Long, category: String = "全部") = {
    uRLDao.submit_pass(url_id, category)
  }

  def system_list = {
    urlPoolDao.list
  }

  def system_submit(url_id: Long, user_id: Long, url: String, title: String, description: String, cover_url: String, category: String) = {
    for {
      id <- uRLDao.create(user_id, url, title, description, 0, cover_url)
      _ <- uRLDao.submit_pass(id, category)
      _ <- urlPoolDao.submit(url_id)
    } yield id
  }

  def editor_submit(user_id: Long, url: String, title: String, description: String, cover_url: String, category: String) = {
    for {
      id <- uRLDao.create(user_id, url, title, description, 0, cover_url)
      _ <- uRLDao.submit_pass(id, category)
    } yield id
  }

  def uncategory_list = {
    uRLDao.uncategory_list
  }

  def set_category(url_id: Long, category: String) = {
    uRLDao.update_category(url_id, category)
  }
}
