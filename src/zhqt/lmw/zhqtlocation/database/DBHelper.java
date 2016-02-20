package zhqt.lmw.zhqtlocation.database;

import zhqt.lmw.zhqtlocation.database.DBUser.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


/**
 * ������ݿ⸨����?
 * 
 */
public class DBHelper
{
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "zhp.db";
	public static final String USER_TABLE_NAME = "user_table";
	public static final String[] USER_COLS = { User.USERNAME, User.PASSWORD,
			User.ISSAVED, User.ISFLAG };
	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;

	public DBHelper(Context context) 
	{
		this.dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	/**
	 * ����ݿ�?
	 */
	private void establishDb() 
	{
		if (this.db == null) 
		{
			this.db = this.dbOpenHelper.getWritableDatabase();
		}
	}

	/**

	 */
	public long insertOrUpdate(String userName, String password, int isSaved, int isFlag) 
	{
		boolean isUpdate = false;
		String[] usernames = queryAllUserName();
		
		for (int i = 0; i < usernames.length; i++) 
		{
			
			if (userName.equals(usernames[i])) 
			{
				
				isUpdate = true;
				break;
				
			}
		}
		long id = -1;
		
		if (isUpdate) 
		{
			
			id = update(userName, password, isSaved, isFlag);
			
		} else 
		{
			if (db != null)
			{
				ContentValues values = new ContentValues();
				values.put(User.USERNAME, userName);
				values.put(User.PASSWORD, password);
				values.put(User.ISSAVED, isSaved);
				values.put(User.ISFLAG, isFlag);
				id = db.insert(USER_TABLE_NAME, null, values);
			}
		}
		return id;
	}
	
	public void updateFlag()
	{
	
		try
		{
			
			establishDb();
			db.execSQL(" update " + USER_TABLE_NAME + " set " + User.ISFLAG + " = " +  0 ); 	
		}finally
		{
			;//cleanup();
		}
		
	}
	
	public void updateCurrentFlag(String userName)
	{
		
		
			establishDb();
			db.execSQL(" update " + USER_TABLE_NAME + " set " + User.ISFLAG + " = " +  1  
			+ " where " + User.USERNAME + " = '" + userName + "' ");
			Log.e("<><><>", "flag"+" update " + USER_TABLE_NAME + " set " + User.ISFLAG + " = " +  1  
					+ " where " + User.USERNAME + " = '" + userName + "' ");
			
	}
		
		

	public int queryIsFlag(String username)
	{
		String sql = " select * from " + USER_TABLE_NAME + " where "
				+ User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		int i = 0 ;
		if (cursor.getCount() > 0) 
		{
			cursor.moveToFirst();
			i = cursor.getInt(cursor.getColumnIndex(User.ISFLAG));
			
			Log.e("真OOOOOOO", "sfsfdfsfsd"+i);
			Log.e("真OOOOOOO", "sfsfdfsfsd"+" select * from " + USER_TABLE_NAME + " where "
					+ User.USERNAME + " = '" + username + "'");
		}

		return i;
	}
	
	/**
	 * ɾ��һ����?
	 * 
	 * @param userName
	 *            �û���
	 * @param tableName
	 *            ����
	 * @return ɾ���¼��id -1��ʾɾ��ɹ�?
	 */
	public long delete(String userName) 
	{
		long id = db.delete(USER_TABLE_NAME, User.USERNAME + " = '" + userName
				+ "'", null);
		return id;
	}

	/**
	 * ����һ����?
	 * 
	 * @param password2
	 * 
	 * @param
	 * 
	 * @param tableName
	 *            ����
	 * @return ���¹���¼��id -1��ʾ���²��ɹ�
	 */
	public long update(String username, String password, int isSaved, int isFlag)
	{
		ContentValues values = new ContentValues();
		values.put(User.USERNAME, username);
		values.put(User.PASSWORD, password);
		values.put(User.ISSAVED, isSaved);
		values.put(User.ISFLAG, isFlag);
		long id = db.update(USER_TABLE_NAME, values, User.USERNAME + " = '"
				+ username + "'", null);
		return id;
	}

	/**
	 * ����û����ѯ����
	 * 
	 * @param username
	 *            �û���
	 * @param tableName
	 *            ����
	 * @return Hashmap ��ѯ�ļ�¼
	 */
	public String queryPasswordByName(String username)
	{
		String sql = "select * from " + USER_TABLE_NAME + " where "
				+ User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		String password = "";
		if (cursor.getCount() > 0) 
		{
			cursor.moveToFirst();
			password = cursor.getString(cursor.getColumnIndex(User.PASSWORD));
		}

		return password;
	}

	public String queryNameByFlag(int flag)
	{
		//establishDb();
		String sql = " select * from " + USER_TABLE_NAME + " where "
				+User.ISFLAG + " =  " + flag ;
		Cursor cursor = db.rawQuery(sql, null);
		String name = "";
		
		if(cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			name = cursor.getString(cursor.getColumnIndex(User.USERNAME));
		}
		
		return name;
		
	}
	
	
	/**
	 * ��ס����ѡ����Ƿ�ѡ��?
	 * 
	 * @param username
	 * @return
	 */
	public int queryIsSavedByName(String username) {
		String sql = "select * from " + USER_TABLE_NAME + " where "
				+ User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		int isSaved = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			isSaved = cursor.getInt(cursor.getColumnIndex(User.ISSAVED));
		}
		return isSaved;
	}

	/**
	 * ��ѯ�����û���
	 * 
	 * @param tableName
	 *            ����
	 * @return ���м�¼��list����
	 */
	public String[] queryAllUserName() 
	{
		
		if (db != null) 
		{
			Cursor cursor = db.query(USER_TABLE_NAME, null, null, null, null,
					null, null);
			int count = cursor.getCount();
			String[] userNames = new String[count];
			if (count > 0) 
			{
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) 
				{
					userNames[i] = cursor.getString(cursor
							.getColumnIndex(User.USERNAME));
					cursor.moveToNext();
				}
			}
			return userNames;
		} 
		else 
		{
			return new String[0];
		}

	}
	
	public String queryName()
	{
		
		/*String sql = " select * from " + USER_TABLE_NAME
				+ " where ip_addr = '" + ip + "'";*/
		Cursor cursor = null;
		String sql = "select" + User.USERNAME +"from" +USER_TABLE_NAME ;
		
		 cursor = db.rawQuery(sql, null);
		
		String username = "";
		if(cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			username = cursor.getString(cursor.getColumnIndex(User.USERNAME));
			Log.e("", "name"+ username);
		}
		
		return username;
	}

	/**
	 * �ر���ݿ�?
	 */
	public void cleanup() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	

	// 通过名字删除�?
	public void delTbByName(String tbName) 
	{
		String sql = "DROP TABLE " + tbName;
		try {
			establishDb();
			db.execSQL(sql);
		} finally {
			cleanup();
		}
	}
	
	/**
	 * ��ݿ⸨����?
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper
	{

		public DBOpenHelper(Context context) 
		{
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("create table " + USER_TABLE_NAME + " ("
					+ BaseColumns._ID + " integer primary key," + User.USERNAME
					+ " text, "  + User.PASSWORD + " text, "
					+ User.ISSAVED + " INTEGER," + User.ISFLAG + " INTEGER) ");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
			onCreate(db);
		}

	}

}
