package com.guidefreitas.gamebox;
import android.content.Context;

import com.guidefreitas.gamebox.callbacks.CreateCategoryCallback;
import com.guidefreitas.gamebox.callbacks.CreateCategoryException;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilherme on 7/11/13.
 */
public class GameBoxService {

    
    public static void InitializeParse(Context context){
    	ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Game.class);
        Parse.initialize(context, ParseManager.applicationId, ParseManager.clientId);
        ParseACL.setDefaultACL(new ParseACL(), true);
    }

    public void CreateInicialCategoriesSync() throws Exception{
    	List<String> categories = new ArrayList<String>();
    	categories.add("First Person Shooter");
    	categories.add("Racing");
    	categories.add("Kids");
    	categories.add("RPG");
    	categories.add("Strategy");
    	
    	for(String cat : categories){
    		ParseObject categoryParse = ParseObject.create("category");
            categoryParse.put("name", cat);
            categoryParse.save();
    	}
    }
    
    public void CreateCategory(final Category category, final CreateCategoryCallback callback){
        ParseObject categoryParse = ParseObject.create("category");
        categoryParse.put("name", category.getName());
        categoryParse.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e == null){
					callback.done(category, null);
				}else{
					CreateCategoryException ex = new CreateCategoryException(e.getMessage());
					callback.done(null, ex);
				}
			}
		});
    }
   

    public void getAllCategories (com.parse.FindCallback<Category> callback){
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.include("category");
        query.clearCachedResult();
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.orderByAscending("name");
        query.findInBackground(callback);
    }
}
