/**
  * Created by likaili on 22/6/2016.
  */
package com.getgua.qdb {

  package object services {

    import play.api.libs.json.Json
    import com.getgua.qdb.models._
    import com.getgua.qdb.daos._

    case class CommentUserStatus(cu: CommentUser, status: Boolean)

    case class UrlUserStatus(uu: UrlUser, status: Boolean, category: String)

    case class UrlStatus(url: Url, status: Boolean)

    case class UserWrapper(user: User)

    implicit val commentWithStatusFormat = Json.format[CommentUserStatus]
    implicit val userWrapperFormat = Json.format[UserWrapper]
    implicit val urlUserStatusFormat = Json.format[UrlUserStatus]
    implicit val urlStatusFormat = Json.format[UrlStatus]

    // Qidian Ws Message
    case class MessageSubmit(user_id: Long, sender: String, message_type: String, message: String)

    implicit val messageSubmitFormat = Json.format[MessageSubmit]
  }

}




