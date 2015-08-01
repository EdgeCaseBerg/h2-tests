package info.ethanjoachimeldridge.routing

import akka.actor.ActorRefFactory
import org.scalatest._
import spray.testkit.ScalatestRouteTest
import akka.actor._


class AuthorRoutingTest extends FlatSpec with AuthorRouting with Matchers with ScalatestRouteTest {
	def actorRefFactory = system

	"Author Routing" should "have a v0/authors route" in {
		Get("/v0/authors") ~> authorRoutes ~> check {
			assert(handled === true)
			//assert(status === 200)
		}
	}

	it should "have a v0/authors/1 route" in {
		Get("/v0/authors/1") ~> authorRoutes ~> check {
			assert(handled === true)
			//assert(status === 200)	
		}
	}

	it should "have a v0/authors/1/books route" in {
		Get("/v0/authors/1/books") ~> authorRoutes ~> check {
			assert(handled === true)
			//assert(status === 200)
		}
	}
} 
