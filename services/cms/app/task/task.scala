
/**
  * Created by likaili on 13/7/2016.
  */
package com.getgua.cms {

  import play.api.Logger

  package object task {
    case class SubmitCommand(name: String)

    val dataWatchLogger = Logger("data.watch")
  }
}
