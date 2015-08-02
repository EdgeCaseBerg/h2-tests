package info.ethanjoachimeldridge.routing

import akka.actor.ActorRefFactory
import org.scalatest._
import spray.testkit.ScalatestRouteTest
import akka.actor._


class BookRoutingTest extends FlatSpec with BookRouting with Matchers with ScalatestRouteTest {
	def actorRefFactory = system

	"Book Routing" should "have a v0/books route" in {
		Get("/v0/books") ~> bookRoutes ~> check {
			assert(handled === true)
			//assert(status === 200)
		}
	}

	it should "have a v0/books/1 route" in {
		Get("/v0/books/1") ~> bookRoutes ~> check {
			assert(handled === true)
			//assert(status === 200)	
		}
	}

} 
