package info.ethanjoachimeldridge.model.validation.exception

abstract class ValidationException extends Exception

case class InvalidModelException(val msg: String) extends ValidationException
