package com.getgua.qdb.daos

import javax.inject.{Inject, Singleton}

import com.getgua.qdb.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 17/8/15.
  */

@Singleton
class UserVersioningDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def update(user_id: Long, version: String) = {
    val query = UserVersioningTable.map(v => (v.user_id, v.version)).insertOrUpdate((user_id, version))

    db.run(query)
  }
}
