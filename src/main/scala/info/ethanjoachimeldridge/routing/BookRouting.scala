package info.ethanjoachimeldridge.routing

import akka.routing._
import spray.routing._
import spray.http._
import spray.httpx.SprayJsonSupport._
import MediaTypes._

import info.ethanjoachimeldridge.model._

/** Actor responsible for routing HTTP requests to appropriate services for Books
 *
 * Mix into an HttpActor and ~ its route
 */
trait BookRouting extends HttpService {

  /** Supported Routes by this HttpService */
  val bookRoutes = {
    pathPrefix("v0" / "books") {
      pathEndOrSingleSlash { 
        get {
          parameters('page ? 0, 'perPage ? 10).as(ApiPagination) { pagination =>
            handle(s"Not Yet GET $pagination")
          }
        } ~ 
        post {
          handle("Not Yet POST")
        }
      } ~
      path(IntNumber) { bookId =>
        pathEndOrSingleSlash {
          get {
            handle(s"$bookId Not Yet GET")
          } ~ 
          put {
            handle(s"$bookId Not Yet PUT")
          } ~ 
          delete {
            handle(s"$bookId Not Yet DELETE")
          }
        }
      } 
    }
  }

  /** A dummy test handle method
   * 
   * @param msg A string to spit out to the user
   * @return A partial function that will complete a request context
   */
  private def handle(msg: String) : RequestContext => Unit = {
    respondWithMediaType(`application/json`) {
      complete(StatusCodes.BadRequest, msg) 
    }
  }

}