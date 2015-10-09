package info.ethanjoachimeldridge.service

import info.ethanjoachimeldridge.dao.DAOContext
import info.ethanjoachimeldridge.model._

import scala.concurrent.{ExecutionContext, Future,future}

class AuthorService(implicit daoContext: DAOContext, ec: ExecutionContext)  {
	def createAuthor(author: Author) = {
		//validate
		daoContext.authorDAO.create(author)
	}
}