package net.bible.service.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bible.service.common.Logger;

import org.crosswire.common.util.Version;
import org.crosswire.jsword.book.Book;

/**
 * Some repos contain the same books.  Get the latest version of duplicates.
 */
public class RepoBookDeduplicator {

	private Map<BookKey, Book> bookSet = new HashMap<>();
	
	private Logger logger = new Logger(this.getClass().getName());
	
	public List<Book> getBooks() {
		return new ArrayList<>(bookSet.values());
	}
	
	/**
	 * Add books if not already added or are a later version than previously added version
	 */
	public void addAll(List<Book> books) {
		for (Book book : books) {
			try {
				BookKey bookKey = new BookKey(book);
				Book existing = bookSet.get(bookKey);
				Book latest = getLatest(existing, book);
				bookSet.put(bookKey, latest);
			} catch (Exception e) {
				logger.error("Error comparing download book versions", e);
			}
		}
	}
	
	/**
	 * Add books if not already added.
	 * Used for beta repo - beta should never override live books.
	 */
	public void addIfNotExists(List<Book> books) {
		for (Book book : books) {
			try {
				BookKey bookKey = new BookKey(book);
				if (!bookSet.containsKey(bookKey)) {
					bookSet.put(bookKey, book);
				}
			} catch (Exception e) {
				logger.error("Error comparing download book versions", e);
			}
		}
	}

	private Book getLatest(Book previous, Book current) {
		if (previous==null) {
			return current;
		}
		
		// SBMD defaults t0 1.0.0 if version does not exist
        Version previousVersion = new Version(previous.getBookMetaData().getProperty("Version"));
        Version currentVersion = new Version(current.getBookMetaData().getProperty("Version"));
        
       	if (previousVersion.compareTo(currentVersion)<0) {
       		return current;
       	} else {
       		return previous;
       	}
	}
	
	private class BookKey {
		private Book book;
		
		BookKey(Book book) {
			this.book = book;
			book.hashCode();
		}
		
	    @Override
	    public boolean equals(Object obj) {
	        // The real bit ...
	        Book thatBook = ((BookKey)obj).book;
	        return book.getBookCategory().equals(thatBook.getBookCategory()) 
	        		&& book.getAbbreviation().equals(thatBook.getAbbreviation()) 
	        		&& book.getLanguage().equals(thatBook.getLanguage());
	    }

		
	    @Override
	    public int hashCode() {
	        return book.getAbbreviation().hashCode();
	    }
	}
}
