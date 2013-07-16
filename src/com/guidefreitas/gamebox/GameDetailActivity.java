package com.guidefreitas.gamebox;

import java.text.SimpleDateFormat;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

		this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		this.detailPanel = (LinearLayout) this.findViewById(R.id.detailPanel);
		this.ivCoverImage = (ParseImageView) this.findViewById(R.id.coverImage);
		this.tvGameName = (TextView) this.findViewById(R.id.gameName);
		this.tvGamePrice = (TextView) this.findViewById(R.id.gamePrice);
		this.tvGameBuyDate = (TextView) this.findViewById(R.id.gameBuyDate);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String gameId = extras.getString("GAME_ID");
			if (gameId != null && !gameId.isEmpty()) {
				ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
				query.whereEqualTo(Game.FIELD_OBJECT_ID, gameId);
				query.getFirstInBackground(new GetCallback<Game>() {

					@Override
					public void done(Game gameDb, ParseException e) {
						if (e == null) {
							game = gameDb;
							tvGameName.setText(game.getName());
							tvGamePrice.setText(game.getBuyValue().toString());
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
		} else {
			showErroMessage("Game Id not informed");
		}
	}

	private void showErroMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.show();
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
			this.showEditGameActivity("teste");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
