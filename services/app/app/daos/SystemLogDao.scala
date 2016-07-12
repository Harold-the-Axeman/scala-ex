package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
  * Created by kailili on 21/6/15.
  */

@Singleton
class SystemLogDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def submit(ls: Seq[(Long, String, String)]) = {
    val query = SystemLogTable.map(s => (s.user_id, s.log_type, s.meta_data)) ++= ls

    db.run(query)
  }
}
