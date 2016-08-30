package com.getgua.qdb.daos

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import com.getgua.qdb.models._
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 5/11/16.
  */
@Singleton
class FeedsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  def latest_feeds = {
    val now = System.currentTimeMillis - 600000
    val nowt = new Timestamp(now)
    val query = (for (
      url <- UrlTable.filter(u => u.is_pass === 1 && u.create_time > nowt).sortBy(_.id.desc).take(1);
      user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map(r => r.map {
      case (url, user) => UrlUser(url, user)
    })
  }

  def random_feeds = {
    val rand = SimpleFunction.nullary[Int]("rand")
    val now = System.currentTimeMillis - 86400000
    val nowt = new Timestamp(now)
    val randt = scala.util.Random.nextInt(300000) + 300000
    val now_up = System.currentTimeMillis - randt
    val nowt_up = new Timestamp(now_up)
    val query = (for (
      url <- UrlTable.filter(u => u.is_pass === 1 && u.priority === 2 && u.create_time < nowt).sortBy(x => rand).take(3);
      user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map(r => r.map {
      case (url, user) => UrlUser(url.copy(create_time = nowt_up), user)
    })
  }

  def priority_feeds: Future[Seq[UrlUser]] = {
    val now = System.currentTimeMillis - 86400000
    val now_up = System.currentTimeMillis - 600000
    val nowt = new Timestamp(now)
    val nowt_up = new Timestamp(now_up)
    val query = (for (
      url <- UrlTable.filter(u => u.is_pass === 1 && u.priority === 2 && u.create_time > nowt && u.create_time < nowt_up).sortBy(_.id.desc).take(5);
      user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map(r => r.map {
      case (url, user) => UrlUser(url, user)
    })
  }

  def common_feeds: Future[Seq[UrlUser]] = {
    val now = System.currentTimeMillis - 600000
    val nowt = new Timestamp(now)
    val query = (for (
    //url <- UrlTable.take(500).sortBy(_.id.desc);
      url <- UrlTable.filter(u => u.is_pass === 1 && u.priority =!= 2 && u.create_time < nowt).sortBy(_.id.desc).take(300);
      user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map(r => r.map {
      case (url, user) => UrlUser(url, user)
    })
  }

  def social_feeds(user_id: Long) = {
    val now = System.currentTimeMillis - 600000
    val nowt = new Timestamp(now)
    val query = (for (
      uu <- SubmitTable if uu.user_id === user_id;
      url <- UrlTable.filter(u => u.is_pass === 0) if url.id === uu.url_id;
      user <- UserTable if user.id === url.owner_id
    ) yield (url, user)).result

    db.run(query).map(r => r.map {
      case (url, user) => UrlUser(url, user)
    })
  }

  def feeds(category: String): Future[Seq[UrlUser]] = {
    val query = (for (
    //url <- UrlTable.take(500).sortBy(_.id.desc);
      url <- UrlTable.filter(u => u.is_pass === 1 && u.category === category).sortBy(_.id.desc).take(300);
      user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map(r => r.map {
      case (url, user) => UrlUser(url, user)
    })
  }
}
