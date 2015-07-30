package info.ethanjoachimeldridge

import com.typesafe.config.ConfigFactory

/** Simple configuration object to reduce hardcoded string usage in codebase */
object Configuration {
	private val config = ConfigFactory.load();

	/** interface (localhost|ip addr|etc) to which the public http service will bind to */
	val httpInterface = config.getString("bindings.http.interface")

	/** port which the public http service will run on  */
	val httpPort = config.getInt("bindings.http.port")

	val defaultPageSize = config.getInt("api.default.pageSize")

}