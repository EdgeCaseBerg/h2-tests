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
				"""DELETE FROM book WHERE bookId = {bookId}"""
			).on("bookId" -> model.bookId).executeUpdate()
			true //true because deleting something that doesn't exist is the same result as deleting something that does
		}
	}
	def read(model: Book)(implicit ec: ExecutionContext): Future[Book] = future {
		val readModel : Book = MySQLConnector.withReadOnlyConnection { implicit connection => 
			val result = SQL(
				"""SELECT bookId, authorId FROM book WHERE bookId = {id}"""
			).on("id" -> model.bookId).as(RowParsers.bookParser *).headOption
			val resultModel : Book = result.fold(throw new DataNotFoundException("Book not found"))(identity)
			resultModel
		}
		readModel		
	}
	
	def update(model: Book)(implicit ec: ExecutionContext): Future[Book] = future {
		val updated = MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""UPDATE book SET authorId = {authorId} WHERE bookId = {id}"""
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
		val books : List[Book] = MySQLConnector.withReadOnlyConnection { implicit connection =>
			val result = SQL(
				"""
				SELECT bookId, authorId FROM book ORDER BY bookId LIMIT {offset}, {perPage}
				"""
			).on("offset" -> page * perPage, "perPage" -> perPage).as(
				RowParsers.bookParser *
			)
			result.toList
		}
		books
	}

	def addBookMetaToBook(book: Book,bookMeta: BookMeta)(implicit ec: ExecutionContext): Future[BookMeta] = future {
		val updatedBookMeta : BookMeta = MySQLConnector.withTransaction { implicit connection =>
			val affectedRows = SQL(
				"""
				INSERT INTO bookMeta (bookId, lang, title, shortDescription, longDescription) 
				VALUES ({bookId}, {lang}, {title}, {shortDescription}, {longDescription})
				"""
			).on(
				"bookId" -> book.bookId,
				"lang" -> bookMeta.lang.toLanguageTag,
				"title" -> bookMeta.title,
				"shortDescription" -> bookMeta.shortDescription,
				"longDescription" -> bookMeta.longDescription
			).executeUpdate()
			affectedRows match {
				case 0 => throw DataErrorException("Could not add BookMeta to book", new Throwable("No rows updated during transaction"))
				case _ => bookMeta.copy(bookId = book.bookId)
			}
		}
		updatedBookMeta
	}

	def deleteBookMeta(bookMeta: BookMeta)(implicit ec: ExecutionContext) : Future[Boolean] = future {
		MySQLConnector.withTransaction { implicit connection =>
			val numberEffected = SQL(
				"""DELETE FROM bookMeta WHERE bookId = {bookId} AND lang = {lang}"""
			).on("bookId" -> bookMeta.bookId, "lang" -> bookMeta.lang.toLanguageTag).executeUpdate()
			true //true because deleting something that doesn't exist is the same result as deleting something that does
			}
	}

	def readMetaForBook(book: Book)(implicit ec: ExecutionContext) : Future[List[BookMeta]] = future {
		val bookMetaList : List[BookMeta] = MySQLConnector.withReadOnlyConnection { implicit connection =>
			val bookMetas = SQL(
				"""
				SELECT bookMeta.bookId, bookMeta.lang, bookMeta.title, bookMeta.shortDescription, bookMeta.longDescription 
				FROM bookMeta 
				WHERE bookId = {bookId}
				"""
			).on(
				"bookId" -> book.bookId
			).as(
				RowParsers.bookMetaParser *
			)
			bookMetas.toList
		}
		bookMetaList
	}


	def readAllWithMeta(page: Int, perPage: Int)(implicit ec: ExecutionContext) : Future[List[(Book, List[BookMeta])]] = future {
		val allWithMeta : List[(Book, List[BookMeta])] = MySQLConnector.withReadOnlyConnection { implicit connection =>
			val result = SQL(
				"""
				SELECT  book.bookId, book.authorId,
								bookMeta.bookId, bookMeta.lang, bookMeta.title, bookMeta.shortDescription, bookMeta.longDescription 
				FROM book 
					JOIN (
						SELECT book.bookId FROM book LIMIT {offset}, {perPage}
					) AS paginationQuery ON book.bookId = paginationQuery.bookId
					LEFT JOIN bookMeta ON book.bookId = bookMetas.bookId
				"""
			).on(
				"offset" -> page * perPage,
				"perPage" -> perPage
			).as(
				RowParsers.bookParser ~ (RowParsers.bookMetaParser ?) *
			).groupBy(_._1).map {
				case(book, list) => {
					(book, list.map(_._2).filter(_.isDefined).map(_.get).toSet.toList)
				}
			}
			result.toList
		}
		allWithMeta
	}

}