package dao

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._
import org.apache.commons.codec.digest.DigestUtils

/**
  * Created by kailili on 5/11/16.
  */
@Singleton
class URLDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
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

    val query = ( for{
      idOpt <- UrlTable.filter(_.hash === url_hash).map(_.id).result.headOption
      res <- idOpt match {
        case Some(id) => DBIO.successful(id)
        case None =>
          (UrlTable.map( u => (u.url, u.hash, u.owner_id, u.title, u.description, u.is_anonymous, u.cover_url)) returning UrlTable.map(_.id)) += (url, url_hash, user_id, title, description, anonymous, cover_url)
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

  /**
    *
    * @param user_id
    * @return
    */
  def list(user_id: Long) = {
    val query = (for (
      uu <- SubmitTable if uu.user_id === user_id;
      url <- UrlTable if url.id === uu.url_id;
      user <- UserTable if user.id === url.owner_id
    ) yield (url, user)).result

    //println(query.statements.headOption)
    db.run(query).map( r => r.map{
      case (url, user) => UrlUser(url, user)
    } )
  }


  /**
    *
    * @return
    */
  def feeds:Future[Seq[UrlUser]] = {
    val query = ( for (
        //url <- UrlTable.take(500).sortBy(_.id.desc);
        url <- UrlTable.filter(_.is_pass === 1).take(500);
        user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map( r => r.map{
      case (url, user) => UrlUser(url, user)
    })
  }


  /**
    *
    * @param url_id
    * @return
    */
  def comments(url_id: Long): Future[Seq[CommentUser]] = {
    val query = ( for (
      c <- CommentTable if c.url_id === url_id;
      u <- UserTable if c.user_id === u.id
    ) yield (c, u)).result

    db.run(query).map( r => r.map {
      case (c, u) => CommentUser(c, u)
    })
  }

  def unpass_list = {
    val query = UrlTable.filter(_.is_pass === 0).take(20).result

    db.run(query)
  }

  def submit_pass(id: Long, category: String) = {
    val query = UrlTable.filter(_.id === id).map(u => (u.is_pass, u.category)).update((1, category))

    db.run(query)
  }

  def update_category(id: Long, category: String) = {
    val query = UrlTable.filter(_.id === id).map(_.category).update(category)

    db.run(query)
  }

  def uncategory_list = {
    val query = UrlTable.filter(_.category === "全部").take(20).result

    db.run(query)
  }
}
