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


/**
  * Created by kailili on 5/11/16.
  */
class URLDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  def createURL(url: String, user_id: Long) = {

    val url_hash = DigestUtils.sha1Hex(url)

    val createStatement = ( for{
      idOpt <- UrlTable.filter(_.url_hash === url_hash).map(_.id).result.headOption
      res <- idOpt match {
        case Some(id) => DBIO.successful(id)
        case None =>
          (UrlTable.map( u => (u.url, u.url_hash, u.owner)) returning UrlTable.map(_.id)) += (url, url_hash, user_id)
      }
      r_exists <- SubmitTable.filter(s => s.url_id === res && s.user_id === user_id).exists.result

      r <- r_exists match {
        case true => DBIO.successful(0)
        case false => SubmitTable.map(s => (s.url_id, s.user_id)) += (res, user_id)
      }

    } yield res).transactionally


    db.run(createStatement)
  }

  def list(user_id: Long) = {
    val query = UrlTable.filter(_.owner === user_id).result

    db.run(query)
  }
}
