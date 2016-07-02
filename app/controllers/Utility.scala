package com.getgua.controllers

import javax.inject.Inject

import com.google.inject.Singleton
import dao.CommentUser
import play.api.Configuration


/**
  * Created by likaili on 13/6/2016.
  */

/**
  * User Post Case Class
  */

case class Auth(client_id: String, auth_type: Option[String], third_party_id: Option[String], name: Option[String], avatar: Option[String])

case class UrlSubmit(user_id: Option[Long], url: String, title: String, description: String, anonymous: Int, cover_url: String)
case class CommentSubmit(url_id: Long, content: String, user_id: Option[Long], at_user_id: Option[Long])

case class UrlCollection(user_id: Option[Long], url: String, title: Option[String])

case class SubmitLog(user_id: Option[Long], log_type: String, meta_data: String)

case class SubmitLogs(logs: Seq[SubmitLog])



