package info.ethanjoachimeldridge.dao.mysql

import info.ethanjoachimeldridge.dao.AuthorDAO
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.dao.exception.DataErrorException
import scala.concurrent.{ExecutionContext, Future,future}

import anorm._
import anorm.SqlParser._

class AuthorMySQLDAO extends AuthorDAO {
	def create(model: Author)(implicit ec: ExecutionContext): Future[Author] = future {
		val possibleNewId = MySQLConnector.withTransaction { implicit connection => 
			val result = SQL(
				"""INSERT INTO authors (name) VALUES ({name})"""
			).on("name" -> model.name).executeInsert()
			result
		}
		possibleNewId.fold(throw new DataErrorException("Could not save Author Object",new Throwable("DAL Error"))) { newId =>
			model.copy(id=newId)
		}
	}
	// def delete(model: Author)(implicit ec: ExecutionContext): Future[Boolean] = 
	// def read(model: Author)(implicit ec: ExecutionContext): Future[Author] = 
	// def update(model: Author)(implicit ec: ExecutionContext): Future[Author] = 


	// def findBooksByAuthor(model: info.ethanjoachimeldridge.model.Author)(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[List[(info.ethanjoachimeldridge.model.Book, List[info.ethanjoachimeldridge.model.BookMeta])]] = ???
	// def readAll(page: Int,perPage: Int)(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[List[info.ethanjoachimeldridge.model.Author]] = ???

}