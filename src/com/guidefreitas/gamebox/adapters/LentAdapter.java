package com.guidefreitas.gamebox.adapters;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.Game;
import com.guidefreitas.gamebox.R;
import com.guidefreitas.gamebox.R.drawable;
import com.guidefreitas.gamebox.R.id;
import com.guidefreitas.gamebox.R.layout;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LentAdapter extends ParseQueryAdapter<ParseObject>{
	
	private Context context;
	
	public LentAdapter(Context context){
		super(context, new QueryFactory<ParseObject>() {
			@Override
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("game");
		        query.orderByAscending("name");
		        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
		        query.whereEqualTo("lent", true);
		        return query;
			}
		});
		this.context = context;
		
	}
	
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent){

	  if (v == null) {
	    v = View.inflate(context, R.layout.game_adapter_item, null);
	  }

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
