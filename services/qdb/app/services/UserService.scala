package services

import javax.inject.{Inject, Singleton}
import com.getgua.qdb.daos._
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by likaili on 8/6/2016.
  */
@Singleton
class UserService @Inject()(userDao: UserDao, userRelationDao: UserRelationDao, navProfileDao: NavProfileDao, sectionProfileDao: SectionProfileDao, userVersioningDao: UserVersioningDao) {

  def profile(id: Long) = {
    userDao.profile(id)
  }

  def other_profile(user_id: Long, me: Long) = {
    for {
      u <- userDao.profile(user_id)
      il <- userRelationDao.is_like(me, user_id)
    } yield OtherProfile(u, il)
  }

  def nav_profile(user_id: Long, ns: Seq[(String, String, String, Int)]) = {
    navProfileDao.deleteAndInsert(user_id, ns)
  }

  def nav_list(user_id: Long) = {
    navProfileDao.list(user_id)
  }

  def section_profile(user_id: Long, ss: Seq[(String, Int)]) = {
    sectionProfileDao.deleteAndInsert(user_id, ss)
  }

  def section_list(user_id: Long) = {
    sectionProfileDao.list(user_id)
  }

  def versioning(user_id: Long, version: String) = {
    userVersioningDao.update(user_id, version)
  }
}
