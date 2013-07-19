package com.guidefreitas.gamebox;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.guidefreitas.gamebox.adapters.CategoriesAdapter;
import com.guidefreitas.gamebox.adapters.GamesAdapter;
import com.guidefreitas.gamebox.adapters.LentAdapter;


public class DataSources {

	public static CategoriesAdapter getCategoriesAdapter(Context context, boolean forceRefresh){
		CategoriesAdapter adapter = new  CategoriesAdapter(context, forceRefresh);
		return adapter;
	}
	
	public static LentAdapter getLentAdapter(FragmentActivity activity){
		LentAdapter adapter = new LentAdapter(activity);
		return adapter;
	}

	public static GamesAdapter getGamesAdapter(FragmentActivity activity, String categoryId, boolean forceRefresh){
		GamesAdapter adapter = new GamesAdapter(activity, categoryId, forceRefresh);
		return adapter;
	}
	
	
}
