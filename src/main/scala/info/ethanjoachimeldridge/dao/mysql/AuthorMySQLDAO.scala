package info.ethanjoachimeldridge.dao.mysql

import info.ethanjoachimeldridge.dao.AuthorDAO
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.dao.exception._
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
	
	def delete(model: Author)(implicit ec: ExecutionContext): Future[Boolean] = future {
		MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""DELETE FROM authors WHERE id = {id}"""
			).on("id" -> model.id).executeUpdate()
			true //true because deleting something that doesn't exist is the same result as deleting something that does
		}
	}
	def read(model: Author)(implicit ec: ExecutionContext): Future[Author] = future {
		val readModel : Author = MySQLConnector.withReadOnlyConnection { implicit connection => 
			val result = SQL(
				"""SELECT id, name FROM authors WHERE id = {id}"""
			).on("id" -> model.id).as(RowParsers.authorParser *).headOption
			val resultModel : Author = result.fold(throw new DataNotFoundException("Author not found"))(identity)
			resultModel
		}
		readModel		
	}
	
	def update(model: Author)(implicit ec: ExecutionContext): Future[Author] = future {
		val updated = MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""UPDATE authors SET name = {name} WHERE id = {id}"""
			).on(
			"name" -> model.name,
			"id" -> model.id
			).executeUpdate()
			numberEffected match {
				case 0 => false
				case _ => true
			}
		}
		if(updated) {
			model
		} else {
			throw new DataNotFoundException("Author to update does not exist")
		}
	}

	// def findBooksByAuthor(model: info.ethanjoachimeldridge.model.Author)(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[List[(info.ethanjoachimeldridge.model.Book, List[info.ethanjoachimeldridge.model.BookMeta])]] = ???
	// def readAll(page: Int,perPage: Int)(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[List[info.ethanjoachimeldridge.model.Author]] = ???

}