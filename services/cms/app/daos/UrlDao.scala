package com.getgua.cms.daos

import javax.inject.{Inject, Singleton}

import com.getgua.cms.models._
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

/**
  * Created by kailili on 5/11/16.
  */
@Singleton
class UrlDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  /**
    * 创建一条用户提交的URL, 如果其他用户已经提交不再提交
    *
    * @param user_id
    * @param url
    * @param title
    * @param description
    * @param anonymous
    * @return
    */

  def create(user_id: Long, url: String, title: String, description: String, anonymous: Int, cover_url: String) = {
    val url_hash = DigestUtils.sha1Hex(url)

    val query = (for {
      idOpt <- UrlTable.filter(_.hash === url_hash).map(_.id).result.headOption
      res <- idOpt match {
        case Some(id) => DBIO.successful(id)
        case None =>
          (UrlTable.map(u => (u.url, u.hash, u.owner_id, u.title, u.description, u.is_anonymous, u.cover_url)) returning UrlTable.map(_.id)) +=(url, url_hash, user_id, title, description, anonymous, cover_url)
      }
    } yield res).transactionally

    db.run(query)
  }

  def get(id: Long) = {
    val query = UrlTable.filter(_.id === id).result.head

    db.run(query)
  }

  /**
    *
    * @param id
    * @return
    */
  def submit_count(id: Long) = {
    val query = (for {
      c <- UrlTable.filter(_.id === id).map(_.submit_count).result.head
      r <- UrlTable.filter(_.id === id).map(_.submit_count).update(c + 1)
    } yield r).transactionally

    db.run(query)
  }

  /**
    *
    * @param id
    * @return
    */
  def comment_count(id: Long) = {
    val query = (for {
      c <- UrlTable.filter(_.id === id).map(_.comment_count).result.head
      r <- UrlTable.filter(_.id === id).map(_.comment_count).update(c + 1)
    } yield r).transactionally

    db.run(query)
  }

  def like_count(id: Long, op: Int) = {
    val query = (for {
      c <- UrlTable.filter(_.id === id).map(_.like_count).result.head
      r <- UrlTable.filter(_.id === id).map(_.like_count).update(c + op)
    } yield r).transactionally

    db.run(query)
  }


  def unpass_list = {
    val query = UrlTable.filter(_.is_pass === 0).sortBy(_.id.desc).take(20).result

    db.run(query)
  }

  def submit_pass(id: Long, category: String, score: Int) = {
    val query = UrlTable.filter(_.id === id).map(u => (u.is_pass, u.category)).update((score, category))

    db.run(query)
  }

  def update_category(id: Long, category: String) = {
    val query = UrlTable.filter(_.id === id).map(_.category).update(category)

    db.run(query)
  }

  def uncategory_list = {
    val query = UrlTable.filter(_.category === "全部").sortBy(_.id.desc).take(20).result

    db.run(query)
  }

  def set_priority_property(id: Long, property: String, priority: Int) = {
    val query = UrlTable.filter(_.id === id).map(u => (u.property, u.priority)).update((property, priority))

    db.run(query)
  }

  /**
    * fix cover Image
    */
  def cover_image_list = {
    val query = UrlTable.filter(_.cover_url like "%http%").filterNot(_.cover_url like "%cdn.gotgua.com%").take(20).result

    db.run(query)
  }

  def set_cover_image(id: Long, url: String) = {
    val query = UrlTable.filter(_.id === id).map(_.cover_url).update(url)

    db.run(query)
  }
}
