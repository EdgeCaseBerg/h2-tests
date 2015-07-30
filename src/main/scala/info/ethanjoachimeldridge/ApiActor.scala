package info.ethanjoachimeldridge

import akka.actor.{Actor,ActorRefFactory,ActorLogging}
import akka.routing._
import spray.http._
import spray.routing._
import akka.util.Timeout
import info.ethanjoachimeldridge.routing._

class ApiActor extends Actor with Api {
  override val actorRefFactory: ActorRefFactory = context
  def receive = runRoute(route)
}

trait Api extends AuthorRouting with BookRouting{
	val route = {
		authorRoutes ~ bookRoutes
	}
}