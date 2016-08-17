package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 17/8/15.
  */

@Singleton
class SectionProfileDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def list(user_id: Long) = {
    val query = SectionProfileTable.filter(_.user_id === user_id).result

    db.run(query)
  }

  // (type, pos)
  def deleteAndInsert(user_id: Long, ss: Seq[(String, Int)]) = {
    val ssu = ss.map(n => (user_id, n._1, n._2))
    val query = (for {
      _ <- SectionProfileTable.filter(_.user_id === user_id).delete
      ret <- SectionProfileTable.map(s => (s.user_id, s.`type`, s.pos)) ++= ssu
    } yield ret).transactionally

    db.run(query)
  }
}

