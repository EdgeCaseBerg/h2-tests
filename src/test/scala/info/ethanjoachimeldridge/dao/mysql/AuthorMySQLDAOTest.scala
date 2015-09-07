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
}