package service

import dao.{CommentUser, UrlUser}
import models.Tables.{Url, User}

/**
  * Created by likaili on 22/6/2016.
  */
case class CommentUserStatus(cu: CommentUser, status: Boolean)
case class UrlUserStatus(uu: UrlUser, status: Boolean)
case class UrlStatus(url: Url, status: Boolean)

case class UserWrapper(user: User)
