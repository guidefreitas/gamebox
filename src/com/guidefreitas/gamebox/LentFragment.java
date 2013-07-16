package com.guidefreitas.gamebox;

import java.util.List;

import com.guidefreitas.gamebox.adapters.LentAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * Created by guilherme on 7/11/13.
 */
public class LentFragment extends ListFragment {

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        View view =  inflater.inflate(R.layout.activity_lent, container, false);
        LentAdapter adapter = DataSources.getLentAdapter(getActivity());
		this.setListAdapter(adapter);
		
		return view;
    }
    
    @Override
    public void onListItemClick (ListView l, View v, int position, long id){
    	Game selectedGame = (Game) this.getListAdapter().getItem(position);
    	String gameId = selectedGame.getObjectId();
    	showGameActivity(gameId);
    }
    

	
	public void showGameActivity(String gameId){
    	Intent intent = new Intent(getActivity(), GameDetailActivity.class);
    	intent.putExtra("GAME_ID", gameId);
    	startActivity(intent);
    }
	
	public void updateData(){
        LentAdapter adapter = (LentAdapter)getListAdapter();
        adapter.loadObjects();
    }
}
