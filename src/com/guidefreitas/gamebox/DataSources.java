package com.guidefreitas.gamebox;

import android.content.Context;
import com.guidefreitas.gamebox.adapters.CategoriesAdapter;
import com.guidefreitas.gamebox.adapters.GamesAdapter;
import com.guidefreitas.gamebox.adapters.LentAdapter;


public class DataSources {

	public static CategoriesAdapter getCategoriesAdapter(Context context, boolean forceRefresh){
		CategoriesAdapter adapter = new  CategoriesAdapter(context, forceRefresh);
		return adapter;
	}
	
	public static LentAdapter getLentAdapter(Context context){
		LentAdapter adapter = new LentAdapter(context);
		return adapter;
	}

	public static GamesAdapter getGamesAdapter(Context context, String categoryId, boolean forceRefresh){
		GamesAdapter adapter = new GamesAdapter(context, categoryId, forceRefresh);
		return adapter;
	}
	
	
}
