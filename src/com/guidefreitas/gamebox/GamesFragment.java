package com.guidefreitas.gamebox;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import com.guidefreitas.gamebox.adapters.CategoriesAdapter;
import com.guidefreitas.gamebox.adapters.GamesAdapter;

public class GamesFragment extends ListFragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    boolean mListShown;

    public void updateData(boolean forceRefresh){

    	CategoriesAdapter categoriesAdapter = DataSources.getCategoriesAdapter(getActivity(), true);
    	spinner.setAdapter(categoriesAdapter);
        GamesAdapter adapter = (GamesAdapter) this.getListAdapter();
		if(adapter != null){
			adapter.loadObjects();
		}
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.activity_games, container, false);
        this.spinner = (Spinner) view.findViewById(R.id.spinner);
        CategoriesAdapter categoriesAdapter = DataSources.getCategoriesAdapter(getActivity(), false);
        spinner.setPrompt("All Categories");
        spinner.setAdapter(categoriesAdapter);
        spinner.setSelection(-1);
        spinner.setOnItemSelectedListener(this);
        
        updateData(true);
        return view;
    }
    
    
    
    public void showGameActivity(String gameId){
    	Intent intent = new Intent(getActivity(), GameDetailActivity.class);
    	intent.putExtra("GAME_ID", gameId);
    	startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    	
    	CategoriesAdapter categoriesAdapter = (CategoriesAdapter) spinner.getAdapter();
		Category category = categoriesAdapter.getItem(i);
		GamesAdapter adapter = DataSources.getGamesAdapter(getActivity(), category.getObjectId(), false);
		this.setListAdapter(adapter);
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
