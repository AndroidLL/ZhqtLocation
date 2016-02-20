package zhqt.lmw.zhqtlocation;

import zhqt.lmw.zhqtlocation.entity.Name;
import zhqt.lmw.zhqtlocationTool.GetHttp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ModelActivity extends Activity implements OnClickListener 
{
	protected static final int RECODE = 3000;
	private long waitTime = 0;
	private LinearLayout GPSlayout;
	private LinearLayout BLElayout;
	String Equipment_path = "http://ttgps.net:8080/JsonWeb/equipment";
	public SharedPreferences preferences;
	public Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case RECODE:
				preferences = getSharedPreferences("test", Context.MODE_APPEND);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("Equipment", (String) msg.obj);
				editor.commit();
				TrunDevice();
				break;

			default:
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.model_selection);
		init();
		SysApplication.getInstance().addActivity(this);

	}

	private void init() 
	{
		GPSlayout = (LinearLayout) findViewById(R.id.panel);
		BLElayout = (LinearLayout) findViewById(R.id.panel2);

		GPSlayout.setOnClickListener(this);
		BLElayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.panel:
			// Equipment information
			Equipment_information(Name.getName(), Equipment_path);
			break;
		case R.id.panel2:
			TrunBLE();
			break;

		default:
			break;
		}
	}

	private void Equipment_information(final String name, String equipment_path) 
	{

		Thread thread = new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{

				String equipment = GetHttp.Equipment(name, Equipment_path,
						"gid");
				Log.e("??????", "" + equipment);
				Message message = Message.obtain(handler, RECODE, equipment);
				handler.sendMessage(message);

			}
		});
		thread.start();

	}

	private void TrunBLE() 
	{

	}

	private void TrunDevice() 
	{
		Intent intent = new Intent();
		intent.setClass(ModelActivity.this, DeviceActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) 
		{

			if (System.currentTimeMillis() - waitTime > 2000) 
			{

				Toast.makeText(
						getApplicationContext(),
						"再按一次退出"
								+ getPackageManager().getApplicationLabel(
										getApplicationInfo()),
						Toast.LENGTH_SHORT).show();

				waitTime = System.currentTimeMillis();
			} else {

				SysApplication.getInstance().exit();

			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
