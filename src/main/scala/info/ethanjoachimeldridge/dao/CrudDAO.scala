package info.ethanjoachimeldridge.dao

import scala.concurrent.{ExecutionContext, Future}

/** Create. Read. Update. Delete. Trait for data access of models
 *
 * Each method takes an implicit ExecutionContext to run futures.
 * This is to performing async database operations if neccesary.
 * @tparam T The type of model which an implementing class will handle
 */
trait CrudDAO[T] {
	/** Create a model of type T in the underlying datastore 
	 *
	 * @param model The model which will be created in the datastore
	 * @param ec Implicit execution context
	 * @return a Future containing the an object of type T updated with auto-values from the datastore if applicable
	 */
	 def create(model: T)(implicit ec: ExecutionContext) : Future[T]

	/** Create a model of type T in the underlying datastore 
	 *
	 * @note By not using an id: Long, field we do not tie ourselves to specific numeric ID's for databases, but leave the implementation up to the implementing class of this trait.
	 *
	 * @param model The model which will be read from the datastore, with any discrimantory values defined
	 * @param ec Implicit execution context
	 * @return a Future containing the an object of type T found by a property on the model
	 */
	 def read(model: T)(implicit ec: ExecutionContext) : Future[T]

	/** Update a model of type T in the underlying datastore 
	 *
	 * @param model The model which will be updated in the datastore
	 * @param ec Implicit execution context
	 * @return a Future containing the an object of type T updated with auto-values from the datastore if applicable
	 */
	 def update(model: T)(implicit ec: ExecutionContext) : Future[T]

	/** Delete a model of type T in the underlying datastore 
	 *
	 * @note By not using an id: Long, field we do not tie ourselves to specific numeric ID's for databases, but leave the implementation up to the implementing class of this trait.
	 *
	 * @note This method is not intended to return a Future[False] value, however this is implementation specific
	 *
	 * @param model The model which will be deleted from the datastore, with any discrimantory values defined
	 * @param ec Implicit execution context
	 * @return a Future containing the a Boolean value of True if the model was deleted or not found, A failed future otherwise.
	 */
	 def delete(model: T)(implicit ec: ExecutionContext) : Future[Boolean]

}