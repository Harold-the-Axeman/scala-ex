package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
//object Tables extends {
  //val profile = slick.driver.MySqlDriver
//} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
object Tables {
  //val profile: slick.driver.JdbcProfile = slick.driver.MySqlDriver
  //import profile.api._

  import slick.driver.MySQLDriver.api._

  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(CommentTable.schema, ScoreTable.schema, SubmitTable.schema, UrlTable.schema, UserLogTable.schema, UserTable.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table CommentTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
    *  @param url_id Database column url_id SqlType(BIGINT)
    *  @param content Database column content SqlType(VARCHAR), Length(512,true), Default()
    *  @param comment_user Database column comment_user SqlType(BIGINT)
    *  @param at_user Database column at_user SqlType(BIGINT), Default(0)
    *  @param create_time Database column create_time SqlType(TIMESTAMP) */
  case class Comment(id: Long, url_id: Long, content: String = "", comment_user: Long, at_user: Long = 0L, create_time: java.sql.Timestamp)
  /** GetResult implicit for fetching Comment objects using plain SQL queries */
  implicit def GetResultComment(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[Comment] = GR{
    prs => import prs._
      Comment.tupled((<<[Long], <<[Long], <<[String], <<[Long], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table comment. Objects of this class serve as prototypes for rows in queries. */
  class CommentTable(_tableTag: Tag) extends Table[Comment](_tableTag, "comment") {
    def * = (id, url_id, content, comment_user, at_user, create_time) <> (Comment.tupled, Comment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(url_id), Rep.Some(content), Rep.Some(comment_user), Rep.Some(at_user), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> Comment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column url_id SqlType(BIGINT) */
    val url_id: Rep[Long] = column[Long]("url_id")
    /** Database column content SqlType(VARCHAR), Length(512,true), Default() */
    val content: Rep[String] = column[String]("content", O.Length(512,varying=true), O.Default(""))
    /** Database column comment_user SqlType(BIGINT) */
    val comment_user: Rep[Long] = column[Long]("comment_user")
    /** Database column at_user SqlType(BIGINT), Default(0) */
    val at_user: Rep[Long] = column[Long]("at_user", O.Default(0L))
    /** Database column create_time SqlType(TIMESTAMP) */
    val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

    /** Index over (url_id) (database name index_url) */
    val index1 = index("index_url", url_id)
  }
  /** Collection-like TableQuery object for table CommentTable */
  lazy val CommentTable = new TableQuery(tag => new CommentTable(tag))

  /** Entity class storing rows of table ScoreTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  case class Score(id: Long)
  /** GetResult implicit for fetching Score objects using plain SQL queries */
  implicit def GetResultScore(implicit e0: GR[Long]): GR[Score] = GR{
    prs => import prs._
      Score(<<[Long])
  }
  /** Table description of table score. Objects of this class serve as prototypes for rows in queries. */
  class ScoreTable(_tableTag: Tag) extends Table[Score](_tableTag, "score") {
    def * = id <> (Score, Score.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = Rep.Some(id).shaped.<>(r => r.map(_=> Score(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table ScoreTable */
  lazy val ScoreTable = new TableQuery(tag => new ScoreTable(tag))

  /** Entity class storing rows of table SubmitTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
    *  @param url_id Database column url_id SqlType(BIGINT)
    *  @param user_id Database column user_id SqlType(BIGINT)
    *  @param create_time Database column create_time SqlType(TIMESTAMP) */
  case class Submit(id: Long, url_id: Long, user_id: Long, create_time: java.sql.Timestamp)
  /** GetResult implicit for fetching Submit objects using plain SQL queries */
  implicit def GetResultSubmit(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[Submit] = GR{
    prs => import prs._
      Submit.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table submit. Objects of this class serve as prototypes for rows in queries. */
  class SubmitTable(_tableTag: Tag) extends Table[Submit](_tableTag, "submit") {
    def * = (id, url_id, user_id, create_time) <> (Submit.tupled, Submit.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(url_id), Rep.Some(user_id), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> Submit.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column url_id SqlType(BIGINT) */
    val url_id: Rep[Long] = column[Long]("url_id")
    /** Database column user_id SqlType(BIGINT) */
    val user_id: Rep[Long] = column[Long]("user_id")
    /** Database column create_time SqlType(TIMESTAMP) */
    val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

    /** Index over (url_id) (database name index_ur) */
    val index1 = index("index_ur", url_id)
    /** Index over (user_id) (database name index_user) */
    val index2 = index("index_user", user_id)
    /** Uniqueness Index over (url_id,user_id) (database name url_id) */
    val index3 = index("url_id", (url_id, user_id), unique=true)
  }
  /** Collection-like TableQuery object for table SubmitTable */
  lazy val SubmitTable = new TableQuery(tag => new SubmitTable(tag))

  /** Entity class storing rows of table UrlTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
    *  @param url Database column url SqlType(VARCHAR), Length(1024,true), Default()
    *  @param url_hash Database column url_hash SqlType(VARCHAR), Length(128,true), Default()
    *  @param owner Database column owner SqlType(BIGINT)
    *  @param create_time Database column create_time SqlType(TIMESTAMP) */
  case class Url(id: Long, url: String = "", url_hash: String = "", owner: Long, create_time: java.sql.Timestamp)
  /** GetResult implicit for fetching Url objects using plain SQL queries */
  implicit def GetResultUrl(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[Url] = GR{
    prs => import prs._
      Url.tupled((<<[Long], <<[String], <<[String], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table url. Objects of this class serve as prototypes for rows in queries. */
  class UrlTable(_tableTag: Tag) extends Table[Url](_tableTag, "url") {
    def * = (id, url, url_hash, owner, create_time) <> (Url.tupled, Url.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(url), Rep.Some(url_hash), Rep.Some(owner), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> Url.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column url SqlType(VARCHAR), Length(1024,true), Default() */
    val url: Rep[String] = column[String]("url", O.Length(1024,varying=true), O.Default(""))
    /** Database column url_hash SqlType(VARCHAR), Length(128,true), Default() */
    val url_hash: Rep[String] = column[String]("url_hash", O.Length(128,varying=true), O.Default(""))
    /** Database column owner SqlType(BIGINT) */
    val owner: Rep[Long] = column[Long]("owner")
    /** Database column create_time SqlType(TIMESTAMP) */
    val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

    /** Index over (owner) (database name index_owner) */
    val index1 = index("index_owner", owner)
    /** Uniqueness Index over (url_hash) (database name uni_url_hash) */
    val index2 = index("uni_url_hash", url_hash, unique=true)
  }
  /** Collection-like TableQuery object for table UrlTable */
  lazy val UrlTable = new TableQuery(tag => new UrlTable(tag))

  /** Entity class storing rows of table UserLogTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  case class UserLog(id: Long)
  /** GetResult implicit for fetching UserLog objects using plain SQL queries */
  implicit def GetResultUserLog(implicit e0: GR[Long]): GR[UserLog] = GR{
    prs => import prs._
      UserLog(<<[Long])
  }
  /** Table description of table user_log. Objects of this class serve as prototypes for rows in queries. */
  class UserLogTable(_tableTag: Tag) extends Table[UserLog](_tableTag, "user_log") {
    def * = id <> (UserLog, UserLog.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = Rep.Some(id).shaped.<>(r => r.map(_=> UserLog(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table UserLogTable */
  lazy val UserLogTable = new TableQuery(tag => new UserLogTable(tag))

  /** Entity class storing rows of table UserTable
    *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
    *  @param uuid Database column uuid SqlType(VARCHAR), Length(64,true), Default()
    *  @param auth_type Database column auth_type SqlType(VARCHAR), Length(32,true), Default()
    *  @param token Database column token SqlType(VARCHAR), Length(256,true), Default()
    *  @param create_time Database column create_time SqlType(TIMESTAMP) */
  case class User(id: Long, uuid: String = "", auth_type: String = "", token: String = "", create_time: java.sql.Timestamp)
  /** GetResult implicit for fetching User objects using plain SQL queries */
  implicit def GetResultUser(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[User] = GR{
    prs => import prs._
      User.tupled((<<[Long], <<[String], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class UserTable(_tableTag: Tag) extends Table[User](_tableTag, "user") {
    def * = (id, uuid, auth_type, token, create_time) <> (User.tupled, User.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(uuid), Rep.Some(auth_type), Rep.Some(token), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column uuid SqlType(VARCHAR), Length(64,true), Default() */
    val uuid: Rep[String] = column[String]("uuid", O.Length(64,varying=true), O.Default(""))
    /** Database column auth_type SqlType(VARCHAR), Length(32,true), Default() */
    val auth_type: Rep[String] = column[String]("auth_type", O.Length(32,varying=true), O.Default(""))
    /** Database column token SqlType(VARCHAR), Length(256,true), Default() */
    val token: Rep[String] = column[String]("token", O.Length(256,varying=true), O.Default(""))
    /** Database column create_time SqlType(TIMESTAMP) */
    val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

    /** Index over (uuid) (database name uuid_index) */
    val index1 = index("uuid_index", uuid)
  }
  /** Collection-like TableQuery object for table UserTable */
  lazy val UserTable = new TableQuery(tag => new UserTable(tag))
}
