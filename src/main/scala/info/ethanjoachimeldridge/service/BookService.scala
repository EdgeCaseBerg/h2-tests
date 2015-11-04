package info.ethanjoachimeldridge.service

import info.ethanjoachimeldridge.dao.DAOContext
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.model.validation.BookValidator
import info.ethanjoachimeldridge.model.validation.exception._

import scala.concurrent.{ExecutionContext, Future,future}

class BookService(implicit daoContext: DAOContext, ec: ExecutionContext) extends Service {
	def withValid[B](a: Book, f: Book => B): B = {
		validateCallWith(BookValidator.apply _)(a) match {
			case errors if errors.isEmpty => f(a)
			case errors => 
				throw new InvalidModelException(
					errors.map { case (f, e) => s"$f: $e;" }.mkString(",")
				)
		}
	}

	def createBook(book: Book) = {
		withValid(book, daoContext.bookDAO.create)
	}

	def deleteBook(book: Book) = {
		withValid(book, daoContext.bookDAO.delete)
	}

	def getBooks(page: Int, perPage: Int) = {
		daoContext.bookDAO.readAll(page, perPage)
	}
}