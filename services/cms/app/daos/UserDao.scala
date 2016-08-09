package com.getgua.cms.daos

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import com.getgua.cms.models._
import org.apache.commons.codec.digest.DigestUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

/**
  * Created by kailili on 9/8/16.
  */
@Singleton
class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  //import slick.driver.MySQLDriver.api._

  import driver.api._

  // 获取一个系统用户
  def get_fake_user = {
    val rand = SimpleFunction.nullary[Int]("rand")
    val query = UserTable.filter(_.auth_type === "qidian").sortBy(x => rand).result.head

    db.run(query)
  }

  // val now = System.currentTimeMillis - 600000
  // val nowt = new Timestamp(now)

  def set_update_time(user_id: Long) = {
    val nowt = new Timestamp(System.currentTimeMillis)
    val query = UserTable.filter(_.id === user_id).map(_.update_time).update(nowt)

    db.run(query)
  }

  // 获取真实用户
  def get_users = {
    val now = System.currentTimeMillis - 86400000
    val nowt = new Timestamp(now)

    val query = UserTable.filter(u => u.auth_type =!= "qidian" && u.update_time < nowt).take(20).result

    db.run(query)
  }
}
