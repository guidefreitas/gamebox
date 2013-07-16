package com.guidefreitas.gamebox.adapters;

import java.util.ArrayList;
import java.util.List;
import com.guidefreitas.gamebox.Category;
import com.guidefreitas.gamebox.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CategoriesAdapter extends BaseAdapter {
	
	private List<Category> categories;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	
	static class ViewHolder{
		private TextView tvTitle;
	}
	
	public CategoriesAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		this.categories = new ArrayList<Category>();
	}
	
	public CategoriesAdapter(Context context, List<Category> categoriesList) {
		mInflater = LayoutInflater.from(context);
		this.categories = categoriesList;
	}
	
	@Override
	public int getCount() {
		if(categories != null){
			return categories.size();
		}
		
		return 0;
	}
	
	public void clear(){
		if(categories != null){
			this.categories.clear();
			notifyDataSetChanged();
		}
	}
	
	public int getPositionById(String categoryId){
		for(int i=0;i<categories.size();i++){
			if(categories.get(i).getObjectId() != null && 
					categories.get(i).getObjectId().equals(categoryId)){
				return i;
			}
		}
		
		return -1;
	}
	
	public void addAllWithBlank(List<Category> categoriesList, String blankLabel){
		Category category = new Category();
		category.setName(blankLabel);
		this.categories.add(0, category);
		this.categories.addAll(categoriesList);
		notifyDataSetChanged();
	}
	
	public void addAll(List<Category> categoriesList){
		this.categories.addAll(categoriesList);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int index) {
		return categories.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.categories_spinner_layout, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Category category = categories.get(position);
		holder.tvTitle.setText(category.getName());

		return convertView;
	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.categories_spinner_layout, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Category category = categories.get(position);
		holder.tvTitle.setText(category.getName());

		return convertView;
    }

}

