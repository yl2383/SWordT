package level4;

import java.util.ArrayList;
import java.util.List;

import level5.Idroortsl5;
import Database.ManageDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.easylearnwords.yl.MyList;
import com.easylearnwords.yl.Mypublicvalue;
import com.easylearnwords.yl.Play;
import com.easylearnwords.yl.ScoreWord;

public class Scorel4 extends Activity {

	private Mypublicvalue myapp;
	private Dialog alertdDialog;
	private TextView score, review;
	private ListView correct, incorret;
	private ArrayAdapter<String> adaptercorrect;
	private ArrayAdapter<String> adapterincorrect;
	private String[][] wrongwords;
	private String[][] words;
	private boolean[] f = new boolean[21];
	private TextView textView1, textView2, textViewlevel;
	private TextView defwordscore, rootscore, idrootscore;

	private int wcon; // 复习控制阀
	private TextView main;
	private int scorenum, defwordscorenum, rootscorenum, idrootscorenum;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.zscore);

		defwordscore = (TextView) this.findViewById(R.id.defwordscore);
		rootscore = (TextView) this.findViewById(R.id.rootscore);
		idrootscore = (TextView) this.findViewById(R.id.idrootscore);

		score = (TextView) this.findViewById(R.id.score);
		review = (TextView) this.findViewById(R.id.reviewbutton);
		main = (TextView) this.findViewById(R.id.mainbutton);

		textView1 = (TextView) this.findViewById(R.id.textview1);
		textView2 = (TextView) this.findViewById(R.id.textview2);
		textViewlevel = (TextView) this.findViewById(R.id.leveltext);

		myapp = (Mypublicvalue) getApplication();
		myapp.stoplevelmusic();

		textView1.setText(underlineclear(myapp.get(0)));
		textView2.setText(myapp.get(1));
		textViewlevel.setText(" Level: " + myapp.get(3));

		main.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Scorel4.this, MyList.class);
				myapp.empty();
				startActivity(intent);
				finish();
			}
		});
		correct = (ListView) this.findViewById(R.id.Listlistviewcorrect);
		incorret = (ListView) this.findViewById(R.id.Listlistviewincorrect);

		List<String> listcorrect = new ArrayList<String>();
		List<String> listincorrect = new ArrayList<String>();

		myapp = (Mypublicvalue) getApplication();

		wcon = myapp.getreviewwrongcontrol();

		if (wcon == 0) {

			words = myapp.getwords();
			wrongwords = myapp.getCwrongwords();
			bool();
		}

		if (wcon == 1) {
			words = myapp.getCwrongwords();
			wrongwords = myapp.getLwrongwords();
			bool();

		}

		scorenum = (int) ((myapp.getscore(1) / myapp.getscore(0)) * 100);

		defwordscorenum = (int) ((myapp.getdefwordscore(1) / myapp
				.getdefwordscore(0)) * 100);

		rootscorenum = (int) ((myapp.getrootscore(1) / myapp.getrootscore(0)) * 100);

		idrootscorenum = (int) ((myapp.getidrootscore(1) / myapp
				.getidrootscore(0)) * 100);

		dbmg();
	//	changecolorscore((int) ((myapp.getscore(1) / myapp.getscore(0)) * 100));
		score.setText((int) ((myapp.getscore(1) / myapp.getscore(0)) * 100)
				+ "%");
		defwordscore.setText((int) ((myapp.getdefwordscore(1) / myapp
				.getdefwordscore(0)) * 100) + "%");
		rootscore
				.setText((int) ((myapp.getrootscore(1) / myapp.getrootscore(0)) * 100)
						+ "%");
		idrootscore.setText((int) ((myapp.getidrootscore(1) / myapp
				.getidrootscore(0)) * 100) + "%");

		for (int i = 0; i < wrongwords.length; i++) {

			if (!wrongwords[i][0].equals("")) {
				listincorrect.add(wrongwords[i][0]);
			}

		}

		for (int i = 0; i < words.length; i++) {

			for (int j = 0; j < wrongwords.length; j++) {

				if (words[i][0].equals(wrongwords[j][0])) {

					f[i] = false;

					break;
				}

			}

		}

		for (int i = 0; i < words.length; i++) {
			if (f[i]) {
				listcorrect.add(words[i][0]);
			}
		}

		adapterincorrect = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, listincorrect);

		incorret.setAdapter(adapterincorrect);

		adaptercorrect = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, listcorrect);

		correct.setAdapter(adaptercorrect);

		if (wcon == 1) {

			correct.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Scorel4.this, ScoreWord.class);

					String value = correct.getItemAtPosition(position) // postion
																		// 1号位
																		// 从0开始算
							.toString();

					for (int i = 0; i < words.length; i++) {
						if (value.equals(words[i][0])) {

							intent.putExtra("name", words[i][0]);

							String string = null;

							if (!words[i][0].equals("")) {

								string = "Definition: " + words[i][1] + ".\n"
										+ "\n";

								if (!words[i][2].equals("")) {

									string = string + words[i][2] + ": "
											+ words[i][3] + ".\n" + "\n";
								}
								if (!words[i][4].equals("")) {

									string = string + words[i][4] + ": "
											+ words[i][5] + ".\n" + "\n";
								}
								if (!words[i][6].equals("")) {

									string = string + words[i][6] + ": "
											+ words[i][7] + ".\n" + "\n";
								}
								if (!words[i][8].equals("")) {

									string = string + words[i][8] + ": "
											+ words[i][9] + ".\n" + "\n";
								}

								intent.putExtra("string", string);

							}
							break;
						}
					}
					startActivity(intent);

				}
			});

			incorret.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Scorel4.this, ScoreWord.class);

					String value = incorret.getItemAtPosition(position) // postion
																		// 1号位
																		// 从0开始算
							.toString();

					for (int i = 0; i < wrongwords.length; i++) {
						if (value.equals(wrongwords[i][0])) {

							intent.putExtra("name", wrongwords[i][0]);

							String string = null;

							if (!wrongwords[i][0].equals("")) {

								string = "Definition: " + wrongwords[i][1]
										+ ".\n" + "\n";

								if (!wrongwords[i][2].equals("")) {

									string = string + wrongwords[i][2] + ": "
											+ wrongwords[i][3] + ".\n" + "\n";
								}
								if (!wrongwords[i][4].equals("")) {

									string = string + wrongwords[i][4] + ": "
											+ wrongwords[i][5] + ".\n" + "\n";
								}
								if (!wrongwords[i][6].equals("")) {

									string = string + wrongwords[i][6] + ": "
											+ wrongwords[i][7] + ".\n" + "\n";
								}
								if (!wrongwords[i][8].equals("")) {

									string = string + wrongwords[i][8] + ": "
											+ wrongwords[i][9] + ".\n" + "\n";
								}

								intent.putExtra("string", string);

							}
							break;
						}
					}
					startActivity(intent);

				}
			});

			review.setText("Next Level");
			review.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(Scorel4.this, Idroortsl5.class);
					myapp.empty();
					ManageDB db = new ManageDB(getBaseContext());
					myapp.setwords(db.getwords());

					myapp.set(3, Integer.toString(5));

					startActivity(intent);
					finish();
				}
			});

		}
		if (wcon == 0) {

			correct.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Scorel4.this, ScoreWord.class);

					String value = correct.getItemAtPosition(position) // postion
																		// 1号位
																		// 从0开始算
							.toString();

					for (int i = 0; i < words.length; i++) {
						if (value.equals(words[i][0])) {

							intent.putExtra("name", words[i][0]);

							String string = null;

							if (!words[i][0].equals("")) {

								string = "Definition: " + words[i][1] + ".\n"
										+ "\n";

								if (!words[i][2].equals("")) {

									string = string + words[i][2] + ": "
											+ words[i][3] + ".\n" + "\n";
								}
								if (!words[i][4].equals("")) {

									string = string + words[i][4] + ": "
											+ words[i][5] + ".\n" + "\n";
								}
								if (!words[i][6].equals("")) {

									string = string + words[i][6] + ": "
											+ words[i][7] + ".\n" + "\n";
								}
								if (!words[i][8].equals("")) {

									string = string + words[i][8] + ": "
											+ words[i][9] + ".\n" + "\n";
								}

								intent.putExtra("string", string);

							}
							break;
						}
					}
					startActivity(intent);

				}
			});

			incorret.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Scorel4.this, ScoreWord.class);

					String value = incorret.getItemAtPosition(position) // postion
																		// 1号位
																		// 从0开始算
							.toString();

					for (int i = 0; i < wrongwords.length; i++) {
						if (value.equals(wrongwords[i][0])) {

							intent.putExtra("name", wrongwords[i][0]);

							String string = null;

							if (!wrongwords[i][0].equals("")) {

								string = "Definition: " + wrongwords[i][1]
										+ ".\n" + "\n";

								if (!wrongwords[i][2].equals("")) {

									string = string + wrongwords[i][2] + ": "
											+ wrongwords[i][3] + ".\n" + "\n";
								}
								if (!wrongwords[i][4].equals("")) {

									string = string + wrongwords[i][4] + ": "
											+ wrongwords[i][5] + ".\n" + "\n";
								}
								if (!wrongwords[i][6].equals("")) {

									string = string + wrongwords[i][6] + ": "
											+ wrongwords[i][7] + ".\n" + "\n";
								}
								if (!wrongwords[i][8].equals("")) {

									string = string + wrongwords[i][8] + ": "
											+ wrongwords[i][9] + ".\n" + "\n";
								}

								intent.putExtra("string", string);

							}
							break;
						}
					}
					startActivity(intent);

				}
			});

			if (scorenum == 100) {

				review.setText("Next Level");
				review.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(Scorel4.this,
								Idroortsl5.class);
						myapp.empty();
						ManageDB db = new ManageDB(getBaseContext());
						myapp.setwords(db.getwords());

						myapp.set(3, Integer.toString(5));

						startActivity(intent);
						finish();
					}
				});

			}

			if (scorenum < 100) {

				review.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						myapp.empty1();
						double h = Math.random();

						Intent intent = new Intent(Scorel4.this,
								Idroortsl4.class);

						startActivity(intent);

						// finish();

						myapp.setreviewwrongcontrol(1); // 设定进入wrong循环

						finish();
					}
				});

			}

		}
	}

	private void bool() {
		for (int i = 0; i < f.length; i++) {
			f[i] = true;
		}
		for (int i = 0; i < words.length; i++) {
			if (words[i][0].equals("")) {
				f[i] = false;
			}
		}

	}

	public void dbmg() {

		ManageDB db = new ManageDB(getBaseContext());

		if (db.coursexist(myapp.get(0))) { // 判断数据库存在否
			System.out.println("有");
		} else {

			System.out.println("无");
			db.coursereivewcreate(myapp.get(0)); // 创建数据
			System.out.println("创建成功");
		}

		if (wcon == 0) {
			db.deletewrongworddb(); // 删除原来的错词
			System.out.println("删除成功");
		}
		db.insertscore(scorenum, defwordscorenum, rootscorenum, idrootscorenum,
				0);// int score, int scoredefword, int scoreroot,int
					// scoreidroot, int scoremisroot
		db.insertdb(wrongwords, "0"); // 第二个参数为默认参数不用管 写入数据库
		System.out.println("输入成功");

		db.cleantdata(); // 清扫数据库
		System.out.println("清扫数据库");

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
					.setMessage("Do you want to exit this level learning？")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									myapp.empty();

									Intent intent = new Intent(Scorel4.this,
											Play.class);
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

			} else {
				item.setChecked(true);
				myapp.setmusic(1, 1);

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
	public void changecolorscore(int key){
		if (key>=86) {
			score.setTextColor(Color.GREEN);
			
		}
		if (key<=64) {
			score.setTextColor(Color.RED);
		}
		if (key>64&&key<86) 
			
	 {
			score.setTextColor(Color.YELLOW);
		}
	}
	
}
