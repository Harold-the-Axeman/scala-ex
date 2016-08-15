package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
  * Created by kailili on 21/6/15.
  */

@Singleton
class SystemLogDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def submit(ls: Seq[(Long, String, String)]) = {
    // NOTE:
    val tag = DigestUtils.sha1Hex(ls(1)._1.toString)
    val nls = ls.map(l => (tag, l._2, l._3))

    val query = SystemLogTable.map(s => (s.user_tag, s.log_type, s.meta_data)) ++= nls

    db.run(query)
  }
}
