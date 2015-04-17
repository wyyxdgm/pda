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
import android.widget.ImageButton;
import android.widget.ListView;

import com.david.pda.adapter.BataanAdapter;
import com.david.pda.weather.model.util.L;

public class MainActivity extends ActionBarActivity {
	public static final int POSTION_TARGET_MANAGE = 0;
	public static final int POSTION_AFFAIR_PLAN = 1;
	public static final int POSTION_TODAY_SCHEDULE = 2;
	public static final int POSTION_FOUR_CLASSES = 3;
	public static final int POSTION_SELF_PRINCIPLE = 4;
	public static final int POSTION_SOME_TOOLS = 5;
	public static final int POSTION_SYSTEM_SETTIONG = 6;
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
				initMainView(position);
				drawerLayout.closeDrawer(GravityCompat.START);
				Log.i(L.t, "view of " + mainViewIds[position] + " show");
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
		Intent intent = getIntent();
		if (intent.getFlags() != 0
				&& intent.getFlags() >= POSTION_TARGET_MANAGE
				&& intent.getFlags() <= POSTION_SYSTEM_SETTIONG) {
			initMainView(intent.getFlags());
		}
	}

	public void initMainView(int position) {
		if (mainContentLayout.getChildCount() > 0) {// hash view already
			if (mainContentLayout.getChildAt(0).getId() != mainViewIds[position]) {
				Log.i(L.t, "mainContentLayout.getChildAt(0).getId():"
						+ mainContentLayout.getChildAt(0).getId());
				Log.i(L.t, "mainViewIds[" + position + "]"
						+ mainViewIds[position]);
				// current view is not witch selected
				// should be replaced
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View currentView = inflater.inflate(mainViewIds[position],
						null, false);
				mainContentLayout.removeViewAt(0);
				mainContentLayout.addView(currentView);
				initMainView(position, currentView);
			}
		}
	}

	private void initMainView(int position, View v) {
		if (position == POSTION_SOME_TOOLS) {
			ImageButton alarm = (ImageButton) v
					.findViewById(R.id.main_some_tools_alarm);
			ImageButton note = (ImageButton) v
					.findViewById(R.id.main_some_tools_note);
			ImageButton weather = (ImageButton) v
					.findViewById(R.id.main_some_tools_weather);
			ImageButton calendar = (ImageButton) v
					.findViewById(R.id.main_some_tools_calendar);
			ImageButton countdown = (ImageButton) v
					.findViewById(R.id.main_some_tools_countdown);
			weather.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.i(L.t, "weather");
					Intent intent = new Intent(MainActivity.this,
							SomeToolsWeatherActivity.class);
					intent.setAction(LAYOUT_INFLATER_SERVICE);
					startActivity(intent);
				}
			});
			note.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Log.i(L.t, "weather");
					Intent intent = new Intent(MainActivity.this,
							SomeToolsMemoActivity.class);
					intent.setAction(LAYOUT_INFLATER_SERVICE);
					startActivity(intent);
				}
			});
		}
		Log.i(L.t, "init view:" + v.getId() + " at " + position);
	}

	// for ActionBarDrawerToggle
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setTitle(String title) {
		getSupportActionBar().setTitle(title);
	}
}
