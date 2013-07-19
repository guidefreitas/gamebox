package com.guidefreitas.gamebox;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.guidefreitas.gamebox.adapters.CategoriesAdapter;
import com.guidefreitas.gamebox.callbacks.FindException;
import com.guidefreitas.gamebox.callbacks.FindOneCallback;
import com.guidefreitas.gamebox.util.UIUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

@SuppressLint("NewApi")
public class EditGameActivity extends FragmentActivity implements
		DatePickerFragment.OnDateSetListener {
	private static final int SELECT_PHOTO = 100;
	private static final int SELECT_CAMERA_PHOTO = 200;

	private ScrollView scrollView;
	private ImageView coverImageView;
	private EditText etGameName;
	private TextView etGameBuyDate;
	private EditText etGamePrice;
	private Spinner spGameCategory;
	private EditText etLentPersonName;
	private CheckBox cbLent;
	
	private ImageFetcher mImageFetcher;
	private Bitmap emptyCoverImage;

	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_game);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		scrollView = (ScrollView) findViewById(R.id.scrollView);
		coverImageView = (ImageView) findViewById(R.id.coverImage);
		etGameName = (EditText) findViewById(R.id.etGameName);
		etGameBuyDate = (TextView) findViewById(R.id.etGameBuyDate);
		etGamePrice = (EditText) findViewById(R.id.etGamePrice);
		etLentPersonName = (EditText) findViewById(R.id.etLentPersonName);
		spGameCategory = (Spinner) findViewById(R.id.spGameCategory);
		cbLent = (CheckBox) findViewById(R.id.cbLent);

		this.mImageFetcher = UIUtils.getImageFetcher(this);
		emptyCoverImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.blank_box);

		CategoriesAdapter categoriesAdapter = DataSources.getCategoriesAdapter(this, false);
		spGameCategory.setAdapter(categoriesAdapter);
		spGameCategory.setPrompt("Category");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String gameId = extras.getString("GAME_ID");
			initGameScreen(gameId);
		} else {
			game = new Game();
			game.setBuyDate(new Date());
			SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
			etGameBuyDate.setText(dt1.format(game.getBuyDate()));
				
		}

		etGameBuyDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment dateDialog = new DatePickerFragment();
				dateDialog.show(getSupportFragmentManager(), "datePicker");
			}
		});

		cbLent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					etLentPersonName.setVisibility(View.VISIBLE);
					etLentPersonName.requestFocus();
				} else {
					etLentPersonName.setText("");
					etLentPersonName.setVisibility(View.GONE);
				}
			}
		});

		coverImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPickImageDialog();
			}
		});

	}

	private void showPickImageDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title_pick_image_source).setItems(
				R.array.image_sources, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							showCameraIntent();
							dialog.dismiss();
							break;
						case 1:
							showGaleryIntent();
							dialog.dismiss();
							break;
						default:
							break;
						}
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void showCameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// start the image capture Intent
		startActivityForResult(intent, SELECT_CAMERA_PHOTO);
	}

	private void showGaleryIntent() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Uri selectedImage = imageReturnedIntent.getData();
					InputStream imageStream = getContentResolver()
							.openInputStream(selectedImage);
					Bitmap coverImage = BitmapFactory.decodeStream(imageStream);
					setCoverImage(coverImage);
				} catch (Exception ex) {
					showMessage(ex.getMessage());
				}
			}
			break;
			
		case SELECT_CAMERA_PHOTO:
			if(resultCode == RESULT_OK){
				Bundle extras = imageReturnedIntent.getExtras();
			    Bitmap coverImage = (Bitmap) extras.get("data");
			    setCoverImage(coverImage);
			}
			break;
			
		}

	}

	private void setCoverImage(Bitmap coverImage) {
		Bitmap coverResized = resizeImage(coverImage, 300, 200);
		this.coverImageView.setImageBitmap(coverResized);
		try {
			uploadImage(coverResized);
		} catch (Exception e) {
			showMessage(e.getMessage());
		}
	}

	private Bitmap resizeImage(Bitmap bmp, int newHeight, int newWidth) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	private void uploadImage(Bitmap bmp) throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		game.setCoverImage(byteArray);
	}

	private void initGameScreen(String gameId) {
		
		final ProgressDialog progressDialog = new ProgressDialog(this);
		String loading = this.getResources().getString(R.string.loading);
		progressDialog.setMessage(loading);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		GameBoxService.getGameById(gameId, new FindOneCallback<Game>() {
			
			@Override
			public void done(Game gameDb, FindException e) {
				if (e == null) {
					game = gameDb;
					etGameName.setText(game.getName());
					etGamePrice.setText(game.getBuyValue().toString());
					Date buyDate = game.getBuyDate();
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					if(buyDate != null){
						etGameBuyDate.setText(dt1.format(game.getBuyDate()));
					}else{
						etGameBuyDate.setText(dt1.format(new Date()));
					}
					
					ParseFile coverImageFile = game.getParseFile(Game.FIELD_COVER_IMAGE);
					mImageFetcher.loadImage(coverImageFile.getUrl(), coverImageView, emptyCoverImage);
					
					Category category = (Category) game.getParseObject(Game.FIELD_CATEGORY);
					CategoriesAdapter adapter = (CategoriesAdapter) spGameCategory.getAdapter();
					int position = adapter.indexOf(category);
					if(category != null){
						spGameCategory.setSelected(true);
						spGameCategory.setSelection(position);
					}
					
					if (game.isLent()) {
						cbLent.setChecked(true);
						etLentPersonName.setText(game.getFriendLent());
					}
					
				} else {
					showMessage(e.getMessage());
				}
				
				progressDialog.dismiss();
			}
		});
	}

	private void showMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.show();
	}

	private void Save() throws Exception {

		if (spGameCategory.getSelectedItem() == null) {
			showErrorMessage("Select a category");
			return;
		}

		if (etGameName.getText().toString().isEmpty()) {
			showErrorMessage("Insert a game name");
			return;
		}

		game.setName(etGameName.getText().toString());
		Category category = (Category) spGameCategory.getSelectedItem();
		game.setCategory(category);
		
		String gamePrice = etGamePrice.getText().toString();
		if(!gamePrice.isEmpty()){
			game.setBuyValue(Double.parseDouble(gamePrice));
		}else{
			game.setBuyValue(Double.valueOf(0));
		}
		
		String gameBuyDate = etGameBuyDate.getText().toString();
		if(!gameBuyDate.isEmpty()){
			SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
			game.setBuyDate(dt1.parse(etGameBuyDate.getText().toString()));
		}else{
			game.setBuyDate(null);
		}

		if (cbLent.isChecked()) {
			game.setLent(true);
			game.setFriendLent(etLentPersonName.getText().toString());
		} else {
			game.setLent(false);
			game.setFriendLent("");
		}
		
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("Saving...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		game.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException arg0) {
				ParseQuery.clearAllCachedResults();
				progress.dismiss();
				showMessage("Game Saved!");
				navigateToHome();
			}
		});

	}

	private void showErrorMessage(String msg) {
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

	public void navigateToHome() {
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
			try {
				Save();

			} catch (Exception e) {
				showErrorMessage(e.getMessage());
			}
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
