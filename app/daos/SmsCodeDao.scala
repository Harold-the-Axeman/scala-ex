package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 29/6/16.
  */

@Singleton
class SmsCodeDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  //import slick.driver.MySQLDriver.api._

  def exists(telephone: String): Future[Boolean] = {
    val query = SmsCodeTable.filter(_.telepohone === telephone).exists.result

    db.run(query)
  }

  def create(telephone: String, code: String): Future[Int] = {
    val query = SmsCodeTable.map(s => (s.telepohone, s.code)) +=(telephone, code)

    db.run(query)
  }

  def update(telephone: String, code: String): Future[Int] = {
    val query = SmsCodeTable.filter(_.telepohone === telephone).map(_.code).update(code)

    db.run(query)
  }

  def check(telephone: String, code: String) = {
    val query = SmsCodeTable.filter(s => s.telepohone === telephone && s.code === code).result.headOption

    db.run(query)
  }

  def set_pass(id: Long) = {
    val query = SmsCodeTable.filter(_.id === id).map(_.is_check).update(1)

    db.run(query)
  }
}
