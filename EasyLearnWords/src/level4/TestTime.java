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
	 * timeTextView.setText("Time over"); myapp.playmusic(3); // ʧ������
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
		if (db.coursexist(myapp.get(0))) { // �ж����ݿ���ڷ�
			System.out.println("��");
		} else {

			System.out.println("��");
			db.coursereivewcreate(myapp.get(0)); // ��������
			System.out.println("�����ɹ�");
		}

		db.deletewrongworddb(); // ɾ��ԭ���Ĵ��
		System.out.println("ɾ���ɹ�");

		db.insertdb(words, "0"); // �ڶ�������ΪĬ�ϲ��� д�����ݿ�
		System.out.println("����ɹ�");

		String[][] wrongwords = db.getwrongwords("0"); // ����Ϊreview �����ݿ�
		System.out.println(wrongwords[0][0]);
		System.out.println(wrongwords[1][0]);

		db.cleantdata(); // ��ɨ���ݿ�
		System.out.println("��ɨ���ݿ�");

		myapp.setreviewwrongcontrol(1);

		int[] k = db.getscore();
		System.out.println("���ݿ�" + k);

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
