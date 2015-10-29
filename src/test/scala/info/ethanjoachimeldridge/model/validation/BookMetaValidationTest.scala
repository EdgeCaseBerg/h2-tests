package info.ethanjoachimeldridge.model.validation

import org.scalatest._
import java.util.Locale
import info.ethanjoachimeldridge.model._

class BookMetaValidationTest extends FlatSpec with Matchers{

	val model = BookMeta(bookId = -1, lang = Locale.ENGLISH, title = "", shortDescription = "", longDescription = "")

	"An BookMeta" should "be invalid if the bookId is <= 0" in {
		val errors = Map("bookId" -> "Must be a positive integer")
		assertResult(errors) {
			BookMetaValidator(model.copy(
				title="NonEmpty",shortDescription="NonEmpty",longDescription="NonEmpty"
			)).validate
		}
	}

	it should "be invalid if the title is empty" in {
		val errors = Map("title" -> "Title may not be empty")
		assertResult(errors) {
			BookMetaValidator(model.copy(
				bookId=1,shortDescription="NonEmpty",longDescription="NonEmpty"
			)).validate
		}	
	}
 
	it should "be valid if the bookMeta has all fields non empty and positive" in {
		val errors = Map[String,String]()
		val result = BookMetaValidator(model.copy(bookId=1,title="NonEmpty",shortDescription="NonEmpty",longDescription="NonEmpty"))
		assertResult(errors) {
			result.validate
		}
		assert(result.isValid)
	}

}