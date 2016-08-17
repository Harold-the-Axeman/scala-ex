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
class NavProfileDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def list(user_id: Long) = {
    val query = NavProfileTable.filter(_.user_id === user_id).result

    db.run(query)
  }

  // (name, url, url_cover, pos)
  def deleteAndInsert(user_id: Long, ns: Seq[(String, String, String, Int)]) = {
    val nsu = ns.map(n => (user_id, n._1, n._2, n._3, n._4))
    val query = (for {
      _ <- NavProfileTable.filter(_.user_id === user_id).delete
      ret <- NavProfileTable.map(n => (n.user_id, n.name, n.url, n.url_cover, n.pos)) ++= nsu
    } yield ret).transactionally

    db.run(query)
  }
}
