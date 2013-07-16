package com.guidefreitas.gamebox;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by guilherme on 7/11/13.
 */
@ParseClassName("category")
public class Category extends ParseObject {
	
	public Category(){
		
	}
	
	public Category(String name){
		this.setName(name);
	}

	
    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }
}
