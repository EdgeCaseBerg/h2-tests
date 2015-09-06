package info.ethanjoachimeldridge.dao.exception

abstract class DALException extends Exception

case class DataErrorException(val msg: String, originalException: Throwable) extends DALException

case class DuplicateDataError(val msg: String, originalException: Throwable) extends DALException

case class TableNotFoundException(val msg: String, originalException: Throwable) extends DALException

case class DataNotFoundException(val msg: String) extends DALException
