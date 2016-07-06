/**
  * Created by likaili on 22/6/2016.
  */
package com.getgua {

  package object services {

    import daos.{CommentUser, UrlUser}
    import models.{Url, User}
    import play.api.libs.json.Json

    case class CommentUserStatus(cu: CommentUser, status: Boolean)

    case class UrlUserStatus(uu: UrlUser, status: Boolean)

    case class UrlStatus(url: Url, status: Boolean)

    case class UserWrapper(user: User)

    implicit val commentWithStatusFormat = Json.format[CommentUserStatus]
    implicit val userWrapperFormat = Json.format[UserWrapper]
    implicit val urlUserStatusFormat = Json.format[UrlUserStatus]
    implicit val urlStatusFormat = Json.format[UrlStatus]
  }

}




