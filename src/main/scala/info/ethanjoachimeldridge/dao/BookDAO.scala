package info.ethanjoachimeldridge.dao

import info.ethanjoachimeldridge.model._
import scala.concurrent.{ExecutionContext, Future}

trait BookDAO extends CrudDAO[Book]{

	/** Read perPage Book's at once from the database
	 *
	 * @param page Which paginated index to use for determining a list result
	 * @param perPage How many Book's to return in a single List
	 * @param ec Implicit execution context
	 * @return A Future containing a List of Book objects
	 */	
	def readAll(page: Int, perPage: Int)(implicit ec: ExecutionContext) : Future[List[Book]]

	/** Add BookMeta to a Book 
	 *
	 * @param book The book to have the meta information added to it
	 * @param bookMeta The meta to be added to the book
	 * @param ec Implicit execution context
	 * @return A Future containing the created BookMeta
	 */
	def addBookMetaToBook(book: Book, bookMeta: BookMeta)(implicit ec: ExecutionContext) : Future[BookMeta]

	/** Update Bookmeta from the DAL
	 *
	 * @param bookMeta The BookMeta to be updated
	 * @param ec Implicit execution context
	 & @return A Future containing the updated BookMeta
	 */
	def updateBookMeta(bookMeta: BookMeta)(implicit ec: ExecutionContext) : Future[BookMeta] 

	/** Delete BookMeta from the DAL
	 *
	 * @param bookMeta The BookMeta to be deleted
	 * @param ec Implicit execution context
	 * @return A Future containing a boolean
	 */
	def deleteBookMeta(bookMeta: BookMeta)(implicit ec: ExecutionContext) : Future[Boolean]

	/** Read meta records for a given Book
	 *
	 * @param book The Book whose meta information will be read
	 * @param ec Implicit execution context
	 * @return A Future containing a List of BookMeta for the given book
	 */
	def readMetaForBook(book: Book)(implicit ec: ExecutionContext) : Future[List[BookMeta]]

	/** Read both books and meta information in paginated form
	 * 
	 * @param page The page to retrieve
	 * @param perPage How many books and meta to retrieve per page
	 * @param ec Implicit execution context
	 * @return A Future containing a List of Tuples with books and meta in each
	 */
	def readAllWithMeta(page: Int, perPage: Int)(implicit ec: ExecutionContext) : Future[List[(Book, List[BookMeta])]]

}