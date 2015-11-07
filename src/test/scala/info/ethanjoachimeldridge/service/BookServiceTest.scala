package info.ethanjoachimeldridge.service

import org.scalatest._

import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.dao.mysql.{MySQLTest,MySQLDAOContext}
import scala.concurrent.ExecutionContext.Implicits.global
import info.ethanjoachimeldridge.model.validation.exception.InvalidModelException

object TestMySQLContext extends MySQLDAOContext {}

class BookServiceTest extends MySQLTest with MySQLDAOContext {

	implicit val s = TestMySQLContext
	val bookService = new BookService

	"The Book Service" should "reject creating invalid books" in {
		intercept[InvalidModelException] {
			bookService.createBook(Book(-1,-1))
		}
	}	

}