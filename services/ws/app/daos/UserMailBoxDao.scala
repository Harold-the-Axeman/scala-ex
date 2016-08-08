package com.getgua.ws.daos

import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import com.getgua.ws.models._
import play.api.db._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by kailili on 6/20/16.
  */


/**
  *
  * @param dbConfigProvider
  */
@Singleton
class UserMailBoxDao @Inject()(@NamedDatabase("ws") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def create(user_id: Long, message_type: Int, message: String) = {
    val query = UserMailboxTable.map(m => (m.user_id, m.message_type, m.message)) +=(user_id, message_type, message)

    db.run(query)
  }

  def list(user_id: Long) = {
    val query = UserMailboxTable.filter(_.user_id === user_id).result

    db.run(query)
  }

  def status_set(user_id:Long, status:Int) = {
    val query = MessageStatusTable.map(m => (m.user_id, m.unread)).insertOrUpdate((user_id, status))

    db.run(query)
  }

  def status_get(user_id: Long) = {
    val query = MessageStatusTable.filter(_.user_id === user_id).map(_.unread).result.headOption

    db.run(query).map(r => r match {
      case Some(1) => true
      case _ => false
    })
  }
}

