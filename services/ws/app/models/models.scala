

/**
  * Created by likaili on 8/7/2016.
  */
package com.getgua.ws {
  package object models {
    import play.api.libs.json.{JsValue, Json, Writes}
    import slick.lifted.TableQuery

    /** Collection-like TableQuery object for table SmsCodeTable */
    lazy val SmsCodeTable = new TableQuery(tag => new SmsCodeTable(tag))
    /** Collection-like TableQuery object for table UserMailboxTable */
    lazy val UserMailboxTable = new TableQuery(tag => new UserMailboxTable(tag))
    /** Collection-like TableQuery object for table PushUserTable */
    lazy val PushUserTable = new TableQuery(tag => new PushUserTable(tag))
    /** Collection-like TableQuery object for table MessageStatusTable */
    lazy val MessageStatusTable = new TableQuery(tag => new MessageStatusTable(tag))

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
}
