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


trait MySQLTest extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfter{
	implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(500, Millis))  

	val conf = ConfigFactory.load();
	val flyway = new Flyway();
	val url = conf.getString("db.default.url")
	val user = conf.getString("db.default.user")
	val pass = conf.getString("db.default.password")
	flyway.setDataSource(url, user, pass, "")
	flyway.setSchemas("h2tests","public")


	before {
    	flyway.migrate()
    /* For some reason we don't need to run flyway.clean in an after blcok */
  	}

}