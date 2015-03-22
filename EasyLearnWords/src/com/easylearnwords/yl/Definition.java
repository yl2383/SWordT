package com.easylearnwords.yl;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easylearnwords.R;
import com.google.analytics.tracking.android.EasyTracker;

public class Definition extends Activity {

	private Dialog alertdDialog;
	private TextView textView1, textView2, wordtTextView, textViewdef1,  //wordtTextView: learning progress,
			textViewdef2, textViewdef3; //wordtextview1-3: 3 options
	private TextView textViewlevel, textViewword, textViewwr, textViewscore;  // level, word's defintion, whole score
	private Mypublicvalue myapp;
	private String[][] words;
	private int wordnum; // the whole amount of words in this level
	private int con; // part 1 or 2 control value
	public int wcon; // review control value复习控制阀
	private String[] roots;  // the whole can be used in word's defintion value will be taked to stored in this roots.
	private int numroot;  // the amount of really roots.
	private int lv1;   // this value control weather it is the first word in play words definition. it will control take 10 random words from 20 words. 
	private ImageButton wenhaoButton; // help button 
	private double clicknum, rightnum; // the amount of click, the right choice number of click
	private double defwordclicknum, defwordrightnum;  // the amount of click in words.class, the amount of choose right in words.class 

	private boolean p1 = false; // the value of control helpbutton shaping. 操控help button的控制阀
	private BroadcastReceiver receiver; // home key
	public long sleeptime;  // how long to turn to next words when user click right option.
	private Timer timergreen; // The count down timer to control sleeptime. 
	private Intent intent;
	private boolean timergreencontrol = false;  // the value of control timergreen timer.

	private Matrix matrix = new Matrix();// for achieve helpbuttion shaping .

	private CountDownTimer helpshape;

	CountDownTimer timerhelp = new CountDownTimer(5000, 1000) {  // 5 second, on finish, start shaping 

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFinish() {

			p1 = true;

			helpshape = new CountDownTimer(2000, 90) { // helping button shaping 2 second.

				boolean key = true;

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub

					Bitmap bitmap = ((BitmapDrawable) (getResources()
							.getDrawable(R.drawable.wenhaored))).getBitmap();

					if (key) {

						matrix.setRotate(10f);
						key = false;
					} else {

						matrix.setRotate(-10f);
						key = true;
					}

					bitmap = Bitmap
							.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
					wenhaoButton.setImageBitmap(bitmap);

				}

				@Override
				public void onFinish() {  // finish 2 second shaping, 
					// TODO Auto-generated method stub

					helpshape.cancel();
					wenhaoButton.setImageResource(R.drawable.wenhao);
					timerhelp.start();  // 5 second loop restart.
					// helpshape.start();

				}
			};

			helpshape.start();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zdefinition);
		receiver = new HomeKeyEventBroadCastReceiver();
		getApplicationContext().registerReceiver(receiver,
				new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		System.out.println("definition.class 启动");

		sleeptime = Long.parseLong(this.getString(R.string.sleeptime));  // take how long time from strings.xml

		wenhaoButton = (ImageButton) this.findViewById(R.id.wenhaobutton); // helpbutton
		textView1 = (TextView) this.findViewById(R.id.textview1); // course
		textView2 = (TextView) this.findViewById(R.id.textview2); // list
		wordtTextView = (TextView) this.findViewById(R.id.wordtestview); //  1/10
		textViewdef1 = (TextView) this.findViewById(R.id.textviewdef1);  // 3 options
		textViewdef2 = (TextView) this.findViewById(R.id.textviewdef2);
		textViewdef3 = (TextView) this.findViewById(R.id.textviewdef3);
		textViewlevel = (TextView) this.findViewById(R.id.leveltext); // level
		textViewword = (TextView) this.findViewById(R.id.wordtext);  //word definition
		textViewwr = (TextView) this.findViewById(R.id.wrtext);  
		textViewscore = (TextView) this.findViewById(R.id.scoretext);

		myapp = (Mypublicvalue) getApplication();
		myapp.startlevelmusic();
		textView1.setText(underlineclear(myapp.get(0)));
		textView2.setText(myapp.get(1));

		textViewlevel.setText(" Level: " + myapp.get(3)); // the numer of level 设定显示level的控件

		wordnum = Integer.parseInt(myapp.get(4));  // the number of words.

		clicknum = myapp.getscore(0);  // amount of click
		rightnum = myapp.getscore(1);  // amount of right

		defwordclicknum = myapp.getdefwordscore(0);
		defwordrightnum = myapp.getdefwordscore(1);

		/* words = myapp.getwords(); */

		wcon = myapp.getreviewwrongcontrol(); // review control复习控制阀值

		con = myapp.getrepeatcontrol(); // part1 or 2 control 循环控制阀

		lv1 = Integer.parseInt(myapp.get(8));

		if (wcon == 0) { // regular condition      标准情况下 words 取用

			if (lv1 == 0) {  // the first words condition 
				words = myapp.getwords();
				words = get10ranwords(words);  // bring 10 random words from 20 words
				myapp.setwordslv1(words);  // store the 10 words to public values.
				myapp.set(8, Integer.toString(1));  // in public value lv1 be changed to 1. means the first words has been play.
				// textViewscore.setText("Score:" + 0 * 100 + "%");

			}

			if (lv1 == 1) {
			
				words = myapp.getwordslv1(); // bring words from public values in second or third and so on.

			}

			textViewwr.setText("");
			textViewwr.setBackgroundColor(Color.TRANSPARENT);

			if (con == 0) { // control part1 or part2
				textViewwr.setText("Part1");

			}
			if (con == 1) {
				textViewwr.setText("Part2");
				textViewwr.setBackgroundColor(Color.GREEN);

			}

		}

		if (wcon == 1) { // 错误情况下， 取用错词
			/* lwords = myapp.getwords(); */
			words = myapp.getCwrongwords();
			textViewwr.setText("Wrong Reivew");
			textViewwr.setBackgroundColor(Color.RED);
			

		}
		changecolorscore((int) ((myapp.getscore(1) / myapp.getscore(0)) * 100));
		textViewscore.setText("Score:"
				+ (int) ((myapp.getscore(1) / myapp.getscore(0)) * 100)
				+ "%");
		textViewword.setText("Word: " + wordnum + " / " + wordnum()); // set the ? / 10         设定显示几号word的控件

		String word = words[wordnum - 1][0];
		wordtTextView.setText(word);  // set the word 

		System.out.println("definition TT 循环控制阀 变量 con "
				+ myapp.getrepeatcontrol());
		System.out.println("definition wrong 循环控制阀 变量 wcon "
				+ myapp.getreviewwrongcontrol());

		this.ran();   // set the 4 options 
		if (myapp.gethelpcontrol(1) == 0) {  // control the timerhelp.  it is about remeber funtion of helpbuttoin.
			timerhelp.start();
		}
		if (Integer.parseInt(myapp.get(6)) < 2) {  //  the frequency in this words.  below 2 times.

		
			
			textViewdef1.setOnClickListener(new View.OnClickListener() { // option1

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String key = (String) textViewdef1.getText();
					// timer.cancel();
					if (wcon == 1) { // in  review condition     错词循环下

						if (key.equals(words[wordnum - 1][1])) { // choose right  做对了

							textViewdef1
									.setBackgroundResource(R.drawable.green);  // backgroundcolor change to bule.
							myapp.playmusic(1); // button right sound 
							myapp.setscore(1, rightnum + 1);  // right number +1 and store in public value
							myapp.setdefwordscore(1, defwordrightnum + 1);// right number in words +1 and store in public value

							defrepeat(0); // the repeat number clear.
							if (!words[wordnum][0].equals("")) {  // detect weather is null in next word. not null  

								System.out.println("错词循环下一个");
								myapp.set(4, Integer.toString(wordnum + 1)); // the number of word +1
								intent = new Intent(Definition.this, Root.class); // next activity.

								myapp.setscore(0, clicknum + 1);  // the number of click +1
								myapp.setdefwordscore(0, defwordclicknum + 1);  // the number of click in words and def +1
								stopshape();  // stop helpbutton shape
								timergreen = new Timer();  // wait 2 second.
								timergreen.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										startActivity(intent);
										finish();
									}
								}, sleeptime);

							}

							if (words[wordnum][0].equals("")) { //  next word is null 错词循环结束了

								System.out.println("错词循环结束");
								intent = new Intent(Definition.this,
										Score.class);   // turn to score.
								myapp.cleanCwrongwords();  // this method is quicksort for using to clear repeat words in wrong words.

								myapp.setscore(0, clicknum + 1); // store click number 
								myapp.setdefwordscore(0, defwordclicknum + 1); 
								stopshape();
								timergreen = new Timer();
								timergreen.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										startActivity(intent);
										finish();
									}
								}, sleeptime);

							}

						}

						if (!key.equals(words[wordnum - 1][1])) { // choose wrong in review condition错词循环下做错了

							textViewdef1.setBackgroundResource(R.drawable.red); // backgroundcolor turn to red
							myapp.Vibrate(); // mobile vibratin 
							myapp.playmusic(0); // button wrong sound
							stopshape();
							
							if (!timergreencontrol) {
							myapp.addwrongwords1(words[wordnum - 1]); // store the word in to wrong words. 
							defrepeat(1); // reapeat number +1 这是做错了加1
							Intent intent = new Intent(Definition.this,
									Definition.class);  // the next activity
							startActivity(intent);
							myapp.setscore(0, clicknum + 1);
							myapp.setdefwordscore(0, defwordclicknum + 1);							
							finish();}

						}

					}

					if (wcon == 0) { // in regular condtion   标准循环下

						if (key.equals(words[wordnum - 1][1])) {//  choose right   标准循环下 做对了

							defrepeat(0); // repeat number clear 
							textViewdef1
									.setBackgroundResource(R.drawable.green);  // blue
							myapp.playmusic(1);
							myapp.setscore(1, rightnum + 1);
							myapp.setdefwordscore(1, defwordrightnum + 1);

							if (con == 1) { // in regular part2 condtion 

								if (wordnum % 5 != 0) { // the 6-9 words 没有走完5个单词

									myapp.set(4, Integer.toString(wordnum + 1)); // word number +1 
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();  
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}

								if (wordnum % 5 == 0) { // the 10th words 走完5个单词了
									myapp.set(4, Integer.toString(wordnum + 1));  
									

									if (words[wordnum][0].equals("")) { // 判断结束
										// 整个list的前两个sequence操作结束
										// 进入复习 wrong
										// words 流程。
										myapp.cleanwrongwords(); // quick sort to clear repeat words in wrong words.快速算法排序
																	// clear wrongwords

										myapp.set(4, Integer.toString(1)); // enter to review condtion,the number of word should be value 1. 
										                                  //进入review wrong 循环 wordnumber给1																						

										intent = new Intent(Definition.this,
												Score.class);

										myapp.setscore(0, clicknum + 1);
										myapp.setdefwordscore(0,
												defwordclicknum + 1);
										stopshape();
										timergreen = new Timer();
										timergreen.schedule(new TimerTask() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												startActivity(intent);
												finish();
											}
										}, sleeptime);

									}

								}
							}

							if (con == 0) { // in part1  部分

								if (wordnum % 5 != 0) { // 1-4 words
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}
								if (wordnum % 5 == 0) { // the 5th words
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}

							}

						}
						if (!key.equals(words[wordnum - 1][1])) { // in reugular condtioin choose wrong 标准循环下做错了

							
							textViewdef1.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);
							stopshape();
							if (!timergreencontrol) {
								defrepeat(1);
							myapp.addwrongword(words[wordnum - 1]);

							Intent intent = new Intent(Definition.this,
									Definition.class);
							startActivity(intent);

							// playmusic(0);
							myapp.setscore(0, clicknum + 1);
							myapp.setdefwordscore(0, defwordclicknum + 1);
							
							finish();

						}}

					}
				}
			});

			textViewdef2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String key = (String) textViewdef2.getText();
					// timer.cancel();
					if (wcon == 1) { // 错词循环下

						if (key.equals(words[wordnum - 1][1])) { // 做对了

							textViewdef2
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);
							myapp.setscore(1, rightnum + 1);
							myapp.setdefwordscore(1, defwordrightnum + 1);

							defrepeat(0);
							if (!words[wordnum][0].equals("")) {

								System.out.println("错词循环下一个");
								myapp.set(4, Integer.toString(wordnum + 1));
								intent = new Intent(Definition.this, Root.class);

								myapp.setscore(0, clicknum + 1);
								myapp.setdefwordscore(0, defwordclicknum + 1);
								stopshape();
								timergreen = new Timer();
								timergreen.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										startActivity(intent);
										finish();
									}
								}, sleeptime);

							}

							if (words[wordnum][0].equals("")) { // 错词循环结束了

								System.out.println("错词循环结束");
								intent = new Intent(Definition.this,
										Score.class);
								myapp.cleanCwrongwords();

								myapp.setscore(0, clicknum + 1);
								myapp.setdefwordscore(0, defwordclicknum + 1);
								stopshape();
								timergreen = new Timer();
								timergreen.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										startActivity(intent);
										finish();
									}
								}, sleeptime);

							}

						}

						if (!key.equals(words[wordnum - 1][1])) { // choose wrong in review condition错词循环下做错了

							textViewdef2.setBackgroundResource(R.drawable.red); // backgroundcolor turn to red
							myapp.Vibrate(); // mobile vibratin 
							myapp.playmusic(0); // button wrong sound
							stopshape();
							
							if (!timergreencontrol) {
							myapp.addwrongwords1(words[wordnum - 1]); // store the word in to wrong words. 
							defrepeat(1); // reapeat number +1 这是做错了加1
							Intent intent = new Intent(Definition.this,
									Definition.class);  // the next activity
							startActivity(intent);
							myapp.setscore(0, clicknum + 1);
							myapp.setdefwordscore(0, defwordclicknum + 1);							
							finish();}

						}

					}

					if (wcon == 0) { // 标准循环下

						if (key.equals(words[wordnum - 1][1])) {// 标准循环下 做对了

							defrepeat(0);
							textViewdef2
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);
							myapp.setscore(1, rightnum + 1);
							myapp.setdefwordscore(1, defwordrightnum + 1);

							if (con == 1) { // part2

								if (wordnum % 5 != 0) { // 没有走完5个单词

									myapp.set(4, Integer.toString(wordnum + 1));
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}

								if (wordnum % 5 == 0) { // 走完5个单词了
									myapp.set(4, Integer.toString(wordnum + 1));
									// myapp.setrepreatcontrol(0); // TT循环到头，
									// 清空控制阀

									if (words[wordnum][0].equals("")) { // 判断结束
										// 整个list的前两个sequence操作结束
										// 进入复习 wrong
										// words 流程。
										myapp.cleanwrongwords(); // 快速算法排序
																	// 清理wrongwords

										myapp.set(4, Integer.toString(1)); // 进入review
																			// wrong
																			// 循环
																			// wordnumber
																			// 给1

										// myapp.setreviewwrongcontrol(1); //
										// 设定进入reviewwrong
										// wcon赋值为1

										intent = new Intent(Definition.this,
												Score.class);

										myapp.setscore(0, clicknum + 1);
										myapp.setdefwordscore(0,
												defwordclicknum + 1);
										stopshape();
										timergreen = new Timer();
										timergreen.schedule(new TimerTask() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												startActivity(intent);
												finish();
											}
										}, sleeptime);

									}

								}
							}

							if (con == 0) { // part1 部分

								if (wordnum % 5 != 0) {
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}
								if (wordnum % 5 == 0) {
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}

							}

						}
						if (!key.equals(words[wordnum - 1][1])) { // in reugular condtioin choose wrong 标准循环下做错了

						
							textViewdef2.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);
							stopshape();
							if (!timergreencontrol) {
								defrepeat(1);
							myapp.addwrongword(words[wordnum - 1]);

							Intent intent = new Intent(Definition.this,
									Definition.class);
							startActivity(intent);

							// playmusic(0);
							myapp.setscore(0, clicknum + 1);
							myapp.setdefwordscore(0, defwordclicknum + 1);
							
							finish();

						}}

					}
				}
			});
			textViewdef3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String key = (String) textViewdef3.getText();
					// timer.cancel();
					if (wcon == 1) { // 错词循环下

						if (key.equals(words[wordnum - 1][1])) { // 做对了

							textViewdef3
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);
							myapp.setscore(1, rightnum + 1);
							myapp.setdefwordscore(1, defwordrightnum + 1);

							defrepeat(0);
							if (!words[wordnum][0].equals("")) {

								System.out.println("错词循环下一个");
								myapp.set(4, Integer.toString(wordnum + 1));
								intent = new Intent(Definition.this, Root.class);

								myapp.setscore(0, clicknum + 1);
								myapp.setdefwordscore(0, defwordclicknum + 1);
								stopshape();
								timergreen = new Timer();
								timergreen.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										startActivity(intent);
										finish();
									}
								}, sleeptime);

							}

							if (words[wordnum][0].equals("")) { // 错词循环结束了

								System.out.println("错词循环结束");
								intent = new Intent(Definition.this,
										Score.class);
								myapp.cleanCwrongwords();

								myapp.setscore(0, clicknum + 1);
								myapp.setdefwordscore(0, defwordclicknum + 1);
								stopshape();
								timergreen = new Timer();
								timergreen.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										startActivity(intent);
										finish();
									}
								}, sleeptime);

							}

						}

						if (!key.equals(words[wordnum - 1][1])) { // choose wrong in review condition错词循环下做错了

							textViewdef3.setBackgroundResource(R.drawable.red); // backgroundcolor turn to red
							myapp.Vibrate(); // mobile vibratin 
							myapp.playmusic(0); // button wrong sound
							stopshape();
							
							if (!timergreencontrol) {
							myapp.addwrongwords1(words[wordnum - 1]); // store the word in to wrong words. 
							defrepeat(1); // reapeat number +1 这是做错了加1
							Intent intent = new Intent(Definition.this,
									Definition.class);  // the next activity
							startActivity(intent);
							myapp.setscore(0, clicknum + 1);
							myapp.setdefwordscore(0, defwordclicknum + 1);							
							finish();}

						}

					}

					if (wcon == 0) { // 标准循环下

						if (key.equals(words[wordnum - 1][1])) {// 标准循环下 做对了

							defrepeat(0);
							textViewdef3
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);
							myapp.setscore(1, rightnum + 1);
							myapp.setdefwordscore(1, defwordrightnum + 1);

							if (con == 1) { // part2

								if (wordnum % 5 != 0) { // 没有走完5个单词

									myapp.set(4, Integer.toString(wordnum + 1));
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}

								if (wordnum % 5 == 0) { // 走完5个单词了
									myapp.set(4, Integer.toString(wordnum + 1));
									// myapp.setrepreatcontrol(0); // TT循环到头，
									// 清空控制阀

									if (words[wordnum][0].equals("")) { // 判断结束
										// 整个list的前两个sequence操作结束
										// 进入复习 wrong
										// words 流程。
										myapp.cleanwrongwords(); // 快速算法排序
																	// 清理wrongwords

										myapp.set(4, Integer.toString(1)); // 进入review
																			// wrong
																			// 循环
																			// wordnumber
																			// 给1

										// myapp.setreviewwrongcontrol(1); //
										// 设定进入reviewwrong
										// wcon赋值为1

										intent = new Intent(Definition.this,
												Score.class);

										myapp.setscore(0, clicknum + 1);
										myapp.setdefwordscore(0,
												defwordclicknum + 1);
										stopshape();
										timergreen = new Timer();
										timergreen.schedule(new TimerTask() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												startActivity(intent);
												finish();
											}
										}, sleeptime);

									}

								}
							}

							if (con == 0) { // part1 部分

								if (wordnum % 5 != 0) {
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}
								if (wordnum % 5 == 0) {
									intent = new Intent(Definition.this,
											Root.class);

									myapp.setscore(0, clicknum + 1);
									myapp.setdefwordscore(0,
											defwordclicknum + 1);
									stopshape();
									timergreen = new Timer();
									timergreen.schedule(new TimerTask() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											startActivity(intent);
											finish();
										}
									}, sleeptime);

								}

							}

						}
						if (!key.equals(words[wordnum - 1][1])) { // in reugular condtioin choose wrong 标准循环下做错了

					
							textViewdef3.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);
							stopshape();
							if (!timergreencontrol) {
								defrepeat(1);
							myapp.addwrongword(words[wordnum - 1]);

							Intent intent = new Intent(Definition.this,
									Definition.class);
							startActivity(intent);

							myapp.setscore(0, clicknum + 1);
							myapp.setdefwordscore(0, defwordclicknum + 1);
							
							finish();

						}}

					}
				}
			});

		}

		if (Integer.parseInt(myapp.get(6)) == 2) {  // the wrong frequency is 2 times

			myapp.greentoast(); // Toast show in this condition. 
			textViewdef1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String key = (String) textViewdef1.getText();
					// timer.cancel();
					if (wcon == 1) { // in review condition 错词循环下

						if (key.equals(words[wordnum - 1][1])) { //choose right  做对了
							defrepeat(0);
							textViewdef1
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);

							if (!words[wordnum][0].equals("")) {

								myapp.set(4, Integer.toString(wordnum + 1));
								Intent intent = new Intent(Definition.this,
										Root.class);
								startActivity(intent);
								stopshape();
								finish();

							}

							if (words[wordnum][0].equals("")) { // next words in review is null 错词循环结束了

								Intent intent = new Intent(Definition.this,
										Score.class);
								myapp.cleanCwrongwords();
								startActivity(intent);
								stopshape();
								finish();

							}

						}

						if (!key.equals(words[wordnum - 1][1])) { // 错词循环下做错了

							textViewdef1.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);

						}

					}

					if (wcon == 0) { // reugular condition 标准循环下

						if (key.equals(words[wordnum - 1][1])) {// 标准循环下 做对了

							defrepeat(0);
							textViewdef1
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);

							if (con == 1) { // part2

								if (wordnum % 5 != 0) { // 6-9

									myapp.set(4, Integer.toString(wordnum + 1));
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}

								if (wordnum % 5 == 0) { // 10th  走完5个单词了
									myapp.set(4, Integer.toString(wordnum + 1));

									if (words[wordnum][0].equals("")) { // 判断结束

										myapp.cleanwrongwords();
										myapp.set(4, Integer.toString(1));
										Intent intent = new Intent(
												Definition.this, Score.class);
										startActivity(intent);
										stopshape();
										finish();

									}

								}
							}

							if (con == 0) { // 标准循环 part1 部分

								if (wordnum % 5 != 0) {
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}
								if (wordnum % 5 == 0) {
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}

							}

						}
						if (!key.equals(words[wordnum - 1][1])) { //chooose wrong  标准循环下做错了

							textViewdef1.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);

						}

					}
				}
			});

			textViewdef2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String key = (String) textViewdef2.getText();
					// timer.cancel();
					if (wcon == 1) { // 错词循环下

						if (key.equals(words[wordnum - 1][1])) { // 做对了
							defrepeat(0);
							textViewdef2
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);

							if (!words[wordnum][0].equals("")) {

								myapp.set(4, Integer.toString(wordnum + 1));
								Intent intent = new Intent(Definition.this,
										Root.class);
								startActivity(intent);
								stopshape();
								finish();

							}

							if (words[wordnum][0].equals("")) { // 错词循环结束了

								Intent intent = new Intent(Definition.this,
										Score.class);
								myapp.cleanCwrongwords();
								startActivity(intent);
								stopshape();
								finish();

							}

						}

						if (!key.equals(words[wordnum - 1][1])) { // 错词循环下做错了

							textViewdef2.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);

						}

					}

					if (wcon == 0) { // 标准循环下

						if (key.equals(words[wordnum - 1][1])) {// 标准循环下 做对了

							defrepeat(0);
							textViewdef2
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);

							if (con == 1) { // part2

								if (wordnum % 5 != 0) {

									myapp.set(4, Integer.toString(wordnum + 1));
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}

								if (wordnum % 5 == 0) { // 走完5个单词了
									myapp.set(4, Integer.toString(wordnum + 1));

									if (words[wordnum][0].equals("")) { // 判断结束

										myapp.cleanwrongwords();
										myapp.set(4, Integer.toString(1));
										Intent intent = new Intent(
												Definition.this, Score.class);
										startActivity(intent);
										stopshape();
										finish();

									}

								}
							}

							if (con == 0) { // 标准循环 and 非TT循环 part1 部分

								if (wordnum % 5 != 0) {
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}
								if (wordnum % 5 == 0) {
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}

							}

						}
						if (!key.equals(words[wordnum - 1][1])) { // 标准循环下做错了

							textViewdef2.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);

						}

					}
				}
			});
			textViewdef3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String key = (String) textViewdef3.getText();
					// timer.cancel();
					if (wcon == 1) { // 错词循环下

						if (key.equals(words[wordnum - 1][1])) { // 做对了
							defrepeat(0);
							textViewdef3
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);

							if (!words[wordnum][0].equals("")) {

								myapp.set(4, Integer.toString(wordnum + 1));
								Intent intent = new Intent(Definition.this,
										Root.class);
								startActivity(intent);
								stopshape();
								finish();

							}

							if (words[wordnum][0].equals("")) { // 错词循环结束了

								Intent intent = new Intent(Definition.this,
										Score.class);
								myapp.cleanCwrongwords();
								startActivity(intent);
								stopshape();
								finish();

							}

						}

						if (!key.equals(words[wordnum - 1][1])) { // 错词循环下做错了

							textViewdef3.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);

						}

					}

					if (wcon == 0) { // 标准循环下

						if (key.equals(words[wordnum - 1][1])) {// 标准循环下 做对了

							defrepeat(0);
							textViewdef3
									.setBackgroundResource(R.drawable.green);
							myapp.playmusic(1);

							if (con == 1) { // part2

								if (wordnum % 5 != 0) {

									myapp.set(4, Integer.toString(wordnum + 1));
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}

								if (wordnum % 5 == 0) { // 走完5个单词了
									myapp.set(4, Integer.toString(wordnum + 1));

									if (words[wordnum][0].equals("")) { // 判断结束

										myapp.cleanwrongwords();
										myapp.set(4, Integer.toString(1));
										Intent intent = new Intent(
												Definition.this, Score.class);
										startActivity(intent);
										stopshape();
										finish();

									}

								}
							}

							if (con == 0) { // 标准循环 and 非TT循环 part1 部分

								if (wordnum % 5 != 0) {
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}
								if (wordnum % 5 == 0) {
									Intent intent = new Intent(Definition.this,
											Root.class);
									startActivity(intent);
									stopshape();
									finish();

								}

							}

						}
						if (!key.equals(words[wordnum - 1][1])) { // 标准循环下做错了

							textViewdef3.setBackgroundResource(R.drawable.red);
							myapp.Vibrate();
							myapp.playmusic(0);

						}

					}
				}
			});

		}
		wenhaoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopshape();

				if (myapp.gethelpcontrol(1) == 0) {
					myapp.sethelpcontrol(1, 1);
				}

				alertdDialog = new AlertDialog.Builder(Definition.this)
						.setTitle("Instruction")
						.setMessage(getString(R.string.defwordhelp))
						.setIcon(R.drawable.ic_launcher)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

										alertdDialog.cancel();
										// textViewwr.setBackgroundColor(Color.TRANSPARENT);
										// timer.start();

									}
								}).create();

				alertdDialog.show();

			}
		});

	}

	private void defrepeat(int key) {

		if (key == 0) {

			myapp.set(6, Integer.toString(0));
			timergreencontrol = true;
		}

		if (key == 1) {
			myapp.set(6, Integer.toString((Integer.parseInt(myapp.get(6))) + 1));

		}

	}

	private void ran() {
		numroot = 0; // 防止 最后出现不够20 出现的情况

		this.getroots();

		/* if (wcon == 0) { */
		for (int i = 0; i < 20; i++) {

			if (!roots[i].equals("")) { // 死循环问题
				numroot = i + 1;
			}
		}

		double h = Math.random() * 3; // 随机为0到1之间， 这里有4个按钮，所以选4

		// System.out.println((int) h);
		int k = (int) h;

		if (k == 0) {
			method(1);

		}
		if (k == 1) {
			method(2);

		}
		if (k == 2) {
			method(3);

		}
		if (k == 3) {
			method(4);
		}
	}

	private void method(int key) {

		double h;
		int a1, a2, a3;

		a1 = 0;
		a2 = 0;
		a3 = 0;

		for (int i = 0; i < numroot; i++) { // numroot多少个可用root。

			if (words[wordnum - 1][1].equals(roots[i])) {
				a1 = i;
				break;
			}
		}

		h = Math.random() * numroot;
		a2 = (int) h;
		while (a1 == a2) {
			h = Math.random() * numroot;
			a2 = (int) h;
		}

		h = Math.random() * numroot;
		a3 = (int) h;
		while (a1 == a3 || a2 == a3) {
			h = Math.random() * numroot;
			a3 = (int) h;
		}

		if (key == 1) {
			textViewdef1.setText(words[wordnum - 1][1]);

			if (Integer.parseInt(myapp.get(6)) == 2) {
				textViewdef1.setBackgroundResource(R.drawable.green);
				// myapp.playmusic(1);myapp.setscore(1, rightnum+1);
				// //playmusic(1);
				// defrepeat(0);

			}
			textViewdef2.setText(roots[a2]);
			textViewdef3.setText(roots[a3]);
			/* wordTextView4.setText(roots[a4]); */
		}

		if (key == 2) {
			textViewdef2.setText(words[wordnum - 1][1]);

			if (Integer.parseInt(myapp.get(6)) == 2) {
				textViewdef2.setBackgroundResource(R.drawable.green);
				// myapp.playmusic(1);myapp.setscore(1, rightnum+1);
				// //playmusic(1);
				// defrepeat(0);
			}

			textViewdef1.setText(roots[a3]);
			textViewdef3.setText(roots[a2]);
			/* wordTextView4.setText(roots[a4]); */
		}

		if (key == 3) {
			textViewdef3.setText(words[wordnum - 1][1]);

			if (Integer.parseInt(myapp.get(6)) == 2) {
				textViewdef3.setBackgroundResource(R.drawable.green);
				// myapp.playmusic(1);myapp.setscore(1, rightnum+1);
				// //playmusic(1);
				// defrepeat(0);
			}

			textViewdef1.setText(roots[a2]);
			textViewdef2.setText(roots[a3]);
		}

	}

	private String[] getroots() {

		String[][] words;

		words = myapp.getwords();

		roots = new String[60];

		for (int i = 0; i < 60; i++) {
			roots[i] = "";

		}
		// System.out.println("root定义");
		//

		int k = 0;

		for (int i = 0; i < 20; i++) {

			if (!words[i][0].equals("")) {
				roots[k] = words[i][1];
				k++;
			}

		}

		return roots;
	}

	private String[][] get10ranwords(String[][] words) { // 取得前10个单词，并随机重排。

		String[][] word = words;

		for (int i = 10; i < word.length; i++) {
			for (int j = 0; j < word[0].length; j++) {

				word[i][0] = "";

			}
		}

		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int p = random.nextInt(10);
			String[] k = word[i];
			word[i] = word[p];
			word[p] = k;
		}

		random = null;

		return word;
	}

	private int wordnum() {
		int k = 0;
		for (int i = 0; i < words.length; i++) {
			if (!words[i][0].equals("")) {
				k++;
			}
		}

		return k;
	}

	private void stopshape() {
		timerhelp.cancel();

		if (p1) {
			helpshape.cancel();
		}

		wenhaoButton.setImageResource(R.drawable.wenhao);

	}

	public String underlineclear(String key) {
		String flag = key.replace("_", " ");
		return flag;
	}
	@Override
	protected void onStart() {
		
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		myapp.startlevelmusic();

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
		EasyTracker.getInstance(this).activityStop(this);
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
						myapp.pauselevelmusic(); // home key处理点
						

					} else if (reason.equals(SYSTEM_RECENT_APPS)) {
						// long home key处理点
					}
				}
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 myapp.pauselevelmusic();
			stopshape();
			if (timergreencontrol) {
				timergreen.cancel();
			}
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
									  myapp.stoplevelmusic();
									myapp.empty();
									Intent intent = new Intent(Definition.this,
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
								     myapp.startlevelmusic();
									if (Integer.parseInt(myapp.get(6)) < 2) {
										if (timergreencontrol) {
											timergreen = new Timer();
											timergreen.schedule(
													new TimerTask() {

														@Override
														public void run() {
															// TODO

															startActivity(intent);
															finish();
														}
													}, sleeptime);
										}

									}

								}
							}).create();

			alertdDialog.show();

		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);

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
		
		if (id == R.id.level) {
			stopshape();
			if (timergreencontrol) {
				timergreen.cancel();
			}
			myapp.stoplevelmusic();
			myapp.empty();
			Intent intent = new Intent(Definition.this, Play.class);
			startActivity(intent);
			finish();
		}

		
		if (id == R.id.PlayStudyReview) {
			stopshape();
			if (timergreencontrol) {
				timergreen.cancel();
			}
			myapp.stoplevelmusic();
			myapp.empty();
			Intent intent = new Intent(Definition.this, MyList.class);
			startActivity(intent);
			finish();
		}

		if (id == R.id.listpage) {
			stopshape();
			if (timergreencontrol) {
				timergreen.cancel();
			}
			myapp.stoplevelmusic();
			myapp.empty();
			Intent intent = new Intent(Definition.this, Listselectactivity.class);
			startActivity(intent);
			finish();
		}

		if (id == R.id.coursepage) {
			stopshape();
			if (timergreencontrol) {
				timergreen.cancel();
			}
			myapp.stoplevelmusic();
			myapp.empty();
			Intent intent = new Intent(Definition.this, MainActivity.class);
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
				myapp.stoplevelmusic();

			} else {
				item.setChecked(true);
				myapp.setmusic(1, 1);
				myapp.startlevelmusic();

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
			textViewscore.setTextColor(Color.GREEN);
			
		}
		if (key<=64) {
			textViewscore.setTextColor(Color.RED);
		}
		if (key>64&&key<86) 
			
	 {
			textViewscore.setTextColor(Color.YELLOW);
		}
	}
	

}
