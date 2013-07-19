package com.guidefreitas.gamebox;

import android.content.Context;

import com.guidefreitas.gamebox.callbacks.CompleteCallback;
import com.guidefreitas.gamebox.callbacks.GameboxException;
import com.guidefreitas.gamebox.callbacks.LoginCallback;
import com.guidefreitas.gamebox.callbacks.LoginException;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by guilherme on 7/11/13.
 */
public class AuthManager {
    private ParseUser parseUser;
    private static AuthManager instance;

    private AuthManager(){
    	
    }

    public static AuthManager getInstance(Context context){
        if(instance == null){
            instance = new AuthManager();
        }

        return instance;
    }

    public boolean isAuthenticated(){
    	ParseUser user = ParseUser.getCurrentUser();
    	if(user != null){
    		return true;
    	}
    	
    	return false;
    }

    public String getEmail() {
        return parseUser.getEmail();
    }

    public void setEmail(String email) {
    	parseUser.setEmail(email);
    	parseUser.saveEventually();
    }

    public void setPassword(String password) {
    	parseUser.setPassword(password);
    	parseUser.saveEventually();
    }

	public void logout() {
		ParseUser.logOut();
	}

	public void login(String email, String password, final LoginCallback callback) {
		
		ParseUser.logInInBackground(email, password, new LogInCallback() {
		  public void done(ParseUser user, ParseException e) {
		    if (user != null) {
		    	parseUser = user;
		    	callback.done(user.getEmail(), null);
		    	
		    } else {
		      LoginException ex = new LoginException(e.getMessage());
		      callback.done(null, ex);
		    }
		  }
		});
	}
	
	public void createUser(final String email, final String password, final CompleteCallback<String> callback){
		final ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
				  parseUser = user;
				  GameBoxService api = new GameBoxService();
				  try{
					  api.CreateInicialCategoriesSync();
					  callback.done(user.getEmail(), null);
				  }catch(Exception ex2){
					  GameboxException ex = new GameboxException(ex2.getMessage());
					  callback.done(null, ex);
				  }
			    } else {
			    	GameboxException ex = new GameboxException(e.getMessage());
			      callback.done(null, ex);
			    }
			}
		});
		
	}
}
