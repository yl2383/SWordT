package com.easylearnwords.yl;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.Toast;

import com.easylearnwords.R;

public class Listselectactivity extends Activity {

	private Mypublicvalue myapp;
	private TextView ListtextView; // show the course name
	private ArrayAdapter<String> adapter;
	private ListView ListlistView; // the listview display the list number.
	private ManageDB db; // call database
	private BroadcastReceiver receiver; // the receiver is used to control Home
										// key.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zlistselect);

		receiver = new HomeKeyEventBroadCastReceiver();
		getApplicationContext().registerReceiver(receiver,
				new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		myapp = (Mypublicvalue) getApplication();

		// myapp.setreceiver(receiver);
		myapp.startsplashmusic();
		String tablename = myapp.get(0); // // get the course name 得到表名

		ListtextView = (TextView) this.findViewById(R.id.Listtextview);

		ListtextView.setText(underlineclear(tablename));

		db = new ManageDB(this);

		int numlist = db.numlist(tablename); // calculate the amount of list

		myapp.setlistnum(numlist); // store the amount of list to public values.
									// it will be used to control bring 20
									// words.

		System.out.println("numberlist of the course " + numlist); // 计算有多少list
																	// 因为每个表至少包含5个单词所以必须有1个list

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < numlist; i++) {
			String listname = "List " + (i + 1);
			list.add(listname);

		}

		adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, list);
		ListlistView = (ListView) this.findViewById(R.id.Listlistview);

//		ListlistView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		ListlistView.setAdapter(adapter);

		ListlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				myapp.playmusic(1); // button sound
				String value = ListlistView.getItemAtPosition(position)  //	// the begin of postion is 0;
						.toString();
				myapp.set(1, value); // store list name to public value  0 代表传递的是表名，1代表传递的list名称
				myapp.set(2, Integer.toString(position + 1)); // store list number to public value 所以需要转换为+1

				// myapp.setwords(db.getwords());

				Toast.makeText(Listselectactivity.this, myapp.get(1), 1).show();

				Intent intent = new Intent(Listselectactivity.this,
						com.easylearnwords.yl.MyList.class);
				finish();
				startActivity(intent);

				// System.out.println(myapp.get(2)+"myapptest");

			}
		});

	}

	public String underlineclear(String key) {  // this method is used to delele the _ from course name
		String flag = key.replace("_", " ");
		return flag;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {  // key back event

			Intent intent = new Intent(Listselectactivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();

		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("Start");

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		myapp.startsplashmusic();
		System.out.println("Restart");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("Resume");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("Stop");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("Pause");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("Destroy");
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
						myapp.pausesplashmusic(); // home key event 处理点

					} else if (reason.equals(SYSTEM_RECENT_APPS)) {
						myapp.pausesplashmusic();// long home key event  处理点
					}
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listselect, menu);

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

		if (id == R.id.mainpage) {
			Intent intent = new Intent(Listselectactivity.this,
					MainActivity.class);
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
