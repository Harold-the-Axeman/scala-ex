package service

import dao.CommentWithUser
import models.Tables.Url

/**
  * Created by likaili on 22/6/2016.
  */
case class CommentWithStatus(comment: CommentWithUser, status: Boolean)
case class UrlWIthStatus(url: Url, status: Boolean)
