package com.guidefreitas.gamebox;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by guilherme on 7/11/13.
 */
@ParseClassName("category")
public class Category extends ParseObject {
	public final static String FIELD_OBJECT_ID="objectId";
	public final static String FIELD_NAME = "name";
	public Category(){
		
	}
	
	public Category(String name){
		this.setName(name);
	}

	
    public String getName() {
        return getString(Category.FIELD_NAME);
    }

    public void setName(String value) {
        put(Category.FIELD_NAME, value);
    }
}
