package com.guidefreitas.gamebox.adapters;

import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.Game;
import com.guidefreitas.gamebox.R;
import com.guidefreitas.gamebox.util.ImageFetcher;
import com.guidefreitas.gamebox.util.UIUtils;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GamesAdapter extends ParseQueryAdapter<Game>{
	
	private FragmentActivity context;
	private ImageFetcher mImageFetcher;
	private Bitmap loadingCoverImage;
	
	public GamesAdapter(FragmentActivity context, final String categoryId,final boolean forceRefresh){
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
		
		this.mImageFetcher = UIUtils.getImageFetcher(context);
		this.loadingCoverImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank_box);
		this.context = context;
		
		
	}
	
	static class ViewHolder {
		ImageView imageView;
		TextView titleView;
		TextView subtitleView;
	}
	
	
	@Override
	public View getItemView(Game game, View v, ViewGroup parent){
	  ViewHolder viewHolder;
	  if (v == null) {
	    v = View.inflate(context, R.layout.game_adapter_item, null);
	    viewHolder = new ViewHolder();
	    viewHolder.imageView = (ImageView) v.findViewById(R.id.coverImage);
	    viewHolder.titleView = (TextView) v.findViewById(R.id.title);
	    viewHolder.subtitleView = (TextView) v.findViewById(R.id.subtitle);
	  }else{
		  viewHolder = (ViewHolder) v.getTag();
	  }
	  
	  ParseFile coverImageFile = game.getParseFile(Game.FIELD_COVER_IMAGE);
	  if(coverImageFile != null){
		  mImageFetcher.loadImage(coverImageFile.getUrl(), viewHolder.imageView, this.loadingCoverImage);
	  }else{
		  viewHolder.imageView.setImageBitmap(this.loadingCoverImage);
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
