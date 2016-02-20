package zhqt.lmw.zhqtlocation;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import zhqt.lmw.zhqtlocation.database.DBHelper;
import zhqt.lmw.zhqtlocation.entity.Name;
import zhqt.lmw.zhqtlocationTool.GetHttp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


	public class MainActivity extends Activity implements OnClickListener 
	{
		private EditText mUserName;
		private EditText mPassword;
		private Button mLoginButton;
		private ImageButton mDropDown;
		private DBHelper dbHelper;
		private CheckBox mCheckBox;
		private PopupWindow popView;
		private MyAdapter dropDownAdapter;
		Context mContext;
		private int checkFlag;
		private int isFlag;
		private String userName = "";
		private String password = "";
		private Thread mThread;
		private URI uri;
		private JSONArray array;
		
		protected String tag = "MainActivity";
		private ProgressDialog progDialog = null;// 搜索时进度条
		String path = "http://ttgps.net:8080/JsonWeb/login.action";
		String Equipment_path = "http://ttgps.net:8080/JsonWeb/Equipment";
		
		
		
		private static final int RIGHT = 1001;
		private static final int ERROR = 1002;
		
		private Handler mHandler = new Handler()
		{
			

			@Override
			public void handleMessage(android.os.Message msg)
			{
				
				switch (msg.what) 
				{
				case RIGHT:
					dissmissProgressDialog();
					Turn();
					
					break;
				case ERROR:
					Toast.makeText(MainActivity.this, "请核对帐号",
							Toast.LENGTH_SHORT).show();
					dissmissProgressDialog();
					
					break;
					
				}
			}

		};
	
		
		
	
		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
		
			SysApplication.getInstance().addActivity(this);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.fragment_main);
			mContext = this;
			initWidget();
		}



		private void initWidget() 
		{
			
			dbHelper = new DBHelper(this);
			mUserName = (EditText) findViewById(R.id.username);
			mPassword = (EditText) findViewById(R.id.password);
			mLoginButton = (Button) findViewById(R.id.login);
			mDropDown = (ImageButton) findViewById(R.id.dropdown_button);
			mCheckBox = (CheckBox) findViewById(R.id.remember);
			mLoginButton.setOnClickListener(this);
			mDropDown.setOnClickListener(this);
			initLoginUserName();

		}

		private void initLoginUserName() 
		{
			String[] usernames = dbHelper.queryAllUserName();
			Log.e("��ʼ��", "��"+ usernames.length);
			if (usernames.length > 1) 
			{
				
				isFlag = 1;
				String tempName = dbHelper.queryNameByFlag(isFlag);
				
				mUserName.setText(tempName);
				mUserName.setSelection(tempName.length());
				String tempPwd = dbHelper.queryPasswordByName(tempName);
				checkFlag = dbHelper.queryIsSavedByName(tempName);

				if (checkFlag == 0) 
				{
					
					mCheckBox.setChecked(false);
					
				} else if (checkFlag == 1) 
				{
					
					mCheckBox.setChecked(true);
				
				}
				mPassword.setText(tempPwd);

			}else if(usernames.length == 1)
			{
				String tempName = usernames[0];
				mUserName.setText(tempName);
				mUserName.setSelection(tempName.length());
				String tempPwd = dbHelper.queryPasswordByName(tempName);
				checkFlag = dbHelper.queryIsSavedByName(tempName);

				if (checkFlag == 0) 
				{
					
					mCheckBox.setChecked(false);
					
				} else if (checkFlag == 1) 
				{
					
					mCheckBox.setChecked(true);
				
				}
				mPassword.setText(tempPwd);

			}else
			{
				mUserName.setText("");
				mPassword.setText("");
			}
			
			
			
			mUserName.addTextChangedListener(new TextWatcher() 
			{

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) 
				{
					
					mPassword.setText("");
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) 
				{

				}

				@Override
				public void afterTextChanged(Editable s) 
				{
					
				}
			});
		}

		@Override
		public void onClick(View v) 
		{

			switch (v.getId())
			{
			case R.id.login:
				
				  userName = mUserName.getText().toString();
				  password = mPassword.getText().toString();

				  Name.setName(userName);
				  if(userName.length() <= 0)
				  {
					  Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
					  mPassword.setText("");
				  }else if(password.isEmpty())
				  {
					  Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
					  mUserName.setText("");
					  
				  }else if(userName.length()==6 && !password.isEmpty())
				  {
					  showProgressDialog();
					mThread = new Thread(new Runnable() 
					{
						boolean result;
						@Override
						public void run() 
						{
							
							try 
							{
								
								result = GetHttp.getHttpClient(path,userName, password);
								Log.e(tag, "login_result = "+ result);
							} catch (Exception e) 
							{
								e.printStackTrace();
							}
							
							if(result)
							{
								
								Message message = Message.obtain();
								message.what = RIGHT;
								mHandler.sendMessage(message);
								
							}
							else
							{
								
								Message message = Message.obtain();
								message.what = ERROR;
								mHandler.sendMessage(message);
							}
						}
					});
						
					 mThread.start();
					  
					  
				  }
				  
				  
				if (mCheckBox.isChecked())
				{
					
					dbHelper.insertOrUpdate(userName, password, 1, 1);
					
				} else if(!mCheckBox.isChecked())
				{
					
					dbHelper.insertOrUpdate(userName, "", 0, 1);
					
				}else
				{
					mPassword.setText("");
				}
				
				dbHelper.updateFlag();
				dbHelper.updateCurrentFlag(userName);
				int w  = dbHelper.queryIsFlag(userName);
				
				break;
				
			case R.id.dropdown_button:
				
				if (popView != null) 
				{
					if (!popView.isShowing()) 
					{
					
						popView.showAsDropDown(mUserName);
					
					} else 
					{
						
						popView.dismiss();
					
					}
				} else 
				{
					if (dbHelper.queryAllUserName().length > 0) 
					{

						initPopView(dbHelper.queryAllUserName());
					
						if (!popView.isShowing()) 
						{
						
							popView.showAsDropDown(mUserName);
						
						} else 
						{
						
							popView.dismiss();
						
						}
					} else 
					{
					
						Toast.makeText(this, "�޼�¼", Toast.LENGTH_SHORT).show();
				
					}

				}
				break;
			}
		}


		/**
		 * ת���豸�б�
		 * ��ҪЯ������
		 */
		protected void Turn() 
		{
			
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ModelActivity.class);
			startActivity(intent);
			this.finish();
			
			
		}
		
		
		@Override
		protected void onResume() 
		{
			super.onResume();
		
		
		}

		@Override
		protected void onPause() 
		{
			super.onPause();
		}

	
		private void initPopView(String[] usernames)
		{
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < usernames.length; i++) 
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", usernames[i]);
				Log.e("douwo", "����"+usernames[i]);
				map.put("drawable", R.drawable.xicon);
				list.add(map);
			}
			
			dropDownAdapter = new MyAdapter(this, list, R.layout.dropdown_item,
					new String[] { "name", "drawable" }, new int[] { R.id.textview,
							R.id.delete });
			
			ListView listView = new ListView(this);
			listView.setAdapter(dropDownAdapter);

			popView = new PopupWindow(listView, mUserName.getWidth(),
					ViewGroup.LayoutParams.WRAP_CONTENT, true);
			popView.setFocusable(true);
			popView.setOutsideTouchable(true);
			popView.setBackgroundDrawable(getResources().getDrawable(
					R.color.white));
			// popView.showAsDropDown(mUserName);
		}

		class MyAdapter extends SimpleAdapter 
		{

			private List<HashMap<String, Object>> data;

			public MyAdapter(Context context, List<HashMap<String, Object>> data,
					int resource, String[] from, int[] to) 
			{
				
				super(context, data, resource, from, to);
				
				this.data = data;
			}

			@Override
			public int getCount() 
			{
				
				return data.size();
			}

			@Override
			public Object getItem(int position)
			{
				
				return position;
			}

			@Override
			public long getItemId(int position) 
			{
				
				return position;
			}

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) 
			{
				System.out.println(position);
				ViewHolder holder;
				
				if (convertView == null)
				{
					holder = new ViewHolder();

					convertView = LayoutInflater.from(MainActivity.this).inflate(
							R.layout.dropdown_item, null);
					holder.btn = (ImageButton) convertView
							.findViewById(R.id.delete);
					holder.tv = (TextView) convertView.findViewById(R.id.textview);

					convertView.setTag(holder);
				} else 
				{
					
					holder = (ViewHolder) convertView.getTag();
				}
				
				holder.tv.setText(data.get(position).get("name").toString());

				holder.tv.setOnClickListener(new View.OnClickListener() 
				{

					@Override
					public void onClick(View v) 
					{
						
					
						String[] usernames = dbHelper.queryAllUserName();

						mUserName.setText(usernames[position]);
						
						mPassword.setText(dbHelper
								.queryPasswordByName(usernames[position]));
						checkFlag = dbHelper.queryIsSavedByName(usernames[position]);

						if (checkFlag == 0) 
						{
							
							mCheckBox.setChecked(false);
							
						} else if (checkFlag == 1) 
						{
							
							mCheckBox.setChecked(true);
						
						}
						
						popView.dismiss();
						
					}
				});

				holder.btn.setOnClickListener(new View.OnClickListener() 
				{

					@Override
					public void onClick(View v) 
					{
						String[] usernames = dbHelper.queryAllUserName();

						if (usernames.length > 0) 
						{
							
							dbHelper.delete(usernames[position]);
							Toast.makeText(mContext, "ɾ����"+usernames[position],Toast.LENGTH_SHORT ).show();
							popView.dismiss();
						}
						
						String[] newusernames = dbHelper.queryAllUserName();

						if (newusernames.length > 0) 
						{
							
							initPopView(newusernames);
							popView.showAsDropDown(mUserName);
							
						} else 
						{
							
							popView.dismiss();
							popView = null;
							
						}
					}
				});
				return convertView;
			}
		}

		class ViewHolder
		{
			
			private TextView tv;
			private ImageButton btn;
			
		}

		@Override
		protected void onStop() 
		{
			super.onStop();
			dbHelper.cleanup();
			
		}
		
		/**
		 * 显示进度框
		 */
		private void showProgressDialog() {
			if (progDialog == null)
				progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(false);
			progDialog.setMessage("正在连接中...");
			progDialog.show();
		}

		
		/**
		 * 隐藏进度框
		 */
		private void dissmissProgressDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}

}	
