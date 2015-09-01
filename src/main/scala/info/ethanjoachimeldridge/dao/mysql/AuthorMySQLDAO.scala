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
				"""INSERT INTO author (name) VALUES ({name})"""
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
				"""DELETE FROM author WHERE id = {id}"""
			).on("id" -> model.id).executeUpdate()
			true //true because deleting something that doesn't exist is the same result as deleting something that does
		}
	}
	def read(model: Author)(implicit ec: ExecutionContext): Future[Author] = future {
		val readModel : Author = MySQLConnector.withReadOnlyConnection { implicit connection => 
			val result = SQL(
				"""SELECT id, name FROM author WHERE id = {id}"""
			).on("id" -> model.id).as(RowParsers.authorParser *).headOption
			val resultModel : Author = result.fold(throw new DataNotFoundException("Author not found"))(identity)
			resultModel
		}
		readModel		
	}
	
	def update(model: Author)(implicit ec: ExecutionContext): Future[Author] = future {
		val updated = MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""UPDATE author SET name = {name} WHERE id = {id}"""
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

	def findBooksByAuthor(model: Author)(implicit ec: ExecutionContext): Future[List[(Book, List[BookMeta])]] = future {
		val books : List[(Book, List[BookMeta])] = MySQLConnector.withReadOnlyConnection { implicit connection =>
			val combinedParsed = RowParsers.bookParser ~ (RowParsers.bookMetaParser ?)
			val resultant = SQL(
				"""
				SELECT 
					books.authorId, 
					books.bookId, 
					bookMeta.lang, 
					bookMeta.title, 
					bookMeta.shortDescription, 
					bookMeta.longDescription 
				FROM book LEFT JOIN bookMeta ON book.bookId = bookMeta.bookId
				WHERE authorId = {authorId}
				"""
			).on(
				"authorId" -> model.id
			).as(combinedParsed *).groupBy(_._1).map {
				case (book, l) => (book, l.map(_._2).filter(_.isDefined).map(_.get))
			}
			resultant.toList
		}
		books
	}
	// def readAll(page: Int,perPage: Int)(implicit ec: ExecutionContext): Future[List[Author]] = ???

}