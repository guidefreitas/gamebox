package com.guidefreitas.gamebox;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
public class ImageCacheManager {
	
	private static ImageCacheManager instance;
	final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final int cacheSize = maxMemory / 8;
	private LruCache<String, Bitmap> mMemoryCache;
	
	private ImageCacheManager(){
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return bitmap.getByteCount() / 1024;
	        }
	    };
	}
	
	public static ImageCacheManager getInstance(){
		if(instance == null){
			instance = new ImageCacheManager();
		}
		
		return instance;
	}
	
	
	public void addImage(String key, byte[] data){
		Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
		addImage(key, bitmap);
	}
	
	public void addImage(String key, Bitmap bitmap) {
	    if (getImage(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getImage(String key) {
	    return mMemoryCache.get(key);
	}
}
