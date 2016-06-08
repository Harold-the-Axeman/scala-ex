package dao

import javax.inject.Inject

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._
import sun.font.TrueTypeFont

/**
  * Created by kailili on 6/9/15.
  */

class UserRelationDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def add(from: Long, to: Long) = {
    val query = UserRelationTable.map(r => (r.from, r.to)) += (from, to)

    db.run(query)
  }

  def delete(from: Long, to: Long) = {
    val query = UserRelationTable.filter(r => (r.from === from && r.to === to)).delete

    db.run(query)
  }

  def list(user_id: Long): Future[Seq[User]] = {
    val query = ( for(
        r <- UserRelationTable if r.from === user_id;
        u <- UserTable if u.id === r.to
    ) yield u ).result

    db.run(query)
  }

  def is_like(from: Long, to: Long):Future[Boolean] = {
    val query = UserRelationTable.filter(r => (r.from === from && r.to === to)).exists.result

    db.run(query)
  }
}

