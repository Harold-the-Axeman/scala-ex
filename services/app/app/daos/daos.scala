package com.getgua

/**
  * Created by kailili on 5/31/16.
  */
package object daos {

  import com.getgua.models._
  import play.api.libs.json.Json

  case class UrlUser(url: Url, user: User)

  case class CommentUser(comment: Comment, user: User)

  case class CommentUrlUser(comment: Comment, url: Url, user: User)

  case class UserProfile(user: User)

  case class OtherProfile(profile: UserProfile, is_like: Boolean)

  case class NavigatorWithType(navigator_type: String, websites: Seq[Navigator])


  implicit val urlWithUserFormat = Json.format[UrlUser]
  implicit val commentWithUserFormat = Json.format[CommentUser]
  implicit val commentWithUrlFormat = Json.format[CommentUrlUser]

  implicit val userProfileFormat = Json.format[UserProfile]
  implicit val otherUserProfileFormat = Json.format[OtherProfile]

  implicit val navigatorWithTypeFormat = Json.format[NavigatorWithType]
}


