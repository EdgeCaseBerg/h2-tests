package info.ethanjoachimeldridge.routing

import akka.routing._
import spray.routing._
import spray.http._
import spray.httpx.SprayJsonSupport._
import MediaTypes._

/** Actor responsible for routing HTTP requests to appropriate services for Authors
 *
 * Mix into an HttpActor and ~ its route
 */
trait AuthorRouting extends HttpService {

  /** dummy test route placeholder */
  val authorRoutes = {
    path("test" / Segment ) { text =>
      handle(text)
    }
  }

  /** A dummy test handle method
   * 
   * @param msg A string to spit out to the user
   * @return A partial function that will complete a request context
   */
  private def handle(msg: String) : RequestContext => Unit = {
    get {
      complete(StatusCodes.BadRequest, msg) 
    }
  }

}