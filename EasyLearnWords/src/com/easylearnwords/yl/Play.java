package com.easylearnwords.yl;

import java.util.ArrayList;
import java.util.List;

import level2.Definitionl2;
import level2.Wordsl2;
import level3.MissRoot;
import level4.Idroortsl4;
import level5.Idroortsl5;
import level6.Rootl6;
import level7.Missrootl7;
import level8.Definitionl8;
import level8.Wordsl8;
import Database.ManageDB;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.easylearnwords.R;

public class Play extends Activity {

	private TextView textView1, textView2;
	private Mypublicvalue myapp;
	private ListView levelListView;
	private ArrayAdapter<String> adapter;
	private List<String> list = new ArrayList<String>();
	private BroadcastReceiver receiver; // home key

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zplay);

		receiver = new HomeKeyEventBroadCastReceiver();
		getApplicationContext().registerReceiver(receiver,
				new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		textView1 = (TextView) this.findViewById(R.id.textview1); // course name
		textView2 = (TextView) this.findViewById(R.id.textview2); // level name

		myapp = (Mypublicvalue) getApplication();

		myapp.startsplashmusic();

		ManageDB db = new ManageDB(getBaseContext()); // store 20 words in
														// public value, because
														// in some condition,
														// other activity will
														// turn to this activity
														// so store many times 
		myapp.setwords(db.getwords());

		textView1.setText(underlineclear(myapp.get(0)));
		textView2.setText(myapp.get(1));

		levelListView = (ListView) this.findViewById(R.id.levellist);

		this.addlistinformation(); // assign level name strings to list.

		adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, list);

	//	levelListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		levelListView.setAdapter(adapter);

		levelListView.setOnItemClickListener(new OnItemClickListener() {

			// managedb db = new managedb(getBaseContext()); ///
			// 因为在list选择界面已经调入words

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				myapp.playmusic(1);

				myapp.set(3, Integer.toString(position + 1)); // store level to public value 存入level等级

				myapp.stopsplashmusic();

				// myapp.setwords(db.getwords()); /// 因为在list选择界面已经调入words

				myapp.startlevelmusic();
				if (position == 0) {  // level 1

					double h = Math.random();

					if (h < 0.5) {
						Intent intent = new Intent(Play.this, Words.class);

						startActivity(intent);

					} else {
						Intent intent = new Intent(Play.this, Definition.class);
						startActivity(intent);
					}

				}
				if (position == 1) {  // level 2
					double h = Math.random();

					if (h < 0.5) {
						Intent intent = new Intent(Play.this, Wordsl2.class);

						startActivity(intent);

						// finish();
					} else {
						Intent intent = new Intent(Play.this,
								Definitionl2.class);
						startActivity(intent);
					}

				}

				if (position == 2) { // choose level3

					Intent intent = new Intent(Play.this, MissRoot.class);
					startActivity(intent);
					
				}

				if (position == 3) { // choose level4
				//	 Intent intent = new Intent(play.this, testvideo.class); 

					Intent intent = new Intent(Play.this, Idroortsl4.class);

					startActivity(intent);
				}

				if (position == 4) { // choose level5
					Intent intent = new Intent(Play.this, Idroortsl5.class);
					startActivity(intent);
				}
				if (position == 5) { // choose level6
					Intent intent = new Intent(Play.this, Rootl6.class);
					startActivity(intent);
				}
				if (position == 6) { // choose level7
					Intent intent = new Intent(Play.this, Missrootl7.class);
					startActivity(intent);
				}
				if (position == 7) { // choose level8
					double h = Math.random();

					if (h < 0.5) {
						Intent intent = new Intent(Play.this,
								Definitionl8.class);

						startActivity(intent);

						// finish();
					} else {
						Intent intent = new Intent(Play.this, Wordsl8.class);
						startActivity(intent);
					}

				}

				finish();
			}
		});

	}

	private void addlistinformation() {  // assign level name 

		list.add(this.getString(R.string.level1));
		list.add(this.getString(R.string.level2));

		list.add(this.getString(R.string.level3));

		list.add(this.getString(R.string.level4));
		list.add(this.getString(R.string.level5));

		list.add(this.getString(R.string.level6));

		list.add(this.getString(R.string.level7));

		list.add(this.getString(R.string.level8));

	}

	public String underlineclear(String key) {
		String flag = key.replace("_", " ");
		return flag;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(Play.this, MyList.class);
			startActivity(intent);
			finish();

		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		myapp.startsplashmusic();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getApplicationContext().unregisterReceiver(receiver);

	}

	protected class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

		static final String SYSTEM_REASON = "reason";
		static final String SYSTEM_HOME_KEY = "homekey";// home key
		static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (reason != null) {
					if (reason.equals(SYSTEM_HOME_KEY)) {
						myapp.pausesplashmusic(); // home key处理点

					} else if (reason.equals(SYSTEM_RECENT_APPS)) {
						myapp.pausesplashmusic();// long home key处理点
					}
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);

		MenuItem musicsound = menu.add(101, 1, 1, "musicsound");
		MenuItem buttonsound = menu.add(101, 2, 2, "buttonsound");
		musicsound.setCheckable(true);
		buttonsound.setCheckable(true);
		if (myapp.getmusic(0) == 0) {
			buttonsound.setChecked(false);

		} else {
			buttonsound.setChecked(true);
		}
		if (myapp.getmusic(1) == 0) {
			musicsound.setChecked(false);

		} else {
			musicsound.setChecked(true);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.PlayStudyReview) {
			Intent intent = new Intent(Play.this, MyList.class);
			startActivity(intent);
			finish();
		}

		if (id == R.id.listpage) {
			Intent intent = new Intent(Play.this, Listselectactivity.class);
			startActivity(intent);
			finish();
		}

		if (id == R.id.coursepage) {
			Intent intent = new Intent(Play.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		if (id == R.id.Exit) {

			System.exit(0);

			return true;
		}

		if (id == 1) { // musicsound

			if (item.isChecked()) {
				item.setChecked(false);
				myapp.setmusic(1, 0);
				myapp.stopsplashmusic();
			} else {
				item.setChecked(true);
				myapp.setmusic(1, 1);
				myapp.startsplashmusic();

			}
			return true;
		}

		if (id == 2) {

			if (item.isChecked()) {
				item.setChecked(false);
				myapp.setmusic(0, 0);
			} else {
				item.setChecked(true);
				myapp.setmusic(0, 1);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
