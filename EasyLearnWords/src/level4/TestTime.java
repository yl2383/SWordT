package level4;

import Database.ManageDB;
import android.app.Activity;
import android.os.Bundle;

import com.easylearnwords.R;
import com.easylearnwords.yl.Mypublicvalue;

public class TestTime extends Activity {
	private Mypublicvalue myapp;

	/*
	 * CountDownTimer timer = new CountDownTimer(15000, 1000) {
	 * 
	 * @Override public void onTick(long millisUntilFinished) { // TODO
	 * Auto-generated method stub
	 * 
	 * int k = (int) (millisUntilFinished / 1000);
	 * 
	 * timeTextView.setText("Time: " + k + "'s");
	 * 
	 * if (k == 5) { timeTextView.setBackgroundColor(Color.RED); } if (k <= 5) {
	 * myapp.playmusic(2); }
	 * 
	 * }
	 * 
	 * @Override public void onFinish() { // TODO Auto-generated method stub
	 * timeTextView.setText("Time over"); myapp.playmusic(3); // 失败音乐
	 * timer.cancel();
	 * 
	 * } };
	 * 
	 * private TextView timeTextView;
	 * 
	 * private String[][] words;
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idroots);
		myapp = (Mypublicvalue) getApplication();
		String[][] words = myapp.getwords();
		ManageDB db = new ManageDB(getBaseContext());
		if (db.coursexist(myapp.get(0))) { // 判断数据库存在否
			System.out.println("有");
		} else {

			System.out.println("无");
			db.coursereivewcreate(myapp.get(0)); // 创建数据
			System.out.println("创建成功");
		}

		db.deletewrongworddb(); // 删除原来的错词
		System.out.println("删除成功");

		db.insertdb(words, "0"); // 第二个参数为默认参数 写入数据库
		System.out.println("输入成功");

		String[][] wrongwords = db.getwrongwords("0"); // 参数为review 读数据库
		System.out.println(wrongwords[0][0]);
		System.out.println(wrongwords[1][0]);

		db.cleantdata(); // 清扫数据库
		System.out.println("清扫数据库");

		myapp.setreviewwrongcontrol(1);

		int[] k = db.getscore();
		System.out.println("数据库" + k);

	}
	/*
	 * startActivity(intent); finish();
	 * 
	 * 
	 * 
	 * words = myapp.getwords();
	 * 
	 * words = get10ranwords(words);
	 * 
	 * for (int i = 0; i < words.length; i++) {
	 * System.out.println(words[i][0]+" ++"); }
	 * 
	 * timeTextView = (TextView) this.findViewById(R.id.wrtext);
	 * 
	 * timer.start();
	 * 
	 * String ss= "aecdefghe"; String s= "efgh";
	 * 
	 * int k=ss.indexOf(s);
	 * 
	 * System.out.println(k);
	 * 
	 * double a1=0; double a2=0; System.out.println((int)(a1/a2));
	 * 
	 * }
	 * 
	 * private String[][] get10ranwords(String[][] words) {
	 * 
	 * String[][] word = words;
	 * 
	 * for (int i = 10; i < word.length; i++) { for (int j = 0; j <
	 * word[0].length; j++) {
	 * 
	 * word[i][0] = "";
	 * 
	 * } }
	 * 
	 * Random random = new Random(); for (int i = 0; i < 10; i++) { int p =
	 * random.nextInt(10); String[] k = word[i]; word[i] = word[p]; word[p] = k;
	 * }
	 * 
	 * random = null;
	 * 
	 * return words; }
	 */

}
