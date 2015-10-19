package info.ethanjoachimeldridge.model.validation

import info.ethanjoachimeldridge.model.Author
import com.plameno.validation._

class AuthorValidator(a: Author) extends FormValidator {
	val validators = Map(
		"name" -> List(
			RequiredStringValidator(a.name, Some("Name may not be empty"))
		),
		"age" -> List(
			PositiveIntValidator(a.id.toInt) /* TODO: Consider adding PR to plameno's repo for this or a [Number] validator */
		)
	)

	def isValid = validate.isEmpty
}

object AuthorValidator {
	def apply(a: Author) = {
		new AuthorValidator(a)
	}
}