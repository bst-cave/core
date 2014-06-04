package io.bst.api

import scala.concurrent.ExecutionContext
import spray.routing.Directives

/**
 * @author Harald Pehl
 */
class HomeService(implicit executionContext: ExecutionContext)
  extends Directives {

  val route =
    pathSingleSlash {
      get {
        complete {
          <html lang="en">
            <body>
              <h1>BST</h1>
              <p>In order to do something useful, you need to login</p>
              <form method="post" action="/login"></form>
              <div>
                <label for="username">Username</label>
                <input type="text" id="username" placeholder="Username"/>
              </div>
              <div>
                <label for="password">Password</label>
                <input type="password" id="password" placeholder="Password"/>
              </div>
              <p>Once you've logged in, the following resources are available:</p>
              <ul>
                <li><a href="/api/contentproviders">list of content providers</a></li>
              </ul>
            </body>
          </html>
        }
      }
    }
}
