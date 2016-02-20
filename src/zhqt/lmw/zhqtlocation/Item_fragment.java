package zhqt.lmw.zhqtlocation;

import zhqt.lmw.function.HistoryFragment;
import zhqt.lmw.function.MapFragment;
import zhqt.lmw.function.SettingFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class Item_fragment extends Activity implements OnCheckedChangeListener
{

	private RadioButton bt_map;
	private RadioButton bt_history;
	private RadioButton bt_fence;
	private RadioButton bt_setting;
	
	private MapFragment mapFragment;
	private HistoryFragment historyFragment;
	private SettingFragment settingFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_item);
		
		init();
		
	}

	private void init() 
	{
		bt_map = (RadioButton)findViewById(R.id.radio_button0);
		bt_history = (RadioButton)findViewById(R.id.radio_button1);
		bt_fence = (RadioButton)findViewById(R.id.radio_button2);
		bt_setting = (RadioButton)findViewById(R.id.radio_button3);		

		bt_map.setOnCheckedChangeListener(this);
		bt_history.setOnCheckedChangeListener(this);
		bt_fence.setOnCheckedChangeListener(this);
		bt_setting.setOnCheckedChangeListener(this);
		
		//设置默认的fragment
		setDefaultFragment();
		
	}

	private void setDefaultFragment() 
	{
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		settingFragment = new SettingFragment();
		fragmentTransaction.replace(R.id.id_content, settingFragment);
		fragmentTransaction.commit();
		
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		if(isChecked)
		{
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			
			switch (buttonView.getId()) 
			{
			case R.id.radio_button0:
				if(mapFragment == null)
				{
					mapFragment = new MapFragment();
				}
				
				ft.replace(R.id.id_content, mapFragment);
				
				break;
			case R.id.radio_button1:
				if(historyFragment == null)
				{
					historyFragment = new HistoryFragment();
				}
				ft.replace(R.id.id_content, historyFragment);
			
				break;
			case R.id.radio_button2:
				
				break;
			case R.id.radio_button3:
				
				if(settingFragment == null)
				{
					settingFragment = new SettingFragment();
				}
				ft.replace(R.id.id_content, settingFragment);
			
				break;
			}
			ft.commit();
			
		}
	
	}

	
	
}
