package com.getgua.cms.models

import slick.driver.MySQLDriver.api._


/** Entity class storing rows of table UrlPoolTable
  *
  * @param id              Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  * @param title           Database column title SqlType(VARCHAR), Length(128,true), Default()
  * @param sub_title       Database column sub_title SqlType(VARCHAR), Length(128,true), Default()
  * @param description     Database column description SqlType(VARCHAR), Length(128,true), Default()
  * @param app_name        Database column app_name SqlType(VARCHAR), Length(128,true), Default()
  * @param url             Database column url SqlType(VARCHAR), Length(512,true), Default()
  * @param score           Database column score SqlType(INT), Default(0)
  * @param count1          Database column count1 SqlType(INT), Default(0)
  * @param count2          Database column count2 SqlType(INT), Default(0)
  * @param count3          Database column count3 SqlType(INT), Default(0)
  * @param review_passed   Database column review_passed SqlType(INT), Default(0)
  * @param online_url_id   Database column online_url_id SqlType(BIGINT), Default(0)
  * @param create_time     Database column create_time SqlType(TIMESTAMP)
  * @param url_create_time Database column url_create_time SqlType(TIMESTAMP) */
case class UrlPool(id: Long, title: String = "", sub_title: String = "", description: String = "", app_name: String = "", url: String = "", score: Int = 0, count1: Int = 0, count2: Int = 0, count3: Int = 0, review_passed: Int = 0, online_url_id: Long = 0L, create_time: java.sql.Timestamp, url_create_time: java.sql.Timestamp)

/** Table description of table url_pool. Objects of this class serve as prototypes for rows in queries. */
class UrlPoolTable(_tableTag: Tag) extends Table[UrlPool](_tableTag, "url_pool") {
  def * = (id, title, sub_title, description, app_name, url, score, count1, count2, count3, review_passed, online_url_id, create_time, url_create_time) <>(UrlPool.tupled, UrlPool.unapply)

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(title), Rep.Some(sub_title), Rep.Some(description), Rep.Some(app_name), Rep.Some(url), Rep.Some(score), Rep.Some(count1), Rep.Some(count2), Rep.Some(count3), Rep.Some(review_passed), Rep.Some(online_url_id), Rep.Some(create_time), Rep.Some(url_create_time)).shaped.<>({ r => import r._; _1.map(_ => UrlPool.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column title SqlType(VARCHAR), Length(128,true), Default() */
  val title: Rep[String] = column[String]("title", O.Length(128, varying = true), O.Default(""))
  /** Database column sub_title SqlType(VARCHAR), Length(128,true), Default() */
  val sub_title: Rep[String] = column[String]("sub_title", O.Length(128, varying = true), O.Default(""))
  /** Database column description SqlType(VARCHAR), Length(128,true), Default() */
  val description: Rep[String] = column[String]("description", O.Length(128, varying = true), O.Default(""))
  /** Database column app_name SqlType(VARCHAR), Length(128,true), Default() */
  val app_name: Rep[String] = column[String]("app_name", O.Length(128, varying = true), O.Default(""))
  /** Database column url SqlType(VARCHAR), Length(512,true), Default() */
  val url: Rep[String] = column[String]("url", O.Length(512, varying = true), O.Default(""))
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
  *  @param priority Database column priority SqlType(INT), Default(0)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class Url(id: Long, url: String = "", hash: String, title: String = "", description: String = "0", cover_url: String = "", submit_count: Int = 0, comment_count: Int = 0, like_count: Int = 0, owner_id: Long, is_anonymous: Int = 0, is_pass: Int = 0, category: String = "全部", tag: String = "", property: String = "text", priority: Int = 0, create_time: java.sql.Timestamp)

/** Table description of table url. Objects of this class serve as prototypes for rows in queries. */
class UrlTable(_tableTag: Tag) extends Table[Url](_tableTag, "url") {
  def * = (id, url, hash, title, description, cover_url, submit_count, comment_count, like_count, owner_id, is_anonymous, is_pass, category, tag, property, priority, create_time) <> (Url.tupled, Url.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(url), Rep.Some(hash), Rep.Some(title), Rep.Some(description), Rep.Some(cover_url), Rep.Some(submit_count), Rep.Some(comment_count), Rep.Some(like_count), Rep.Some(owner_id), Rep.Some(is_anonymous), Rep.Some(is_pass), Rep.Some(category), Rep.Some(tag), Rep.Some(property), Rep.Some(priority), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> Url.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

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
  /** Database column priority SqlType(INT), Default(0) */
  val priority: Rep[Int] = column[Int]("priority", O.Default(0))
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")

  /** Index over (owner_id) (database name index_owner) */
  val index1 = index("index_owner", owner_id)
  /** Index over (hash) (database name uni_url) */
  val index2 = index("uni_url", hash)
}

/** Entity class storing rows of table EditorUrlTable
  *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
  *  @param url Database column url SqlType(VARCHAR), Length(1024,true), Default()
  *  @param title Database column title SqlType(VARCHAR), Length(128,true), Default()
  *  @param description Database column description SqlType(VARCHAR), Length(128,true), Default(0)
  *  @param cover_url Database column cover_url SqlType(VARCHAR), Length(1024,true), Default()
  *  @param category Database column category SqlType(VARCHAR), Length(32,true), Default(全部)
  *  @param tag Database column tag SqlType(VARCHAR), Length(128,true), Default()
  *  @param property Database column property SqlType(VARCHAR), Length(32,true), Default(text)
  *  @param priority Database column priority SqlType(INT), Default(0)
  *  @param is_submit Database column is_submit SqlType(INT), Default(0)
  *  @param submit_time Database column submit_time SqlType(TIMESTAMP)
  *  @param create_time Database column create_time SqlType(TIMESTAMP) */
case class EditorUrl(id: Long, url: String = "", title: String = "", description: String = "0", cover_url: String = "", category: String = "全部", tag: String = "", property: String = "text", priority: Int = 0, is_submit: Int = 0, submit_time: java.sql.Timestamp, create_time: java.sql.Timestamp)

/** Table description of table editor_url. Objects of this class serve as prototypes for rows in queries. */
class EditorUrlTable(_tableTag: Tag) extends Table[EditorUrl](_tableTag, "editor_url") {
  def * = (id, url, title, description, cover_url, category, tag, property, priority, is_submit, submit_time, create_time) <> (EditorUrl.tupled, EditorUrl.unapply)
  /** Maps whole row to an option. Useful for outer joins. */
  def ? = (Rep.Some(id), Rep.Some(url), Rep.Some(title), Rep.Some(description), Rep.Some(cover_url), Rep.Some(category), Rep.Some(tag), Rep.Some(property), Rep.Some(priority), Rep.Some(is_submit), Rep.Some(submit_time), Rep.Some(create_time)).shaped.<>({r=>import r._; _1.map(_=> EditorUrl.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  /** Database column url SqlType(VARCHAR), Length(1024,true), Default() */
  val url: Rep[String] = column[String]("url", O.Length(1024,varying=true), O.Default(""))
  /** Database column title SqlType(VARCHAR), Length(128,true), Default() */
  val title: Rep[String] = column[String]("title", O.Length(128,varying=true), O.Default(""))
  /** Database column description SqlType(VARCHAR), Length(128,true), Default(0) */
  val description: Rep[String] = column[String]("description", O.Length(128,varying=true), O.Default("0"))
  /** Database column cover_url SqlType(VARCHAR), Length(1024,true), Default() */
  val cover_url: Rep[String] = column[String]("cover_url", O.Length(1024,varying=true), O.Default(""))
  /** Database column category SqlType(VARCHAR), Length(32,true), Default(全部) */
  val category: Rep[String] = column[String]("category", O.Length(32,varying=true), O.Default("全部"))
  /** Database column tag SqlType(VARCHAR), Length(128,true), Default() */
  val tag: Rep[String] = column[String]("tag", O.Length(128,varying=true), O.Default(""))
  /** Database column property SqlType(VARCHAR), Length(32,true), Default(text) */
  val property: Rep[String] = column[String]("property", O.Length(32,varying=true), O.Default("text"))
  /** Database column priority SqlType(INT), Default(0) */
  val priority: Rep[Int] = column[Int]("priority", O.Default(0))
  /** Database column is_submit SqlType(INT), Default(0) */
  val is_submit: Rep[Int] = column[Int]("is_submit", O.Default(0))
  /** Database column submit_time SqlType(TIMESTAMP) */
  val submit_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("submit_time")
  /** Database column create_time SqlType(TIMESTAMP) */
  val create_time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_time")
}







