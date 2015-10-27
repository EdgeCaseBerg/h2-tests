package info.ethanjoachimeldridge.model.validation

import info.ethanjoachimeldridge.model.BookMeta
import com.plameno.validation._

class BookMetaValidator(a: BookMeta) extends FormValidator {
	val validators = Map(
		"lang" -> List(
			RequiredStringValidator(a.lang.toLanguageTag, Some("Name may not be empty"))
		),
		"title" -> List(
			RequiredStringValidator(a.title, Some("Title may not be empty"))
		),
		"shortDescription" -> List(
			RequiredStringValidator(a.shortDescription, Some("Short description may not be empty"))
		),
		"longDescription" -> List(
			RequiredStringValidator(a.longDescription, Some("Long description may not be empty"))
		),
		"bookId" -> List(
			PositiveIntValidator(a.bookId.toInt)
		)
	)

	def isValid = validate.isEmpty
}

object BookMetaValidator {
	def apply(a: BookMeta) = {
		new BookMetaValidator(a)
	}
}