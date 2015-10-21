package info.ethanjoachimeldridge.model.validation

import info.ethanjoachimeldridge.model.Book
import com.plameno.validation._

class BookValidator(a: Book) extends FormValidator {
	val validators = Map(
		"authorId" -> List(
			PositiveIntValidator(a.authorId.toInt)
		),
		"bookId" -> List(
			PositiveIntValidator(a.bookId.toInt)
		)
	)

	def isValid = validate.isEmpty
}

object BookValidator {
	def apply(a: Book) = {
		new BookValidator(a)
	}
}