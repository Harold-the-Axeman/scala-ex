package dao

import java.sql.Timestamp
import javax.inject.Inject

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._
import org.apache.commons.codec.digest.DigestUtils


case class URLWithUser(url: Url, user: User)
case class CommentWithUser(comment: Comment, user: User)

/**
  * Created by kailili on 5/11/16.
  */
class URLDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  def create(user_id: Long, url: String, title: String, description: String, anonymous: Int) = {

    val url_hash = DigestUtils.sha1Hex(url)

    val createStatement = ( for{
      idOpt <- UrlTable.filter(_.hash === url_hash).map(_.id).result.headOption
      res <- idOpt match {
        case Some(id) => DBIO.successful(id)
        case None =>
          (UrlTable.map( u => (u.url, u.hash, u.owner_id, u.title, u.description, u.is_anonymous)) returning UrlTable.map(_.id)) += (url, url_hash, user_id, title, description, anonymous)
      }

      // URL submit table
      r_exists <- SubmitTable.filter(s => s.url_id === res && s.user_id === user_id).exists.result

      r <- r_exists match {
        case true => DBIO.successful(0)
        case false => for {
          _ <- SubmitTable.map(s => (s.url_id, s.user_id, s.description)) += (res, user_id, description)
          // column ++
          c <- UrlTable.filter(_.id === res).map(_.submit_count).result.head
          x <- UrlTable.filter(_.id === res).map(_.submit_count).update(c + 1)
        } yield x
      }

    } yield res).transactionally


    db.run(createStatement)
  }

  def list(user_id: Long) = {
    //val query = UrlTable.filter(_.owner === user_id).result

    val query = (for (
      uu <- SubmitTable if uu.user_id === user_id;
      url <- UrlTable if url.id === uu.url_id;
      user <- UserTable if user.id === url.owner_id
    ) yield (url, user)).result

    db.run(query).map( r => r.map{
      case (url, user) => URLWithUser(url, user)
    } )
  }

  // limit 100
  def feeds:Future[Seq[URLWithUser]] = {
    val query = ( for (
        url <- UrlTable.take(100);
        user <- UserTable if url.owner_id === user.id
    ) yield (url, user)).result

    db.run(query).map( r => r.map{
      case (url, user) => URLWithUser(url, user)
    } )
  }

  // comments list

  def comments(url_id: Long): Future[Seq[CommentWithUser]] = {
    val query = ( for (
      c <- CommentTable if c.url_id === url_id;
      u <- UserTable if c.user_id === u.id
    ) yield (c, u)).result

    db.run(query).map( r => r.map {
      case (c, u) => CommentWithUser(c, u)
    })
  }
}
