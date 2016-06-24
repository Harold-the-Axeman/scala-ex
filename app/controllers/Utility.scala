package controllers

import dao.CommentWithUser


/**
  * Created by likaili on 13/6/2016.
  */


case class Auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name: Option[String], avatar: Option[String])

case class UrlSubmit(user_id: Option[Long], url: String, title: String, description: String, anonymous: Int, cover_url: String)
case class CommentSubmit(url_id: Long, content: String, user_id: Option[Long], at_user_id: Option[Long])

case class UrlCollection(user_id: Option[Long], url: String, title: Option[String])

case class SubmitLog(user_id: Option[Long], log_type: String, meta_data: String)

case class SubmitLogs(logs: Seq[SubmitLog])

//user_id: Long, text: String, message: String, message_type: String
case class PushMessage(user_id: Option[Long], text: String, message: String, message_type: String, code: String = "33d60800441663873b190641607fe978")





