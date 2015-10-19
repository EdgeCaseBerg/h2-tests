package info.ethanjoachimeldridge.model.validation

import org.scalatest._

import info.ethanjoachimeldridge.model._

class AuthorValidationTest extends FlatSpec with Matchers{

	val model = Author(-1, "")

	"An author" should "be invalid if the id is <= 0" in {
		val errors = Map("age" -> "Must be a positive integer")
		assertResult(errors) {
			AuthorValidator(model.copy(name="name")).validate
		}
		assertResult(errors) {
			AuthorValidator(model.copy(id=0,name="name")).validate
		}
	}

	it should "be invalid if the name field is empty" in {
		val errors = Map("name" -> "Name may not be empty")
		assertResult(errors) {
			AuthorValidator(model.copy(id=1,name="")).validate
		}
	}

	it should "be valid if the id is > 0 and the name is nonempty" in {
		val errors = Map[String,String]()
		val result = AuthorValidator(model.copy(id=1,name="name"))
		assertResult(errors) {
			result.validate
		}
		assert(result.isValid)
	}
}