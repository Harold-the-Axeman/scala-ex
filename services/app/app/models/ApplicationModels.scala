package com.getgua.models

// AUTO-GENERATED Slick data model


import slick.driver.MySQLDriver.api._


/** Entity class storing rows of table CommentLikeTable
  *  @param comment_id Database column comment_id SqlType(BIGINT)
  *  @param user_id Database column user_id SqlType(BIGINT)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class CommentLike(comment_id: Long, user_id: Long, create_time: java.sql.Timestamp)

/** Table description of table comment_like. Objects of this class serve as prototypes for rows in queries. */
class CommentLikeTable(_tableTag: Tag) extends Table[CommentLike](_tableTag, "comment_like") {
  def * = (comment_id, user_id, create_time) <> (CommentLike.tupled, CommentLike.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(comment_id), Rep.Some(user_id), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> CommentLike.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column comment_id SqlType(BIGINT) */
  val comment_id: Rep[Long] = column[Long]("comment_id")
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Primary key of CommentLikeTable (database name comment_like_PK) */
  val pk = primaryKey("comment_like_PK", (comment_id, user_id))
}

/** Entity class storing rows of table CommentTable
  *
  * @param id          Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param url_id      Database column url_id SqlType(BIGINT)
  * @param content     Database column content SqlType(VARCHAR), Length(512,true)
  * @param user_id     Database column user_id SqlType(BIGINT)
  * @param at_user_id  Database column at_user_id SqlType(BIGINT), Default(0)
  * @param like_count  Database column like_count SqlType(INT), Default(0)
  * @param create_time Database column create_time SqlType(TIMESTAMP) */
case class Comment(id: Long, url_id: Long, content: String, user_id: Long, at_user_id: Long = 0L, like_count: Int = 0, create_time: java.sql.Timestamp)

/** Table description of table comment. Objects of this class serve as prototypes for rows in queries. */
class CommentTable(_tableTag: Tag) extends Table[Comment](_tableTag, "comment") {
  def * = (id, url_id, content, user_id, at_user_id, like_count, create_time) <>(Comment.tupled, Comment.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(url_id), Rep.Some(content), Rep.Some(user_id), Rep.Some(at_user_id), Rep.Some(like_count), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => Comment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column url_id SqlType(BIGINT) */
  val url_id: Rep[Long] = column[Long]("url_id")
  /** Database column content SqlType(VARCHAR), Length(512,true) */
  val content: Rep[String] = column[String]("content", O.Length(512, varying = true))
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column at_user_id SqlType(BIGINT), Default(0) */
  val at_user_id: Rep[Long] = column[Long]("at_user_id", O.Default(0L))
  /** Database column like_count SqlType(INT), Default(0) */
  val like_count: Rep[Int] = column[Int]("like_count", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Index over (url_id) (database name index_url) */
  val index1 = index("index_url", url_id)
}

/** Entity class storing rows of table NavigatorTable
  *
  * @param id          Database column id SqlType(INT UNSIGNED), AutoInc, PrimaryKey
  * @param `type`      Database column type SqlType(VARCHAR), Length(16,true), Default()
  * @param url         Database column url SqlType(VARCHAR), Length(256,true), Default()
  * @param name        Database column name SqlType(VARCHAR), Length(32,true), Default()
  * @param image_url   Database column image_url SqlType(VARCHAR), Length(256,true)
  * @param create_time Database column create_time SqlType(TIMESTAMP) */
case class Navigator(id: Int, `type`: String = "", url: String = "", name: String = "", image_url: String, create_time: java.sql.Timestamp)

/** Table description of table navigator. Objects of this class serve as prototypes for rows in queries.
  * NOTE: The following names collided with Scala keywords and were escaped: type */
class NavigatorTable(_tableTag: Tag) extends Table[Navigator](_tableTag, "navigator") {
  def * = (id, `type`, url, name, image_url, create_time) <>(Navigator.tupled, Navigator.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(`type`), Rep.Some(url), Rep.Some(name), Rep.Some(image_url), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => Navigator.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(INT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  /** Database column type SqlType(VARCHAR), Length(16,true), Default()
    * NOTE: The name was escaped because it collided with a Scala keyword. */
  val `type`: Rep[String] = column[String]("type", O.Length(16, varying = true), O.Default(""))
  /** Database column url SqlType(VARCHAR), Length(256,true), Default() */
  val url: Rep[String] = column[String]("url", O.Length(256, varying = true), O.Default(""))
  /** Database column name SqlType(VARCHAR), Length(32,true), Default() */
  val name: Rep[String] = column[String]("name", O.Length(32, varying = true), O.Default(""))
  /** Database column image_url SqlType(VARCHAR), Length(256,true) */
  val image_url: Rep[String] = column[String]("image_url", O.Length(256, varying = true))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table SubmitTable
  *
  * @param id           Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param url_id       Database column url_id SqlType(BIGINT)
  * @param user_id      Database column user_id SqlType(BIGINT)
  * @param description  Database column description SqlType(VARCHAR), Length(512,true)
  * @param is_anonymous Database column is_anonymous SqlType(INT), Default(0)
  * @param create_time  Database column create_time SqlType(TIMESTAMP) */
case class Submit(id: Long, url_id: Long, user_id: Long, description: String, is_anonymous: Int = 0, create_time: java.sql.Timestamp)

/** Table description of table submit. Objects of this class serve as prototypes for rows in queries. */
class SubmitTable(_tableTag: Tag) extends Table[Submit](_tableTag, "submit") {
  def * = (id, url_id, user_id, description, is_anonymous, create_time) <>(Submit.tupled, Submit.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(url_id), Rep.Some(user_id), Rep.Some(description), Rep.Some(is_anonymous), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => Submit.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column url_id SqlType(BIGINT) */
  val url_id: Rep[Long] = column[Long]("url_id")
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column description SqlType(VARCHAR), Length(512,true) */
  val description: Rep[String] = column[String]("description", O.Length(512, varying = true))
  /** Database column is_anonymous SqlType(INT), Default(0) */
  val is_anonymous: Rep[Int] = column[Int]("is_anonymous", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Index over (url_id) (database name index_ur) */
  val index1 = index("index_ur", url_id)
  /** Index over (user_id) (database name index_user) */
  val index2 = index("index_user", user_id)
  /** Uniqueness Index over (url_id,user_id) (database name url_id) */
  val index3 = index("url_id", (url_id, user_id), unique = true)
}

/** Entity class storing rows of table SystemLogTable
  *
  * @param id          Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param user_id     Database column user_id SqlType(BIGINT)
  * @param log_type    Database column log_type SqlType(VARCHAR), Length(32,true), Default()
  * @param meta_data   Database column meta_data SqlType(VARCHAR), Length(1024,true), Default()
  * @param create_time Database column create_time SqlType(TIMESTAMP) */
case class SystemLog(id: Long, user_id: Long, log_type: String = "", meta_data: String = "", create_time: java.sql.Timestamp)

/** Table description of table system_log. Objects of this class serve as prototypes for rows in queries. */
class SystemLogTable(_tableTag: Tag) extends Table[SystemLog](_tableTag, "system_log") {
  def * = (id, user_id, log_type, meta_data, create_time) <>(SystemLog.tupled, SystemLog.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(user_id), Rep.Some(log_type), Rep.Some(meta_data), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => SystemLog.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column log_type SqlType(VARCHAR), Length(32,true), Default() */
  val log_type: Rep[String] = column[String]("log_type", O.Length(32, varying = true), O.Default(""))
  /** Database column meta_data SqlType(VARCHAR), Length(1024,true), Default() */
  val meta_data: Rep[String] = column[String]("meta_data", O.Length(1024, varying = true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table UrlTable
  *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  *  @param url Database column url SqlType(VARCHAR), Length(1024,true), Default()
  *  @param hash Database column hash SqlType(VARCHAR), Length(128,true)
  *  @param title Database column title SqlType(VARCHAR), Length(128,true), Default()
  *  @param description Database column description SqlType(VARCHAR), Length(128,true), Default(0)
  *  @param cover_url Database column cover_url SqlType(VARCHAR), Length(1024,true), Default()
  *  @param submit_count Database column submit_count SqlType(INT), Default(0)
  *  @param comment_count Database column comment_count SqlType(INT), Default(0)
  *  @param like_count Database column like_count SqlType(INT), Default(0)
  *  @param owner_id Database column owner_id SqlType(BIGINT)
  *  @param is_anonymous Database column is_anonymous SqlType(INT), Default(0)
  *  @param is_pass Database column is_pass SqlType(INT), Default(0)
  *  @param category Database column category SqlType(VARCHAR), Length(32,true), Default(全部)
  *  @param tag Database column tag SqlType(VARCHAR), Length(128,true), Default()
  *  @param property Database column property SqlType(VARCHAR), Length(32,true), Default(text)
  *  @param property_url Database column property_url SqlType(VARCHAR), Length(1024,true), Default()
  *  @param priority Database column priority SqlType(INT), Default(0)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class Url(id: Long, url: String = "", hash: String, title: String = "", description: String = "0", cover_url: String = "", submit_count: Int = 0, comment_count: Int = 0, like_count: Int = 0, owner_id: Long, is_anonymous: Int = 0, is_pass: Int = 0, category: String = "全部", tag: String = "", property: String = "text", property_url: String = "", priority: Int = 0, create_time: java.sql.Timestamp)

/** Table description of table url. Objects of this class serve as prototypes for rows in queries. */
class UrlTable(_tableTag: Tag) extends Table[Url](_tableTag, "url") {
  def * = (id, url, hash, title, description, cover_url, submit_count, comment_count, like_count, owner_id, is_anonymous, is_pass, category, tag, property, property_url, priority, create_time) <> (Url.tupled, Url.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(url), Rep.Some(hash), Rep.Some(title), Rep.Some(description), Rep.Some(cover_url), Rep.Some(submit_count), Rep.Some(comment_count), Rep.Some(like_count), Rep.Some(owner_id), Rep.Some(is_anonymous), Rep.Some(is_pass), Rep.Some(category), Rep.Some(tag), Rep.Some(property), Rep.Some(property_url), Rep.Some(priority), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> Url.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get, _18.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column url SqlType(VARCHAR), Length(1024,true), Default() */
  val url: Rep[String] = column[String]("url", O.Length(1024,varying=true), O.Default(""))
  /** Database column hash SqlType(VARCHAR), Length(128,true) */
  val hash: Rep[String] = column[String]("hash", O.Length(128,varying=true))
  /** Database column title SqlType(VARCHAR), Length(128,true), Default() */
  val title: Rep[String] = column[String]("title", O.Length(128,varying=true), O.Default(""))
  /** Database column description SqlType(VARCHAR), Length(128,true), Default(0) */
  val description: Rep[String] = column[String]("description", O.Length(128,varying=true), O.Default("0"))
  /** Database column cover_url SqlType(VARCHAR), Length(1024,true), Default() */
  val cover_url: Rep[String] = column[String]("cover_url", O.Length(1024,varying=true), O.Default(""))
  /** Database column submit_count SqlType(INT), Default(0) */
  val submit_count: Rep[Int] = column[Int]("submit_count", O.Default(0))
  /** Database column comment_count SqlType(INT), Default(0) */
  val comment_count: Rep[Int] = column[Int]("comment_count", O.Default(0))
  /** Database column like_count SqlType(INT), Default(0) */
  val like_count: Rep[Int] = column[Int]("like_count", O.Default(0))
  /** Database column owner_id SqlType(BIGINT) */
  val owner_id: Rep[Long] = column[Long]("owner_id")
  /** Database column is_anonymous SqlType(INT), Default(0) */
  val is_anonymous: Rep[Int] = column[Int]("is_anonymous", O.Default(0))
  /** Database column is_pass SqlType(INT), Default(0) */
  val is_pass: Rep[Int] = column[Int]("is_pass", O.Default(0))
  /** Database column category SqlType(VARCHAR), Length(32,true), Default(全部) */
  val category: Rep[String] = column[String]("category", O.Length(32,varying=true), O.Default("全部"))
  /** Database column tag SqlType(VARCHAR), Length(128,true), Default() */
  val tag: Rep[String] = column[String]("tag", O.Length(128,varying=true), O.Default(""))
  /** Database column property SqlType(VARCHAR), Length(32,true), Default(text) */
  val property: Rep[String] = column[String]("property", O.Length(32,varying=true), O.Default("text"))
  /** Database column property_url SqlType(VARCHAR), Length(1024,true), Default() */
  val property_url: Rep[String] = column[String]("property_url", O.Length(1024,varying=true), O.Default(""))
  /** Database column priority SqlType(INT), Default(0) */
  val priority: Rep[Int] = column[Int]("priority", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Index over (owner_id) (database name index_owner) */
  val index1 = index("index_owner", owner_id)
  /** Index over (hash) (database name uni_url) */
  val index2 = index("uni_url", hash)
}


/** Entity class storing rows of table UserCollectionTable
  *
  * @param id          Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param user_id     Database column user_id SqlType(BIGINT)
  * @param url         Database column url SqlType(VARCHAR), Length(1024,true), Default()
  * @param title       Database column title SqlType(VARCHAR), Length(512,true), Default()
  * @param create_time Database column create_time SqlType(TIMESTAMP) */
case class UserCollection(id: Long, user_id: Long, url: String = "", title: String = "", create_time: java.sql.Timestamp)

/** Table description of table user_collection. Objects of this class serve as prototypes for rows in queries. */
class UserCollectionTable(_tableTag: Tag) extends Table[UserCollection](_tableTag, "user_collection") {
  def * = (id, user_id, url, title, create_time) <>(UserCollection.tupled, UserCollection.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(user_id), Rep.Some(url), Rep.Some(title), Rep.Some(create_time)).shaped.<>({ r => import r._; _1.map(_ => UserCollection.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column url SqlType(VARCHAR), Length(1024,true), Default() */
  val url: Rep[String] = column[String]("url", O.Length(1024, varying = true), O.Default(""))
  /** Database column title SqlType(VARCHAR), Length(512,true), Default() */
  val title: Rep[String] = column[String]("title", O.Length(512, varying = true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table UserLogTable
  *
  * @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
case class UserLog(id: Long)

/** Table description of table user_log. Objects of this class serve as prototypes for rows in queries. */
class UserLogTable(_tableTag: Tag) extends Table[UserLog](_tableTag, "user_log") {
  def * = id <>(UserLog, UserLog.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = Rep.Some(id).shaped.<>(r => r.map(_ => UserLog(r.get)), (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
}

/** Entity class storing rows of table UserRelationTable
  *  @param from Database column from SqlType(BIGINT)
  *  @param to Database column to SqlType(BIGINT)
  *  @param is_like Database column is_like SqlType(INT), Default(0)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class UserRelation(from: Long, to: Long, is_like: Int = 0, create_time: java.sql.Timestamp)

/** Table description of table user_relation. Objects of this class serve as prototypes for rows in queries. */
class UserRelationTable(_tableTag: Tag) extends Table[UserRelation](_tableTag, "user_relation") {
  def * = (from, to, is_like, create_time) <> (UserRelation.tupled, UserRelation.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(from), Rep.Some(to), Rep.Some(is_like), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> UserRelation.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column from SqlType(BIGINT) */
  val from: Rep[Long] = column[Long]("from")
  /** Database column to SqlType(BIGINT) */
  val to: Rep[Long] = column[Long]("to")
  /** Database column is_like SqlType(INT), Default(0) */
  val is_like: Rep[Int] = column[Int]("is_like", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Primary key of UserRelationTable (database name user_relation_PK) */
  val pk = primaryKey("user_relation_PK", (from, to))
}

/** Entity class storing rows of table UserTable
  *
  * @param id             Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param client_id      Database column client_id SqlType(VARCHAR), Length(64,true), Default()
  * @param auth_type      Database column auth_type SqlType(VARCHAR), Length(32,true), Default()
  * @param third_party_id Database column third_party_id SqlType(VARCHAR), Length(128,true), Default()
  * @param name           Database column name SqlType(VARCHAR), Length(64,true), Default()
  * @param avatar         Database column avatar SqlType(VARCHAR), Length(2048,true), Default()
  * @param submit_count   Database column submit_count SqlType(INT), Default(0)
  * @param comment_count  Database column comment_count SqlType(INT), Default(0)
  * @param like_count     Database column like_count SqlType(INT), Default(0)
  * @param create_time    Database column create_time SqlType(TIMESTAMP)
  * @param update_time    Database column update_time SqlType(TIMESTAMP) */
case class User(id: Long, client_id: String = "", auth_type: String = "", third_party_id: String = "", name: String = "", avatar: String = "", submit_count: Int = 0, comment_count: Int = 0, like_count: Int = 0, create_time: java.sql.Timestamp, update_time: java.sql.Timestamp)

/** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
class UserTable(_tableTag: Tag) extends Table[User](_tableTag, "user") {
  def * = (id, client_id, auth_type, third_party_id, name, avatar, submit_count, comment_count, like_count, create_time, update_time) <>(User.tupled, User.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(client_id), Rep.Some(auth_type), Rep.Some(third_party_id), Rep.Some(name), Rep.Some(avatar), Rep.Some(submit_count), Rep.Some(comment_count), Rep.Some(like_count), Rep.Some(create_time), Rep.Some(update_time)).shaped.<>({ r => import r._; _1.map(_ => User.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column client_id SqlType(VARCHAR), Length(64,true), Default() */
  val client_id: Rep[String] = column[String]("client_id", O.Length(64, varying = true), O.Default(""))
  /** Database column auth_type SqlType(VARCHAR), Length(32,true), Default() */
  val auth_type: Rep[String] = column[String]("auth_type", O.Length(32, varying = true), O.Default(""))
  /** Database column third_party_id SqlType(VARCHAR), Length(128,true), Default() */
  val third_party_id: Rep[String] = column[String]("third_party_id", O.Length(128, varying = true), O.Default(""))
  /** Database column name SqlType(VARCHAR), Length(64,true), Default() */
  val name: Rep[String] = column[String]("name", O.Length(64, varying = true), O.Default(""))
  /** Database column avatar SqlType(VARCHAR), Length(2048,true), Default() */
  val avatar: Rep[String] = column[String]("avatar", O.Length(2048, varying = true), O.Default(""))
  /** Database column submit_count SqlType(INT), Default(0) */
  val submit_count: Rep[Int] = column[Int]("submit_count", O.Default(0))
  /** Database column comment_count SqlType(INT), Default(0) */
  val comment_count: Rep[Int] = column[Int]("comment_count", O.Default(0))
  /** Database column like_count SqlType(INT), Default(0) */
  val like_count: Rep[Int] = column[Int]("like_count", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
  /** Database column update_time SqlType(TIMESTAMP) */
  val update_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("update_time")

  /** Index over (third_party_id,auth_type) (database name social_index) */
  val index1 = index("social_index", (third_party_id, auth_type))
  /** Index over (client_id) (database name uuid_index) */
  val index2 = index("uuid_index", client_id)
}

/** Entity class storing rows of table UrlLikeTable
  *  @param url_id Database column url_id SqlType(BIGINT)
  *  @param user_id Database column user_id SqlType(BIGINT)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class UrlLike(url_id: Long, user_id: Long, create_time: java.sql.Timestamp)

/** Table description of table url_like. Objects of this class serve as prototypes for rows in queries. */
class UrlLikeTable(_tableTag: Tag) extends Table[UrlLike](_tableTag, "url_like") {
  def * = (url_id, user_id, create_time) <> (UrlLike.tupled, UrlLike.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(url_id), Rep.Some(user_id), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> UrlLike.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column url_id SqlType(BIGINT) */
  val url_id: Rep[Long] = column[Long]("url_id")
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Primary key of UrlLikeTable (database name url_like_PK) */
  val pk = primaryKey("url_like_PK", (url_id, user_id))
}

/** Entity class storing rows of table UserRegisterTrackingTable
  *  @param user_id Database column user_id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  *  @param from Database column from SqlType(VARCHAR), Length(128,true), Default()
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class UserRegisterTracking(user_id: Long, from: String = "", create_time: java.sql.Timestamp)

/** Table description of table user_register_tracking. Objects of this class serve as prototypes for rows in queries. */
class UserRegisterTrackingTable(_tableTag: Tag) extends Table[UserRegisterTracking](_tableTag, "user_register_tracking") {
  def * = (user_id, from, create_time) <> (UserRegisterTracking.tupled, UserRegisterTracking.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(user_id), Rep.Some(from), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> UserRegisterTracking.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column user_id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val user_id: Rep[Long] = column[Long]("user_id", O.AutoInc, O.PrimaryKey)
  /** Database column from SqlType(VARCHAR), Length(128,true), Default() */
  val from: Rep[String] = column[String]("from", O.Length(128,varying=true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}

/** Entity class storing rows of table SectionsTable
  *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  *  @param `type` Database column type SqlType(VARCHAR), Length(32,true), Default()
  *  @param title Database column title SqlType(VARCHAR), Length(256,true), Default()
  *  @param description Database column description SqlType(VARCHAR), Length(256,true), Default()
  *  @param url Database column url SqlType(VARCHAR), Length(2048,true), Default()
  *  @param meta_url Database column meta_url SqlType(VARCHAR), Length(2048,true), Default()
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class Sections(id: Long, `type`: String = "", title: String = "", description: String = "", url: String = "", meta_url: String = "", create_time: java.sql.Timestamp)

/** Table description of table sections. Objects of this class serve as prototypes for rows in queries.
  *  NOTE: The following names collided with Scala keywords and were escaped: type */
class SectionsTable(_tableTag: Tag) extends Table[Sections](_tableTag, "sections") {
  def * = (id, `type`, title, description, url, meta_url, create_time) <> (Sections.tupled, Sections.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(`type`), Rep.Some(title), Rep.Some(description), Rep.Some(url), Rep.Some(meta_url), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> Sections.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column type SqlType(VARCHAR), Length(32,true), Default()
    *  NOTE: The name was escaped because it collided with a Scala keyword. */
  val `type`: Rep[String] = column[String]("type", O.Length(32,varying=true), O.Default(""))
  /** Database column title SqlType(VARCHAR), Length(256,true), Default() */
  val title: Rep[String] = column[String]("title", O.Length(256,varying=true), O.Default(""))
  /** Database column description SqlType(VARCHAR), Length(256,true), Default() */
  val description: Rep[String] = column[String]("description", O.Length(256,varying=true), O.Default(""))
  /** Database column url SqlType(VARCHAR), Length(2048,true), Default() */
  val url: Rep[String] = column[String]("url", O.Length(2048,varying=true), O.Default(""))
  /** Database column meta_url SqlType(VARCHAR), Length(2048,true), Default() */
  val meta_url: Rep[String] = column[String]("meta_url", O.Length(2048,varying=true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}


/** Entity class storing rows of table LocationLogTable
  *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  *  @param user_id Database column user_id SqlType(BIGINT)
  *  @param address Database column address SqlType(VARCHAR), Length(64,true), Default()
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class LocationLog(id: Long, user_id: Long, address: String = "", create_time: java.sql.Timestamp)

/** Table description of table location_log. Objects of this class serve as prototypes for rows in queries. */
class LocationLogTable(_tableTag: Tag) extends Table[LocationLog](_tableTag, "location_log") {
  def * = (id, user_id, address, create_time) <> (LocationLog.tupled, LocationLog.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(user_id), Rep.Some(address), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> LocationLog.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column user_id SqlType(BIGINT) */
  val user_id: Rep[Long] = column[Long]("user_id")
  /** Database column address SqlType(VARCHAR), Length(64,true), Default() */
  val address: Rep[String] = column[String]("address", O.Length(64,varying=true), O.Default(""))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}
