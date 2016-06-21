package service

import javax.inject.{Inject, Singleton}

import dao._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 20/6/2016.
  */
@Singleton
class UserCollectionService @Inject()(userCollectionDao: UserCollectionDao) {

  def add(user_id: Long, url: String, title: String) = userCollectionDao.add(user_id, url, title)

  def delete(user_id: Long, url: String) = userCollectionDao.delete(user_id, url)

  def list(user_id: Long) = userCollectionDao.list(user_id)
}
