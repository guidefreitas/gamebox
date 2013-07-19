package com.guidefreitas.gamebox.adapters;

import com.guidefreitas.gamebox.Game;
import com.guidefreitas.gamebox.ImageFetcher;
import com.guidefreitas.gamebox.R;
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

public class LentAdapter extends ParseQueryAdapter<Game>{
	
	private FragmentActivity context;
	private ImageFetcher mImageFetcher;
	private Bitmap emptyCoverImage;
	
	public LentAdapter(FragmentActivity context){
		super(context, new QueryFactory<Game>() {
			@Override
			public ParseQuery<Game> create() {
				ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
		        query.orderByAscending("name");
		        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
		        query.whereEqualTo("lent", true);
		        return query;
			}
		});
		this.context = context;
		this.mImageFetcher = UIUtils.getImageFetcher(context);
		emptyCoverImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank_box);
		
	}
	
	@Override
	public View getItemView(Game object, View v, ViewGroup parent){

	  if (v == null) {
	    v = View.inflate(context, R.layout.game_adapter_item, null);
	  }

	  ImageView imageView = (ImageView) v.findViewById(R.id.coverImage);
	  ParseFile coverImageFile = object.getParseFile(Game.FIELD_COVER_IMAGE);
	  mImageFetcher.loadImage(coverImageFile.getUrl(), imageView, emptyCoverImage);

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
