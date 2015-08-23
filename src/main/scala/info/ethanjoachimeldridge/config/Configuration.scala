package info.ethanjoachimeldridge.config

import com.typesafe.config.ConfigFactory

/** Simple configuration object to reduce hardcoded string usage in codebase */
trait BaseConfiguration {
	protected val config = ConfigFactory.load();

	/** interface (localhost|ip addr|etc) to which the public http service will bind to */
	val httpInterface = config.getString("bindings.http.interface")

	/** port which the public http service will run on  */
	val httpPort = config.getInt("bindings.http.port")

	val defaultPageSize = config.getInt("api.default.pageSize")

	

}

trait SqlConfiguration extends BaseConfiguration {
	val dbDriver = config.getString("db.default.driver")

	val dbUrl = config.getString("db.default.url")

	val dbUser = config.getString("db.default.user")

	val dbPassword = config.getString("db.default.password")
}

object Configuration extends BaseConfiguration with SqlConfiguration 