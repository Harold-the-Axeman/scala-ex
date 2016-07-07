

/**
  * Created by likaili on 7/7/2016.
  */

package com.getgua.cms {

  package object controllers {

    import play.api.libs.json.Json
    /** Submit
      * user: url_id
      * system: url_id, url, user_id, description, category
      * editor: url, user_id, description, category
      */
    case class CMSSubmit(url_id: Option[Long], url: Option[String], user_id: Option[Long], title: Option[String], cover_url: Option[String], description: Option[String], category: Option[String], score: Int)
    object CMSConfig {
      val code = "411e3fa7c5dc344f84e97abe952190ee"
    }
    implicit val cMSSubmitFormat = Json.format[CMSSubmit]
  }
}
