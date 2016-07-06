package com.getgua.dao

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import com.getgua.models._
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
    val query = UrlPoolTable.filter(_.review_passed === 0).sortBy(_.id.desc).take(20).result

    db.run(query)
  }

  def submit(id: Long, score: Int) = {
    val query = UrlPoolTable.filter(_.id === id).map(_.review_passed).update(score)

    db.run(query)
  }
}
