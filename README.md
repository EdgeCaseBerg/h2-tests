H2 Tests
=====================================================================

Neccesary MySQL Setup

	mysql> create database h2tests;
	mysql> CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';
	mysql> GRANT ALL ON h2tests.* TO 'test'@'localhost';
	mysql> FLUSH PRIVILEGES
