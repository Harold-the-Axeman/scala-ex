package com.getgua.dao

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import com.getgua.models._

/**
  * Created by kailili on 30/6/15.
  */

@Singleton
class UrlLikeDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def add(user_id: Long, url_id: Long) = {
    val query = UrlLikeTable.map(u => (u.user_id, u.url_id)) += (user_id, url_id)

    db.run(query)
  }

  def delete(user_id: Long, url_id: Long) = {
    val query = UrlLikeTable.filter(u => u.user_id === user_id && u.url_id === url_id).delete

    db.run(query)
  }

  def list(url_id: Long) = {
    val query = (for(
      u <- UrlLikeTable if u.url_id === url_id;
      user <- UserTable if user.id === u.user_id
    ) yield user).result

    db.run(query)
  }

  def url_list(user_id: Long): Future[Seq[Long]] = {
    val query = UrlLikeTable.filter(_.user_id === user_id).map(_.url_id).result

    db.run(query)
  }
}
