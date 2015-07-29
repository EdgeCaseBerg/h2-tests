organization := "info.ethanjoachimeldridge"

name := "h2-example"

version := "0.0.0-SNAPSHOT" 

scalaVersion := "2.10.5"

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
		"com.typesafe" % "config" % "1.2.1",
		"com.typesafe.play" %% "anorm" % anormVersion,
		"org.scalikejdbc" %% "scalikejdbc"        % "2.2.+",
		"com.h2database"  %  "h2"                % "1.4.187",
		"mysql" % "mysql-connector-java" % "5.1.36",
		"com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
		"io.spray"            %%  "spray-testkit" % sprayV  % "test",
		"org.scalatest" % "scalatest_2.10" % "2.2.4" % "test",
		"org.flywaydb" % "flyway-core" % "3.2.1"
	)
}

fork in Test := true

javaOptions in Test += "-XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=256M"

//Flyway for database settings: http://flywaydb.org/documentation/sbt/

seq(flywaySettings: _*)

flywayUrl := "jdbc:mysql://localhost/h2example"

flywayUser := "test"

flywayPassword := "test"

flywayDriver := "com.mysql.jdbc.Driver"
