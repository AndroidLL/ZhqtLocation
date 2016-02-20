package zhqt.lmw.function;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import zhqt.lmw.zhqtlocation.R;
import zhqt.lmw.zhqtlocationTool.GetHttp;
import zhqt.lmw.zhqtlocationTool.OnChangedListener;
import zhqt.lmw.zhqtlocationTool.SlipButton;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnChangedListener
{

	//设备编号 
	private EditText id;
	//设备电量
	private EditText et_electric;
	//上报时间
	private EditText et_time;
	//超速值
	private EditText et_outSpeedValue;
	//移动距离
	private EditText et_distance_move_value;
	//绑定号码
	private EditText et_bundle_phone_value;
	//gps模式
	private SlipButton slipButton;
	//电话应答模式
	private SlipButton slipButtonphone;
	//时区
	private SlipButton slipButton_time;
	//超速报警
	private SlipButton slipButton_speed;
	//移动报警
	private SlipButton slipButton_sos_move;
	//震动报警
	private SlipButton slipButton_sos_shock;
	
	private String equipment_id,electric,time,outSpeedValue, distance_move_value,bundle_phone_value;
	
	public Button bt_save;
	
	private String state_gps;
	
	private String state_phone;
	private SharedPreferences preferences;
	private String eString;
	private String msleepreport;
	private String i;
	private Map<String,String> m;
	private String mgpsmode;
	private String mcallringmode;
	private String mspeedenable;
	private String mmoveenable;
	private String mshockenable;
	private String mspeedmax;
	private String mmovedistance;
	private String bindnumber;
	private String mtimezone;
	
	private ArrayList<Map<String, String>> arrayList;
	private String tag = "SettingActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_setting);
	
		init();
	}

	private void init() 
	{
		id = (EditText)findViewById(R.id.et_id);
		et_bundle_phone_value= (EditText)findViewById(R.id.et_bundle_phone_value);
		et_distance_move_value = (EditText)findViewById(R.id.et_distance_move_value);
		et_electric = (EditText)findViewById(R.id.et_electric);
		et_outSpeedValue = (EditText)findViewById(R.id.et_outSpeedValue);
		et_time = (EditText)findViewById(R.id.et_time);
		
		slipButton = (SlipButton)findViewById(R.id.slipButton);
		slipButton_sos_move = (SlipButton)findViewById(R.id.slipButton_sos_move);
		slipButton_sos_shock = (SlipButton)findViewById(R.id.sos_shock);
		slipButton_speed = (SlipButton)findViewById(R.id.slipButton_speed);
		slipButton_time = (SlipButton)findViewById(R.id.slipButton_time);
		slipButtonphone = (SlipButton)findViewById(R.id.slipButton_phone);
		
		bt_save = (Button)findViewById(R.id.bt_save);
		
		
		slipButton.SetOnChangedListener("mgspmodel", this);
		slipButton_sos_move.SetOnChangedListener("mmoveenable", this);
		slipButton_sos_shock.SetOnChangedListener("mshockenable", this);
		slipButton_speed.SetOnChangedListener("mspeedenable", this);
		slipButtonphone.SetOnChangedListener("mgspmodel", this);
	       
		
		
//	     button.setEnabled(false);//����button������
		slipButton.setChecked(false);//����button�ĳ�ʼ��״̬Ϊ��	
	
	
	
	
   }

	public void OnChanged(String strName, boolean CheckState) 
	{
		if(CheckState)
		{
			i = "1";
			Toast.makeText(SettingActivity.this, strName+" is opened!", Toast.LENGTH_LONG).show();
		}
		else
		{
			i = "0";
			Toast.makeText(SettingActivity.this, strName+" is closed!", Toast.LENGTH_LONG).show();
			 
		}
		 m = new IdentityHashMap<String, String>();
	     m.put(strName,i);
	     Log.e(tag, "strName = "+strName +"i = "+ i);
	
	     arrayList= new ArrayList<Map<String,String>>();
	     arrayList.add(m);
	}
	
	
	public void bt_save(View v)
	{
		
		/*
		  String equipment_id,electric,time,outSpeedValue, 
		  distance_move_value,bundle_phone_value;
		 */
		preferences = getSharedPreferences("SpecificName",
				Context.MODE_PRIVATE);
		eString = preferences.getString("specificname", "");
		 Log.e("获取的sn",eString);
		
		 msleepreport = et_time.getText().toString().trim();
		 
		 for(String key : m.keySet())
		 {
			 mgpsmode = m.get("mgspmodel");
			 mcallringmode= m.get("mcallringmode");
			 mspeedenable = m.get("mspeedenable");
			 mmoveenable = m.get("mmoveenable");
			 mshockenable = m.get("mshockenable");
		 }
		 
		 mspeedmax = et_outSpeedValue.getText().toString().trim();
		 mmovedistance = et_distance_move_value.getText().toString().trim();
		 bindnumber = et_bundle_phone_value.getText().toString().trim();
		 mtimezone = "12";
		 Log.e(tag , "=="+"eString ="+eString +"msleepreport = "+msleepreport
				 +"mgpsmode = "+ mgpsmode+" mcallringmode = "+mcallringmode+"mtimezone = "+mtimezone
				 +"mspeedenable = "+mspeedenable+"mshockenable = "+mshockenable +"mspeedmax = "+mspeedmax);
		 
		GetHttp.alter(eString,msleepreport,mgpsmode,mcallringmode,
				mtimezone,mspeedenable,mspeedmax,mmoveenable,mmovedistance,
				mshockenable,bindnumber);
		
		Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
		
	}
}
