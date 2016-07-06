package com.getgua

/**
  * Created by likaili on 6/7/2016.
  */

package object models {

  import java.sql.Timestamp
  import java.util.Date

  import play.api.libs.json._
  import slick.lifted.TableQuery

  /**
    * Special Handle for Timestamp
    */
  implicit val timestampFormat = new Format[Timestamp] {
    def writes(ts: Timestamp): JsValue = JsNumber(new Date(ts.getTime()).getTime)

    def reads(ts: JsValue): JsResult[Timestamp] = {
      try {
        JsSuccess(new Timestamp(ts.as[Long]))
      } catch {
        case e: IllegalArgumentException => JsError("Unable to parse timestamp")
      }
    }
  }

  /**
    * CMS Models
    */
  /** Collection-like TableQuery object for table UrlPoolTable */
  lazy val UrlPoolTable = new TableQuery(tag => new UrlPoolTable(tag))

  implicit val urlPoolFormat = Json.format[UrlPool]

  /**
    * Models
    */
  /** Collection-like TableQuery object for table CommentLikeTable */
  lazy val CommentLikeTable = new TableQuery(tag => new CommentLikeTable(tag))
  /** Collection-like TableQuery object for table CommentTable */
  lazy val CommentTable = new TableQuery(tag => new CommentTable(tag))
  /** Collection-like TableQuery object for table NavigatorTable */
  lazy val NavigatorTable = new TableQuery(tag => new NavigatorTable(tag))
  /** Collection-like TableQuery object for table PushUserTable */
  lazy val PushUserTable = new TableQuery(tag => new PushUserTable(tag))
  /** Collection-like TableQuery object for table ScoreTable */
  lazy val ScoreTable = new TableQuery(tag => new ScoreTable(tag))
  /** Collection-like TableQuery object for table SmsCodeTable */
  lazy val SmsCodeTable = new TableQuery(tag => new SmsCodeTable(tag))
  /** Collection-like TableQuery object for table SubmitTable */
  lazy val SubmitTable = new TableQuery(tag => new SubmitTable(tag))
  /** Collection-like TableQuery object for table SystemLogTable */
  lazy val SystemLogTable = new TableQuery(tag => new SystemLogTable(tag))
  /** Collection-like TableQuery object for table UrlTable */
  lazy val UrlTable = new TableQuery(tag => new UrlTable(tag))
  /** Collection-like TableQuery object for table UserCollectionTable */
  lazy val UserCollectionTable = new TableQuery(tag => new UserCollectionTable(tag))
  /** Collection-like TableQuery object for table UserLogTable */
  lazy val UserLogTable = new TableQuery(tag => new UserLogTable(tag))
  /** Collection-like TableQuery object for table UserMailboxTable */
  lazy val UserMailboxTable = new TableQuery(tag => new UserMailboxTable(tag))
  /** Collection-like TableQuery object for table UserRelationTable */
  lazy val UserRelationTable = new TableQuery(tag => new UserRelationTable(tag))
  /** Collection-like TableQuery object for table UserTable */
  lazy val UserTable = new TableQuery(tag => new UserTable(tag))
  /** Collection-like TableQuery object for table UrlLikeTable */
  lazy val UrlLikeTable = new TableQuery(tag => new UrlLikeTable(tag))

  implicit val urlFormat = Json.format[Url]
  implicit val userFormat = Json.format[User]
  implicit val commentFormat = Json.format[Comment]
  implicit val userCollectionFormat = Json.format[UserCollection]
  implicit val systemLogFormat = Json.format[SystemLog]

  implicit val navigatorFormat = Json.format[Navigator]

  implicit val userMailboxFormat = new Writes[UserMailbox] {
    def writes(um: UserMailbox): JsValue = {
      Json.obj(
        "id" -> um.id,
        "user_id" -> um.user_id,
        "message_type" -> um.message_type,
        "message" -> Json.parse(um.message),
        "create_time" -> um.create_time
      )
    }
  }
}