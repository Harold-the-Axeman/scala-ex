

/**
  * Created by likaili on 10/7/2016.
  */

package com.getgua.ws {

  import javax.inject.Inject

  import com.google.inject.Singleton
  import play.api.{Configuration, Logger}

  package object controllers {
    /**
      * Wechat
      */
    @Singleton
    class WeichatConfig @Inject()(configuration: Configuration) {
      val appid = configuration.getString("wechat.app.id").getOrElse("wx0ab15104e2a02d6a")
      val app_secret = configuration.getString("wechat.app.secret").getOrElse("a1a637316fc3a71295a2d9109d19d3dc")
      val grant_type = configuration.getString("wechat.grant.type").getOrElse("authorization_code")
    }

    /**
      * Message
      */
    case class MessageInfo(message_type: String, message_index: Int, push_text: String, has_push: Boolean)

    object MessageInfos {
      val info = Map(
        "user-comment-url" -> MessageInfo("user-comment-url", 1, "评论了你的分享", true),
        "user-comment-user" -> MessageInfo("user-comment-user", 2, "评论了你的评论", true),
        "user-comment-like" -> MessageInfo("user-comment-like", 3, "", false),
        "user-like" -> MessageInfo("user-like", 4, "喜欢了你", true),
        "user-url-like" -> MessageInfo("user-url-like", 5, "喜欢了你的推荐", true)
      )
    }

    /**
      * Logger
      */
    val dataWatchLogger = Logger("data.watch")
  }
}
