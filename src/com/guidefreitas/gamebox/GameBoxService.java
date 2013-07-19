package com.guidefreitas.gamebox;
import android.content.Context;

import com.guidefreitas.gamebox.callbacks.CompleteCallback;
import com.guidefreitas.gamebox.callbacks.FindException;
import com.guidefreitas.gamebox.callbacks.FindOneCallback;
import com.guidefreitas.gamebox.callbacks.GameboxException;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
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
	
	public static final String applicationId = "x2YnBUxAskUDXXDFj8o3tKUi6KuJ5He3tGAnJc8n";
    public static final String clientId = "8gEzghntbxv34JyA1aKrIp0BUfjehyxiU8UQbUGu";

	
    public static void InitializeParse(Context context){
    	ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Game.class);
        Parse.initialize(context, applicationId, clientId);
        ParseACL.setDefaultACL(new ParseACL(), true);
    }

    public static void getGameById(String gameId, final FindOneCallback<Game> callback){
    	if (gameId != null && !gameId.isEmpty()) {
    		ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
			query.whereEqualTo(Game.FIELD_OBJECT_ID, gameId);
			query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
			query.getFirstInBackground(new GetCallback<Game>() {

				@Override
				public void done(Game game, ParseException e) {
					if (e == null) {
						callback.done(game, null);
					}else{
						FindException ex = new FindException(e.getMessage());
						callback.done(null, ex);
					}
				}
			});
    		
    	}else{
    		FindException ex = new FindException("Game id not informed");
    		callback.done(null, ex);
    	}
    }
    public static void CreateInicialCategoriesSync() throws Exception{
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
    
    public static void CreateGame(final Game game, final CompleteCallback<Game> callback){
    	game.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException ex) {
				if(callback != null){
					if(ex == null){
						callback.done(game, null);
					}else{
						GameboxException e = new GameboxException(ex.getMessage());
						callback.done(null, e);
					}
				}
			}
		});
    }
    
    public static void DeleteGame(final Game game, final CompleteCallback<Game> callback){
    	game.deleteInBackground(new DeleteCallback() {
			
			@Override
			public void done(ParseException ex) {
				ParseQuery.clearAllCachedResults();
				if(callback != null){
					if(ex == null){
						
						callback.done(game, null);
					}else{
						GameboxException e = new GameboxException(ex.getMessage());
						callback.done(null, e);
					}
				}
			}
		});
    }
    
    public static void DeleteCategory(final Category category, final CompleteCallback<Category> callback){
    	category.deleteInBackground(new DeleteCallback() {
			
			@Override
			public void done(ParseException e) {
				ParseQuery.clearAllCachedResults();
				if(callback != null){
					if(e == null){
						callback.done(category, null);
					}else{
						GameboxException ex = new GameboxException(e.getMessage());
						callback.done(null, ex);
					}
				}
			}
		});
    }
    
    public static void CreateCategory(final Category category, final CompleteCallback<Category> callback){
        Category categoryDb = ParseObject.create(Category.class);
        categoryDb.put("name", category.getName());
        categoryDb.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				ParseQuery.clearAllCachedResults();
				if(e == null){
					//ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
					//query.clearAllCachedResults();
					
					callback.done(category, null);
				}else{
					GameboxException ex = new GameboxException(e.getMessage());
					callback.done(null, ex);
				}
			}
		});
    }
   

    public static void getAllCategories (com.parse.FindCallback<Category> callback){
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.include("category");
        query.clearCachedResult();
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.orderByAscending("name");
        query.findInBackground(callback);
    }
    
    public static void clearCache(){
    	ParseQuery.clearAllCachedResults();
    }
}
