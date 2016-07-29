package com.getgua.cms.daos

import javax.inject.{Inject, Singleton}

import com.getgua.cms.models._
import play.api.db.NamedDatabase
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Logger

/**
  * Created by kailili on 28/6/16.
  */
@Singleton
class EditorUrlDao @Inject()(@NamedDatabase("cms") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  val priorities = Seq(5, 3, 1)

  def list: Future[Seq[EditorUrl]] = {
    for {
      r1 <- list_priority(2)
      r2 <- list_priority(1)
      r3 <- list_priority(0)
    } yield (r1 ++ r2 ++ r3)
  }

  def list_priority(priority: Int):Future[Seq[EditorUrl]] = {
    //Logger.info("list priority: " + priority)
    val rand = SimpleFunction.nullary[Int]("rand")
    val query = EditorUrlTable.filter(u => u.priority === priority && u.is_submit === 0).sortBy(x => rand).take(priorities(priority)).result

    db.run(query)
  }

  def submit(id: Long) = {
    val query = EditorUrlTable.filter(_.id === id).map(_.is_submit).update(1)

    db.run(query)
  }
}
