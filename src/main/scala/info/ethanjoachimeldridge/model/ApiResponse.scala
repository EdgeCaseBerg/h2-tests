package info.ethanjoachimeldridge.model

/** A response from the service 
 *
 * @param status A status code representing the response status
 * @param result The Data to send back to a client
 */
case class ApiResponse(status: Int, result: Any)