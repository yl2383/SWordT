package com.easylearnwords.yl;

import review.ReviwLevel;
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
import android.widget.Button;
import android.widget.TextView;

import com.easylearnwords.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class MyList extends Activity {

	private Mypublicvalue myapp;
	private TextView textView1, textView2; // display course name and list name
	private Button playButton, studyButton, reviewButton;

	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zlist);

		receiver = new HomeKeyEventBroadCastReceiver();
		getApplicationContext().registerReceiver(receiver,
				new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		textView1 = (TextView) this.findViewById(R.id.textview1); // course name
		textView2 = (TextView) this.findViewById(R.id.textview2); // list name

		myapp = (Mypublicvalue) getApplication();

		myapp.startsplashmusic();
		ManageDB db = new ManageDB(getBaseContext());
		myapp.setwords(db.getwords()); // according the course name and list
										// name, now we can ensure how can
										// getwords and store them in to public
										// value

		textView1.setText(underlineclear(myapp.get(0)));  
		textView2.setText(myapp.get(1));

		playButton = (Button) this.findViewById(R.id.button1);
		studyButton = (Button) this.findViewById(R.id.button2);
		reviewButton = (Button) this.findViewById(R.id.button3);

		studyButton.setOnClickListener(new View.OnClickListener() {  

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				studyButton.setBackgroundResource(R.drawable.green);  // when press the button, its background will change color.
				myapp.playmusic(1);  // button sound
				Intent intent = new Intent(MyList.this, StudyWordColor.class);

				startActivity(intent);
				finish();
			}
		});

		playButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 EasyTracker easyTracker = EasyTracker.getInstance(MyList.this);

				  // MapBuilder.createEvent().build() returns a Map of event fields and values
				  // that are set and sent with the hit.
				  easyTracker.send(MapBuilder
				      .createEvent(myapp.get(0),     // Event category (required)
				                   "button_press_to_play",  // Event action (required)
				                   myapp.get(1),   // Event label
				                   null)            // Event value
				      .build()
				  );

				playButton.setBackgroundResource(R.drawable.green);
				myapp.playmusic(1);
				Intent intent = new Intent(MyList.this, Play.class);

				startActivity(intent);
				finish();
			}
		});

		reviewButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reviewButton.setBackgroundResource(R.drawable.green);
				myapp.playmusic(1);
				Intent intent = new Intent(MyList.this, ReviwLevel.class);

				startActivity(intent);
				finish();

			}
		});

	}

	public String underlineclear(String key) {
		String flag = key.replace("_", " ");
		return flag;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(MyList.this, Listselectactivity.class);
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
		getMenuInflater().inflate(R.menu.list, menu);

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

		if (id == R.id.listselect) {
			Intent intent = new Intent(MyList.this, Listselectactivity.class);
			startActivity(intent);
			finish();
		}

		if (id == R.id.mainpage) {
			Intent intent = new Intent(MyList.this, MainActivity.class);
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
