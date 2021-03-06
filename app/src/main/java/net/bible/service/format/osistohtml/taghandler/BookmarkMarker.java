package net.bible.service.format.osistohtml.taghandler;

import net.bible.android.control.bookmark.BookmarkStyle;
import net.bible.service.format.osistohtml.OsisToHtmlParameters;
import net.bible.service.format.osistohtml.osishandlers.OsisToHtmlSaxHandler.VerseInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Display an img if the current verse has MyNote
 * 
 * @author Martin Denham [mjdenham at gmail dot com]
 * @see gnu.lgpl.License for license details.<br>
 *      The copyright to this program is held by it's author. 
 */
public class BookmarkMarker {

	private Map<Integer, List<BookmarkStyle>> bookmarkStylesByBookmarkedVerse = new HashMap<>();
	
	private OsisToHtmlParameters parameters;
	
	private VerseInfo verseInfo;

	public BookmarkMarker(OsisToHtmlParameters parameters, VerseInfo verseInfo) {
		this.parameters = parameters;
		this.verseInfo = verseInfo;

		// create hashset of verses to optimise verse note lookup
		bookmarkStylesByBookmarkedVerse.clear();
		if (parameters.getBookmarkStylesByBookmarkedVerse()!=null) {
			bookmarkStylesByBookmarkedVerse = parameters.getBookmarkStylesByBookmarkedVerse();
		}
	}
	
	/** Get any bookmark classes for current verse
	 */
	public List<String> getBookmarkClasses() {
		if (bookmarkStylesByBookmarkedVerse !=null && parameters.isShowBookmarks()) {
			if (bookmarkStylesByBookmarkedVerse.containsKey(verseInfo.currentVerseNo)) {
				final List<BookmarkStyle> bookmarkStyles = bookmarkStylesByBookmarkedVerse.get(verseInfo.currentVerseNo);
				if (bookmarkStyles==null || bookmarkStyles.isEmpty()) {
					return Collections.singletonList(parameters.getDefaultBookmarkStyle().name());
				} else {
					return getStyleNames(bookmarkStyles);
				}
			}
		}
		return Collections.emptyList();
	}

	private List<String> getStyleNames(List<BookmarkStyle> bookmarkStyles) {
		List<String> styleNames = new ArrayList<>();
		for (BookmarkStyle bookmarkStyle : bookmarkStyles) {
			styleNames.add(bookmarkStyle.name());
		}
		return styleNames;
	}
}
