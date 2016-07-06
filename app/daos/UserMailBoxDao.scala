package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
  * Created by kailili on 6/20/16.
  */


/**
  *
  * @param dbConfigProvider
  */
@Singleton
class UserMailBoxDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def create(sender_id: Long, user_id: Long, message_type: Int, message: String) = {
    val query = UserMailboxTable.map(m => (m.sender_id, m.user_id, m.message_type, m.message)) +=(sender_id, user_id, message_type, message)

    db.run(query)
  }

  def list(user_id: Long) = {
    val query = UserMailboxTable.filter(m => (m.user_id === user_id) && (m.sender_id =!= user_id)).result

    db.run(query)
  }

  /* def delete(user_id: Long) = {
     val query = UserMailboxTable.filter(_.user_id === user_id).delete

     db.run(query)
   }*/
}

