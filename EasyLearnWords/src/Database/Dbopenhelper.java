package Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbopenhelper extends SQLiteOpenHelper {

	private static String name = "mydb.db"; // ��ʾ���ݿ�����
	private static int version = 2; // ��ʾ���ݿ�İ汾��

	public Dbopenhelper(Context context) {
		super(context, name, null, version); // �������Ϊ�գ����ݿ���ؽ����ڴ���
		// TODO Auto-generated constructor stub ��ʵ�ϲ�û�д������ݿ⣬ֱ������getwrite����ʱ���Żᴴ��

	}

	public Dbopenhelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	// �����ݿⴴ��ʱ���ǵ�һ�α�ִ�У���ɶ����ݿ��Ĵ���
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// �ı��͵����ݿ�֧�ֵ��������ͣ� �������ݣ��ַ������ͣ��������ͣ������Ƶ���������
		/*
		 * String sql =
		 * "create table person(id integer primary key autoincrement,name varchar(64),address varchar(64))"
		 * ; db.execSQL(sql);
		 */
		System.out.println("database oncreate"); // �ڴ������ݿ������£�oncreate��������

	}

	@Override
	// ���µİ汾�ű��ϵİ汾�Ŵ�ʱ��ִ��������䡣
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		String sql = "";
		db.execSQL(sql);

	}
}
