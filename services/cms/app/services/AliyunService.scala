package services

import java.net.URL
import javax.inject.{Inject, Singleton}

import com.getgua.cms.daos.UrlDao
import com.getgua.utils.aliyun.QidianOSS
import play.api.Logger

/**
  * Created by likaili on 10/8/2016.
  */
@Singleton
class AliyunService @Inject() (urlDao: UrlDao, oss: QidianOSS){

  val dataWatchLogger = Logger("data.watch")

  def fix_image = {
    urlDao.cover_image_list.map{ r =>
      r.map{ u =>
        try {
          val url = new URL("http://www.baidu.com")
          val in = url.openStream

          val key_name = oss.putNetworkObject(u.cover_url)
          val new_url = s"http://cdn.gotgua.com/$key_name@!large"
          urlDao.set_cover_image(u.id, new_url)
        } catch  {
          case e: Exception => {
            dataWatchLogger.info(s"/cms/oss/fix $u.id")
            urlDao.set_cover_image(u.id, "")
          }
        }
      }
      dataWatchLogger.info(s"/cms/oss/fix: image_processed -> ${r.length}")
      //JsonOk(Json.obj())
    }
  }
}
