package com.guidefreitas.gamebox.adapters;

import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.R;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GamesAdapter extends ParseQueryAdapter<ParseObject>{
	
	private Context context;
	private Drawable emptyCoverImage;
	
	public GamesAdapter(Context context, final String categoryId,final boolean forceRefresh){
		super(context, new QueryFactory<ParseObject>() {
			@Override
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("game");
		        query.orderByAscending("name");
		        if(forceRefresh){
		        	query.clearCachedResult();
		        }
		        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
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
	public View getItemView(ParseObject game, View v, ViewGroup parent){
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
	  
	  viewHolder.imageView.setParseFile(game.getParseFile("cover_image"));
	  viewHolder.imageView.setPlaceholder(emptyCoverImage);
	  viewHolder.imageView.loadInBackground();
	  
	  viewHolder.titleView.setText(game.getString("name"));
	  
	  if(game.getBoolean("lent")){
		  String friendLent = game.getString("friend_lent");
		  String lentTo = v.getResources().getString(R.string.label_lent_to);
		  viewHolder.subtitleView.setText(lentTo + " " + friendLent);
	  }else{
		  viewHolder.subtitleView.setText("");
	  }
	  v.setTag(viewHolder);
	  
	  return v;
	}


}
