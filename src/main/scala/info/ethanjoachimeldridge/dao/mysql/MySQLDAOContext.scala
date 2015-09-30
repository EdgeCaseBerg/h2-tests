package info.ethanjoachimeldridge.dao.mysql

trait MySQLDAOContext extends DAOContext {
	val authorDAO : AuthorDAO 
	val bookDAO : BookDAO
}