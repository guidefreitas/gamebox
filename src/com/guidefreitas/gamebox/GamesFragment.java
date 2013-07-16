package com.guidefreitas.gamebox;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import com.guidefreitas.gamebox.adapters.GamesAdapter;
import com.guidefreitas.gamebox.callbacks.DataSourceGetObjectsCallback;

public class GamesFragment extends ListFragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private LinearLayout listView;
    protected ProgressBar progressBar;
    boolean mListShown;

    public void updateData(){

        spinner.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        DataSources.getInstance(getActivity()).getCategories(new DataSourceGetObjectsCallback<Category>() {
			@Override
			public
			void done(List<Category> objects, DataSourceGetDataException e) {
				spinner.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
			}
		});
    }
    
    public void setListShown(boolean shown, boolean animate){
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
            	progressBar.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            	listView.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            }
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
            	progressBar.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            	listView.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            }
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }
    
    @Override
    public void setListShown(boolean shown){
        setListShown(shown, true);
    }
    
    @Override
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }
   
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.activity_games, container, false);
        this.spinner = (Spinner) view.findViewById(R.id.spinner);
        this.listView = (LinearLayout) view.findViewById(R.id.itensList);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        spinner.setAdapter(DataSources.getInstance(getActivity()).categoryAdapter);
        spinner.setPrompt("All Categories");
        spinner.setOnItemSelectedListener(this);
        
        
        updateData();
        return view;
    }
    
    public void showGameActivity(String gameId){
    	Intent intent = new Intent(getActivity(), GameDetailActivity.class);
    	intent.putExtra("GAME_ID", gameId);
    	startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    	if(i > 0){
    		Category category = (Category) DataSources.getInstance(getActivity()).categoryAdapter.getItem(i);
    		GamesAdapter adapter = DataSources.getGamesAdapter(getActivity(), category.getObjectId());
    		this.setListAdapter(adapter);
    		
    	}else{
    		GamesAdapter adapter = DataSources.getGamesAdapter(getActivity(), null);
    		this.setListAdapter(adapter);
    	}
    	
    }
    
    @Override
    public void onListItemClick (ListView l, View v, int position, long id){
    	Game selectedGame = (Game) this.getListAdapter().getItem(position);
    	
    	String gameId = selectedGame.getObjectId();
    	showGameActivity(gameId);
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
