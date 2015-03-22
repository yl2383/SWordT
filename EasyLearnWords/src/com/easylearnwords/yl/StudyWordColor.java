package com.easylearnwords.yl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.easylearnwords.R;

public class StudyWordColor extends Activity {

	private Dialog alertdDialog;
	private TextView textView1, textView2;
	private ListView wordListView;
	private Mypublicvalue myapp;
	private SimpleAdapter adapter = null;   // simpleadapter is used to display color words and its definition and roots
	private String[][] words;    // take  words from public values. 
	private BroadcastReceiver receiver;
	private List<Map<String, Object>> list = null;
	Map<String, Object> hashmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zwordlist);

		receiver = new HomeKeyEventBroadCastReceiver();
		getApplicationContext().registerReceiver(receiver,
				new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		textView1 = (TextView) this.findViewById(R.id.textview1);
		textView2 = (TextView) this.findViewById(R.id.textview2);

		myapp = (Mypublicvalue) getApplication();

		textView1.setText(underlineclear(myapp.get(0)));
		textView2.setText(myapp.get(1));

		wordListView = (ListView) this.findViewById(R.id.wordlistview);

		words = myapp.getwords();
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> hashmap = new HashMap<String, Object>();

		for (int i = 0; i < 20; i++) {

			String string = null;

			if (!words[i][0].equals("")) {

				string = words[i][0] + ": " + words[i][1] + ".\n" + "\n";   // words :  defintion
				hashmap = new HashMap<String, Object>();
				hashmap.put("word", words[i][0] + ":  ");
				hashmap.put("definition", words[i][1]);

				if (!words[i][2].equals("")) {

					string = string + words[i][2] + ": " + words[i][3] + ".\n"   // root1 : rootdef1
							+ "\n";
					hashmap.put("root1", words[i][2] + ":  ");
					hashmap.put("rootdef1", words[i][3]);
				}
				if (!words[i][4].equals("")) {

					string = string + words[i][4] + ": " + words[i][5] + ".\n"  // root2 : rootdef2
							+ "\n";
					hashmap.put("root2", words[i][4] + ":  ");
					hashmap.put("rootdef2", words[i][5]);
				}
				if (!words[i][6].equals("")) {

					string = string + words[i][6] + ": " + words[i][7] + ".\n"  // root3 : rootdef3
							+ "\n";
					hashmap.put("root3", words[i][6] + ":  ");
					hashmap.put("rootdef3", words[i][7]);
				}
				if (!words[i][8].equals("")) {

					string = string + words[i][8] + ": " + words[i][9] + ".\n"  // root4 : rootdef4
							+ "\n";
					hashmap.put("root4", words[i][8] + ":  ");
					hashmap.put("rootdef4", words[i][9]);
				}

				list.add(hashmap);
			}

		}

		// System.out.println(list);

		adapter = new SimpleAdapter(StudyWordColor.this, list,
				R.layout.listview, new String[] { "word", "definition",
						"root1", "rootdef1", "root2", "rootdef2", "root3",
						"rootdef3", "root4", "rootdef4" }, new int[] {
						R.id.word, R.id.definition, R.id.root1, R.id.rootdef1,
						R.id.root2, R.id.rootdef2, R.id.root3, R.id.rootdef3,
						R.id.root4, R.id.rootdef4, });
		wordListView.setAdapter(adapter);

	}

	public String underlineclear(String key) {
		String flag = key.replace("_", " ");
		return flag;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			alertdDialog = new AlertDialog.Builder(this)
					.setTitle("EXIT LEVEL")
					.setMessage("Do you want to exit this list word？")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									Intent intent = new Intent(
											StudyWordColor.this, MyList.class);
									startActivity(intent);
									finish();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									alertdDialog.cancel();
								}
							}).create();

			alertdDialog.show();

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
		getMenuInflater().inflate(R.menu.main, menu);

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
