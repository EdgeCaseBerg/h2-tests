package info.ethanjoachimeldridge.dao.mysql

import info.ethanjoachimeldridge.dao.BookDAO
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.dao.exception._
import scala.concurrent.{ExecutionContext, Future,future}

import anorm._
import anorm.SqlParser._

class BookMySQLDAO extends BookDAO {
	def create(model: Book)(implicit ec: ExecutionContext): Future[Book] = future {
		val possibleNewId = MySQLConnector.withTransaction { implicit connection => 
			val result = SQL(
				"""INSERT INTO book (authorId) VALUES ({authorId})"""
			).on("authorId" -> model.authorId).executeInsert()
			result
		}
		possibleNewId.fold(throw new DataErrorException("Could not save Book Object",new Throwable("DAL Error"))) { newId =>
			model.copy(bookId=newId)
		}
	}
	
	def delete(model: Book)(implicit ec: ExecutionContext): Future[Boolean] = future {
		MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""DELETE FROM Book WHERE bookId = {bookId}"""
			).on("bookId" -> model.bookId).executeUpdate()
			true //true because deleting something that doesn't exist is the same result as deleting something that does
		}
	}
	def read(model: Book)(implicit ec: ExecutionContext): Future[Book] = future {
		val readModel : Book = MySQLConnector.withReadOnlyConnection { implicit connection => 
			val result = SQL(
				"""SELECT bookId, authorId FROM Book WHERE bookId = {id}"""
			).on("id" -> model.bookId).as(RowParsers.bookParser *).headOption
			val resultModel : Book = result.fold(throw new DataNotFoundException("Book not found"))(identity)
			resultModel
		}
		readModel		
	}
	
	def update(model: Book)(implicit ec: ExecutionContext): Future[Book] = future {
		val updated = MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""UPDATE Book SET authorId = {authorId} WHERE bookId = {id}"""
			).on(
			"authorId" -> model.authorId,
			"id" -> model.bookId
			).executeUpdate()
			numberEffected match {
				case 0 => false
				case _ => true
			}
		}
		if(updated) {
			model
		} else {
			throw new DataNotFoundException("Book to update does not exist")
		}
	}
	
	def readAll(page: Int,perPage: Int)(implicit ec: ExecutionContext): Future[List[Book]] = future {
		val Books : List[Book] = MySQLConnector.withReadOnlyConnection { implicit connection =>
			val result = SQL(
				"""
				SELECT bookId, authorId FROM Book ORDER BY bookId LIMIT {offset}, {perPage}
				"""
			).on("offset" -> page * perPage, "perPage" -> perPage).as(
				RowParsers.bookParser *
			)
			result.toList
		}
		Books
	}

}