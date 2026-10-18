package wiring

import controllers.HomeController
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.filters.HttpFiltersComponents
import router.Routes

class AppLoader extends ApplicationLoader {
  override def load(context: ApplicationLoader.Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach(
      _.configure(context.environment, context.initialConfiguration, Map.empty))
    new AppComponents(context).application
  }
}

// The whole object graph lives here. As the app grows, add "slices" as traits
// (e.g. ServiceComponents, RepositoryComponents) with a self-type
// `self: BuiltInComponents =>` containing lazy vals, then add them to the
// `with` chain below. config is read in the wiring, not in services.
class AppComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context) with HttpFiltersComponents {
  lazy val homeController = new HomeController(controllerComponents)
  lazy val router: Router = new Routes(httpErrorHandler, homeController)
}
