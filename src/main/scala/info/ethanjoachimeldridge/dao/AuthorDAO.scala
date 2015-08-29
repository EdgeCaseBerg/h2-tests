package info.ethanjoachimeldridge.dao

import info.ethanjoachimeldridge.model._
import scala.concurrent.{ExecutionContext, Future}

trait AuthorDAO extends CrudDAO[Author]{

	/** 
	 * @param page Which paginated index to use for determining a list result
	 * @param perPage How many Author's to return in a single List
	 * @param ec Implicit execution context
	 * @return A Future containing a List of Author objects
	 */	
	def readAll(page: Int, perPage: Int)(implicit ec: ExecutionContext) : Future[List[Author]]

	/** 
	 * @page model An author model to look up books for
	 * @param ec Implicit execution context
	 * @return A Future containing a List of Book objects and their associated metadata in tuples
	 */
	def findBooksByAuthor(model: Author)(implicit ec: ExecutionContext) : Future[List[(Book,List[BookMeta])]]
}