package zhqt.lmw.zhqtlocation.database;

import android.provider.BaseColumns;

/**
 * BaseColumns �ṩ��һ�޶���ID�ͷ�������
 */
public final class DBUser 
{

	public static final class User implements BaseColumns 
	{
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
		public static final String ISSAVED = "issaved";
		public static final String ISFLAG = "isflag";
	}

}
