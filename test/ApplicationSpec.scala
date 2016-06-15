import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsNull, JsValue}
import play.api.test._
import play.api.test.Helpers._
import utils.JsonFormat._

import scala.concurrent.Future

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
object ApplicationSpec extends Specification{
  val application: Application = GuiceApplicationBuilder().build()

  def response_ok_check(response: Future[play.api.mvc.Result]) = {
    status(response) must equalTo(OK)
    contentType(response) must beSome.which(_ == "application/json")
    (contentAsJson(response) \ "status").as[Int] must equalTo(QIDIAN_OK)
  }

  def response_error_check(response: Future[play.api.mvc.Result]) = {
    status(response) must equalTo(OK)
    contentType(response) must beSome.which(_ == "application/json")
    (contentAsJson(response) \ "status").as[Int] must equalTo(QIDIAN_ERROR)
  }

  def get_resonse_data(response: Future[play.api.mvc.Result]) = {
    (contentAsJson(response) \ "data")
  }

  def ok_check(method: String, path: String, parameter: Map[String, Any] = Map(), body: JsValue = JsNull): JsValue ={
    val request = route(FakeRequest(method, path)).get
    response_ok_check(request)
    get_resonse_data(request).as[JsValue]
  }
}

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  import ApplicationSpec._

  "Application" should {

    /**
      * Error Handler Test
      */
    "Bad Request" in new WithApplication{
      val badRequest = route(FakeRequest(GET, "/boum")).get
      response_error_check(badRequest)
    }

    /**
      * Api Test
      */
    "/" in new WithApplication{
      ok_check(GET, "/")
      //(data \"navigator_type").as[Set[String]] must equalTo(Set("文艺", "团购", "资讯"))
    }

  }
}
