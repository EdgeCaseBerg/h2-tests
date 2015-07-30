package info.ethanjoachimeldridge.model

/** A parameters holding object 
 * 
 * @page The page to retrieve of the API request
 * @perPage The number of items per page to retrieve
 */
case class ApiPagination(val page: Option[Int], val perPage: Option[Int])
