package Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.easylearnwords.yl.Mypublicvalue;

public class ManageDB {

	public static final int listwordnum = 20;
	public Mypublicvalue myapp;
	public Dbopenhelper helper;

	public static final String DB_NAME = "mydb.db"; // 保存的数据库文件名

	public static final String WRONG_NAME = "wrong.mp3";
	public static final String RIGHT_NAME = "right.mp3";
	public static final String PACKAGE_NAME = "com.easylearnwords";// 工程包名
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases"; // 在手机里存放数据库的位置
	public static final String B_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;

	private Context context;

	public String[][] words; // 20个单词缓存在这个字符串数组中 需要21个，因为最后一个可以当做结束符

	public ManageDB(Context context) {
		this.context = context;
		helper = new Dbopenhelper(context);
		myapp = (Mypublicvalue) context.getApplicationContext();
	}

	public void CopyDatabase() {
		String dbfile = DB_PATH + "/" + DB_NAME;
		File path = new File(dbfile);

		if (!path.exists()) { // 文件夹不存在必须先创建文件夹
			System.out.println("不存在 ");
			File dbpath = new File(DB_PATH);
			dbpath.mkdir();

			AssetManager am = this.context.getAssets();

			InputStream is = null;
			FileOutputStream fos = null;

			try {
				is = am.open(DB_NAME);//
				this.context.getResources().getAssets().open(DB_NAME); // //欲导入的数据库
				fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[1024];
				int length = 0;

				while ((length = is.read(buffer)) > 0) {
					fos.write(buffer, 0, length);

				}
				fos.flush();

				fos.close();// 关闭输出流

				is.close();// 关闭输入流
				System.out.println("databse writein success");

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	public List<String> listtablename(String[] selectionArgs) {// 返回多条记录
		// TODO Auto-generated method stub

		List<String> list = new ArrayList<String>();

		String sql = "select  name  from sqlite_master where type='table' and name<>'android_metadata' and name<>'sqlite_sequence' and name not like '%wrong%' ";
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs); // 行查询返回是游标
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) {

				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					String colums_value = cursor.getString(cursor
							.getColumnIndex(colums_name));
					if (colums_value == null) {
						colums_value = "";
					}

					list.add(colums_value);

				}

			}

		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		return list;
	}

	public void deletereviewrecord() {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		String sql = "delete from score_wrong";

		database.execSQL(sql);
		database.close();

	}

	public void deletewrongworddb() {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		database.delete(myapp.get(0) + "_wrong",
				"course=? and list=? and level=?", new String[] { myapp.get(0),
						myapp.get(2), myapp.get(3) });

		database.delete("score_wrong", "course=? and list=? and level=?",
				new String[] { myapp.get(0), myapp.get(2), myapp.get(3) });

		database.close();

	}

	public void insertscore(int score, int scoredefword, int scoreroot,
			int scoreidroot, int scoremisroot) {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("score", score);
		initialValues.put("scoredefword", scoredefword);
		initialValues.put("scoreroot", scoreroot);
		initialValues.put("scoreidroot", scoreidroot);
		initialValues.put("scoremisroot", scoremisroot);

		initialValues.put("course", myapp.get(0));
		initialValues.put("list", myapp.get(2));
		initialValues.put("level", myapp.get(3));
		initialValues.put("review", myapp.getreviewwrongcontrol());
		database.insert("score_wrong", null, initialValues);

		database.close();
	}

	public void setdefault() {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();

		initialValues.put("buttonsound", 1);

		initialValues.put("musicsound", 1);

		initialValues.put("misroot", 0);

		initialValues.put("definition", 0);

		initialValues.put("idroot", 0);

		initialValues.put("root", 0);
		
		initialValues.put("greenhelp", 0);
		
		initialValues.put("video", 0);

		database.update("account_wrong", initialValues, "ID=?",
				new String[] { "1" });

		database.close();

		System.out.println("setdefatult");
	}

	public void updateacount(int key, int value) {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		ContentValues Values = new ContentValues();

		if (key == 0) {
			Values.put("buttonsound", value);
		}
		if (key == 1) {
			Values.put("musicsound", value);
		}

		if (key == 2) {
			Values.put("misroot", value);
		}
		if (key == 3) {
			Values.put("definition", value);
		}
		if (key == 4) {
			Values.put("idroot", value);
		}
		if (key == 5) {
			Values.put("root", value);
		}
		if (key == 6) {
			Values.put("greenhelp", value);
		}
		if (key == 7) {
			Values.put("video", value);
		}

		database.update("account_wrong", Values, "ID=?", new String[] { "1" });

		database.close();

		System.out.println("update ");
	}

	public int[] getacount() {

		int[] k = new int[8];
		SQLiteDatabase database = null;

		String sql = "select buttonsound, musicsound,misroot,definition,idroot,root,greenhelp,video from account_wrong where ID=1";

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					int colums_value = cursor.getInt(cursor
							.getColumnIndex(colums_name));

					if (colums_value == 0) {
						colums_value = 0;
					}

					if (colums_name.equals("buttonsound")) {
						k[i] = colums_value;

					}

					if (colums_name.equals("musicsound")) {
						k[i] = colums_value;
					}

					if (colums_name.equals("misroot")) {
						k[i] = colums_value;
					}

					if (colums_name.equals("definition")) {
						k[i] = colums_value;
					}

					if (colums_name.equals("idroot")) {
						k[i] = colums_value;
					}

					if (colums_name.equals("root")) {
						k[i] = colums_value;
					}
					if (colums_name.equals("greenhelp")) {
						k[i] = colums_value;
					}
					if (colums_name.equals("video")) {
						k[i] = colums_value;
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		return k;

	}

	public int[] getscore() {
		int[] k = new int[4];
		SQLiteDatabase database = null;

		String sql = "select score, scoreroot, scoredefword, scoreidroot from score_wrong where list="
				+ myapp.get(2)
				+ " and level="
				+ myapp.get(3)
				+ " and review="
				+ myapp.getreviewwrongcontrol()
				+ " and course like"
				+ "'%"
				+ myapp.get(0) + "%'";

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					int colums_value = cursor.getInt(cursor
							.getColumnIndex(colums_name));

					if (colums_value == 0) {
						colums_value = 0;
					}

					if (colums_name.equals("score")) {
						k[i] = colums_value;

					}

					if (colums_name.equals("scoreroot")) {
						k[i] = colums_value;
					}
					if (colums_name.equals("scoredefword")) {
						k[i] = colums_value;
					}
					if (colums_name.equals("scoreidroot")) {
						k[i] = colums_value;
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		return k;
	}

	public void insertdb(String[][] words, String correct) {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		for (int i = 0; i < words.length; i++) {

			if (!words[i][0].equals("")) {
				ContentValues initialValues = new ContentValues();
				initialValues.put("name", words[i][0]);
				initialValues.put("def", words[i][1]);
				initialValues.put("froot", words[i][2]);
				initialValues.put("fdef", words[i][3]);
				initialValues.put("sroot", words[i][4]);
				initialValues.put("sdef", words[i][5]);
				initialValues.put("troot", words[i][6]);
				initialValues.put("tdef", words[i][7]);
				initialValues.put("fouthroot", words[i][8]);
				initialValues.put("fouthdef", words[i][9]);
				initialValues.put("course", myapp.get(0));
				initialValues.put("list", myapp.get(2));
				initialValues.put("level", myapp.get(3));
				initialValues.put("review", myapp.getreviewwrongcontrol());
				initialValues.put("correct", correct);
				database.insert(myapp.get(0) + "_wrong", null, initialValues);
			}

		}

		database.close();

	}

	public void coursereivewcreate(String tablename) {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		String sql = "CREATE TABLE ["
				+ tablename
				+ "_wrong] ([name] varchar(20) NOT NULL,  [def] VARCHAR(120),   [froot] varchar(20),   [fdef] varchar(120),  [sroot] VARCHAR(20),  [sdef] VARCHAR(120),   [troot] varchar(20),  [tdef] VARCHAR(120),  [fouthroot] VARCHAR(20),  [fouthdef] VARCHAR(120),  [course] VARCHAR(120),  [list] VARCHAR(20),  [level] VARCHAR(20),  [review] VARCHAR(20),[correct] VARCHAR(20))";
		database.execSQL(sql);

		database.close();

	}

	public boolean levelexist(String level) {
		SQLiteDatabase database = null;
		boolean flag = false;
		String levelscore = "";
		String sql = "select score  from score_wrong where course='"
				+ myapp.get(0) + "'and list=" + myapp.get(2) + " and level="
				+ level;

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					String colums_value = cursor.getString(cursor
							.getColumnIndex(colums_name));

					levelscore = colums_value;

				}
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		if (levelscore.equals("")) {
			flag = false;
		}
		if (!levelscore.equals("")) {
			flag = true;
		}

		return flag;

	}

	// select name from sqlite_master where type='table' and name='LSAT'

	public boolean coursexist(String tablename) {
		SQLiteDatabase database = null;
		boolean flag = false;
		String course = "";
		String sql = "select  name  from sqlite_master where type='table' and name= '"
				+ tablename + "_wrong'";

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					String colums_value = cursor.getString(cursor
							.getColumnIndex(colums_name));

					course = colums_value;

				}
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		if (course.equals("")) {
			flag = false;
		}
		if (!course.equals("")) {
			flag = true;
		}

		return flag;

	}

	public int numlist(String tablename) {

		SQLiteDatabase database = null;

		int number = 0; // 用来储存有多少个字在表里
		String sql = "select count(*) from " + "'" + tablename + "'";

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					int colums_value = cursor.getInt(cursor
							.getColumnIndex(colums_name));

					number = colums_value;

				}
			}

		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		return number / listwordnum; // 因为每个表至少包含5个单词所以必须有1个list 所以加1是正确的

	}

	public void cleantdata() {
		SQLiteDatabase database = null;
		database = helper.getWritableDatabase();
		database.delete(myapp.get(0) + "_wrong", "name=? ", new String[] { "" });
		database.close();

	}

	public String[][] getwrongwords(String key) { // 取得20个单词 key为review值。

		String[][] words = new String[21][10];

		for (int i = 0; i < 21; i++) { // 这里是初始化各个单词为""；

			for (int j = 0; j < 10; j++) {

				words[i][j] = "";
			}
		}

		String tablename = myapp.get(0);
		SQLiteDatabase database = null;
		int k = 0; // 二维字符串数组第一参数
		String sql = "select name, def, froot,fdef,sroot,sdef,troot,tdef,fouthroot,fouthdef from "
				+ tablename
				+ "_wrong"
				+ " where list= "
				+ myapp.get(2)
				+ " and level="
				+ myapp.get(3)
				+ " and review="
				+ key
				+ " and course like" + "'%" + myapp.get(0) + "%'";

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					String colums_value = cursor.getString(cursor
							.getColumnIndex(colums_name));

					if (colums_value == null) {
						colums_value = "";
					}

					if (colums_name.equals("name")) {
						words[k][i] = colums_value;

					}

					if (colums_name.equals("def")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("froot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("fdef")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("sroot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("sdef")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("troot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("tdef")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("fouthroot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("fouthdef")) {
						words[k][i] = colums_value;
					}

				}
				k++;
			}
		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		return words;

	}

	public String[][] getwords() { //take 20 words  取得20个单词

		words = new String[21][10];

		for (int i = 0; i < 21; i++) { // initial array like "" 这里是初始化各个单词为""；

			for (int j = 0; j < 10; j++) {

				words[i][j] = "";
			}
		}

		int listnum = Integer.parseInt(myapp.get(2)); // get the number of list 取得几号表，用来计算取单词

		int numlist = myapp.getlistnum(); // get how many list in the course  一共有多少list

		if (listnum == numlist) {
			listnum = 0;
		}

		String tablename = myapp.get(0); // course name
		SQLiteDatabase database = null;
		int k = 0; // 二维字符串数组第一参数
		String sql = "select name, def, froot,fdef,sroot,sdef,troot,tdef,fouthroot,fouthdef from "
				+ tablename + " where id% " + numlist + " = " + listnum;

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();

			while (cursor.moveToNext()) { // 标准形式，否则cursor游标无效
				for (int i = 0; i < colums; i++) {
					String colums_name = cursor.getColumnName(i);
					String colums_value = cursor.getString(cursor
							.getColumnIndex(colums_name));

					if (colums_value == null) {
						colums_value = "";
					}

					if (colums_name.equals("name")) {
						words[k][i] = colums_value;

					}

					if (colums_name.equals("def")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("froot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("fdef")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("sroot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("sdef")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("troot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("tdef")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("fouthroot")) {
						words[k][i] = colums_value;
					}
					if (colums_name.equals("fouthdef")) {
						words[k][i] = colums_value;
					}

				}
				k++;
			}
		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			if (database != null)
				database.close();
		}

		return words;

	}
}
