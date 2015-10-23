package info.ethanjoachimeldridge.model.validation

import org.scalatest._

import info.ethanjoachimeldridge.model._

class BookValidationTest extends FlatSpec with Matchers{

	val model = Book(-1, -1)

	"An Book" should "be invalid if the authorId is <= 0" in {
		val errors = Map("authorId" -> "Must be a positive integer")
		assertResult(errors) {
			BookValidator(model.copy(bookId=1)).validate
		}
	}

	it should "be invalid if the bookId is <= 0" in {
		val errors = Map("bookId" -> "Must be a positive integer")
		assertResult(errors) {
			BookValidator(model.copy(authorId=1)).validate
		}
	}
 
	it should "be valid if the bookId and authorId is > 0" in {
		val errors = Map[String,String]()
		val result = BookValidator(model.copy(authorId=1,bookId=1))
		assertResult(errors) {
			result.validate
		}
		assert(result.isValid)
	}

}