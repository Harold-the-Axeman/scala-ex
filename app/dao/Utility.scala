package dao

import models.Tables._
/**
  * Created by kailili on 5/31/16.
  */
object DaoUtility {

  // println(query.statements.headOption)   with result
}

case class URLWithUser(url: Url, user: User)
case class CommentWithUser(comment: Comment, user: User)
case class CommentWithUrl(comment: Comment, url: Url)


case class UserProfile(user: User)
case class OtherUserProfile(user: UserProfile, is_like:Boolean)

case class NavigatorWithType(navigator_type: String, websites: Seq[Navigator])
