package com.guidefreitas.gamebox;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.guidefreitas.gamebox.adapters.CategoriesAdapter;
import com.guidefreitas.gamebox.adapters.GamesAdapter;
import com.guidefreitas.gamebox.adapters.LentAdapter;
import com.guidefreitas.gamebox.callbacks.DataSourceGetObjectsCallback;
import com.parse.FindCallback;
import com.parse.ParseException;

public class DataSources {
	
	private Activity activity;
	private GameBoxService gbApi = null;
	public CategoriesAdapter categoryAdapter = null;
	
	private static DataSources instance;
	private DataSources(Activity activity){
		this.activity = activity;
		gbApi = new GameBoxService(activity);
		categoryAdapter = new CategoriesAdapter(activity);

	}
	public static DataSources getInstance(Activity activity){
		if(instance == null)
			instance = new DataSources(activity);
		
		return instance;
	}
	
	
	public void getCategories(final DataSourceGetObjectsCallback<Category> callback){
		
		gbApi.getAllCategories(new FindCallback<Category>() {
			@Override
			public void done(List<Category> categoriesList, ParseException e) {
                if (e == null) {
                    categoryAdapter.clear();
                    String emptyCategoryLabel =  activity.getResources().getString(R.string.hint_category);
                    categoryAdapter.addAllWithBlank(categoriesList, emptyCategoryLabel);
                    callback.done(categoriesList, null);
                } else {
                	Log.e("score", "Error: " + e.getMessage());
                	DataSourceGetDataException ex = new DataSourceGetDataException(e.getMessage());
                	callback.done(null, ex);
                }
            }
        });
	}
	
	public static LentAdapter getLentAdapter(Context context){
		LentAdapter adapter = new LentAdapter(context);
		return adapter;
	}

	public static GamesAdapter getGamesAdapter(Context context, String categoryId){
		GamesAdapter adapter = new GamesAdapter(context, categoryId);
		return adapter;
	}
	
	
}
