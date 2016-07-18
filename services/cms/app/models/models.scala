/**
  * Created by likaili on 6/7/2016.
  */
package com.getgua.cms {

package object models {

  import java.sql.Timestamp
  import java.util.Date

  import play.api.libs.json._
  import slick.lifted.TableQuery

  /**
    * Special Handle for Timestamp
    */
  implicit val timestampFormat = new Format[Timestamp] {
    def writes(ts: Timestamp): JsValue = JsNumber(new Date(ts.getTime()).getTime)

    def reads(ts: JsValue): JsResult[Timestamp] = {
      try {
        JsSuccess(new Timestamp(ts.as[Long]))
      } catch {
        case e: IllegalArgumentException => JsError("Unable to parse timestamp")
      }
    }
  }

  /**
    * CMS Models
    */
  /** Collection-like TableQuery object for table UrlPoolTable */
  lazy val UrlPoolTable = new TableQuery(tag => new UrlPoolTable(tag))

  implicit val urlPoolFormat = Json.format[UrlPool]

  /**
    * Models
    */
  /** Collection-like TableQuery object for table UrlTable */
  lazy val UrlTable = new TableQuery(tag => new UrlTable(tag))

  implicit val urlFormat = Json.format[Url]

  /** Collection-like TableQuery object for table EditorUrlTable */
  lazy val EditorUrlTable = new TableQuery(tag => new EditorUrlTable(tag))

  }
}