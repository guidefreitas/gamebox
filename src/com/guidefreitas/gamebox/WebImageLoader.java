package com.guidefreitas.gamebox;

import java.io.InputStream;
import java.net.URL;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class WebImageLoader extends AsyncTask<String, Void, Void> {
	Drawable imgLoad;
	WebImageLoaderListener mListener;
	
	public interface WebImageLoaderListener {
		public abstract void onImageLoaded(Drawable image);
	}
	
	public void setListener(WebImageLoaderListener listener) {
		mListener = listener;
	}
	
	public WebImageLoader(WebImageLoaderListener listener){
		mListener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(String... params) {
		imgLoad = LoadImageFromWeb(params[0]);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(mListener != null){
			mListener.onImageLoaded(imgLoad);
		}
	}
	
	private Drawable LoadImageFromWeb(String url) 
    {
        try 
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
