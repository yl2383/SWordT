package com.easylearnwords.yl;

import java.util.ArrayList;
import java.util.List;

import Database.ManageDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.easylearnwords.R;

public class MainActivity extends Activity {
	
	private MenuItem musicsound;           // control the splashmusic on and off
	private MenuItem buttonsound;          //  control the button sound. 

	private ListView listView;
	private ArrayAdapter<String> adapter;   //  course list.
	private Mypublicvalue myapp;           // public value and function, store in this java(mypublicvalue.java). 
	private Dialog alertdDialog;           // press the back key, show the dialog. 
	private LinearLayout splash;           // splash screen 

	private boolean exit = true;           //  control in this activtity, the home key can exit the app. 

	private static final int STOPSPLASH = 0;
	// time in milliseconds
	private static final long SPLASHTIME = 1000;

	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {         // this handlemessage is responsible for splashscreen display delay
			switch (msg.what) {
			case STOPSPLASH:
				SystemClock.sleep(1000);
				splash.setVisibility(View.GONE);       // after display the splash, set the Linearlayout hide.
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setTitle("");
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.zmain);

		splash = (LinearLayout) findViewById(R.id.splashscreen);

		myapp = (Mypublicvalue) getApplication(); // public value call; 公共变量传值

		myapp.startsplashmusic();         

		if (!myapp.getsplashscreen()) {     // this getsplashscreen ensure that splashscreen only display once. 
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);

			myapp.setsplashscreen();       //  match the getsplashscreen.
		} else {
			splash.setVisibility(View.GONE);
		}

		List<String> list = new ManageDB(this).listtablename(null);

		List<String> listtablename = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {     
			if(i == 1 || i ==2) continue;  //Skip not installed data;
			String p = list.get(i);
			p = p.replace("_", " ");    // becasue databse table name can't have space, but we need to show space between words, so !!
			listtablename.add(p);

		}

		System.out.println(list); // 输出导入的数据库中的表名称

		// System.out.println("test");

		listView = (ListView) this.findViewById(R.id.listview);

		adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, listtablename);

	//	listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				// TODO Auto-generated method stub

				/* sp.play(music, 1, 1, 0, 0, 1); */

				String value = listView.getItemAtPosition(postion) // postion
																	// 第一位从0开始算
						.toString();

				value = value.replace(" ", "_");

				myapp.set(0, value); // store table name to public value.   0 代表传递的是表名

				exit = false;

				myapp.playmusic(1);   // button sound

				Toast.makeText(MainActivity.this, myapp.get(0), 1).show(); // Toast show choiced table name  显示点击的课程也就是表名

				Intent intent = new Intent(MainActivity.this,
						Listselectactivity.class);

				// myapp.Vibrate(); //震动

				startActivity(intent);

				finish();

			}
		});

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
		exit = true;
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

		if (exit) {
			System.exit(0);
		}

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

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {   
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {   // Back key event
			alertdDialog = new AlertDialog.Builder(this)
					.setTitle("EXIT")
					.setMessage("Do you want to exit complete？")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									System.exit(0);

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
	public boolean onCreateOptionsMenu(Menu menu) {    // Menu design. 

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		 musicsound = menu.add(101, 1, 1, "musicsound");
		 buttonsound = menu.add(101, 2, 2, "buttonsound");
		MenuItem deletereview = menu.add(101, 3, 3, "Delete Review");
		MenuItem setdefault = menu.add(101, 4, 4, "Set Default");

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
	public boolean onPrepareOptionsMenu(Menu menu) { //this method will be call before press menu. 菜单显示前 触发
		// TODO Auto-generated method stub
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
		
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.Exit) { // press exit button

			System.exit(0);

			return true;
		}

		if (id == 1) { // press musicsound

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

		if (id == 2) {  //press buttonsound

			if (item.isChecked()) {
				item.setChecked(false);
				myapp.setmusic(0, 0);
			} else {
				item.setChecked(true);
				myapp.setmusic(0, 1);
			}
			return true;
		}

		if (id == 3) { // press review 

			alertdDialog = new AlertDialog.Builder(this)
					.setTitle("Delete Review Record")
					.setMessage("Do you want to delete review record?")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									myapp.deletereviewrecord();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									alertdDialog.cancel();
									// timer.start();
								}
							}).create();

			alertdDialog.show();

			return true;
		}

		if (id == 4) { // press set default

			alertdDialog = new AlertDialog.Builder(this)
					.setTitle("Set Default")
					.setMessage("Do you want to set default?")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									myapp.setdefault();
									myapp.stopsplashmusic();
									myapp.startsplashmusic();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									alertdDialog.cancel();
									// timer.start();
								}
							}).create();

			alertdDialog.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
