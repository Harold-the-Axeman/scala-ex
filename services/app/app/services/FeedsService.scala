package com.getgua.services

import javax.inject.{Inject, Singleton}

import com.getgua.daos._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class FeedsService @Inject()(feedsDao: FeedsDao, urlLikeDao: UrlLikeDao, userDao: UserDao) {

  def feeds(user_id: Long) = {
    //println("feeds")
    for {
      ul <- latest_feeds(user_id)
      ur <- random_feeds(user_id)
      pu <- priority_feeds(user_id)
      cu <- common_feeds(user_id)

      // user activity status refreshing, make push system does not send push message to this users.
      _ <- userDao.set_update_time(user_id)
    } yield ul ++ ur ++ pu ++ cu

    //feeds.toSet.toSeq.sortWith(_.uu.url.id > _.uu.url.id)
  }

  def latest_feeds(user_id: Long) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      ul <- feedsDao.latest_feeds.map(u => u.map(x => UrlUserStatus(x, us.contains(x.url.id), "latest_feeds")))
    } yield ul
  }

  def random_feeds(user_id: Long) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      ur <- feedsDao.random_feeds.map(u => u.map(x => UrlUserStatus(x, us.contains(x.url.id), "random_feeds")))
    } yield ur
  }

  // feeds part 1
  def priority_feeds(user_id: Long) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      up <- feedsDao.priority_feeds.map(u => u.map(x => UrlUserStatus(x, us.contains(x.url.id), "priority_feeds")))
    } yield up
  }

  // feeds part 2: Reviewed Urls and User's (or social) Url
  def common_feeds(user_id: Long) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      uf <- feedsDao.common_feeds.map(u => u.map(x => UrlUserStatus(x, us.contains(x.url.id), "common_feeds")))
      uu <- feedsDao.social_feeds(user_id).map(u => u.map(x => UrlUserStatus(x, us.contains(x.url.id), "social_feeds")))
      //no need to remove duplicate keys here
      ul = (uf ++ uu).sortWith(_.uu.url.id > _.uu.url.id)
    } yield ul
  }

  def feeds_category(user_id: Long, category: String) = {
    for {
      us <- urlLikeDao.url_list(user_id)
      ul <- feedsDao.feeds(category).map {
        u => u.map(x => UrlUserStatus(x, us.contains(x.url.id), "category_feeds"))
      }
    } yield ul
  }

}
