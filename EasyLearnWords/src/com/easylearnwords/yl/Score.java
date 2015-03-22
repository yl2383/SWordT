package com.easylearnwords.yl;

import java.util.ArrayList;
import java.util.List;

import level2.Definitionl2;
import level2.Wordsl2;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easylearnwords.R;

public class Score extends Activity {

	private Mypublicvalue myapp;
	private Dialog alertdDialog;
	private TextView score, review;
	private ListView correct, incorret;  // listview to show correct and incorrect wordlist.
	private ArrayAdapter<String> adaptercorrect;
	private ArrayAdapter<String> adapterincorrect;
	private String[][] wrongwords;
	private String[][] words;
	private boolean[] f = new boolean[21];
	private LinearLayout defwordline, idrootline, rootline; // accoring to different level ,we can close relevent score record.
	private TextView textView1, textView2, textViewlevel;
	private int wcon; // review control.  
	private TextView main;
	private int scorenum, defwordscorenum, rootscorenum;

	private TextView defwordscore, rootscore, idrootscore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zscore);
		textView1 = (TextView) this.findViewById(R.id.textview1);
		textView2 = (TextView) this.findViewById(R.id.textview2);
		textViewlevel = (TextView) this.findViewById(R.id.leveltext);
		idrootline = (LinearLayout) this.findViewById(R.id.idrootline);
		idrootline.setVisibility(View.GONE);

		defwordscore = (TextView) this.findViewById(R.id.defwordscore);
		rootscore = (TextView) this.findViewById(R.id.rootscore);
		idrootscore = (TextView) this.findViewById(R.id.idrootscore);
		score = (TextView) this.findViewById(R.id.score);
		review = (TextView) this.findViewById(R.id.reviewbutton);
		main = (TextView) this.findViewById(R.id.mainbutton);

		main.setOnClickListener(new View.OnClickListener() { // main button to list.class

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Score.this, MyList.class);
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
		myapp.stoplevelmusic();
		textView1.setText(underlineclear(myapp.get(0))); // course 
		textView2.setText(myapp.get(1)); // list 
		textViewlevel.setText(" Level: " + myapp.get(3));// level
		wcon = myapp.getreviewwrongcontrol(); // review control

		if (wcon == 0) {

			words = myapp.getwordslv1();
			wrongwords = myapp.getCwrongwords();
			bool(); // this method is used to define when words is "". the f is false.
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

		dbmg();  // store every score into databse
//
		changecolorscore((int) ((myapp.getscore(1) / myapp.getscore(0)) * 100));
		score.setText((int) ((myapp.getscore(1) / myapp.getscore(0)) * 100)
				+ "%");

		defwordscore.setText((int) ((myapp.getdefwordscore(1) / myapp
				.getdefwordscore(0)) * 100) + "%");
		rootscore
				.setText((int) ((myapp.getrootscore(1) / myapp.getrootscore(0)) * 100)
						+ "%");

		for (int i = 0; i < wrongwords.length; i++) {

			if (!wrongwords[i][0].equals("")) {
				listincorrect.add(wrongwords[i][0]);
			}

		}

		for (int i = 0; i < words.length; i++) { // this is used to select right words.

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
					Intent intent = new Intent(Score.this, ScoreWord.class);

					String value = correct.getItemAtPosition(position) // postion
																		// 1��λ
																		// ��0��ʼ��
							.toString();

					for (int i = 0; i < words.length; i++) {
						if (value.equals(words[i][0])) {

							intent.putExtra("name", words[i][0]);  // deliever string by intent method.

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
					Intent intent = new Intent(Score.this, ScoreWord.class);

					String value = incorret.getItemAtPosition(position) // postion
																		// 1��λ
																		// ��0��ʼ��
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

			review.setText("Next Level"); // go to next level
			review.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myapp.empty();
					ManageDB db = new ManageDB(getBaseContext());
					myapp.setwords(db.getwords());

					myapp.set(3, Integer.toString(2));

					double h = Math.random();

					if (h < 0.5) {
						Intent intent = new Intent(Score.this, Wordsl2.class);

						startActivity(intent);

						// finish();
					} else {
						Intent intent = new Intent(Score.this,
								Definitionl2.class);
						startActivity(intent);
					}
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
					Intent intent = new Intent(Score.this, ScoreWord.class);

					String value = correct.getItemAtPosition(position) // postion
																		// 1��λ
																		// ��0��ʼ��
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
					Intent intent = new Intent(Score.this, ScoreWord.class);

					String value = incorret.getItemAtPosition(position) // postion
																		// 1��λ
																		// ��0��ʼ��
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

			if (scorenum == 100) {  // it means if socre is 100. there is not review condition.

				review.setText("Next Level");
				review.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						myapp.empty();
						ManageDB db = new ManageDB(getBaseContext());
						myapp.setwords(db.getwords());

						myapp.set(3, Integer.toString(2));

						double h = Math.random();

						if (h < 0.5) {
							Intent intent = new Intent(Score.this,
									Wordsl2.class);

							startActivity(intent);

							// finish();
						} else {
							Intent intent = new Intent(Score.this,
									Definitionl2.class);
							startActivity(intent);
						}
						finish();
					}
				});

			}

			if (scorenum < 100) {

				myapp.empty1();

				review.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(Score.this, Root.class);
						myapp.setreviewwrongcontrol(1); // �趨����wrongѭ��

						startActivity(intent);
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

		if (db.coursexist(myapp.get(0))) { // judge weather exist the table in database .�ж����ݿ���ڷ�
			System.out.println("���ݿ���");
		} else {

			System.out.println("���ݿ���");
			db.coursereivewcreate(myapp.get(0)); // if not exist, create it. ��������
			System.out.println("�����ɹ�");
		}

		if (wcon == 0) {  // delete the order record.
			db.deletewrongworddb(); // ɾ��ԭ���Ĵ��
			System.out.println("ɾ���ɹ�");
		}
		db.insertscore(scorenum, defwordscorenum, rootscorenum, 0, 0); // store every score.
		db.insertdb(wrongwords, "0"); // store worngwords in databse  �ڶ�������ΪĬ�ϲ������ù� д�����ݿ�
		System.out.println("����ɹ�");

		db.cleantdata(); // ��ɨ���ݿ� clean wrongwords when it is ""��
		System.out.println("��ɨ���ݿ�");

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
					.setMessage("Do you want to exit this level learning��")
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									myapp.empty();

									Intent intent = new Intent(Score.this,
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
