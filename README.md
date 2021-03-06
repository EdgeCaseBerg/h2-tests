H2 Tests
=====================================================================

Neccesary MySQL Setup

	mysql> create database h2tests;
	mysql> CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';
	mysql> GRANT ALL ON h2tests.* TO 'test'@'localhost';
	mysql> FLUSH PRIVILEGES

Neccesary Dependency Setup

You need to publish the validation library locally to have sbt pick up on it. 

	git clone https://github.com/siman/scala-validation.git
	sbt +publishLocal


Notes for blogpost:

- Always include the tablename in row parsers so that joins don't mess up. (Be sure to include a couple examples.)
- Note that you can't set the engine in the migrations or h2 fails.
- Note the libraryDependencies in project/build.sbt to affect the build's classes themselves. (so we can flyway configure in one place)
- h2 constraints seperate from create table
- `<logger name="org.flywaydb.core" level="WARN"/>` to debug for troubleshooting migrations
- h2 query logging for checking errors via TRACE_LEVEL_SYSTEM_OUT=3 in the connection url level 2 is good.
- Local publishing of dependencies for proprietary or built from source/ modified versions of libraries
