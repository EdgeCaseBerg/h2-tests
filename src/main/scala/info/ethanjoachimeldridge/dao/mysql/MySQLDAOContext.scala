package info.ethanjoachimeldridge.dao.mysql

import info.ethanjoachimeldridge.dao.{AuthorDAO,BookDAO,DAOContext}

trait MySQLDAOContext extends DAOContext {
	val authorDAO : AuthorDAO = new AuthorMySQLDAO
	val bookDAO : BookDAO = new BookMySQLDAO
}