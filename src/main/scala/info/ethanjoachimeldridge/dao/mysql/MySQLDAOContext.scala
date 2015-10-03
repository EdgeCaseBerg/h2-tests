package info.ethanjoachimeldridge.dao.mysql

import info.ethanjoachimeldridge.dao.{AuthorDAO,BookDAO,DAOContext}

trait MySQLDAOContext extends DAOContext {
	val authorDAO : AuthorDAO 
	val bookDAO : BookDAO
}