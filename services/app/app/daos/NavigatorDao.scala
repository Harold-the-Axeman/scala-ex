package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 6/3/15.
  */

@Singleton
class NavigatorDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  //import slick.driver.MySQLDriver.api._
  def info: Future[Iterable[NavigatorWithType]] = {
    val query = NavigatorTable.result

    db.run(query).map(nl => nl.groupBy(_.`type`).map {
      case (t, n) => NavigatorWithType(t, n)
    })
  }
}