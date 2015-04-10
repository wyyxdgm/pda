package com.david.pda;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.david.pda.adapter.BataanAdapter;
import com.david.pda.weather.model.util.L;

public class MainActivity extends ActionBarActivity {

	private DrawerLayout drawerLayout;
	private ListView listView;
	private ActionBarDrawerToggle drawerListener;
	private OnItemClickListener menuItemCLickListener;
	private BataanAdapter bataanAdapter;
	private Button firstBtn;
	private Button secondBtn;
	private FrameLayout mainContentLayout;
	private int[] mainViewIds = new int[] { R.layout.activity_main,
			R.layout.main_affair_plan, R.layout.main_today_schedule,
			R.layout.main_four_classes, R.layout.main_self_principle,
			R.layout.main_some_tools, R.layout.main_system_setting };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		Log.i(L.t, intent != null && intent.getFlags() == RESULT_OK ? "true"
				: "false");
		mainContentLayout = (FrameLayout) findViewById(R.id.mainContent);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		listView = (ListView) findViewById(R.id.drawerList);
		firstBtn = (Button) findViewById(R.id.main_btn1);
		secondBtn = (Button) findViewById(R.id.main_btn2);
		bataanAdapter = new BataanAdapter(this);
		listView.setAdapter(bataanAdapter);
		menuItemCLickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listView.setItemChecked(position, true);
				Log.i(L.t, position + " selected");
				if (mainContentLayout.getChildCount() > 0) {//hash view already
					if(mainContentLayout.getChildAt(0).getId() != mainViewIds[position]){
						//current view is not witch selected
						//should be replaced
						mainContentLayout.removeViewAt(0);
						LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						mainContentLayout.addView( inflater.inflate(mainViewIds[position], parent, false));
						drawerLayout.closeDrawer(GravityCompat.START);
						Log.i(L.t,"view of "+mainViewIds[position]+" show");
					}
				}else{
					mainContentLayout.addView(findViewById(mainViewIds[position]));
					Log.i(L.t,"view of "+mainViewIds[position]+" show");
				}
				Log.i(L.t, "error position of navBar:" + position);
			}
		};
		listView.setOnItemClickListener(menuItemCLickListener);

		drawerListener = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

		};

		drawerLayout.setDrawerListener(drawerListener);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		firstBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Button b = (Button) view;
				Log.i(L.t, b.getText() + "");
			}
		});
		secondBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Button b = (Button) view;
				Log.i(L.t, b.getText() + "");
			}
		});
	}
	//for ActionBarDrawerToggle
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}
	//for ActionBarDrawerToggle
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

	public void setTitle(String title) {
		getSupportActionBar().setTitle(title);
	}
}
