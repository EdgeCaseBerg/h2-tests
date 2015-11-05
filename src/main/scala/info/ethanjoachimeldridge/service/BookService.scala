package info.ethanjoachimeldridge.service

import info.ethanjoachimeldridge.dao.DAOContext
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.model.validation.{BookValidator,BookMetaValidator}
import info.ethanjoachimeldridge.model.validation.exception._

import scala.concurrent.{ExecutionContext, Future,future}

class BookService(implicit daoContext: DAOContext, ec: ExecutionContext) extends Service {
	def withValidBook[B](a: Book, f: Book => B): B = {
		validateCallWith(BookValidator.apply _)(a) match {
			case errors if errors.isEmpty => f(a)
			case errors => 
				throw new InvalidModelException(
					errors.map { case (f, e) => s"$f: $e;" }.mkString(",")
				)
		}
	}

	def withValidBookMeta[B](a: BookMeta, f: BookMeta => B): B = {
		validateCallWith(BookMetaValidator.apply _)(a) match {
			case errors if errors.isEmpty => f(a)
			case errors => 
				throw new InvalidModelException(
					errors.map { case (f, e) => s"$f: $e;" }.mkString(",")
				)
		}
	}

	def createBook(book: Book) = {
		withValidBook(book, daoContext.bookDAO.create)
	}

	def findBookById(bookId: Long) ={
		daoContext.bookDAO.read(Book(-1,bookId))
	}

	def createBookMeta(bookMeta: BookMeta) = {
		val createResult = for {
			book <- daoContext.bookDAO.read(Book(-1, bookMeta.bookId))
			addedMeta <- withValidBookMeta(bookMeta,daoContext.bookDAO.addBookMetaToBook(book, _:BookMeta))
		} yield (book, addedMeta)
		createResult.map {
			case (book,addedMeta) => addedMeta
		}.recover {
			case e => throw e
		}	
	}

	def deleteBook(book: Book) = {
		withValidBook(book, daoContext.bookDAO.delete)
	}

	def getBooks(page: Int, perPage: Int) = {
		daoContext.bookDAO.readAll(page, perPage)
	}
}