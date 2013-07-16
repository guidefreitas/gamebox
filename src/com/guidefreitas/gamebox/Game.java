package com.guidefreitas.gamebox;
import java.util.Date;

import com.guidefreitas.gamebox.callbacks.GetFileCallback;
import com.guidefreitas.gamebox.callbacks.GetFileException;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by guilherme on 7/11/13.
 */
@ParseClassName("game")
public class Game  extends ParseObject {
	
	final static String FIELD_OBJECT_ID="objectId";
	final static String FIELD_NAME = "name";
	final static String FIELD_BUY_DATE = "buy_date";
	final static String FIELD_LENT = "lent";
	final static String FIELD_FRIEND_LENT = "friend_lent";
	final static String FIELD_COVER_IMAGE = "cover_image";
	final static String FIELD_BUY_VALUE = "buy_value";
	final static String FIELD_CATEGORY = "category";
	final static String FIELD_RATING = "rating";
	final static String FIELD_CREATED_AT = "createdAt";
	final static String FIELD_UPDATED_AT = "updatedAt";
	
	
	public void setCategoryId(String categoryId){
		put(FIELD_CATEGORY, categoryId);
	}
	
	public String getCategoryId(){
		return getString(FIELD_CATEGORY);
	}
	public void setCategory(Category category){
		put(FIELD_CATEGORY, category);
	}
	
	public Date getCreatedAt(){
		return getDate(FIELD_CREATED_AT);
	}
	
	public Date getUpdatedAt(){
		return getDate(FIELD_UPDATED_AT);
	}
	
	public int getRating(){
		return getInt(FIELD_RATING);
	}
	
	public void setRating(int rating){
		put(FIELD_RATING, rating);
	}
	
	public Double getBuyValue(){
		return getDouble(FIELD_BUY_VALUE);
	}
	
	public void setBuyValue(Double value){
		put(FIELD_BUY_VALUE, value);
	}
	
    public String getName() {
        return getString(FIELD_NAME);
    }

    public void setName(String value) {
        put(FIELD_NAME, value);
    }
    
    public Date getBuyDate(){
    	return getDate(FIELD_BUY_DATE);
    }
    
    public void setBuyDate(Date date){
    	put(FIELD_BUY_DATE, date);
    }
    
    public boolean isLent(){
    	return getBoolean(FIELD_LENT);
    }
    
    public void setLent(boolean lent){
    	put(FIELD_LENT, lent);
    }
    
    public String getFriendLent(){
    	return getString(FIELD_FRIEND_LENT);
    }
    
    public void setFriendLent(String friendName){
    	put(FIELD_FRIEND_LENT, friendName);
    }
    
    public void setCoverImage(byte[] image) throws Exception{
    	ParseFile file = new ParseFile("cover.jpg", image);
		file.save();
		put(FIELD_COVER_IMAGE, file);
    }
    
    public ParseFile getCoverImage(){
    	ParseFile imageCover = getParseFile(FIELD_COVER_IMAGE);
    	return imageCover;
    }
}
