package com.easylearnwords.yl;

import Database.ManageDB;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.easylearnwords.R;

public class Mypublicvalue extends Application {

	public int buttonsound, musicsound;

	public boolean splashscreen;
	
	public float buttonvolume;
	public float musicvolume;

	public String tablename; // 0 代表表名 1代表list名 2代表几号list的阿拉伯数字
	public String listname;
	public String list;
	public String[][] words;
	public String[][] wordslv1;
	public String[][] wordslv2;
	public String level; // 3代表level多少
	public String wordnum; // 4代表学到几号词了

	public String step; // 代表root的时候第几步； seq 为5

	public String definitionrepeat; // 命名为definition+word+root图重复次数， seq 为6

	public String controllv3; // 控制level3的控制阀。 seq为7.

	public String lv1; // 控制level1 和 2的 随机取值控制阀 。 seq 分别为 8

	public int repeatcontrol; // tt 循环控制阀值

	public String[][] wrongwords = new String[300][10];
	public String[][] wrongwords1 = new String[300][10];

	public int wrongwordkey, wrongwordkey1;

	public String[][] Cwrongwords = new String[21][10];
	public String[][] Lwrongwords = new String[21][10];

	public String[][] ranwords;

	public String[] x = new String[10];
	public String[] e = new String[10];

	public double clicknum; // 点击多少个activity次数
	public double rightnum; // 做对了多少个activity次数

	public double idrootclicknum, idrootrightnum;
	public double rootclicknum, rootrightnum;
	public double defwordclicknum, defwordrightnum;

	public int reviewwrongcontrol;

	private SoundPool sp;// 声明一个SoundPool
	private MediaPlayer mediaPlayer;

	private MediaPlayer mediaPlayerlv1, mediaPlayerlv2, mediaPlayerlv3,
			mediaPlayerlv4, mediaPlayerlv5, mediaPlayerlv6, mediaPlayerlv7,
			mediaPlayerlv8;
	private int musicright;// 定义一个整型用load（）；来设置suondID
	private int musicwrong;
	private int musicalarm;
	private int musicfailure;
	private int musictick;

	public int misroot, definition, idroot, root, greenhelp;

	public Vibrator vibrator;
	public String[][] emptywords = new String[21][10];

	private String[][] rootword;
	private String[][] crootword = new String[100][10];

	public int numwords; // 每个list中words不为空的数量

	public int listnum;
	
	public int video;
	
	
	public int getvideo(){
		return video;
	}
	
	public void setvideo(){
		db.updateacount(7, 1);
	}

	// public BroadcastReceiver receiver;

	private ManageDB db;

	public void setdefault() {
		db.setdefault();
		misroot = 0;
		idroot = 0;
		definition = 0;
		root = 0;
		musicsound = 1;
		buttonsound = 1;
	}

	public void deletereviewrecord() {

		db.deletereviewrecord();
	}

	public void setsplashscreen() {

		splashscreen = true;

	}

	public boolean getsplashscreen() {
		return splashscreen;
	}

	public void setmusic(int key, int value) {
		if (key == 0) {
			buttonsound = value;
			db.updateacount(0, value);
		}
		if (key == 1) {
			musicsound = value;
			db.updateacount(1, value);
		}

	}

	public int getmusic(int key) {
		int flag = 0;
		if (key == 0) {
			flag = buttonsound;

		}
		if (key == 1) {
			flag = musicsound;
		}
		return flag;

	}

	/*
	 * public void setreceiver(BroadcastReceiver key) { // no use
	 * 
	 * receiver = key; }
	 */
	/*
	 * public BroadcastReceiver getreceiver() { // no use
	 * 
	 * return receiver; }
	 */
	public void setlistnum(int key) {

		listnum = key;
	}

	public int getlistnum() {
		return listnum;
	}

	public void sethelpcontrol(int key, int p) {
		if (key == 0) {

			if (misroot == 0 && p == 1) {
				db.updateacount(2, 1);
			}

			misroot = p;
		}
		if (key == 1) {
			if (definition == 0 && p == 1) {
				db.updateacount(3, 1);
			}
			definition = p;
		}
		if (key == 2) {
			if (idroot == 0 && p == 1) {
				db.updateacount(4, 1);
			}
			idroot = p;
		}
		if (key == 3) {
			if (root == 0 && p == 1) {
				db.updateacount(5, 1);
			}
			root = p;
		}
		if (key == 4) {

			if (greenhelp == 0 && p == 1) {
				db.updateacount(6, 1);
			}

			greenhelp = p;
		}

	}

	public int gethelpcontrol(int key) { // helpbutton shaping control
		int flag = 0;

		if (key == 0) {
			flag = misroot;

		}
		if (key == 1) {
			flag = definition;
		}
		if (key == 2) {
			flag = idroot;
		}
		if (key == 3) {
			flag = root;
		}
		if (key == 4) {
			flag = greenhelp; // greentoast
		}

		return flag;

	}

	public void greentoast() {

		if (gethelpcontrol(4) == 0) {
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View toastRoot = inflater.inflate(R.layout.toast, null);

			Toast toast = new Toast(getApplicationContext());
			toast.setView(toastRoot);
			TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
			tv.setText(getString(R.string.greenhelp));
			toast.show();
			sethelpcontrol(4, 1);

		} else {

		}

	}

	public void Vibrate() {

		vibrator.vibrate(300);

	}

	public String[][] getrootword() {
		return crootword;
	}

	public void setrootword(String[][] word) {
		rootword = word;
	}

	public double getdefwordscore(int key) {
		double flag = 0;
		if (key == 0) {
			flag = defwordclicknum;
		}
		if (key == 1) {
			flag = defwordrightnum;
		}
		return flag;
	}

	public void setdefwordscore(int key, double number) {
		if (key == 0) {
			defwordclicknum = number;
		}
		if (key == 1) {
			defwordrightnum = number;
		}

	}

	public double getidrootscore(int key) {
		double flag = 0;
		if (key == 0) {
			flag = idrootclicknum;
		}
		if (key == 1) {
			flag = idrootrightnum;
		}
		return flag;
	}

	public void setidrootscore(int key, double number) {
		if (key == 0) {
			idrootclicknum = number;
		}
		if (key == 1) {
			idrootrightnum = number;
		}

	}

	public double getrootscore(int key) {
		double flag = 0;
		if (key == 0) {
			flag = rootclicknum;
		}
		if (key == 1) {
			flag = rootrightnum;
		}
		return flag;
	}

	public void setrootscore(int key, double number) {
		if (key == 0) {
			rootclicknum = number;
		}
		if (key == 1) {
			rootrightnum = number;
		}

	}

	public double getscore(int key) {
		double flag = 0;
		if (key == 0) {
			flag = clicknum;
		}
		if (key == 1) {
			flag = rightnum;
		}
		return flag;
	}

	public void setscore(int key, double number) {
		if (key == 0) {
			clicknum = number;
		}
		if (key == 1) {
			rightnum = number;
		}

	}

	public void setranwords(String[][] key) {
		ranwords = key;

	}

	public String[][] getranwords() {

		return ranwords;

	}

	public String[][] emptywords() { // support a empty array. 提供空words数组的方法

		return emptywords;
	}

	public int getreviewwrongcontrol() {
		return reviewwrongcontrol;

	}

	public void setreviewwrongcontrol(int key) {
		reviewwrongcontrol = key;
	}

	public int getrepeatcontrol() {

		return repeatcontrol;
	}

	public void setrepreatcontrol(int key) {

		repeatcontrol = key;

	}

	public String get(int seq) {
		String flag = new String();
		flag = "test";
		if (seq == 0) {
			flag = tablename;
		}
		if (seq == 1) {
			flag = listname;
		}
		if (seq == 2) {
			flag = list;
		}

		if (seq == 3) {
			flag = level;

		}

		if (seq == 4) {
			flag = wordnum;
		}

		if (seq == 5) {
			flag = step;
		}

		if (seq == 6) {
			flag = definitionrepeat;
		}

		if (seq == 7) {
			flag = controllv3;
		}
		if (seq == 8) {
			flag = lv1;
		}

		return flag;

	}

	public String[][] getwordslv1() {
		return wordslv1;
	}

	public void setwordslv1(String[][] words) {
		wordslv1 = words;
	}

	public String[][] getwordslv2() {
		return wordslv2;
	}

	public void setwordslv2(String[][] words) {
		wordslv2 = words;
	}

	public String[][] getwords() {

		return words;

	}

	public void set(int seq, String value) {
		if (seq == 0) {

			tablename = value;

		}

		if (seq == 1) {
			listname = value;
		}

		if (seq == 2) {
			list = value;
		}

		if (seq == 3) {
			level = value;
		}
		if (seq == 4) {
			wordnum = value;
		}
		if (seq == 5)
			step = value;
		if (seq == 6) {
			definitionrepeat = value;
		}

		if (seq == 7) {
			controllv3 = value;
		}
		if (seq == 8) {
			lv1 = value;
		}

	}

	public void setwords(String[][] words) { // 赋值words的同时也把words中不为空的数目确定

		this.words = words;

	}

	public void addwrongword(String[] word) {

		wrongwords[wrongwordkey] = word;
		wrongwordkey++;

	}

	public void addwrongwords1(String[] word) {
		wrongwords1[wrongwordkey1] = word;
		wrongwordkey1++;
	}

	public void cleanrootwords() // 注意这个wrongword记住循环的错误结果 然后通过快速排序，去掉重复的项

	{

		quicksort2(0, rootword.length - 1);

		int k = 0;
		for (int i = 0; i < rootword.length - 1; i++) {

			if (!rootword[i][0].equals(rootword[i + 1][0])) {
				if (!rootword[i][0].equals(""))

				{
					crootword[k] = rootword[i];
					k++;
				}
			}

		}

	}

	public void quicksort2(int p, int r) {

		if (p < r) {
			int q = partition2(p, r);
			quicksort2(p, q - 1);
			quicksort2(q + 1, r);
		}

	}

	public int partition2(int p, int r) {
		x = rootword[r];
		int i = p - 1;
		for (int j = p; j <= r - 1; j++) {
			if (rootword[j][0].compareTo(x[0]) >= 0) {
				i = i + 1;
				e = rootword[i];
				rootword[i] = rootword[j];
				rootword[j] = e;

			}

		}
		e = rootword[i + 1];
		rootword[i + 1] = rootword[r];
		rootword[r] = e;

		return i + 1;
	}

	public void cleanCwrongwords() // 注意这个wrongword记住循环的错误结果 然后通过快速排序，去掉重复的项

	{

		quicksort1(0, wrongwords1.length - 1);

		int k = 0;
		for (int i = 0; i < wrongwords1.length - 1; i++) {

			if (!wrongwords1[i][0].equals(wrongwords1[i + 1][0])) {
				if (!wrongwords1[i][0].equals(""))

				{
					Lwrongwords[k] = wrongwords1[i];
					k++;
				}
			}

		}

	}

	public void cleanwrongwords() // 注意这个wrongword记住循环的错误结果 然后通过快速排序，去掉重复的项

	{

		quicksort(0, wrongwords.length - 1);

		int k = 0;
		for (int i = 0; i < wrongwords.length - 1; i++) {

			if (!wrongwords[i][0].equals(wrongwords[i + 1][0])) {
				if (!wrongwords[i][0].equals(""))

				{
					Cwrongwords[k] = wrongwords[i];
					k++;
				}
			}

		}

	}

	public void quicksort(int p, int r) {

		if (p < r) {
			int q = partition(p, r);
			quicksort(p, q - 1);
			quicksort(q + 1, r);
		}

	}

	public int partition(int p, int r) {
		x = wrongwords[r];
		int i = p - 1;
		for (int j = p; j <= r - 1; j++) {
			if (wrongwords[j][0].compareTo(x[0]) >= 0) {
				i = i + 1;
				e = wrongwords[i];
				wrongwords[i] = wrongwords[j];
				wrongwords[j] = e;

			}

		}
		e = wrongwords[i + 1];
		wrongwords[i + 1] = wrongwords[r];
		wrongwords[r] = e;

		return i + 1;
	}

	public void quicksort1(int p, int r) {

		if (p < r) {
			int q = partition1(p, r);
			quicksort1(p, q - 1);
			quicksort1(q + 1, r);
		}

	}

	public int partition1(int p, int r) {
		x = wrongwords1[r];
		int i = p - 1;
		for (int j = p; j <= r - 1; j++) {
			if (wrongwords1[j][0].compareTo(x[0]) >= 0) {
				i = i + 1;
				e = wrongwords1[i];
				wrongwords1[i] = wrongwords1[j];
				wrongwords1[j] = e;

			}

		}
		e = wrongwords1[i + 1];
		wrongwords1[i + 1] = wrongwords1[r];
		wrongwords1[r] = e;

		return i + 1;
	}

	public void empty1() {

		set(4, "1"); // 第几号词归1
		set(5, "2"); // root归2 因为root1 是第二位
		set(6, "0"); // 错词变红归0
		set(7, "0");
		set(8, "0");
		set(9, "0");

		setscore(0, 0);
		setscore(1, 0);

		setdefwordscore(0, 0);
		setdefwordscore(1, 0);

		setidrootscore(0, 0);
		setidrootscore(1, 0);

		setrootscore(0, 0);
		setrootscore(1, 0);

	}

	public void empty() { // 0代表清除错词，1代表完全归零

		wrongwordkey = 0; // 错词归0
		wrongwordkey1 = 0;
		set(4, "1"); // 第几号词归1
		set(5, "2"); // root归2 因为root1 是第二位
		set(6, "0"); // 错词变红归0
		set(7, "0");
		set(8, "0");
		set(9, "0");

		setscore(0, 0);
		setscore(1, 0);

		setdefwordscore(0, 0);
		setdefwordscore(1, 0);

		setidrootscore(0, 0);
		setidrootscore(1, 0);

		setrootscore(0, 0);
		setrootscore(1, 0);

		setrepreatcontrol(0); // TT循环控制阀归0

		for (int i = 0; i < wrongwords.length; i++) {
			for (int j = 0; j < wrongwords[0].length; j++) {
				wrongwords[i][j] = "";
			}

		}
		for (int i = 0; i < wrongwords1.length; i++) {
			for (int j = 0; j < wrongwords1[0].length; j++) {
				wrongwords1[i][j] = "";
			}

		}

		reviewwrongcontrol = 0; // 错词循环控制阀归0

		for (int i = 0; i < Cwrongwords.length; i++) {
			for (int j = 0; j < Cwrongwords[0].length; j++) {
				Cwrongwords[i][j] = "";
			}
		}

		for (int i = 0; i < Lwrongwords.length; i++) {
			for (int j = 0; j < Lwrongwords[0].length; j++) {
				Lwrongwords[i][j] = "";
			}
		}

	}

	public String[][] getwrongwords() {

		return wrongwords;

	}

	public String[][] getCwrongwords() {

		return Cwrongwords;
	}

	public String[][] getLwrongwords() {

		return Lwrongwords;
	}

	public void playmusic(int key) {

		if (buttonsound == 1) {

			if (key == 1) {
				sp.play(musicright, buttonvolume, buttonvolume, 1, 0, 1);

			}

			if (key == 0) {

				sp.play(musicwrong, buttonvolume, buttonvolume, 1, 0, 1);
			}

			if (key == 2) {
				sp.play(musicalarm, buttonvolume, buttonvolume, 1, 0, 1);
			}
			if (key == 3) {
				sp.play(musicfailure, buttonvolume, buttonvolume, 1, 0, 1);
			}
			if (key == 4) {
				sp.play(musictick, buttonvolume, buttonvolume, 1, 0, 1);
			}
		} else {

		}
	}

	public void startlevelmusic() {

		if (musicsound == 1) {

			if (level.equals("1")) {
				mediaPlayerlv1.setVolume(musicvolume, musicvolume);
				mediaPlayerlv1.start();
			}
			if (level.equals("2")) {
				mediaPlayerlv2.setVolume(musicvolume, musicvolume);
				mediaPlayerlv2.start();
			}
			if (level.equals("3")) {
				mediaPlayerlv3.setVolume(musicvolume, musicvolume);
				mediaPlayerlv3.start();
			}

			if (level.equals("4")) {
				mediaPlayerlv4.setVolume(musicvolume, musicvolume);
				mediaPlayerlv4.start();
			}
			if (level.equals("5")) {
				mediaPlayerlv5.setVolume(musicvolume, musicvolume);
				mediaPlayerlv5.start();
			}
			if (level.equals("6")) {
				mediaPlayerlv6.setVolume(musicvolume, musicvolume);
				mediaPlayerlv6.start();
			}
			if (level.equals("7")) {
				mediaPlayerlv7.setVolume(musicvolume, musicvolume);
				mediaPlayerlv7.start();
			}
			if (level.equals("8")) {
				mediaPlayerlv8.setVolume(musicvolume, musicvolume);
				mediaPlayerlv8.start();
			}

		}
	}
	
	public void pauselevelmusic() {

		if (musicsound == 1) {

			if (level.equals("1")) {
				mediaPlayerlv1.pause();
			}
			if (level.equals("2")) {
				mediaPlayerlv2.pause();
			}
			if (level.equals("3")) {
				mediaPlayerlv3.pause();
			}

			if (level.equals("4")) {
				mediaPlayerlv4.pause();
			}
			if (level.equals("5")) {
				mediaPlayerlv5.pause();
			}
			if (level.equals("6")) {
				mediaPlayerlv6.pause();
			}
			if (level.equals("7")) {
				mediaPlayerlv7.pause();
			}
			if (level.equals("8")) {
				mediaPlayerlv8.pause();
			}

		}
	}
	
	
	
	
	
	

	
	
	
	public void stoplevelmusic() {

	

			if (level.equals("1")) {
				mediaPlayerlv1.stop();
				mediaPlayerlv1.release();
				mediaPlayerlv1 = MediaPlayer.create(this, R.raw.arch_vid2);
				mediaPlayerlv1.setLooping(true);
			}
			if (level.equals("2")) {
				mediaPlayerlv2.stop();
				mediaPlayerlv2.release();
				mediaPlayerlv2 = MediaPlayer.create(this, R.raw.newbutold);
				mediaPlayerlv2.setLooping(true);
			}
			if (level.equals("3")) {
				mediaPlayerlv3.stop();
				mediaPlayerlv3.release();
				mediaPlayerlv3 = MediaPlayer.create(this, R.raw.outofharlem);
				mediaPlayerlv3.setLooping(true);
			}

			if (level.equals("4")) {
				mediaPlayerlv4.stop();
				mediaPlayerlv4.release();
				mediaPlayerlv4 = MediaPlayer.create(this, R.raw.neworleans);
				mediaPlayerlv4.setLooping(true);
			}
			if (level.equals("5")) {
				mediaPlayerlv5.stop();
				mediaPlayerlv5.release();
				mediaPlayerlv5 = MediaPlayer.create(this, R.raw.reincarnation);
				mediaPlayerlv5.setLooping(true);
			}
			if (level.equals("6")) {
				mediaPlayerlv6.stop();
				mediaPlayerlv6.release();
				mediaPlayerlv6 = MediaPlayer.create(this, R.raw.rootstheme);
				mediaPlayerlv6.setLooping(true);
			}
			if (level.equals("7")) {
				mediaPlayerlv7.stop();
				mediaPlayerlv7.release();
				mediaPlayerlv7 = MediaPlayer.create(this, R.raw.snownewness);
				mediaPlayerlv7.setLooping(true);
			}
			if (level.equals("8")) {
				mediaPlayerlv8.stop();
				mediaPlayerlv8.release();
				mediaPlayerlv8 = MediaPlayer.create(this, R.raw.summertimegreen3);
				mediaPlayerlv8.setLooping(true);
			}

		
	}

	public void startsplashmusic() {

		if (musicsound == 1) {
			mediaPlayer.setVolume(musicvolume, musicvolume);
			mediaPlayer.start();
			
		}

	}

	public void pausesplashmusic() {

		if (musicsound == 1) {
			mediaPlayer.pause();
		}

	}

	public void stopsplashmusic() {

		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = MediaPlayer.create(this, R.raw.splashmusic);
	
		mediaPlayer.setLooping(true);

	}

	public String underlineclear(String key) {
		String flag = key.replace("_", " ");
		return flag;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		splashscreen = false;

		db = new ManageDB(this);

		db.CopyDatabase();

		int[] k = db.getacount();

		buttonsound = k[0];
		musicsound = k[1];
		misroot = k[2];

		definition = k[3];
		idroot = k[4];
		root = k[5];

		greenhelp = k[6];
		
		video=k[7];

		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		musicright = sp.load(this, R.raw.right, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
		musicwrong = sp.load(this, R.raw.wrong, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级\
		musicalarm = sp.load(this, R.raw.timealarm, 1);
		musicfailure = sp.load(this, R.raw.failure, 1);
		musictick = sp.load(this, R.raw.tick, 1);
		
		buttonvolume=Float.parseFloat(getResources().getString(R.string.buttonvolume));

	    musicvolume=Float.parseFloat(getResources().getString(R.string.musicvolume));
		
		mediaPlayer = MediaPlayer.create(this, R.raw.splashmusic);		
		mediaPlayer.setLooping(true);		
		mediaPlayerlv1 = MediaPlayer.create(this, R.raw.arch_vid2);
		mediaPlayerlv1.setLooping(true);
		mediaPlayerlv2 = MediaPlayer.create(this, R.raw.newbutold);
		mediaPlayerlv2.setLooping(true);
		mediaPlayerlv3 = MediaPlayer.create(this, R.raw.outofharlem);
		mediaPlayerlv3.setLooping(true);
		mediaPlayerlv4 = MediaPlayer.create(this, R.raw.neworleans);
		mediaPlayerlv4.setLooping(true);
		mediaPlayerlv5 = MediaPlayer.create(this, R.raw.reincarnation);
		mediaPlayerlv5.setLooping(true);
		mediaPlayerlv6 = MediaPlayer.create(this, R.raw.rootstheme);
		mediaPlayerlv6.setLooping(true);
		mediaPlayerlv7 = MediaPlayer.create(this, R.raw.snownewness);
		mediaPlayerlv7.setLooping(true);
		mediaPlayerlv8 = MediaPlayer.create(this, R.raw.summertimegreen3);
		mediaPlayerlv8.setLooping(true);
		
		
		
		mediaPlayer.setVolume(1, 1);
		mediaPlayerlv1.setVolume(3, 3);
		mediaPlayerlv2.setVolume(3, 3);
		mediaPlayerlv3.setVolume(3, 3);
		mediaPlayerlv4.setVolume(3, 3);
		mediaPlayerlv5.setVolume(3, 3);
		mediaPlayerlv6.setVolume(3, 3);
		mediaPlayerlv7.setVolume(3, 3);
		mediaPlayerlv8.setVolume(3, 3);
		

		clicknum = 0;
		rightnum = 0;
		idrootclicknum = 0;
		idrootrightnum = 0;
		defwordclicknum = 0;
		defwordrightnum = 0;
		rootclicknum = 0;
		rootrightnum = 0;

		reviewwrongcontrol = 0;
		wrongwordkey = 0;
		wrongwordkey1 = 0;
		setlistnum(0);
		set(0, "test");
		set(4, Integer.toString(1));
		set(5, "2"); // root 起始位置
		set(6, "0");
		set(7, "0"); // level3
		set(8, "0"); // lv1
		set(9, "0"); // lv2
		setrepreatcontrol(0);

		for (int i = 0; i < wrongwords.length; i++) {
			for (int j = 0; j < wrongwords[0].length; j++) {
				wrongwords[i][j] = "";
			}

		}

		for (int i = 0; i < wrongwords1.length; i++) {
			for (int j = 0; j < wrongwords1[0].length; j++) {
				wrongwords1[i][j] = "";
			}

		}

		for (int i = 0; i < crootword.length; i++) {
			for (int j = 0; j < crootword[0].length; j++) {
				crootword[i][j] = "";
			}
		}

		for (int i = 0; i < Cwrongwords.length; i++) {
			for (int j = 0; j < Cwrongwords[0].length; j++) {
				Cwrongwords[i][j] = "";
			}
		}

		for (int i = 0; i < Lwrongwords.length; i++) {
			for (int j = 0; j < Lwrongwords[0].length; j++) {
				Lwrongwords[i][j] = "";
			}
		}

		for (int i = 0; i < emptywords.length; i++) {
			for (int j = 0; j < emptywords[0].length; j++) {

				emptywords[i][j] = "";

			}
		}
	}
}
