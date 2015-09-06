package info.ethanjoachimeldridge.dao.mysql

import java.sql.{Connection,SQLException}
import scalikejdbc._
import anorm._

import info.ethanjoachimeldridge.config.Configuration
import info.ethanjoachimeldridge.dao.exception._

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

object MySQLConnector {
	val logger = Logger(LoggerFactory.getLogger("MySQLConnector"))

	/* Ensure that the Driver is loaded properly. */
	Class.forName(Configuration.dbDriver)

	/* For a production system you would want to setup a connection pool underneath  */
	ConnectionPool.singleton(Configuration.dbUrl, Configuration.dbUser, Configuration.dbPassword)

	logger.info(s"Initialized MySQLConnector")

	/* Encapsulate Database Specific Errors into Application Errors that can be handled */
	private def sqlToApplicationException(ex: Throwable) : Throwable = ex match {
		case sql : SQLException if sql.getErrorCode() == 23505 => DuplicateDataError("Could not perform request, unique data violation", ex)
		case sql : SQLException if sql.getErrorCode() == 42102 => TableNotFoundException("Could not perform request, underlying structore not present", ex)
		case sql : SQLException => 
			logger.warn(s"Unhandled Converted MySQL Exception: ${sql.getSQLState()} ${sql.getErrorCode()}")
			DataErrorException(ex.getMessage, ex)
		case _ => { ex }
	}

	/**
	* Execute a block of code, in the scope of a JDBC connection.
	* The connection and all created statements are automatically released.
	* The connection is automatically committed, unless an exception occurs.
	*
	* @param block Code block to execute with an implicit connection in scope
	*/
	def withConnection[A](implicit block: Connection => A): A = {
		implicit val connection: Connection = ConnectionPool().borrow()
		try {
			block(connection)
		} catch {
			case e : Throwable => throw sqlToApplicationException(e)
		} finally {
			connection.close()
		}
	}

	/**
	* Execute a block of code, in the scope of a readOnly JDBC connection.
	* The connection is automatically released.
	*
	* @note Will cause java.sql.SQLException if you attempt to insert/update while using this method
	* @note This method should be preffered over withConnection when performing reads for security and additional protection against injection attacks
	*
	* @param block Code block to execute with an implicit connection in scope
	*/
	def withReadOnlyConnection[A](implicit block: Connection => A): A ={
		withConnection { implicit connection =>
			connection.setReadOnly(true)
			try {
				block(connection)
			} catch {
				case e : Throwable => throw sqlToApplicationException(e)
			} finally {
				connection.setReadOnly(false)
				connection.close()
			}
		}
	}

	/**
	* Execute a block of code, in the scope of a JDBC transaction.
	* The connection and all created statements are automatically released.
	* The transaction is automatically committed, unless an exception occurs.
	*
	* @param block Code block to execute with an implicit connection in scope
	*/
	def withTransaction[A](block: Connection => A): A = {
		withConnection { implicit connection =>
	  		try {
				connection.setAutoCommit(false)
				val r = block(connection)
				connection.commit()
				r
	  		} catch {
				case e: Throwable => connection.rollback(); throw sqlToApplicationException(e)
	  		} finally {
				connection.close()
			}
		}
	}
}