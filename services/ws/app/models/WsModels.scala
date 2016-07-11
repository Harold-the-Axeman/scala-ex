package com.getgua.ws.models

import slick.driver.MySQLDriver.api._

/**
  * Created by likaili on 8/7/2016.
  */

/** Entity class storing rows of table SmsCodeTable
  *  @param telephone Database column telephone SqlType(VARCHAR), PrimaryKey, Length(32,true)
  *  @param code Database column code SqlType(VARCHAR), Length(16,true), Default(0614)
  *  @param is_check Database column is_check SqlType(INT), Default(0)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class SmsCode(telephone: String, code: String = "0614", is_check: Int = 0, create_time: java.sql.Timestamp)
/** Table description of table sms_code. Objects of this class serve as prototypes for rows in queries. */
class SmsCodeTable(_tableTag: Tag) extends Table[SmsCode](_tableTag, "sms_code") {
  def * = (telephone, code, is_check, create_time) <> (SmsCode.tupled, SmsCode.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(telephone), Rep.Some(code), Rep.Some(is_check), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> SmsCode.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column telephone SqlType(VARCHAR), PrimaryKey, Length(32,true) */
  val telephone: Rep[String] = column[String]("telephone", O.PrimaryKey, O.Length(32,varying=true))
  /** Database column code SqlType(VARCHAR), Length(16,true), Default(0614) */
  val code: Rep[String] = column[String]("code", O.Length(16,varying=true), O.Default("0614"))
  /** Database column is_check SqlType(INT), Default(0) */
  val is_check: Rep[Int] = column[Int]("is_check", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table UserMailboxTable
  *
  * @param id           Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param user_id      Database column user_id SqlType(BIGINT)
  * @param message_type Database column message_type SqlType(INT)
  * @param message      Database column message SqlType(VARCHAR), Length(8192,true), Default()
  * @param create_time  Database column create_time SqlType(TIMESTAMP) */
case class UserMailbox(id: Long,  user_id: Long, message_type: Int, message: String = "", create_time: java.sql.Timestamp)

/** Table description of table user_mailbox. Objects of this class serve as prototypes for rows in queries. */
class UserMailboxTable(_tableTag: Tag) extends Table[UserMailbox](_tableTag, "user_mailbox") {
  def * = (id,  user_id, message_type, message, create_time) <>(UserMailbox.tupled, UserMailbox.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(user_id), Rep.Some(message_type), Rep.Some(message), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => UserMailbox.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column message_type SqlType(INT) */
  val message_type: Rep[Int] = column[Int]("message_type")
  /** Database column message SqlType(VARCHAR), Length(8192,true), Default() */
  val message: Rep[String] = column[String]("message", O.Length(8192, varying = true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table PushUserTable
  *
  * @param id           Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param user_id      Database column user_id SqlType(BIGINT)
  * @param device_token Database column device_token SqlType(VARCHAR), Length(64,true), Default()
  * @param device_type  Database column device_type SqlType(VARCHAR), Length(16,true), Default()
  * @param create_time  Database column create_time SqlType(TIMESTAMP) */
case class PushUser(id: Long, user_id: Long, device_token: String = "", device_type: String = "", create_time: java.sql.Timestamp)

/** Table description of table push_user. Objects of this class serve as prototypes for rows in queries. */
class PushUserTable(_tableTag: Tag) extends Table[PushUser](_tableTag, "push_user") {
  def * = (id, user_id, device_token, device_type, create_time) <>(PushUser.tupled, PushUser.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(user_id), Rep.Some(device_token), Rep.Some(device_type), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => PushUser.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column device_token SqlType(VARCHAR), Length(64,true), Default() */
  val device_token: Rep[String] = column[String]("device_token", O.Length(64, varying = true), O.Default(""))
  /** Database column device_type SqlType(VARCHAR), Length(16,true), Default() */
  val device_type: Rep[String] = column[String]("device_type", O.Length(16, varying = true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table MessageStatusTable
  *  @param user_id Database column user_id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  *  @param unread Database column unread SqlType(INT), Default(0)
  *  @param update_time Database column update_time SqlType(TIMESTAMP) */
case class MessageStatus(user_id: Long, unread: Int = 0, update_time: java.sql.Timestamp)

/** Table description of table message_status. Objects of this class serve as prototypes for rows in queries. */
class MessageStatusTable(_tableTag: Tag) extends Table[MessageStatus](_tableTag, "message_status") {
  def * = (user_id, unread, update_time) <> (MessageStatus.tupled, MessageStatus.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(user_id), Rep.Some(unread), Rep.Some(update_time)).shaped.<>({r=>import r._; _1.map(_=> MessageStatus.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column user_id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val user_id: Rep[Long] = column[Long]("user_id", O.AutoInc, O.PrimaryKey)
  /** Database column unread SqlType(INT), Default(0) */
  val unread: Rep[Int] = column[Int]("unread", O.Default(0))
  /** Database column update_time SqlType(TIMESTAMP) */
  val update_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("update_time")
}
