package com.david.pda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.sqlite.model.Target;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DrawUtil;
import com.david.pda.weather.model.util.L;

public class TargetManageOptionActivity extends Activity {
	ImageView imageView;
	ImageButton backward;
	ImageButton plus;
	LinearLayout linearLayout;
	RelativeLayout topBar;
	Canvas c;
	Paint p;
	RectF rectf = new RectF(0, 0, 400, 400);// 380/2=190
	Bitmap currentBitmap;
	private List<Target> targets = new ArrayList<Target>();
	private Target startTarget;
	private Target endTarget;
	private long[] times = new long[] { 1l, 2l };
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_target_manage_option);
		backward = (ImageButton) findViewById(R.id.main_target_manage_option_topbar_backward);
		Bind.bindReturn(backward, TargetManageOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_TARGET_MANAGE);
		plus = (ImageButton) findViewById(R.id.main_target_manage_option_topbar_plus);
		imageView = (ImageView) findViewById(R.id.main_target_manage_option_imageView);
		topBar = (RelativeLayout) findViewById(R.id.main_target_manage_option_topbar);
		linearLayout = (LinearLayout) findViewById(R.id.main_target_manage_option_linearLayout);
		refreshBitmap();
		loadData();
		drawDataToBitmap();// 利用数据 draw bitmap
		imageView.setOnTouchListener(new ImageOnTouchListener());
		plus.setOnClickListener(new PlusOnclickListener());
	}

	class PlusOnclickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(TargetManageOptionActivity.this,
					TestBarActivity.class);
			TargetManageOptionActivity.this.startActivity(i);
			TargetManageOptionActivity.this.finish();
		}

	}

	private void refreshBitmap() {
		currentBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.s400).copy(Bitmap.Config.ARGB_8888, true);
		imageView.setImageBitmap(currentBitmap);
		c = new Canvas(currentBitmap);
	}

	class ImageOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent e) {
			String text = e.getX() + "," + e.getY();

			// Toast.makeText(TargetManageOptionActivity.this, text,
			// Toast.LENGTH_SHORT).show();

			switch (e.getAction()) {

			case MotionEvent.ACTION_DOWN: {// 按下时触发
				Log.i(L.mo, "ACTION_DOWN" + text);
				startTarget = getBlockIndexByXY(e.getX(), e.getY());
				times[0] = System.currentTimeMillis();
				if (startTarget != null)
					Log.i(L.mo, "startTarget:" + startTarget.getName());
			}
				break;
			case MotionEvent.ACTION_MOVE: {// 移动时触发
			}
				break;
			case MotionEvent.ACTION_UP: {
				// 触摸后触发
				Log.i(L.mo, "ACTION_UP" + text);
				endTarget = getBlockIndexByXY(e.getX(), e.getY());
				times[1] = System.currentTimeMillis();
				if (endTarget != null)
					Log.i(L.mo, "endTarget:" + endTarget.getName());
				doAfterActionUp();
			}
				break;
			}
			return true;
		}
	}

	private void doAfterActionUp() {
		if (startTarget != null && endTarget != null) {
			if (startTarget.getName().equals(endTarget.getName())) {
				endTarget = endTarget.clone();// 因为引用同一个对象
				long gap = times[1] - times[0];
				if (gap > 1000) {
					showWindowForAdd();
				} else {
					Toast.makeText(this,
							"想要通过'" + startTarget.getName() + "'分身？请长按",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				// Toast.makeText(this,
				// startTarget.getName() + "->" + endTarget.getName(),
				// Toast.LENGTH_SHORT).show();
				showWindowForUpdate();
			}
		}
		// 不管如何，都清零
	}

	private Target getBlockIndexByXY(float x, float y) {
		return getTargetByScale(getScaleByXY(x, y));
	}

	private Target getTargetByScale(int scale) {
		for (int i = 0, startScale = 0; i < targets.size(); i++) {
			Target t = targets.get(i);
			if (scale > startScale && scale <= startScale + t.getScale()) {
				return targets.get(i);
			}
			startScale += t.getScale();
		}
		return null;
	}

	/*
	 * COS B = (a2+c2-b2)/2ab COSA=(c^2+b^2-a^2)/2bc坐由反余弦得出：
	 * 标A=ACOS(COSA)*180/PI()
	 */
	private int getScaleByXY(float x, float y) {
		if (isInCircle(x, y)) {
			Log.i(L.mo, "y");
			if (x == 200) {
				if (y > 200) {
					return 270;
				} else if (y < 200) {
					return 90;
				} else {// y==0
					return -1;
				}
			}
			if (y == 200) {
				if (x < 200) {
					return 180;
				} else if (x > 200) {
					return 0;
				} else
					return -1;
			}
			double scalePi = Math.atan2(y - 200, x - 200);
			Log.i(L.mo, "scalePi:" + scalePi);
			return (int) ((scalePi > 0 ? scalePi : (2 * Math.PI + scalePi))
					/ Math.PI * 180);
		} else {
			Log.i(L.mo, "n");
		}
		return -1;
	}

	private boolean isInCircle(float x, float y) {
		return Math.pow(x - 200, 2) + Math.pow(y - 200, 2) < Math.pow(200, 2);
	}

	private void loadData() {
		DemoDB<Target> db = new DemoDB<Target>(new Target());
		targets = db.getList(TargetManageOptionActivity.this);
	}

	private void drawDataToBitmap() {
		refreshBitmap();
		linearLayout.removeAllViewsInLayout();// 去除之前的数据
		Canvas cc;
		Bitmap bm;
		TextView txt;
		ImageView icon;
		Paint p;
		Target t;
		for (int i = 0, startScale = 0; i < targets.size(); startScale += targets
				.get(i).getScale(), i++) {
			t = targets.get(i);
			p = DrawUtil.getPaint(i, i + 1 == targets.size());
			c.drawArc(rectf, startScale, t.getScale(), true, p);
			LinearLayout l = new LinearLayout(this);
			l.setOrientation(LinearLayout.HORIZONTAL);

			bm = BitmapFactory.decodeResource(getResources(), R.drawable.s40)
					.copy(Bitmap.Config.ARGB_8888, true);
			cc = new Canvas(bm);
			cc.drawCircle(20, 20, 10, p);
			icon = new ImageView(this);
			icon.setImageBitmap(bm);
			icon.setScaleType(ScaleType.CENTER);
			icon.setMaxHeight(10);
			icon.setMaxWidth(10);
			txt = new TextView(this);
			txt.setText(targets.get(i).getName()
					+ "-"
					+ targets.get(i).getScale()
					+ "("
					+ new BigDecimal(targets.get(i).getScale() * 100.0 / 360)
							.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()
					+ "%)");
			l.addView(icon);
			l.addView(txt);
			linearLayout.addView(l);
		}
	}

	private void showWindowForAdd() {
		if (startTarget == null) {
			return;
		}
		initWindow();
		popupAddLayout.setVisibility(View.VISIBLE);
		popupUpdateLayout.setVisibility(View.INVISIBLE);
		popupAddTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		initAddLayoutContent();
		showWindow();
	}

	private void showWindowForUpdate() {
		if (startTarget == null || endTarget == null) {
			return;
		}
		initWindow();
		popupAddLayout.setVisibility(View.INVISIBLE);
		popupUpdateLayout.setVisibility(View.VISIBLE);
		popupUpdateTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		initUpdateLayoutContent();
		showWindow();
	}

	private void initWindow() {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			popupView = layoutInflater.inflate(
					R.layout.main_target_manage_option_popup, null);
			// 创建一个PopuWidow对象
			popupUpdateLayout = (RelativeLayout) popupView
					.findViewById(R.id.main_target_manage_option_popup_update_layout);
			popupAddLayout = (RelativeLayout) popupView
					.findViewById(R.id.main_target_manage_option_popup_add_layout);
			Point size = new Point();
			getWindowManager().getDefaultDisplay().getSize(size);
			popupWindow = new PopupWindow(popupView, size.x - 20, 350);
			initLayout();
			popupAddTitle.setText("添加分身");
			popupUpdateTitle.setText("分身传输");
		}
	}

	private void showWindow() {
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int xPos = size.x / 2 - popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(topBar, xPos, 20);
	}

	private void hideWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	private void initUpdateLayoutContent() {
		popupUpdateStartTargetName.setText(startTarget.getName());
		popupUpdateEndTargetName.setText(endTarget.getName());
		popupUpdateSeekbar
				.setMax(startTarget.getScale() + endTarget.getScale());
		popupUpdateSeekbar.setProgress(startTarget.getScale());
		popupUpdateSeekbarScaleLeft.setText(startTarget.getScale() + "");
		popupUpdateSeekbarScaleRight.setText(endTarget.getScale() + "");
		popupUpdateSeekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						popupUpdateSeekbarScaleLeft.setText(seekBar
								.getProgress() + "");
						popupUpdateSeekbarScaleRight.setText(seekBar.getMax()
								- seekBar.getProgress() + "");
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						popupUpdateSeekbarScaleLeft.setText(seekBar
								.getProgress() + "");
						popupUpdateSeekbarScaleRight.setText(seekBar.getMax()
								- seekBar.getProgress() + "");
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						popupUpdateSeekbarScaleLeft.setText(progress + "");
						popupUpdateSeekbarScaleRight.setText(seekBar.getMax()
								- progress + "");
					}
				});
		popupUpdateButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		popupUpdateButtonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startTarget.setScale(Integer
						.valueOf(popupUpdateSeekbarScaleLeft.getText()
								.toString()));
				endTarget.setScale(Integer.valueOf(popupUpdateSeekbarScaleRight
						.getText().toString()));
				Log.i(L.t, "left:" + startTarget.getScale());
				Log.i(L.t, "right:" + endTarget.getScale());
				try {// 修改后，如果比例为零，表示要删掉
					DemoDB<Target> db = new DemoDB<Target>(new Target());
					if (startTarget.getScale() > 0) {
						db.update(startTarget, TargetManageOptionActivity.this);
					} else {
						db.realRemove(startTarget.get_id() + "",
								TargetManageOptionActivity.this);
					}

					DemoDB<Target> db2 = new DemoDB<Target>(new Target());
					if (endTarget.getScale() > 0) {
						db2.update(endTarget, TargetManageOptionActivity.this);
					} else {
						db2.realRemove(endTarget.get_id() + "",
								TargetManageOptionActivity.this);
					}
					Toast.makeText(TargetManageOptionActivity.this, "操作成功！",
							Toast.LENGTH_SHORT).show();
					hideWindow();
					loadData();
					drawDataToBitmap();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// add layout

	private void initAddLayoutContent() {
		popupAddStartTargetName.setText(startTarget.getName());
		popupAddSeekbar.setMax(startTarget.getScale());
		popupAddSeekbar.setProgress(startTarget.getScale());
		popupAddSeekbarScaleLeft.setText(startTarget.getScale() + "");
		popupAddSeekbarScaleRight.setText("0");
		popupAddSeekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						popupAddSeekbarScaleLeft.setText(seekBar.getProgress()
								+ "");
						popupAddSeekbarScaleRight.setText(seekBar.getMax()
								- seekBar.getProgress() + "");
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						popupAddSeekbarScaleLeft.setText(seekBar.getProgress()
								+ "");
						popupAddSeekbarScaleRight.setText(seekBar.getMax()
								- seekBar.getProgress() + "");
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						popupAddSeekbarScaleLeft.setText(progress + "");
						popupAddSeekbarScaleRight.setText(seekBar.getMax()
								- progress + "");
					}
				});
		popupAddButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		popupAddButtonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startTarget.setScale(Integer.valueOf(popupAddSeekbarScaleLeft
						.getText().toString()));
				endTarget.setScale(Integer.valueOf(popupAddSeekbarScaleRight
						.getText().toString()));
				endTarget.setDelFlag(Model.FLAG_EXISTS);
				endTarget.setName(popupAddEndTargetName.getText().toString());
				if (TextUtils.isEmpty(endTarget.getName())) {
					Toast.makeText(TargetManageOptionActivity.this,
							"分身名称不能为空，请重新填写！", Toast.LENGTH_SHORT).show();
					return;
				}
				try {// 修改后，如果比例为零，表示要删掉
					DemoDB<Target> db = new DemoDB<Target>(new Target());
					if (startTarget.getScale() > 0) {
						db.update(startTarget, TargetManageOptionActivity.this);
					} else {
						db.realRemove(startTarget.get_id() + "",
								TargetManageOptionActivity.this);
					}
					DemoDB<Target> db2 = new DemoDB<Target>(new Target());
					if (endTarget.getScale() > 0) {
						endTarget.set_id(null);
						db2.insert(endTarget, TargetManageOptionActivity.this);
					}
					Toast.makeText(TargetManageOptionActivity.this, "操作成功！",
							Toast.LENGTH_SHORT).show();
					hideWindow();
					loadData();
					drawDataToBitmap();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// window
	private View popupView;
	// update
	private RelativeLayout popupUpdateLayout;
	private TextView popupUpdateTitle;// main_target_manage_option_popup_update_title
	private TextView popupUpdateStartTargetName;// main_target_manage_option_popup_update_start_target
	private TextView popupUpdateEndTargetName;// main_target_manage_option_popup_update_end_target
	private TextView popupUpdateSeekbarScaleLeft;// main_target_manage_option_popup_update_seekbar_left
	private TextView popupUpdateSeekbarScaleRight;// main_target_manage_option_popup_update_seekbar_right
	private SeekBar popupUpdateSeekbar;// main_target_manage_option_popup_update_seekbar
	private Button popupUpdateButtonCancel;// main_target_manage_option_popup_update_button_cancel
	private Button popupUpdateButtonConfirm;// addmain_target_manage_option_popup_update_button_confirm

	private RelativeLayout popupAddLayout;
	private TextView popupAddTitle;// main_target_manage_option_popup_add_title
	private TextView popupAddStartTargetName;// main_target_manage_option_popup_add_start_target
	private TextView popupAddEndTargetName;// main_target_manage_option_popup_add_end_target
	private TextView popupAddSeekbarScaleLeft;// main_target_manage_option_popup_add_seekbar_left
	private TextView popupAddSeekbarScaleRight;// main_target_manage_option_popup_add_seekbar_right
	private SeekBar popupAddSeekbar;// main_target_manage_option_popup_add_seekbar
	private Button popupAddButtonCancel;// main_target_manage_option_popup_add_button_cancel
	private Button popupAddButtonConfirm;// addmain_target_manage_option_popup_add_button_confirm

	private void initLayout() {
		// update

		popupUpdateTitle = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_title);
		popupUpdateStartTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_start_target);// main_target_manage_option_popup_update_start_target
		popupUpdateEndTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_end_target);// main_target_manage_option_popup_update_end_target
		popupUpdateSeekbarScaleLeft = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_seekbar_left);// main_target_manage_option_popup_update_seekbar_left
		popupUpdateSeekbarScaleRight = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_seekbar_right);// main_target_manage_option_popup_update_seekbar_right
		popupUpdateSeekbar = (SeekBar) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_seekbar);// main_target_manage_option_popup_update_seekbar
		popupUpdateButtonCancel = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_button_cancel);// main_target_manage_option_popup_update_button_cancel
		popupUpdateButtonConfirm = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_button_confirm);// addmain_target_manage_option_popup_update_button_confirm
		// add
		popupAddTitle = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_title);
		popupAddStartTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_start_target);// main_target_manage_option_popup_add_start_target
		popupAddEndTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_end_target);// main_target_manage_option_popup_add_end_target
		popupAddSeekbarScaleLeft = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_seekbar_left);// main_target_manage_option_popup_add_seekbar_left
		popupAddSeekbarScaleRight = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_seekbar_right);// main_target_manage_option_popup_add_seekbar_right
		popupAddSeekbar = (SeekBar) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_seekbar);// main_target_manage_option_popup_add_seekbar
		popupAddButtonCancel = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_button_cancel);// main_target_manage_option_popup_add_button_cancel
		popupAddButtonConfirm = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_button_confirm);// addmain_target_manage_option_popup_add_button_confirm
	}

}
