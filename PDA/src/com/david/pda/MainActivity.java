package com.david.pda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.david.pda.adapter.MainAffairPlanAdapter;
import com.david.pda.adapter.SystemMenuAdapter;
import com.david.pda.adapter.SystemSettingListAdapter;
import com.david.pda.adapter.TodayScheduleListAdapter;
import com.david.pda.application.SysApplication;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.Target;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.DateUtil;
import com.david.pda.util.other.DrawUtil;
import com.david.pda.util.time.PlanCycleUtil;
import com.david.pda.weather.model.util.L;

public class MainActivity extends ActionBarActivity {
	public int currentPostion = 0;
	public static final int POSTION_TARGET_MANAGE = 0;
	public static final int POSTION_AFFAIR_PLAN = 1;
	public static final int POSTION_TODAY_SCHEDULE = 2;
	public static final int POSTION_FOUR_CLASSES = 3;/*
													 * public static final int
													 * POSTION_SELF_PRINCIPLE =
													 * 4; public static final
													 * int POSTION_SOME_TOOLS =
													 * 5; public static final
													 * int
													 * POSTION_SYSTEM_SETTIONG =
													 * 6;
													 */
	public static final int POSTION_SOME_TOOLS = 4;
	public static final int POSTION_SYSTEM_SETTIONG = 5;
	private DrawerLayout drawerLayout;
	private ListView listView;
	private ActionBarDrawerToggle drawerListener;
	private OnItemClickListener menuItemCLickListener;
	private SystemMenuAdapter systemMenuAdapter;
	private Button firstBtn;
	private Button secondBtn;
	private FrameLayout mainContentLayout;
	private int[] mainViewIds = new int[] { R.layout.main_target_manage,
			R.layout.main_affair_plan, R.layout.main_today_schedule,
			R.layout.main_four_classes, /* R.layout.main_self_principle, */
			R.layout.main_some_tools, R.layout.main_system_setting };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SysApplication.getInstance().addActivity(this);
		mainContentLayout = (FrameLayout) findViewById(R.id.mainContent);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		listView = (ListView) findViewById(R.id.drawerList);
		firstBtn = (Button) findViewById(R.id.main_btn1);
		secondBtn = (Button) findViewById(R.id.main_btn2);
		systemMenuAdapter = new SystemMenuAdapter(this);
		listView.setAdapter(systemMenuAdapter);
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
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setMessage("确认退出？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SysApplication.getInstance().exit();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
		secondBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, TipActivity.class);
				MainActivity.this.startActivity(i);
			}
		});
		Intent intent = getIntent();
		if (intent.getFlags() >= POSTION_TARGET_MANAGE
				&& intent.getFlags() <= POSTION_SYSTEM_SETTIONG) {
			initMainView(intent.getFlags());
		}
	}

	public void initMainView(int position) {
		currentPostion = position;
		if (mainContentLayout.getChildCount() > 0) {// hash view already
			if (mainContentLayout.getChildAt(0).getId() != mainViewIds[position]) {
				Log.i(L.t, "mainContentLayout.getChildAt(0).getId():"
						+ mainContentLayout.getChildAt(0).getId());
				Log.i(L.t, "mainViewIds[" + position + "]"
						+ mainViewIds[position]);
				// current view is not which is selected
				// should be replaced
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View currentView = inflater.inflate(mainViewIds[position],
						null, false);
				mainContentLayout.removeViewAt(0);
				mainContentLayout.addView(currentView);
				initMainView(position, currentView);
			}
		} else {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View currentView = inflater.inflate(mainViewIds[position], null,
					false);
			mainContentLayout.addView(currentView);
			initMainView(position, currentView);
		}
	}

	private void initMainView(int position, View v) {
		switch (position) {
		case POSTION_SOME_TOOLS:
			initSomeTools(v);
			break;
		case POSTION_AFFAIR_PLAN:
			initAffairPlan(v);
			break;
		case POSTION_FOUR_CLASSES:
			initFourClasses(v);
			break;
		case POSTION_TARGET_MANAGE:
			initTargetManage(v);
			break;
		case POSTION_SYSTEM_SETTIONG:
			initSystemSetting(v);
			break;
		/*
		 * case POSTION_SELF_PRINCIPLE: initSelfPrinciple(v); break;
		 */
		case POSTION_TODAY_SCHEDULE:
			initTodaySchedule(v);
			break;
		default:
			Toast.makeText(this, "no result", Toast.LENGTH_SHORT).show();
			break;
		}
		Log.i(L.t, "init view:" + v.getId() + " at " + position);
	}

	private ViewPager mViewPager;
	private TabWidget mTabWidget;
	private static String[] addresses = { "紧急&重要", "紧急&不重要", "重要&不紧急",
			"不紧急&不重要" };
	private Button[] mBtnTabs = new Button[addresses.length];

	private OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == mBtnTabs[0]) {
				mViewPager.setCurrentItem(0);
			} else if (v == mBtnTabs[1]) {
				mViewPager.setCurrentItem(1);
			} else if (v == mBtnTabs[2]) {
				mViewPager.setCurrentItem(2);
			} else if (v == mBtnTabs[3]) {
				mViewPager.setCurrentItem(3);
			}
		}
	};

	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			mTabWidget.setCurrentTab(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return MyFragment.create(MainActivity.this, addresses[position]);
		}

		@Override
		public int getCount() {
			return addresses.length;
		}
	}

	public static class MyFragment extends Fragment {
		static Context context = null;

		public static MyFragment create(Context context, String address) {
			MyFragment f = new MyFragment();
			if (MyFragment.context == null) {
				MyFragment.context = context;
			}
			Bundle b = new Bundle();
			b.putString("address", address);
			f.setArguments(b);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Bundle b = getArguments();
			View v = inflater.inflate(R.layout.main_four_classes_pager, null);
			// Random r = new Random(System.currentTimeMillis());
			// v.setBackgroundColor(r.nextInt() >> 8 | 0xFF << 24);
			final ListView plan_listview = (ListView) v
					.findViewById(R.id.main_four_classes_pager_list_view);
			DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
			String ui = b.getString("address");
			final int colors[] = new int[] { Color.RED, Color.YELLOW,
					Color.GREEN, Color.BLUE };
			for (int i = 0; i < addresses.length; i++) {
				if (addresses[i].equals(ui)) {// 00,01,10,11
					ui = "" + (3 - i);
					break;
				}
			}
			v.setBackgroundColor(colors[Integer.valueOf(ui)]);
			final List<Plan> planList = db.getList(context,
					Plan.URGENCYIMPORTANT + "=?", new String[] { ui }, null);
			MainAffairPlanAdapter planadapter = new MainAffairPlanAdapter(
					context, planList);
			plan_listview.setAdapter(planadapter);
			plan_listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(context,
							AffairPlanOptionActivity.class);
					intent.setFlags(AffairPlanOptionActivity.FLAG_UPDATE);
					intent.putExtra("from", MainActivity.POSTION_FOUR_CLASSES);
					intent.putExtra("plan", planList.get(position));
					context.startActivity(intent);
				}
			});
			plan_listview
					.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, final int position, long arg3) {
							AlertDialog.Builder builder = new Builder(context);
							builder.setMessage("确认删除吗？");
							builder.setTitle("提示");
							builder.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											DemoDB<Plan> db = new DemoDB<Plan>(
													new Plan());
											List<Plan> list = db
													.getList(context);
											try {
												DemoDB<CycleDetailsForPlan> detailsDb = new DemoDB<CycleDetailsForPlan>(
														new CycleDetailsForPlan());
												List<CycleDetailsForPlan> ds = detailsDb
														.getList(
																context,
																CycleDetailsForPlan.CYLEFOR
																		+ "=?",
																new String[] { list
																		.get(position)
																		.get_id()
																		+ "" },
																null);
												if (ds.size() > 0) {
													detailsDb.realRemove(ds
															.get(0).get_id()
															+ "", context);
												}
												db.realRemove(list
														.get(position).get_id()
														+ "", context);
												Toast.makeText(
														context,
														"删除《"
																+ list.get(
																		position)
																		.getTitle()
																+ "》成功！",
														Toast.LENGTH_SHORT)
														.show();
												list.remove(position);
												plan_listview
														.setAdapter(new MainAffairPlanAdapter(
																context,
																db.getList(context)));
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									});
							builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							builder.create().show();
							return true;
						}
					});
			return v;
		}
	}

	private void initFourClasses(View v) {
		mTabWidget = (TabWidget) v.findViewById(R.id.tabWidget1);
		mTabWidget.setStripEnabled(false);
		mBtnTabs[0] = new Button(this);
		mBtnTabs[0].setFocusable(true);
		mBtnTabs[0].setText(addresses[0]);
		mBtnTabs[0].setTextColor(getResources().getColorStateList(
				R.color.abc_search_url_text_holo));
		mTabWidget.addView(mBtnTabs[0]);
		/*
		 * Listener必须在mTabWidget.addView()之后再加入，用于覆盖默认的Listener，
		 * mTabWidget.addView()中默认的Listener没有NullPointer检测。
		 */
		mBtnTabs[0].setOnClickListener(mTabClickListener);
		mBtnTabs[1] = new Button(this);
		mBtnTabs[1].setFocusable(true);
		mBtnTabs[1].setText(addresses[1]);
		mBtnTabs[1].setTextColor(getResources().getColorStateList(
				R.color.abc_search_url_text_holo));
		mTabWidget.addView(mBtnTabs[1]);
		mBtnTabs[1].setOnClickListener(mTabClickListener);
		mBtnTabs[2] = new Button(this);
		mBtnTabs[2].setFocusable(true);
		mBtnTabs[2].setText(addresses[2]);
		mBtnTabs[2].setTextColor(getResources().getColorStateList(
				R.color.abc_search_url_text_holo));
		mTabWidget.addView(mBtnTabs[2]);
		mBtnTabs[2].setOnClickListener(mTabClickListener);

		mBtnTabs[3] = new Button(this);
		mBtnTabs[3].setFocusable(true);
		mBtnTabs[3].setText(addresses[3]);
		mBtnTabs[3].setTextColor(getResources().getColorStateList(
				R.color.abc_search_url_text_holo));
		mTabWidget.addView(mBtnTabs[3]);
		mBtnTabs[3].setOnClickListener(mTabClickListener);

		mViewPager = (ViewPager) v.findViewById(R.id.viewPager1);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(mPageChangeListener);

		mTabWidget.setCurrentTab(0);

	}

	ListView plan_listview = null;
	final List<Plan> planList = new ArrayList<Plan>();

	public void setPlanList(List<Plan> newList) {
		planList.clear();
		planList.addAll(newList);
	}

	public void reloadPlan(String title, Long targetId, boolean jj, boolean zy) {
		String select = null;
		String[] selectOption = new String[] { targetId + "",
				((jj ? 2 * 1 : 2 * 0) + (zy ? 1 : 0)) + "" };
		if (targetId == -1) {
			selectOption = new String[] { ((jj ? 2 * 1 : 2 * 0) + (zy ? 1 : 0))
					+ "" };
			if (title != null && !title.equals("")) {
				select = Plan.TITLE + " like '%" + title + "%' and "
						+ Plan.URGENCYIMPORTANT + "=? ";
			} else {
				select = Plan.URGENCYIMPORTANT + "=? ";
			}
		} else {
			if (title != null && !title.equals("")) {
				select = Plan.TITLE + " like '%" + title + "%' and "
						+ Plan.TARGET + "=? and " + Plan.URGENCYIMPORTANT
						+ "=? ";
			} else {
				select = Plan.TARGET + "=? and " + Plan.URGENCYIMPORTANT
						+ "=? ";
			}
		}
		DemoDB<Plan> planDb = new DemoDB<Plan>(new Plan());
		setPlanList(planDb.getList(this, select, selectOption, null));
		plan_listview.setAdapter(new MainAffairPlanAdapter(this, planList));
	}

	private void initAffairPlan(View v) {

		DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
		setPlanList(db.getList(this));
		final List<Target> ts = new DemoDB<Target>(new Target()).getList(this);
		ts.add(0, new Target(-1l, "所有目标", 360));
		Button go = (Button) v.findViewById(R.id.affair_plan_top_go);
		final CheckBox jj = (CheckBox) v
				.findViewById(R.id.affair_plan_top_mid_jj);
		final CheckBox zy = (CheckBox) v
				.findViewById(R.id.affair_plan_top_mid_zy);
		final Spinner targets = (Spinner) v
				.findViewById(R.id.affair_plan_top_target_spinner);
		List<String> titles = new ArrayList<String>();
		for (Plan p : planList) {
			if (p.getTitle() != null && p.getTitle().length() > 1)
				titles.add(p.getTitle());
		}
		List<String> names = new ArrayList<String>();
		for (Target t : ts) {
			names.add(t.getName());
		}
		targets.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, names) {
			@Override
			public long getItemId(int position) {
				return ts.get(position).get_id();
			}

		});
		final AutoCompleteTextView searchText = (AutoCompleteTextView) v
				.findViewById(R.id.affair_plan_top_search);
		searchText.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, titles));
		searchText.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				reloadPlan(searchText.getText().toString(),
						targets.getSelectedItemId(), jj.isChecked(),
						zy.isChecked());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		searchText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.i(L.t, event.getCharacters());
				reloadPlan(searchText.getText().toString(),
						targets.getSelectedItemId(), jj.isChecked(),
						zy.isChecked());
				return false;
			}
		});
		targets.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				reloadPlan(searchText.getText().toString(),
						targets.getSelectedItemId(), jj.isChecked(),
						zy.isChecked());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		jj.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				reloadPlan(searchText.getText().toString(),
						targets.getSelectedItemId(), jj.isChecked(),
						zy.isChecked());
			}
		});
		zy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				reloadPlan(searchText.getText().toString(),
						targets.getSelectedItemId(), jj.isChecked(), checked);
			}
		});
		go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						AffairPlanOptionActivity.class);
				intent.setFlags(AffairPlanOptionActivity.FLAG_ADD);
				startActivity(intent);
			}
		});
		plan_listview = (ListView) findViewById(R.id.main_affair_plan_listview);
		MainAffairPlanAdapter planadapter = new MainAffairPlanAdapter(this,
				planList);
		plan_listview.setAdapter(planadapter);
		plan_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						AffairPlanOptionActivity.class);
				intent.setFlags(AffairPlanOptionActivity.FLAG_UPDATE);
				intent.putExtra("plan", planList.get(position));
				MainActivity.this.startActivity(intent);
			}
		});
		plan_listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setMessage("确认删除吗？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
								List<Plan> list = db.getList(MainActivity.this);
								try {
									DemoDB<CycleDetailsForPlan> detailsDb = new DemoDB<CycleDetailsForPlan>(
											new CycleDetailsForPlan());
									List<CycleDetailsForPlan> ds = detailsDb
											.getList(
													MainActivity.this,
													CycleDetailsForPlan.CYLEFOR
															+ "=?",
													new String[] { list.get(
															position).get_id()
															+ "" }, null);
									if (ds.size() > 0) {
										detailsDb.realRemove(ds.get(0).get_id()
												+ "", MainActivity.this);
									}
									db.realRemove(list.get(position).get_id()
											+ "", MainActivity.this);
									Toast.makeText(
											MainActivity.this,
											"删除《"
													+ list.get(position)
															.getTitle()
													+ "》成功！",
											Toast.LENGTH_SHORT).show();
									list.remove(position);
									plan_listview
											.setAdapter(new MainAffairPlanAdapter(
													MainActivity.this,
													db.getList(MainActivity.this)));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	private void initSomeTools(View v) {

		ImageButton alarm = (ImageButton) v
				.findViewById(R.id.main_some_tools_alarm);
		ImageButton memo = (ImageButton) v
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
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		memo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i(L.t, "memo");
				Intent intent = new Intent(MainActivity.this,
						SomeToolsMemoActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		countdown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i(L.t, "countdown");
				Intent intent = new Intent(MainActivity.this,
						SomeToolsCountdownActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		calendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i(L.t, "calendar");
				Intent intent = new Intent(MainActivity.this,
						SomeToolsCalendarActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		alarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(L.t, "alarm");
				Intent intent = new Intent(MainActivity.this,
						SomeToolsAlarmActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});

	}

	// private void initSelfPrinciple(View v) {
	//
	// final ListView listView = (ListView) v
	// .findViewById(R.id.main_self_principle_listview);
	// listView.setAdapter(new SelfPrincipleListAdapter(this));
	// listView.setOnItemClickListener(new OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Intent i = new Intent(MainActivity.this,
	// SelfPrincipleOptionActivity.class);
	// i.putExtra("opt", "update");
	// i.putExtra("position", arg2);
	// MainActivity.this.startActivity(i);
	// }
	// });
	// listView.setOnItemLongClickListener(new OnItemLongClickListener() {
	// @Override
	// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
	// final int position, long arg3) {
	// AlertDialog.Builder builder = new Builder(MainActivity.this);
	// builder.setMessage("确认删除吗？");
	// builder.setTitle("提示");
	// builder.setPositiveButton("确认",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// DemoDB<Principle> db = new DemoDB<Principle>(
	// new Principle());
	// List<Principle> list = db
	// .getList(MainActivity.this);
	// try {
	// DemoDB<CycleDetailsForPrinciple> db2 = new
	// DemoDB<CycleDetailsForPrinciple>(
	// new CycleDetailsForPrinciple());
	// List<CycleDetailsForPrinciple> details = db2
	// .getList(
	// MainActivity.this,
	// CycleDetailsForPrinciple.CYLEFOR
	// + "=?",
	// new String[] { ""
	// + list.get(position)
	// .get_id() },
	// null);
	// for (CycleDetailsForPrinciple c : details) {
	// db2.realRemove(c.get_id() + "",
	// MainActivity.this);
	// }
	// db.realRemove(list.get(position).get_id()
	// + "", MainActivity.this);
	// Toast.makeText(
	// MainActivity.this,
	// "删除《"
	// + list.get(position)
	// .getTitle()
	// + "》成功！",
	// Toast.LENGTH_SHORT).show();
	// list.remove(position);
	// listView.setAdapter(new SelfPrincipleListAdapter(
	// MainActivity.this));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// builder.setNegativeButton("取消",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// return true;
	// }
	// });
	// }

	private void initSystemSetting(View v) {

		final ListView listView = (ListView) v
				.findViewById(R.id.main_system_setting_listview);
		listView.setAdapter(new SystemSettingListAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(MainActivity.this,
						CycleTypeOptionActivity.class);
				i.putExtra("opt", "update");
				i.putExtra("position", arg2);
				MainActivity.this.startActivity(i);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setMessage("确认删除吗？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								DemoDB<CycleType> db = new DemoDB<CycleType>(
										new CycleType());
								List<CycleType> list = db
										.getList(MainActivity.this);
								try {
									db.realRemove(list.get(position).get_id()
											+ "", MainActivity.this);
									Toast.makeText(
											MainActivity.this,
											"删除《"
													+ list.get(position)
															.getName() + "》成功！",
											Toast.LENGTH_SHORT).show();
									list.remove(position);
									listView.setAdapter(new SystemSettingListAdapter(
											MainActivity.this));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
				return true;
			}
		});

	}

	@SuppressLint("UseSparseArrays")
	private void initTodaySchedule(View v) {
		ListView listView = (ListView) v
				.findViewById(R.id.main_today_schedule_list);
		Long startTime = DateUtil.getTodayStartTime();
		Long endTime = DateUtil.getTodayEndTime();
		DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
		DemoDB<CycleDetailsForPlan> ddb = new DemoDB<CycleDetailsForPlan>(
				new CycleDetailsForPlan());
		List<CycleDetailsForPlan> details = ddb.getList(this, null, null,
				CycleDetailsForPlan.STARTTIME + " asc");
		Plan plan = null;
		DemoDB<CycleType> cdb = new DemoDB<CycleType>(new CycleType());
		final List<Plan> plans = new ArrayList<Plan>();
		for (CycleDetailsForPlan d : details) {
			plan = db.get(d.getCycleFor() + "", this);
			plan.setDetail(d);
			plan.setCycleTypeObj(cdb.get(plan.getCycleType() + "", this));
			plans.add(plan);
		}
		Plan pl = null;
		final List<CycleDetailsForPlan> ds = new ArrayList<CycleDetailsForPlan>();
		final Map<Long, Plan> planMap = new HashMap<Long, Plan>();
		for (int i = 0; i < plans.size(); i++) {
			planMap.put(plans.get(i).get_id(), plans.get(i));
			pl = plans.get(i);
			PlanCycleUtil ce = new PlanCycleUtil(startTime, endTime, pl,
					pl.getCycleTypeObj(), pl.getDetail(), pl.getHashTipCycle());
			ds.addAll(ce.getTimes());
		}
		Collections.sort(ds);
		listView.setAdapter(new TodayScheduleListAdapter(planMap, ds, this));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						AffairPlanOptionActivity.class);
				intent.setFlags(AffairPlanOptionActivity.FLAG_UPDATE);
				intent.putExtra("plan",
						planMap.get(ds.get(position).getCycleFor()));
				intent.putExtra("from", MainActivity.POSTION_TODAY_SCHEDULE);// for
				MainActivity.this.startActivity(intent);
			}
		});

	}

	private void initTargetManage(View v) {
		ActionBar bar = super.getActionBar();
		bar.setIcon(R.drawable.ziwaixian);
		ImageView imageView = (ImageView) v
				.findViewById(R.id.main_target_imageView);
		Bitmap bm400 = BitmapFactory.decodeResource(getResources(),
				R.drawable.s400).copy(Bitmap.Config.ARGB_8888, true);
		Canvas c400 = new Canvas(bm400);
		LinearLayout linearLayout = (LinearLayout) v
				.findViewById(R.id.main_target_icon_group);
		Canvas c40;
		Bitmap bm40;
		TextView txt;
		ImageView icon;
		Paint p;
		Target t;
		RectF rectf = new RectF(0, 0, 400, 400);
		DemoDB<Target> db = new DemoDB<Target>(new Target());
		List<Target> targets = db.getList(MainActivity.this);
		if (targets.size() == 0) {// tianjiayigedemo
			db.insert(new Target("我的储备精力", 360), this);
			targets = db.getList(this);
		}
		for (int i = 0, startScale = 0; i < targets.size(); startScale += targets
				.get(i).getScale(), i++) {
			t = targets.get(i);
			p = DrawUtil.getPaint(i, i + 1 == targets.size());
			c400.drawArc(rectf, startScale, t.getScale(), true, p);
			LinearLayout l = new LinearLayout(this);
			l.setOrientation(LinearLayout.HORIZONTAL);
			bm40 = BitmapFactory.decodeResource(getResources(), R.drawable.s40)
					.copy(Bitmap.Config.ARGB_8888, true);
			c40 = new Canvas(bm40);
			c40.drawCircle(20, 20, 10, p);
			icon = new ImageView(this);
			icon.setImageBitmap(bm40);
			icon.setScaleType(ScaleType.CENTER);
			icon.setMaxHeight(10);
			icon.setMaxWidth(10);
			txt = new TextView(this);
			txt.setText(targets.get(i).getName()
					+ "("
					+ new BigDecimal(targets.get(i).getScale() * 100.0 / 360)
							.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()
					+ "%)");
			l.addView(icon);
			l.addView(txt);
			linearLayout.addView(l);
		}
		imageView.setImageBitmap(bm400);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						TargetManageOptionActivity.class);
				startActivity(intent);
			}
		});
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

		getMenuInflater().inflate(R.menu.menu_system_setting, menu);
		menu.getItem(0).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						Intent i = new Intent(MainActivity.this,
								CycleTypeOptionActivity.class);
						i.setFlags(POSTION_SYSTEM_SETTIONG);
						startActivity(i);
						return false;
					}
				});
		menu.getItem(1).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						Intent i = new Intent(MainActivity.this,
								AffairPlanOptionActivity.class);
						i.setFlags(POSTION_AFFAIR_PLAN);
						startActivity(i);
						return false;
					}
				});
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
