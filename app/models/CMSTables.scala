package models


/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
object CMSTables {
  val profile: slick.driver.JdbcProfile = slick.driver.MySQLDriver
  import profile.api._

  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(UrlPoolTable.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table AdminAcclTable
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param group_id Database column group_id SqlType(BIGINT)
    *  @param resource_id Database column resource_id SqlType(BIGINT)
    *  @param access_type Database column access_type SqlType(VARCHAR), Length(6,true), Default(r/w)
    *  @param dt_create Database column dt_create SqlType(TIMESTAMP) */
  case class AdminAccl(id: Long, group_id: Long, resource_id: Long, access_type: String = "r/w", dt_create: java.sql.Timestamp)
  /** GetResult implicit for fetching AdminAccl objects using plain SQL queries */
  implicit def GetResultAdminAccl(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[AdminAccl] = GR{
    prs => import prs._
      AdminAccl.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[java.sql.Timestamp]))
  }

  /** Entity class storing rows of table UrlPoolTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
    *  @param title Database column title SqlType(VARCHAR), Length(128,true), Default()
    *  @param sub_title Database column sub_title SqlType(VARCHAR), Length(128,true), Default()
    *  @param description Database column description SqlType(VARCHAR), Length(128,true), Default()
    *  @param app_name Database column app_name SqlType(VARCHAR), Length(128,true), Default()
    *  @param url Database column url SqlType(VARCHAR), Length(512,true), Default()
    *  @param score Database column score SqlType(INT), Default(0)
    *  @param count1 Database column count1 SqlType(INT), Default(0)
    *  @param count2 Database column count2 SqlType(INT), Default(0)
    *  @param count3 Database column count3 SqlType(INT), Default(0)
    *  @param review_passed Database column review_passed SqlType(INT), Default(0)
    *  @param online_url_id Database column online_url_id SqlType(BIGINT), Default(0)
    *  @param create_time Database column create_time SqlType(TIMESTAMP)
    *  @param url_create_time Database column url_create_time SqlType(TIMESTAMP) */
  case class UrlPool(id: Long, title: String = "", sub_title: String = "", description: String = "", app_name: String = "", url: String = "", score: Int = 0, count1: Int = 0, count2: Int = 0, count3: Int = 0, review_passed: Int = 0, online_url_id: Long = 0L, create_time: java.sql.Timestamp, url_create_time: java.sql.Timestamp)
  /** GetResult implicit for fetching UrlPool objects using plain SQL queries */
  implicit def GetResultUrlPool(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[UrlPool] = GR{
    prs => import prs._
      UrlPool.tupled((<<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table url_pool. Objects of this class serve as prototypes for rows in queries. */
  class UrlPoolTable(_tableTag: Tag) extends Table[UrlPool](_tableTag, "url_pool") {
    def * = (id, title, sub_title, description, app_name, url, score, count1, count2, count3, review_passed, online_url_id, create_time, url_create_time) <> (UrlPool.tupled, UrlPool.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(title), Rep.Some(sub_title), Rep.Some(description), Rep.Some(app_name), Rep.Some(url), Rep.Some(score), Rep.Some(count1), Rep.Some(count2), Rep.Some(count3), Rep.Some(review_passed), Rep.Some(online_url_id), Rep.Some(create_time), Rep.Some(url_create_time)).shaped.<>({r=>import r._; _1.map(_=> UrlPool.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(VARCHAR), Length(128,true), Default() */
    val title: Rep[String] = column[String]("title", O.Length(128,varying=true), O.Default(""))
    /** Database column sub_title SqlType(VARCHAR), Length(128,true), Default() */
    val sub_title: Rep[String] = column[String]("sub_title", O.Length(128,varying=true), O.Default(""))
    /** Database column description SqlType(VARCHAR), Length(128,true), Default() */
    val description: Rep[String] = column[String]("description", O.Length(128,varying=true), O.Default(""))
    /** Database column app_name SqlType(VARCHAR), Length(128,true), Default() */
    val app_name: Rep[String] = column[String]("app_name", O.Length(128,varying=true), O.Default(""))
    /** Database column url SqlType(VARCHAR), Length(512,true), Default() */
    val url: Rep[String] = column[String]("url", O.Length(512,varying=true), O.Default(""))
    /** Database column score SqlType(INT), Default(0) */
    val score: Rep[Int] = column[Int]("score", O.Default(0))
    /** Database column count1 SqlType(INT), Default(0) */
    val count1: Rep[Int] = column[Int]("count1", O.Default(0))
    /** Database column count2 SqlType(INT), Default(0) */
    val count2: Rep[Int] = column[Int]("count2", O.Default(0))
    /** Database column count3 SqlType(INT), Default(0) */
    val count3: Rep[Int] = column[Int]("count3", O.Default(0))
    /** Database column review_passed SqlType(INT), Default(0) */
    val review_passed: Rep[Int] = column[Int]("review_passed", O.Default(0))
    /** Database column online_url_id SqlType(BIGINT), Default(0) */
    val online_url_id: Rep[Long] = column[Long]("online_url_id", O.Default(0L))
    /** Database column create_time SqlType(TIMESTAMP) */
    val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
    /** Database column url_create_time SqlType(TIMESTAMP) */
    val url_create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("url_create_time")
  }
  /** Collection-like TableQuery object for table UrlPoolTable */
  lazy val UrlPoolTable = new TableQuery(tag => new UrlPoolTable(tag))

}
