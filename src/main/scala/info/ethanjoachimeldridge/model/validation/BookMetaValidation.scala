package info.ethanjoachimeldridge.model.validation

import info.ethanjoachimeldridge.model.Author
import com.plameno.validation._

class BookMetaValidation(a: Author) extends FormValidator {
	val validators = Map(
		"lang" -> List(
			RequiredStringValidator(a.lang, Some("Name may not be empty"))
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
			PositiveIntValidator(a.id.toInt)
		)
	)

	def isValid = validate.isEmpty
}

object BookMetaValidation {
	def apply(a: Author) = {
		new BookMetaValidation(a)
	}
}