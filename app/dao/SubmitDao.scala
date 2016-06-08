package dao

import javax.inject.Inject

import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import models.Tables._

/**
  * Created by kailili on 6/8/15.
  */


class SubmitDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  /**
    * 创建一条用户分享URL记录(url, user)
    * @param user_id
    * @param url_id
    * @param description
    * @param anonymous
    * @return
    */
  def create(user_id: Long, url_id: Long,  description: String, anonymous: Int) = {

    val query = ( for{
      re <- SubmitTable.filter(s => s.url_id === url_id && s.user_id === user_id).exists.result
      r <- re match {
        case true => DBIO.successful(0)
        case false => SubmitTable.map(s => (s.url_id, s.user_id, s.description, s.is_anonymous)) += (url_id, user_id, description, anonymous)
      }
    } yield r).transactionally

    db.run(query)
  }


}
