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


class AuthorMySQLDAOTest extends MySQLTest {

	val testAuthor = Author(-1, "testAuthor")
	var testAuthorId = -1L
	var testAuthorId2 = -1L
	val authorMySQLDAO = new AuthorMySQLDAO

	"The AuthorMySQLDAO" should "be able to create an Author" in {
		val res = authorMySQLDAO.create(testAuthor)
		whenReady(res) { result =>
			testAuthorId = result.id
			assertResult(testAuthor.copy(id=testAuthorId)) { result }
		}
	}

	it should "fail to create an author with a non-unique id" in {
		val res = authorMySQLDAO.create(testAuthor)
		whenReady(res.failed) { ex =>
			ex shouldBe an [DuplicateDataError]
		}
	}

	it should "be able to update an Author" in {
		val res = authorMySQLDAO.update(testAuthor.copy(name = "testAuthor2",id=testAuthorId))
		whenReady(res) { result =>
			assertResult(testAuthor.copy(id=testAuthorId, name="testAuthor2")) {
				result
			}
		}
	}

	it should "be able to create an author with the old name" in {
		val res = authorMySQLDAO.create(testAuthor)
		testAuthorId2 = testAuthorId
		whenReady(res) { result =>
			testAuthorId = result.id
			assertResult(testAuthor.copy(id=result.id)) { result }
		}	
	}

	it should "be able to read an Author" in {
		val res = authorMySQLDAO.read(testAuthor.copy(id=testAuthorId))
		whenReady(res) { result =>
			assertResult(testAuthor.copy(id=testAuthorId)) {
				result
			}
		}
	}

	it should "be able to list authors" in {
		val res = authorMySQLDAO.readAll(0,10)
		whenReady(res) { result =>
			assertResult(2) {
				result.size
			}
			result.map { author =>
				assert(List(testAuthorId,testAuthorId2).contains(author.id))
			}
		}
	}

	it should "be to delete an Author" in {
		val res = authorMySQLDAO.delete(testAuthor.copy(id=testAuthorId2))
		whenReady(res) { result =>
			assert(result)
		}
	}

	it should "have correct syntax for findBooksByAuthor" in {
		val res = authorMySQLDAO.findBooksByAuthor(testAuthor.copy(id=testAuthorId))
		whenReady(res) { result =>
			assert(result.isEmpty)
		}
		//This test would fail if the syntax of the query is wrong.
	}

}