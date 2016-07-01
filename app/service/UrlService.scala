package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UrlService @Inject() (urlDao: URLDao, submitDao: SubmitDao, userDao: UserDao, urlLikeDao: UrlLikeDao, commentLikeDao: CommentLikeDao) {

  /**
    * 用户分享URL
    *
    * @param user_id
    * @param url
    * @param title
    * @param description
    * @param anonymous
    * @return
    */
  def create(user_id: Long, url: String, title: String, description: String, anonymous: Int, cover_url: String): Future[Long] = {
    for {
      url_id <- urlDao.create(user_id, url, title, description, anonymous, cover_url)
      _ <- submitDao.create(user_id, url_id, description, anonymous)
      _ <- urlDao.submit_count(url_id)
      _ <- userDao.submit_count(user_id)
    } yield url_id
  }

  def list(user_id: Long) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      ul <- urlDao.list(user_id).map(_.sortWith(_.url.id > _.url.id))
        .map{
          u => u.map( x => UrlUserStatus(x, us.contains(x.url.id)))
        }
    } yield ul

  }

  //TODO: , category: String
  def feeds(user_id: Long) = {
    //.map(_.sortWith(_.url.id > _.url.id))
    for {
      uf <- urlDao.feeds
      uu <- urlDao.list(user_id)
      us <- urlLikeDao.url_list(user_id)

      ul  = (uf ++ uu).map(x => UrlUserStatus(x, us.contains(x.url.id))).toSet.toSeq.sortWith(_.uu.url.id > _.uu.url.id)
    } yield ul
  }

  def feeds_category(user_id: Long, category: String) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      ul <- urlDao.feeds(category).map(_.sortWith(_.url.id > _.url.id))
        .map{
            u => u.map( x => UrlUserStatus(x, us.contains(x.url.id)))
        }
    } yield ul
  }

  def comments(url_id: Long, user_id: Long): Future[Seq[CommentUserStatus]] = {
    for {
      cs <- commentLikeDao.comment_list(user_id)
      cu <- urlDao.comments(url_id: Long).map(_.sortWith(_.comment.id > _.comment.id))
        .map{
          c => c.map(x => CommentUserStatus(x, cs.contains(x.comment.id)))
        }
      //cb <- cu.
    } yield cu
  }

  def get(id: Long, user_id: Long) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      u <- urlDao.get(id).map( x => UrlStatus(x, us.contains(x.id)))
    } yield u
  }
}
