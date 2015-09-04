organization := "info.ethanjoachimeldridge"

name := "h2-example"

version := "0.0.0-SNAPSHOT" 

scalaVersion := "2.11.6"

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _)

libraryDependencies ++= { 
	val sprayV = "1.3.3" 
	val akkaV = "2.3.9"
	val anormVersion = "2.3.+"
	Seq(
		"io.spray"            %%  "spray-can"     % sprayV,
		"io.spray"            %%  "spray-routing" % sprayV,
		"io.spray"            %%  "spray-json"    % "1.3.1",
		"com.typesafe.akka"   %%  "akka-actor"    % akkaV,
		"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
		"ch.qos.logback"  %  "logback-classic"   % "1.1.3",
		"com.typesafe" % "config" % "1.2.1",
		"com.typesafe.play" %% "anorm" % anormVersion,
		"org.scalikejdbc" %% "scalikejdbc"        % "2.2.+",
		"com.h2database"  %  "h2"                % "1.4.187",
		"mysql" % "mysql-connector-java" % "5.1.36",
		"com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
		"io.spray"            %%  "spray-testkit" % sprayV  % "test",
		"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
		"org.flywaydb" % "flyway-core" % "3.2.1"
	)
}

Revolver.settings

fork in Test := true

javaOptions in Test += "-XX:MaxPermSize=256M"

//Flyway for database settings: http://flywaydb.org/documentation/sbt/

Conf.dbConf := {
  val cfg = com.typesafe.config.ConfigFactory.parseFile((resourceDirectory in Compile).value / "application.conf")
  val prefix = "db.default"
  (cfg.getString(s"$prefix.url"), cfg.getString(s"$prefix.user"), cfg.getString(s"$prefix.password"))
}

seq(flywaySettings: _*)

flywayUrl := Conf.dbConf.value._1

flywayUser := Conf.dbConf.value._2

flywayPassword := Conf.dbConf.value._3

flywayDriver := "com.mysql.jdbc.Driver"
