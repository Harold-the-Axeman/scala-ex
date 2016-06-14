package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UrlService @Inject() (urlDao: URLDao, submitDao: SubmitDao) {

  /**
    * 用户分享URL
    * @param user_id
    * @param url
    * @param title
    * @param description
    * @param anonymous
    * @return
    */
  def create(user_id: Long, url: String, title: String, description: String, anonymous: Int): Future[Long] = {
    for {
      url_id <- urlDao.create(user_id, url, title, description, anonymous)
      _ <- submitDao.create(user_id, url_id, description, anonymous)
      _ <- urlDao.submit_count(url_id)
    } yield url_id
  }

  def list(user_id: Long) = urlDao.list(user_id)

  def feeds = urlDao.feeds

  def comments(url_id: Long) = urlDao.comments(url_id: Long)
}
