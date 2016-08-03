package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 6/8/15.
  */


/**
  *
  * @param dbConfigProvider
  */
@Singleton
class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  //import slick.driver.MySQLDriver.api._

  /**
    *
    * @param id
    * @return
    */
  def profile(id: Long): Future[UserProfile] = {
    //val query = UserTable.filter(_.id === id).result.headOption

    val query = UserTable.filter(_.id === id).result.head


    //println(query.statements.headOption)
    db.run(query).map {
      case u => UserProfile(u)
    }
  }

  def get(id: Long): Future[User] = {
    val query = UserTable.filter(_.id === id).result.head

    db.run(query)
  }

  /**
    *
    * @param id
    * @return
    */
  def submit_count(id: Long) = {
    val query = (for {
      c <- UserTable.filter(_.id === id).map(_.submit_count).result.head
      r <- UserTable.filter(_.id === id).map(_.submit_count).update(c + 1)
    } yield r).transactionally

    db.run(query)
  }

  /**
    *
    * @param id
    * @return
    */
  def comment_count(id: Long, op: Int) = {
    val query = (for {
      c <- UserTable.filter(_.id === id).map(_.comment_count).result.head
      r <- UserTable.filter(_.id === id).map(_.comment_count).update(c + op)
    } yield r).transactionally

    db.run(query)
  }

  /**
    *
    * @param id
    * @return
    */
  def like_count(id: Long, op: Int) = {
    val query = (for {
      c <- UserTable.filter(_.id === id).map(_.like_count).result.head
      r <- UserTable.filter(_.id === id).map(_.like_count).update(c + op)
    } yield r).transactionally

    db.run(query)
  }
}
