package info.ethanjoachimeldridge.dao.exception

abstract class DALException extends Exception

case class DataErrorException(val msg: String, originalException: Throwable) extends DALException