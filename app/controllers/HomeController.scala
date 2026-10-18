package controllers

import play.api.mvc._

class HomeController(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("Play up and running!")
  }
}
