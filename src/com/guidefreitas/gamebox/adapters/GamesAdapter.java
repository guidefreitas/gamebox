package com.guidefreitas.gamebox.adapters;

import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.Game;
import com.guidefreitas.gamebox.ImageCacheManager;
import com.guidefreitas.gamebox.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GamesAdapter extends ParseQueryAdapter<Game>{
	
	private Context context;
	private Drawable emptyCoverImage;
	
	public GamesAdapter(Context context, final String categoryId,final boolean forceRefresh){
		super(context, new QueryFactory<Game>() {
			@Override
			public ParseQuery<Game> create() {
				ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
		        query.orderByAscending(Game.FIELD_NAME);
		        if(forceRefresh){
		        	query.clearCachedResult();
		        }else{
		        	query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
		        }
		        if(categoryId != null && !categoryId.isEmpty()){
		        	Category category = new Category();
		        	category.setObjectId(categoryId);
		        	query.whereEqualTo("category", category);
		        }
		        
		        return query;
			}
		});
		this.context = context;
		emptyCoverImage = context.getResources().getDrawable(R.drawable.blank_box);
		
	}
	
	static class ViewHolder {
		ParseImageView imageView;
		TextView titleView;
		TextView subtitleView;
	}
	
	
	@Override
	public View getItemView(Game game, View v, ViewGroup parent){
	  ViewHolder viewHolder;
	  if (v == null) {
	    v = View.inflate(context, R.layout.game_adapter_item, null);
	    viewHolder = new ViewHolder();
	    viewHolder.imageView = (ParseImageView) v.findViewById(R.id.coverImage);
	    viewHolder.titleView = (TextView) v.findViewById(R.id.title);
	    viewHolder.subtitleView = (TextView) v.findViewById(R.id.subtitle);
	  }else{
		  viewHolder = (ViewHolder) v.getTag();
	  }
	  
	  ParseFile coverImageFile = game.getParseFile(Game.FIELD_COVER_IMAGE);
	  final String coverImageId = coverImageFile.getUrl();
	  Bitmap cachedImage = ImageCacheManager.getInstance().getImage(coverImageId);
	  if(cachedImage == null){
		  viewHolder.imageView.setParseFile(coverImageFile);
		  viewHolder.imageView.setPlaceholder(emptyCoverImage);
		  viewHolder.imageView.loadInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] data, ParseException arg1) {
				ImageCacheManager.getInstance().addImage(coverImageId, data);
			}
		  });
	  }else{
		  viewHolder.imageView.setImageBitmap(cachedImage);
	  }
	  
	  
	  viewHolder.titleView.setText(game.getString(Game.FIELD_NAME));
	  
	  if(game.getBoolean(Game.FIELD_LENT)){
		  String friendLent = game.getString(Game.FIELD_FRIEND_LENT);
		  String lentTo = v.getResources().getString(R.string.label_lent_to);
		  viewHolder.subtitleView.setText(lentTo + " " + friendLent);
	  }else{
		  viewHolder.subtitleView.setText("");
	  }
	  v.setTag(viewHolder);
	  
	  return v;
	}


}
