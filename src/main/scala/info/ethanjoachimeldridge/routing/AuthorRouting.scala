package info.ethanjoachimeldridge.routing

import akka.routing._
import spray.routing._
import spray.http._
import spray.httpx.SprayJsonSupport._
import MediaTypes._

import info.ethanjoachimeldridge.model._

/** Actor responsible for routing HTTP requests to appropriate services for Authors
 *
 * Mix into an HttpActor and ~ its route
 */
trait AuthorRouting extends HttpService {

  /** Supported Routes by this HttpService */
  val authorRoutes = {
    pathPrefix("v0" / "authors") {
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
      path(IntNumber) { authorId =>
        pathEndOrSingleSlash {
          get {
            handle(s"$authorId Not Yet GET")
          } ~ 
          put {
            handle(s"$authorId Not Yet PUT")
          } ~ 
          delete {
            handle(s"$authorId Not Yet DELETE")
          }
        }
      } ~ 
      path(IntNumber / "books") { authorId =>
        pathEndOrSingleSlash {
          parameters('page ? 0, 'perPage ? 10).as(ApiPagination) { pagination =>
            handle(s"$authorId Not Yet GET $pagination")
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