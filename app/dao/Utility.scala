package dao

import models.Tables._
/**
  * Created by kailili on 5/31/16.
  */


case class UrlUser(url: Url, user: User)
case class CommentUser(comment: Comment, user: User)
case class CommentUrlUser(comment: Comment, url: Url, user: User)


case class UserProfile(user: User)
case class OtherProfile(profile: UserProfile, is_like:Boolean)

case class NavigatorWithType(navigator_type: String, websites: Seq[Navigator])
