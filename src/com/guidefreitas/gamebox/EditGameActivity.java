package com.guidefreitas.gamebox;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditGameActivity extends FragmentActivity implements
		DatePickerFragment.OnDateSetListener {
	private static final int SELECT_PHOTO = 100;
	private static final int SELECT_CAMERA_PHOTO = 200;

	private ScrollView scrollView;
	private ParseImageView coverImageView;
	private EditText etGameName;
	private TextView etGameBuyDate;
	private EditText etGamePrice;
	private Spinner spGameCategory;
	private EditText etLentPersonName;
	private CheckBox cbLent;

	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_game);
		

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		scrollView = (ScrollView) findViewById(R.id.scrollView);
		coverImageView = (ParseImageView) findViewById(R.id.coverImage);
		etGameName = (EditText) findViewById(R.id.etGameName);
		etGameBuyDate = (TextView) findViewById(R.id.etGameBuyDate);
		etGamePrice = (EditText) findViewById(R.id.etGamePrice);
		etLentPersonName = (EditText) findViewById(R.id.etLentPersonName);
		spGameCategory = (Spinner) findViewById(R.id.spGameCategory);
		cbLent = (CheckBox) findViewById(R.id.cbLent);

		spGameCategory
				.setAdapter(DataSources.getInstance(this).categoryAdapter);
		spGameCategory.setPrompt("Category");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String gameId = extras.getString("GAME_ID");
			initGameScreen(gameId);
		} else {
			game = new Game();
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
		if (gameId != null && !gameId.isEmpty()) {
			ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
			query.whereEqualTo(Game.FIELD_OBJECT_ID, gameId);
			query.getFirstInBackground(new GetCallback<Game>() {

				@Override
				public void done(Game gameDb, ParseException e) {
					if (e == null) {
						game = gameDb;
						etGameName.setText(game.getName());
						etGamePrice.setText(game.getBuyValue().toString());
						SimpleDateFormat dt1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						etGameBuyDate.setText(dt1.format(game.getBuyDate()));
						coverImageView.setPlaceholder(getResources()
								.getDrawable(R.drawable.blank_box));
						coverImageView.setParseFile(game.getCoverImage());
						coverImageView.loadInBackground();

						String categoryId = game.getCategoryId();
						int pos = DataSources.getInstance(getParent()).categoryAdapter
								.getPositionById(categoryId);
						if (pos != -1) {

							spGameCategory.setSelected(true);
							spGameCategory.setSelection(pos);
						}

						if (game.isLent()) {
							cbLent.setChecked(true);
							etLentPersonName.setText(game.getFriendLent());
						}
					} else {
						showMessage(e.getMessage());
					}
				}
			});

		}

	}

	private void showMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.show();
	}

	private void Save() throws Exception {

		if (spGameCategory.getSelectedItem() == null
				| spGameCategory.getSelectedItemPosition() == 0) {
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
		game.setBuyValue(Double.parseDouble(etGamePrice.getText().toString()));
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
		game.setBuyDate(dt1.parse(etGameBuyDate.getText().toString()));

		if (cbLent.isChecked()) {
			game.setLent(true);
			game.setFriendLent(etLentPersonName.getText().toString());
		} else {
			game.setLent(false);
			game.setFriendLent("");
		}
		game.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException arg0) {
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
