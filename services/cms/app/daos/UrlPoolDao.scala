package com.getgua.cms.daos

import javax.inject.{Inject, Singleton}

import com.getgua.cms.models._
import play.api.db.NamedDatabase
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
  * Created by kailili on 28/6/16.
  */
@Singleton
class UrlPoolDao @Inject()(@NamedDatabase("cms") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
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
