package com.guidefreitas.gamebox;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class GameDetailActivity extends Activity {

	private ProgressBar progressBar = null;
	private LinearLayout detailPanel = null;
	private ParseImageView ivCoverImage = null;
	private TextView tvGameName = null;
	private TextView tvGamePrice = null;
	private TextView tvGameBuyDate = null;

	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_detail);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		this.detailPanel = (LinearLayout) this.findViewById(R.id.detailPanel);
		this.ivCoverImage = (ParseImageView) this.findViewById(R.id.coverImage);
		this.tvGameName = (TextView) this.findViewById(R.id.gameName);
		this.tvGamePrice = (TextView) this.findViewById(R.id.gamePrice);
		this.tvGameBuyDate = (TextView) this.findViewById(R.id.gameBuyDate);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String gameId = extras.getString("GAME_ID");
			initGameScreen(gameId);
		} else {
			showErroMessage("Game Id not informed");
		}
	}
	
	private void initGameScreen(String gameId){
		if (gameId != null && !gameId.isEmpty()) {
			ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
			query.whereEqualTo(Game.FIELD_OBJECT_ID, gameId);
			query.getFirstInBackground(new GetCallback<Game>() {

				@Override
				public void done(Game gameDb, ParseException e) {
					if (e == null) {
						game = gameDb;
						tvGameName.setText(game.getName());
						NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
						String priceString = baseFormat.format(game.getBuyValue());
						tvGamePrice.setText(priceString);
						SimpleDateFormat dt1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						tvGameBuyDate.setText(dt1.format(game.getBuyDate()));
						ivCoverImage.setPlaceholder(getResources()
								.getDrawable(R.drawable.blank_box));
						ivCoverImage.setParseFile(game.getCoverImage());
						ivCoverImage.loadInBackground();
						progressBar.setVisibility(View.GONE);
						detailPanel.setVisibility(View.VISIBLE);
					} else {
						showErroMessage(e.getMessage());
					}
				}
			});

		}
	}

	private void showErroMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.show();
	}
	
	private void confirmDeletion(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(R.string.dialog_delete_game_message)
	       .setTitle(R.string.dialog_delete_game_title);
		
		// Add the buttons
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               DeleteGame();
		           }
		       });
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               
		           }
		       });
		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_detail, menu);
		return true;
	}

	public void showEditGameActivity(String gameId) {
		Intent intent = new Intent(this, EditGameActivity.class);
		intent.putExtra("GAME_ID", gameId);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit_game:
			this.showEditGameActivity(game.getObjectId());
			return true;
		case R.id.action_delete_game:
			confirmDeletion();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void DeleteGame() {
		game.deleteInBackground(new DeleteCallback() {
			@Override
			public void done(ParseException arg0) {
				showErroMessage("Game deleted!");
				onBackPressed();
			}
		});
	}

}
