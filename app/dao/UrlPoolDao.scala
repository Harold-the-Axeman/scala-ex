package dao

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.CMSTables._
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.NamedDatabase

/**
  * Created by kailili on 28/6/16.
  */
@Singleton
class UrlPoolDao @Inject() (@NamedDatabase("cms") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  def list = {
    val query = UrlPoolTable.filter(_.review_passed === 0).take(20).result

    db.run(query)
  }

  def submit(id: Long) = {
    val query = UrlPoolTable.filter(_.id === id).map(_.review_passed).update(1)

    db.run(query)
  }
}
