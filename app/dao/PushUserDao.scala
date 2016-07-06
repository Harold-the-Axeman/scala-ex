package com.getgua.dao

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import com.getgua.models._

/**
  * Created by kailili on 23/6/16.
  */

@Singleton
class PushUserDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  //import slick.driver.MySQLDriver.api._

  def add(user_id: Long, device_token: String, device_type: String) = {
    val query = PushUserTable.map(p => (p.user_id, p.device_token, p.device_type)) += (user_id, device_token, device_type)

    db.run(query)
  }

  def get(user_id: Long): Future[Option[String]] = {
    val query = PushUserTable.filter(_.user_id === user_id).sortBy(_.id.desc).map(_.device_token).result.headOption

    db.run(query)
  }

}
