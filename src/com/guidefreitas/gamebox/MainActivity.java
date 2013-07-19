package com.guidefreitas.gamebox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.guidefreitas.gamebox.adapters.ViewPagerAdapter;
import com.guidefreitas.gamebox.util.UIUtils;
import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class MainActivity  extends FragmentActivity implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
        CategoryDialog.CategoryDialogListener {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;

    private class TabInfo {
        private String tag;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
        }
    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameBoxService.InitializeParse(this);
        
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            // Define a Tab de acordo com o estado salvo
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        this.verifyNetworkConnection();
        
        // Inicializa o ViewPager
        this.initializeViewPager();


        ParseAnalytics.trackAppOpened(getIntent());

    }
    
    public void verifyNetworkConnection(){
    	if(!UIUtils.isOnline(this)){
    		String msgInternetConnection = this.getResources().getString(R.string.msg_internet_connection_required);
    		Toast toast = Toast.makeText(this, msgInternetConnection, Toast.LENGTH_LONG);
    		toast.show();
    	}
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	verifyNetworkConnection();
    	if(!AuthManager.getInstance().isAuthenticated()){
    		NavigateToLogin();
    	}
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag());
        super.onSaveInstanceState(outState);
    }

    private void initializeViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, GamesFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, LentFragment.class.getName()));
        this.mPagerAdapter = new ViewPagerAdapter(
                super.getSupportFragmentManager(), fragments);
        this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }


    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
        String tabGamesName = this.getResources().getString(R.string.tab_title_games);
        String tabLentName = this.getResources().getString(R.string.tab_title_lent);
        
        TabInfo tabInfo = null;
        MainActivity.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("TabGames").setIndicator(tabGamesName),
                (tabInfo = new TabInfo("TabGames", GamesFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("TabLent").setIndicator(tabLentName),
                (tabInfo = new TabInfo("TabLent", LentFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        mTabHost.setOnTabChangedListener(this);
    }

    private static void AddTab(MainActivity activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void showNewCategoryDialog(){
        CategoryDialog newFragment = CategoryDialog.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
    
    public void showNewGameActivity(){
    	Intent intent = new Intent(this, EditGameActivity.class);
    	//intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.action_refresh:
        		this.updateData();
        		return true;
            case R.id.action_logout:
            	Logout();
            	return true;
            case R.id.action_add_game:
            	showNewGameActivity();
                return true;
            case R.id.action_add_category:
                showNewCategoryDialog();
                return true;
            case R.id.action_remove_category:
                showRemoveCategoryDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void showRemoveCategoryDialog(){
    	RemoveCategoryDialog dialog = new RemoveCategoryDialog();
    	dialog.show(getSupportFragmentManager(), RemoveCategoryDialog.class.toString());
    }
    
    private void NavigateToLogin(){
    	Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }
    
    private void Logout() {
		AuthManager.getInstance().logout();
		Toast toast = Toast.makeText(this, "Logout", Toast.LENGTH_LONG);
    	toast.show();
		NavigateToLogin();
	}

	public void updateData(){
		 GameBoxService.clearCache();
    	 GamesFragment gamesFragment = (GamesFragment) ((ViewPagerAdapter) mPagerAdapter).getItem(0);
    	 LentFragment lentFragment = (LentFragment) ((ViewPagerAdapter) mPagerAdapter).getItem(1);
         gamesFragment.updateData(true);
         lentFragment.updateData();
    }
    
    @Override
    public void onCategoryDialogSaveClick(CategoryDialog dialog) {
    	this.updateData();
    }

    @Override
    public void onCategoryDialogCancelClick(CategoryDialog dialog) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        this.mTabHost.setCurrentTab(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabChanged(String s) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }
}
