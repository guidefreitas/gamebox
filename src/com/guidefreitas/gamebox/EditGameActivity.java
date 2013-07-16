package com.guidefreitas.gamebox;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class EditGameActivity extends FragmentActivity implements DatePickerFragment.OnDateSetListener{
	
	private ScrollView scrollView;
	private ImageView coverImageView;
	private EditText etGameName;
	private TextView etGameBuyDate;
	private EditText etGamePrice;
	private Spinner spGameCategory;
	private EditText etLentPersonName;
	private CheckBox cbLent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_game);
		final Activity activity = this;
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		coverImageView = (ImageView) findViewById(R.id.coverImage);
		etGameName = (EditText) findViewById(R.id.etGameName);
		etGameBuyDate = (TextView) findViewById(R.id.etGameBuyDate);
		etGamePrice = (EditText) findViewById(R.id.etGamePrice);
		etLentPersonName = (EditText) findViewById(R.id.etLentPersonName);
		spGameCategory = (Spinner) findViewById(R.id.spGameCategory);
		cbLent = (CheckBox) findViewById(R.id.cbLent);
		
		spGameCategory.setAdapter(DataSources.getInstance(this).categoryAdapter);
		spGameCategory.setPrompt("Category");
		
		coverImageView.setImageResource(R.drawable.blank_box);
		
		etGameBuyDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerFragment dateDialog = new DatePickerFragment();
				dateDialog.show(getSupportFragmentManager(), "datePicker");
			}
		});
		
		cbLent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					etLentPersonName.setVisibility(View.VISIBLE);
					etLentPersonName.requestFocus();
				}else{
					etLentPersonName.setText("");
					etLentPersonName.setVisibility(View.GONE);
				}
			}
		});
		
		coverImageView.setOnClickListener(new View.OnClickListener() {
			   @Override
			   public void onClick(View v) {
				  
			      Toast toast = Toast.makeText(activity, "Imagem clicada", Toast.LENGTH_LONG);
			      toast.show();
			   }        
			});

	}
	
	private void Save(){
		
		if(spGameCategory.getSelectedItem() == null | spGameCategory.getSelectedItemPosition() == 0){
			showErrorMessage("Select a category");
		    return;
		}
		
		if(etGameName.getText().toString().isEmpty()){
			showErrorMessage("Insert a game name");
		    return;
		}
		
		
		
	}
	private void showErrorMessage(String msg){
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	    scrollView.startAnimation(shake);
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
	    toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_game, menu);
		return true;
	}
	
	public void navigateToHome(){
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case android.R.id.home:
        		navigateToHome();
        		return true;
        	case R.id.action_save_game:
        		Save();
        		return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onDateSelected(int year, int month, int day) {
		etGameBuyDate.setText(day + "/" + month + "/" + year);
	}


}
