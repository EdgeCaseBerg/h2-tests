CREATE TABLE book (
	bookId INT(16) UNSIGNED NOT NULL auto_increment PRIMARY KEY,
	authorId INT(16) UNSIGNED NOT NULL,
	CONSTRAINT FOREIGN KEY (`authorId`) REFERENCES `author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE bookMeta (
	bookId INT(16) UNSIGNED NOT NULL,
	lang VARCHAR(8) NOT NULL,
	title VARCHAR(128) NOT NULL,
	shortDescription VARCHAR(160), 
	longDescription LONGTEXT,
	PRIMARY KEY(bookId, lang),
	CONSTRAINT FOREIGN KEY (`bookId`) REFERENCES `book` (`bookId`) ON DELETE CASCADE ON UPDATE CASCADE
);