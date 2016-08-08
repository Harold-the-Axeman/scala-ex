package com.getgua.ws.daos

import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import com.getgua.ws.models._
import play.api.db._

/**
  * Created by kailili on 29/6/16.
  */

@Singleton
class SmsCodeDao @Inject()(@NamedDatabase("ws") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  //import slick.driver.MySQLDriver.api._

  def create(telephone: String, code: String): Future[Int] = {
    //val query = SmsCodeTable.map(s => (s.telepohone, s.code)) +=(telephone, code)
    val query = SmsCodeTable.map(s => (s.telephone, s.code )).insertOrUpdate((telephone, code))

    db.run(query)
  }

  def check(telephone: String, code: String) = {
    //TODO: sms code can only be used once
    //val query = SmsCodeTable.filter(s => s.telephone === telephone && s.code === code && s.is_check === 0).result.headOption
    val query = SmsCodeTable.filter(s => s.telephone === telephone && s.code === code).result.headOption

    db.run(query)
  }

  def set_pass(telephone: String) = {
    val query = SmsCodeTable.filter(_.telephone === telephone).map(_.is_check).update(1)

    db.run(query)
  }
}
