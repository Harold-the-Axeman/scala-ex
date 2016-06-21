package service

import dao.CommentWithUser

/**
  * Created by likaili on 22/6/2016.
  */
case class CommentWithStatus(comment: CommentWithUser, status: Boolean)
