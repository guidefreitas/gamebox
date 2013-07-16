package com.guidefreitas.gamebox;

import java.util.ArrayList;
import java.util.List;

import com.guidefreitas.gamebox.adapters.CategoriesAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RemoveCategoryDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    final List<Category> selectedCategories = new ArrayList<Category>(); 
	    final CategoriesAdapter adapter = DataSources.getInstance(getActivity()).categoryAdapter;
	     
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(R.string.dialog_delete_category_title);
	    builder.setSingleChoiceItems(adapter, 0, null);
	    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
	            	   Category category = (Category) adapter.getItem(selectedPosition);
	            	   category.deleteInBackground();
	               }
	           });
	    
	    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   dismiss();
	               }
	           });

	    return builder.create();
	}
}
