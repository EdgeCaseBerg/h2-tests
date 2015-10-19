package info.ethanjoachimeldridge.service

import info.ethanjoachimeldridge.dao.DAOContext
import info.ethanjoachimeldridge.model._
import info.ethanjoachimeldridge.model.validation.AuthorValidator
import info.ethanjoachimeldridge.model.validation.exception._

import scala.concurrent.{ExecutionContext, Future,future}

class AuthorService(implicit daoContext: DAOContext, ec: ExecutionContext) extends Service {
	def withValid[B](a: Author, f: Author => B): B = {
		validateCallWith(AuthorValidator.apply _)(a) match {
			case errors if errors.isEmpty => f(a)
			case errors => 
				throw new InvalidModelException(
					errors.map { case (f, e) => s"$f: $e;" }.mkString(",")
				)
		}
	}

	def createAuthor(author: Author) = {
		withValid(author, daoContext.authorDAO.create)
	}

	def deleteAuthor(author: Author) = {
		withValid(author, daoContext.authorDAO.delete)
	}

	def findAuthorById(id: Long) = {
		daoContext.authorDAO.read(Author(id=id,name=""))
	}

	def getBooksForAuthor(author: Author) = {
		withValid(author, daoContext.authorDAO.findBooksByAuthor)
	}

	def getAuthors(page: Int, perPage: Int) = {
		daoContext.authorDAO.readAll(page, perPage)
	}
}