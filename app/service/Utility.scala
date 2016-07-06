package com.getgua.service

import com.getgua.daos.{CommentUser, UrlUser}
import com.getgua.models.{Url, User}

/**
  * Created by likaili on 22/6/2016.
  */
case class CommentUserStatus(cu: CommentUser, status: Boolean)
case class UrlUserStatus(uu: UrlUser, status: Boolean)
case class UrlStatus(url: Url, status: Boolean)

case class UserWrapper(user: User)
