package com.getgua.daos

import javax.inject.{Inject, Singleton}

import play.api.db.NamedDatabase
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import com.getgua.models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by kailili on 18/7/16.
  */
@Singleton
class UserRegisterTrackingDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._
  //import slick.driver.MySQLDriver.api._

  def create(user_id: Long, from: String) = {
    val query = (for {
      exists <- UserRegisterTrackingTable.filter(_.user_id === user_id).exists.result
      ret <- exists match {
        case false => UserRegisterTrackingTable.map(u => (u.user_id, u.from)).insertOrUpdate((user_id, from))
        case true => DBIO.successful(0)
      }
    } yield ret).transactionally

    db.run(query)
  }

}
