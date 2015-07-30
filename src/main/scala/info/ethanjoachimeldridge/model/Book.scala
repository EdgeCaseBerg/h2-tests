package info.ethanjoachimeldridge.model

import java.util.Locale


/** A Book reference 
 * 
 * @param authorId The id of the [[Author]] who wrote the book
 * @param bookId The id of the [[Book]] itself
 */
case class Book(val authorId: Long, val bookId: Long)

/** A Book's meta information
 *
 * @param bookId The id of the book to which this meta information describes
 * @param lang The language this meta information is in
 * @param title The title of the book referenced by bookId
 * @param shortDescription A short description of the book, suitable for RSS/Twitter/Social Media
 * @param longDescription A long form description of the book for displaying detailed information about it
 */
case class BookMeta(val bookId: Long, val lang: Locale, val title: String, val shortDescription: String, val longDescription: String)