package com.sporeg.foodcollection.database;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionSampleProvider extends
		SearchRecentSuggestionsProvider {
	public final static String AUTHORITY = "com.example.SuggestionProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;

	public SearchSuggestionSampleProvider() {
		super();
		setupSuggestions(AUTHORITY, MODE);
	}

}
