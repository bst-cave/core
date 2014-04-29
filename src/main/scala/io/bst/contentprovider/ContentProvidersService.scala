package io.bst.contentprovider

import io.bst.user.User
import scala.concurrent.ExecutionContext
import spray.json.DefaultJsonProtocol
import spray.routing.Directives

class ContentProvidersService(implicit executionContext: ExecutionContext)
  extends Directives {

  implicit val userFormat = DefaultJsonProtocol.jsonFormat4(User)

  val route =
    path("api" / "contentproviders") {
      get {
        complete {
          "Not yet implement!"
        }
      }
    }

}
