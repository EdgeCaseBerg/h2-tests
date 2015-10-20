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
			authorService.createAuthor(Author(-1,""))
		}
	}	

	it should "reject deleting an invalid author" in {
		intercept[InvalidModelException] {
			authorService.deleteAuthor(Author(-1, ""))
		}
	}

	it should "reject retrieving books for an invalid author" in {
		intercept[InvalidModelException] {
			authorService.getBooksForAuthor(Author(-1,""))
		}
	}

	var createdAuthorId = -1L

	it should "create an Author with a valid author" in {
		val author = Author(1,"Author")
		val res = authorService.createAuthor(author)
		whenReady(res) { result =>
			assertResult(result.name)(author.name)
			createdAuthorId = result.id
		}
	}

	it should "be able to retrieve an author by id" in {
		whenReady(authorService.findAuthorById(createdAuthorId)) { result =>
			assertResult(result.id)(createdAuthorId)
		}
	}

	it should "be able to delete an author" in {
		whenReady(authorService.deleteAuthor(Author(createdAuthorId,"Author"))) { result =>
			assert(result)
		}
	}

}