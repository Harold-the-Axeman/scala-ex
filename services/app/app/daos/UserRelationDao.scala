package com.getgua.daos

import javax.inject.{Inject, Singleton}

import com.getgua.models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by kailili on 6/9/15.
  */
@Singleton
class UserRelationDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def add(from: Long, to: Long): Future[Int] = {
    val query = UserRelationTable.map(r => (r.from, r.to, r.is_like)).insertOrUpdate((from, to, 0))

    db.run(query)
  }

  def delete(from: Long, to: Long): Future[Int] = {
    //val query = UserRelationTable.filter(r => (r.from === from && r.to === to)).delete
    val query = UserRelationTable.map(r => (r.from, r.to, r.is_like)).insertOrUpdate((from, to, -1))

    db.run(query)
  }

  def list(user_id: Long): Future[Seq[User]] = {
    val query = (for (
      r <- UserRelationTable if r.from === user_id && r.is_like === 0;
      u <- UserTable if u.id === r.to
    ) yield u).result

    db.run(query)
  }

  def is_like(from: Long, to: Long): Future[Boolean] = {
    val query = UserRelationTable.filter(r => (r.from === from && r.to === to && r.is_like === 0)).exists.result

    db.run(query)
  }
}

