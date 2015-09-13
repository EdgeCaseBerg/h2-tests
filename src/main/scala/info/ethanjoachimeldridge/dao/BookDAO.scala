package info.ethanjoachimeldridge.dao

import info.ethanjoachimeldridge.model._
import scala.concurrent.{ExecutionContext, Future}

trait BookDAO extends CrudDAO[Book]{

	/** 
	 * @param page Which paginated index to use for determining a list result
	 * @param perPage How many Book's to return in a single List
	 * @param ec Implicit execution context
	 * @return A Future containing a List of Book objects
	 */	
	def readAll(page: Int, perPage: Int)(implicit ec: ExecutionContext) : Future[List[Book]]

}