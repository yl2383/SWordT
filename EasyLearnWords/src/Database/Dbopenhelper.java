package Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbopenhelper extends SQLiteOpenHelper {

	private static String name = "mydb.db"; // 表示数据库名称
	private static int version = 2; // 表示数据库的版本号

	public Dbopenhelper(Context context) {
		super(context, name, null, version); // 如果名字为空，数据库会重建在内存中
		// TODO Auto-generated constructor stub 事实上并没有创建数据库，直到调用getwrite方法时，才会创建

	}

	public Dbopenhelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	// 当数据库创建时，是第一次被执行，完成对数据库表的创建
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 文本型的数据库支持的数据类型： 整型数据，字符串类型，日期类型，二进制的数据类型
		/*
		 * String sql =
		 * "create table person(id integer primary key autoincrement,name varchar(64),address varchar(64))"
		 * ; db.execSQL(sql);
		 */
		System.out.println("database oncreate"); // 在存在数据库的情况下，oncreate方法不用

	}

	@Override
	// 当新的版本号比老的版本号大时，执行这条语句。
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		String sql = "";
		db.execSQL(sql);

	}
}
