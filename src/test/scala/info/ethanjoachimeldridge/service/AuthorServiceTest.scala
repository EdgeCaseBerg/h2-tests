package info.ethanjoachimeldridge.service

import org.scalatest._

import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.dao.mysql.{MySQLTest,MySQLDAOContext}
import scala.concurrent.ExecutionContext.Implicits.global
import info.ethanjoachimeldridge.model.validation.exception.InvalidModelException

object TestMySQLContext extends MySQLDAOContext {}

class AuthorServiceTest extends MySQLTest with MySQLDAOContext {

	implicit val s = TestMySQLContext
	val authorService = new AuthorService

	"The Author Service" should "reject creating invalid authors" in {
		intercept[InvalidModelException] {
			authorService.createAuthor(Author(-1,"name"))
		}
	}

}