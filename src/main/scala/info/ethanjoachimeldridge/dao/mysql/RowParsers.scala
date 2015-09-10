package info.ethanjoachimeldridge.dao.mysql

import anorm._
import anorm.SqlParser._

import java.util.Locale

import info.ethanjoachimeldridge.model.{Author, Book, BookMeta}

/** Object to contain useful row parsers that can be used throughout the DAO
 *
 * Note that we use the table name in the column specifier, this is important 
 * or else you might end up with non-nullable column errors
 */
object RowParsers {

	/** Simple Anorm Parser to extract Author Records */
	val authorParser = {
		get[Long]("author.id") ~ 
		get[String]("author.name") map {
			case id ~ name => Author(id, name)
		}
	}

	/** Simple Anorm Parser to extract Book Records */
	val bookParser = {
		get[Long]("book.authorId") ~
		get[Long]("book.bookId") map {
			case authorId ~ bookId => Book(authorId, bookId)
		}
	}

	/** Simple Anorm Parser to extract Book Meta Records */
	val bookMetaParser = {
		get[Long]("bookMeta.bookId") ~
		get[String]("bookMeta.lang") ~
		get[String]("bookMeta.title") ~
		get[String]("bookMeta.shortDescription") ~
		get[String]("bookMeta.longDescription") map {
			case bookId ~ lang ~ title ~ shortDescription ~ longDescription =>
				BookMeta(bookId, new Locale(lang), title, shortDescription, longDescription)
		}
	}

}