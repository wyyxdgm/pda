package com.david.pda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends ActionBarActivity {

	protected Button mLoginButton, mSignupButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mLoginButton = (Button) findViewById(R.id.loginButton);
		mSignupButton = (Button) findViewById(R.id.signupButton);

		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		mSignupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					JSONArray a = new JSONArray("[{'age':1},{'age':2}]");
					for (int i = 0; i < a.length(); i++) {
						JSONObject o = (JSONObject) a.get(i);
						int jo = o.getInt("age");
						System.out.println(jo);
						Log.i("tag", jo + "");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Log.i("xxx", "" + id);
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
