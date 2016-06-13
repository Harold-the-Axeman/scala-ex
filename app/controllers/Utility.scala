package controllers

/**
  * Created by likaili on 13/6/2016.
  */


case class Auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name: Option[String], avatar: Option[String])

case class UrlSubmit(user_id: Long, url: String, title: String, description: String, anonymous: Int)
case class CommentSubmit(url_id: Long, content: String, user_id: Long, at_user_id: Option[Long])
