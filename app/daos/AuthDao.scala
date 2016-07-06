package com.getgua.daos

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import com.getgua.models._

/**
  * Created by kailili on 6/3/15.
  */



/**
  *
  * @param dbConfigProvider
  */
@Singleton
class AuthDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  //import slick.driver.MySQLDriver.api._

  //NOTE: auth_type and third_party_id == "", is an anonymous user
  def uuid_exists(client_id: String) = UserTable.filter(u => u.client_id === client_id && u.auth_type === "" && u.third_party_id === "" ).map(_.id).result.headOption
  def uuid_create(client_id: String) = (UserTable.map(u => (u.client_id)) returning UserTable.map(_.id)) += (client_id)

  def social_exists(client_id: String, auth_type: String, third_party_id: String) = UserTable.filter(u => u.auth_type === auth_type && u.third_party_id === third_party_id).map(_.id).result.headOption
  def social_update_client_id(client_id: String, auth_type: String, third_party_id: String, u: Long) = {
    for {
      _ <- UserTable.filter(u => u.auth_type === auth_type && u.third_party_id === third_party_id).map(u => (u.client_id)).update((client_id))
      i <- DBIO.successful(u)
    } yield i
  }

  def social_update_social_info(client_id: String, auth_type: String, third_party_id: String, iu: Long, name: String, avatar: String) = {
    for {
      _ <- UserTable.filter(u => u.client_id === client_id && u.auth_type === "" && u.third_party_id === "" ).map(u => (u.auth_type, u.third_party_id, u.name, u.avatar)).update((auth_type, third_party_id, name, avatar))
      x <- DBIO.successful(iu)
    } yield x
  }

  def social_create(client_id: String, auth_type: String, third_party_id: String, name: String, avatar: String) = (UserTable.map(u => (u.client_id, u.auth_type, u.third_party_id, u.name, u.avatar)) returning UserTable.map(_.id)) += (client_id, auth_type, third_party_id, name, avatar)

  /**
    *
    */
  def telephone_exists(telephone: String) = {
    val query = UserTable.filter(u => u.auth_type === "qidian-telephone" && u.third_party_id === telephone).exists.result

    db.run(query)
  }

  /**
    *
    * @param client_id
    * @return
    */
  def uuid_login(client_id: String): Future[Long] = {
    val query = (for {
      idOpt <- uuid_exists(client_id)
      id <- idOpt match {
        case Some(i) => DBIO.successful(i)
        case None => uuid_create(client_id)
      }
    } yield id).transactionally

    db.run(query)
  }

  /**
    *
    * @param client_id
    * @param auth_type
    * @param third_party_id
    * @param name
    * @param avatar
    * @return
    */
  def auth_login(client_id: String, auth_type: String, third_party_id: String, name: String, avatar: String): Future[Long] = {
    val query = (for {
      idOpt <- social_exists(client_id, auth_type, third_party_id)
      id <- idOpt match {
          // is a Qidian User, update client_id
        case Some(u) => social_update_client_id(client_id, auth_type, third_party_id, u)
          // a new Qidian User, try to merge with an anonymous user
        case None => for {
          // if client_id already exists,
          idOpt <- uuid_exists(client_id)
          idu: Long <- idOpt match {
            case Some(iu) => social_update_social_info(client_id, auth_type, third_party_id, iu, name, avatar)
            case None => social_create(client_id, auth_type, third_party_id, name, avatar)
          }
        } yield idu
      }
    } yield id).transactionally

    db.run(query)
  }

}

