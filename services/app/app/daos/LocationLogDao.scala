package com.getgua.daos

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import com.getgua.models._
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

import org.apache.commons.codec.digest.DigestUtils

/**
  * Created by kailili on 8/11/16.
  */
@Singleton
class LocationLogDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  def create(user_id: Long, address: String) = {
    val tag = DigestUtils.sha1Hex(user_id.toString)

    val query = LocationLogTable.map(l => (l.user_tag, l.address)) += (tag, address)

    db.run(query)
  }
}
