package info.ethanjoachimeldridge.dao.mysql

import java.util.Locale
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis,Seconds,Span}
import spray.json._	

import scala.concurrent.ExecutionContext.Implicits.global
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.dao.exception._

import org.h2.jdbcx.JdbcDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway;


class BookMySQLDAOTest extends MySQLTest {

	var testAuthorId = -1L
	var testAuthor = Author(testAuthorId, "testBookAuthor")
	var testBookId1 = -1L
	var testBookId2 = -1L
	var testBook1 = Book(testBookId1, testAuthorId)
	var testBook2 = Book(testBookId2, testAuthorId)
	var testBookMeta = BookMeta(testBookId2, Locale.forLanguageTag("en"), "title","short","longer")

	
	val authorMySQLDAO = new AuthorMySQLDAO
	val bookMySQLDAO = new BookMySQLDAO 


	"The AuthorMySQLDAO" should "have at least 1 author to use for book tests" in {
		val res = authorMySQLDAO.readAll(0,1)
		whenReady(res) { result =>
			result.map(_.id).map { id =>
				testAuthorId = id	
			}

			if(testAuthorId == -1L) {
				/* There isn't an author, make one. */
				val res = authorMySQLDAO.create(testAuthor)
				whenReady(res) { result =>
					testAuthorId = result.id
					testAuthor = testAuthor.copy(id=testAuthorId)
					assertResult(testAuthor) { result }
				}
			}

			testBook1 = testBook1.copy(authorId = testAuthorId)
			testBook2 = testBook2.copy(authorId = testAuthorId)
		}
	}

	"The BookMySQLDAO" should "create a book for an author" in {
		assume(testAuthorId != -1, "Author of book never created in database")
		val res = bookMySQLDAO.create(testBook1)
		whenReady(res) { result =>
			testBookId1 = result.bookId
			testBook1 = testBook1.copy(bookId = testBookId1)
			assertResult(testBook1) {
				result
			}
		}
	}

	it should "be able to read a single book by id" in {
		assume(testAuthorId != -1, "Author of book never created in database")
		val res = bookMySQLDAO.read(Book(-1,testBookId1))
		whenReady(res) { result =>
			assertResult(testBook1) {
				result
			}
		}
	}

	it should "throw an exception on not present data" in {
		assume(testAuthorId != -1, "Author of book never created in database")
		val res = bookMySQLDAO.read(Book(-1,-1))
		whenReady(res.failed) { ex =>
			ex shouldBe an [DataNotFoundException]
		}
	}

	it should "delete a book for an author" in {
		assume(testAuthorId != -1, "Author of book never created in database")
		val res = bookMySQLDAO.delete(testBook1)
		whenReady(res) { result => 
			assert(result)
			//After we delete it, add it back in for more tests
			val res2 = bookMySQLDAO.create(testBook1)
			whenReady(res2) { result2 =>
				testBookId1 = result2.bookId
				testBook1 = testBook1.copy(bookId = testBookId1)
				assertResult(testBook1) {
					result2
				}
			}
		}
	}

	it should "create a second book for an author" in {
		assume(testAuthorId != -1, "Author of book never created in database")
		val res = bookMySQLDAO.create(testBook2)
		whenReady(res) { result =>
			testBook2 = testBook2.copy(bookId = result.bookId)
			assertResult(testBook2) {
				result
			}
			testBookId2 = result.bookId
		}
	}

	it should "list both books" in {
		assume(testBookId2 != -1, "Second book was not created")
		val res = bookMySQLDAO.readAll(0,2)
		whenReady(res) { result =>
			assertResult(2) { result.size }
			result.map(_.authorId == testAuthorId).map(assert(_))
			result.map(i => assert(List(testBook1,testBook2).contains(i)))
		}
	}

	it should "add meta information to one of the books" in {
		assume(testBookId2 != -1, "Second book was not created")
		val res = bookMySQLDAO.addBookMetaToBook(testBook2, testBookMeta)
		whenReady(res) { result =>
			testBookMeta = testBookMeta.copy(bookId = result.bookId)
			assertResult(testBookMeta) {
				result
			}
		}
	}

	it should "update meta information to one of the books" in {
		assume(testBookId2 != -1, "Second book was not created")
		val res = bookMySQLDAO.updateBookMeta(testBookMeta.copy(title="other"))
		whenReady(res) { result =>
			assertResult(testBookMeta.copy(title="other")) {
				result
			}
			testBookMeta = result
		}
	}


	"The BookMySQLDAOTest" should "clean up the resources it generated" in {
		val res = for {
			a <- authorMySQLDAO.delete(testAuthor)
			b <- bookMySQLDAO.delete(testBook1)
			c <- bookMySQLDAO.delete(testBook2)
		} yield (a,b,c)
		whenReady(res) { result =>
			val (x,y,z) = result
			assert(x)
			assert(y)
			assert(z)
		}
	}

}