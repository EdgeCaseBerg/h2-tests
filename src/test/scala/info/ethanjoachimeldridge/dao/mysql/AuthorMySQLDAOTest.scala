package info.ethanjoachimeldridge.dao.mysql

import java.util.Locale
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis,Seconds,Span}
import spray.json._	

import scala.concurrent.ExecutionContext.Implicits.global
import info.ethanjoachimeldridge.model._

import org.h2.jdbcx.JdbcDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

class AuthorMySQLDAOTest extends FlatSpec with Matchers with ScalaFutures {
	implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(500, Millis))  

	val testAuthor = Author(-1, "testAuthor")
	var testAuthorId = -1L
	val authorMySQLDAO = new AuthorMySQLDAO
	

	"After loading the database" should "be able to create an Author" in {
		val res = authorMySQLDAO.create(testAuthor)
		whenReady(res) { result =>
			testAuthorId = result.id
			assertResult(testAuthor.copy(id=testAuthorId)) { result }
		}
	}
}