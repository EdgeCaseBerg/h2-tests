H2 Tests
=====================================================================

Neccesary MySQL Setup

	mysql> create database h2tests;
	mysql> CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';
	mysql> GRANT ALL ON h2tests.* TO 'test'@'localhost';
	mysql> FLUSH PRIVILEGES

Notes for blogpost:

- Always include the tablename in row parsers so that joins don't mess up. 
  Be sure to include a couple examples.

- Note that you can't set the engine in the migrations or h2 fails.
- Note the libraryDependencies in project/build.sbt to affect the build's classes themselves. (so we can flyway configure in one place)