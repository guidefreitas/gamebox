package com.guidefreitas.gamebox.adapters;

import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.R;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GamesAdapter extends ParseQueryAdapter<ParseObject>{
	
	private Context context;
	
	public GamesAdapter(Context context, final String categoryId){
		super(context, new QueryFactory<ParseObject>() {
			@Override
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("game");
		        query.orderByAscending("name");
		        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
		        if(categoryId != null && !categoryId.isEmpty()){
		        	Category category = new Category();
		        	category.setObjectId(categoryId);
		        	query.whereEqualTo("category", category);
		        }
		        return query;
			}
		});
		this.context = context;
		
	}
	
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent){
	  //super.getItemView(object, v, parent);
	  if (v == null) {
	    v = View.inflate(context, R.layout.game_adapter_item, null);
	  }
	 
	  // Take advantage of ParseQueryAdapter's getItemView logic for
	  // populating the main TextView/ImageView.
	  // The IDs in your custom layout must match what ParseQueryAdapter expects
	  // if it will be populating a TextView or ImageView for you.
	 
	 
	  // Do additional configuration before returning the View.
	  
	  ParseImageView imageView = (ParseImageView) v.findViewById(R.id.coverImage);
	  imageView.setParseFile(object.getParseFile("cover_image"));
	  imageView.setPlaceholder(context.getResources().getDrawable(R.drawable.blank_box));
	  imageView.loadInBackground();
	  
	  TextView titleView = (TextView) v.findViewById(R.id.title);
	  titleView.setText(object.getString("name"));
	  
	  TextView subtitleView = (TextView) v.findViewById(R.id.subtitle);
	  if(object.getBoolean("lent")){
		  String friendLent = object.getString("friend_lent");
		  String lentTo = v.getResources().getString(R.string.label_lent_to);
		  subtitleView.setText(lentTo + " " + friendLent);
	  }else{
		  subtitleView.setText("");
	  }
	  return v;
	}


}
