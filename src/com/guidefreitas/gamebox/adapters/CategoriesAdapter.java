package com.guidefreitas.gamebox.adapters;

import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CategoriesAdapter extends ParseQueryAdapter<Category> {

	private Context context;

	static class ViewHolder{
		private TextView tvTitle;
	}
	
	public CategoriesAdapter(Context context, final boolean forceRefresh){
		super(context, new QueryFactory<Category>() {
			@Override
			public ParseQuery<Category> create() {
				ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
		        query.orderByAscending("name");
		        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
		        if(forceRefresh){
		        	query.clearCachedResult();
		        }
		        
		        return query;
			}
		});
		this.setTextKey("name");
		this.context = context;

	}
	
	public int indexOf(Category category){
		for(int i=0;i<getCount();i++){
			Category catDb = getItem(i);
			if(catDb.getObjectId().equals(category.getObjectId())){
				return i;
			}
		}
		
		return -1;
	}
	
	
	@Override
	public View getItemView(Category object, View convertView, ViewGroup viewGroup){
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.categories_spinner_layout, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(object.getString("name"));
		return convertView;
	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.categories_spinner_layout, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ParseObject object = this.getItem(position);
		holder.tvTitle.setText(object.getString("name"));

		return convertView;
    }
	
}

