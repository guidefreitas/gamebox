package com.guidefreitas.gamebox;

import com.guidefreitas.gamebox.callbacks.CompleteCallback;
import com.guidefreitas.gamebox.callbacks.GameboxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by guilherme on 7/11/13.
 */
public class CategoryDialog extends DialogFragment {

	CategoryDialogListener mListener;

	static CategoryDialog newInstance() {
		return new CategoryDialog();
	}

	public interface CategoryDialogListener {
		public void onCategoryDialogSaveClick(CategoryDialog dialog);

		public void onCategoryDialogCancelClick(CategoryDialog dialog);
	}

	public void setListener(CategoryDialogListener listener) {
		mListener = listener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Verify that the host activity implements the callback interface
		// Verifica se a atividade que abriu o Dialog implementa a interface
		try {
			if (mListener == null) {
				mListener = (CategoryDialogListener) activity;
			}
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement CategoryDialogListener");
		}
	}
	
	public void showErrorMessage(String msg){
		Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.title_category);
		final EditText input = new EditText(getActivity());
		input.setMinEms(20);
		input.setHint(R.string.hint_category_name);
		builder.setView(input);

		builder.setPositiveButton(R.string.save,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Salva os dados
						GameBoxService gbApi = new GameBoxService();
						String categoryName = input.getText().toString();
						Log.v("Category: ", categoryName);
						Category category = new Category();
						category.setName(categoryName);
						gbApi.CreateCategory(category, new CompleteCallback<Category>() {
							
							@Override
							public void done(Category category, GameboxException e) {
								if(e == null){
									mListener.onCategoryDialogSaveClick(CategoryDialog.this);
								}else{
									showErrorMessage(e.getMessage());
								}
							}
						});
						
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener
								.onCategoryDialogCancelClick(CategoryDialog.this);
						CategoryDialog.this.getDialog().cancel();
					}
				});
		return builder.create();
	}

}
