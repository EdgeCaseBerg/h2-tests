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

	it should "reject creating meta for a book that does not exist" in {
		intercept[InvalidModelException] {
			bookService.createBookMeta(BookMeta(-1,new java.util.Locale("EN"), "title","short","long"))
		}
	}
	
	it should "be able to create a book if valid" in {
		val result = bookService.createBook(Book(1,1))
		// check if future failed or not
		
	}
	
	it should "reject creating meta if that meta is invalid" in {
		intercept[InvalidModelException] {
			
		}
	}

}
