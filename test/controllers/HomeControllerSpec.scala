package controllers

import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec {

  private val controller = new HomeController(stubControllerComponents())

  "HomeController#index" should {
    "return a 200" in {
      val result = controller.index().apply(FakeRequest())
      status(result) mustBe 200
    }
  }
}
