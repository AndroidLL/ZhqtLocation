package zhqt.lmw.function;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import zhqt.lmw.zhqtlocation.R;
import zhqt.lmw.zhqtlocation.entity.Location;
import zhqt.lmw.zhqtlocationTool.GetHttp;
import zhqt.lmw.zhqtlocationTool.Utile;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class Time_choseActivity extends Activity implements View.OnTouchListener
{
		private EditText etStartTime; 
	    private EditText etEndTime; 
	    public  String time_start = null;
	    public String time_end = null;
	    public String eString;
	    public ProgressDialog progDialog;
	    private Button btn_submit;
		String history_Path = "http://ttgps.net:8080/JsonWeb/history";
		protected static final int PAGES_MESSAGE = 1000;
		protected static final int HISTORY_MESSAGE = 2000;
		protected String tag = "Time_choseActivity";
		private SharedPreferences preferences;
		private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
	    
		public Handler handler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				switch (msg.what) 
				{
				case HISTORY_MESSAGE:
					if("err".equals(msg.obj))
					{
						dissmissProgressDialog();
						Toast.makeText(getApplicationContext(), "该时间段内无记录！", Toast.LENGTH_SHORT).show();
					}else
					{
						dissmissProgressDialog();
						String getResult = (String)msg.obj;
						ArrayList<Location> arrayList = Utile.getLocation(getResult);
						Log.e(tag, ""+arrayList.size());
						Intent intent = new Intent();
						intent.putExtra("History_Data", arrayList);
						intent.setClass(getApplicationContext(), HistoryActivity.class);
						startActivity(intent);
					}	
					
					break;
				default:
					break;
				}
			};
		};
		
	    @Override
		public void onCreate(Bundle savedInstanceState) 
	    { 
	        super.onCreate(savedInstanceState); 
	        setContentView(R.layout.time_chose); 
	           
	        etStartTime = (EditText) this.findViewById(R.id.et_start_time); 
	        etEndTime = (EditText) this.findViewById(R.id.et_end_time); 
	        btn_submit = (Button)findViewById(R.id.button_submit);
	        etStartTime.setOnTouchListener(this); 
	        etEndTime.setOnTouchListener(this); 
	           
	    } 

		@Override 
	    public boolean onTouch(View v, MotionEvent event) 
	    { 
	        if (event.getAction() == MotionEvent.ACTION_DOWN) 
	        { 
	   
	            AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	            View view = View.inflate(this, R.layout.date_time, null); 
	            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker); 
	            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker); 
	            builder.setView(view); 
	   
	            Calendar cal = Calendar.getInstance(); 
	            cal.setTimeInMillis(System.currentTimeMillis()); 
	            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null); 
	   
	            timePicker.setIs24HourView(true); 
	            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY)); 
	            timePicker.setCurrentMinute(Calendar.MINUTE); 
	   
	            if (v.getId() == R.id.et_start_time) 
	            { 
	                final int inType = etStartTime.getInputType(); 
	                etStartTime.setInputType(InputType.TYPE_NULL); 
	                etStartTime.onTouchEvent(event); 
	                etStartTime.setInputType(inType); 
	                etStartTime.setSelection(etStartTime.getText().length()); 
	                   
	                builder.setTitle("选取起始时间"); 
	                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener()
	                { 
	   
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) 
	                    { 
	   
	                        StringBuffer sb = new StringBuffer(); 
	                        sb.append(String.format("%d-%02d-%02d",  
	                                datePicker.getYear(),  
	                                datePicker.getMonth() + 1, 
	                                datePicker.getDayOfMonth())); 
	                        sb.append("  "); 
	                        sb.append(timePicker.getCurrentHour()) 
	                        .append(":").append(timePicker.getCurrentMinute())
	                        .append(":").append(0).append(0); 
	                        
	                        etStartTime.setText(sb); 
	                      
	                        etEndTime.requestFocus(); 
	                           
	                        dialog.cancel(); 
	                    } 
	                }); 
	                   
	            } else if (v.getId() == R.id.et_end_time) 
	            { 
	                int inType = etEndTime.getInputType(); 
	                etEndTime.setInputType(InputType.TYPE_NULL);     
	                etEndTime.onTouchEvent(event); 
	                etEndTime.setInputType(inType); 
	                etEndTime.setSelection(etEndTime.getText().length()); 
	   
	                builder.setTitle("选取结束时间"); 
	                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() 
	                { 
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which)
	                    { 
	   
	                        StringBuffer sb = new StringBuffer(); 
	                        sb.append(String.format("%d-%02d-%02d",  
	                                datePicker.getYear(),  
	                                datePicker.getMonth() + 1,  
	                                datePicker.getDayOfMonth())); 
	                        sb.append("  "); 
	                        sb.append(timePicker.getCurrentHour()) 
	                        .append(":").append(timePicker.getCurrentMinute())
	                        .append(":").append(0).append(0);
	                        etEndTime.setText(sb); 
	                           
	                        dialog.cancel(); 
	                    } 
	                }); 
	            } 
	               
	            Dialog dialog = builder.create(); 
	            dialog.show(); 
	        } 
	   
	        return true; 
	    } 
	
	    public void btn_submit_click(View v)
		{
	    	
              Log.e("获取的文本时间",etStartTime.getText().toString());
              Log.e("获取的文本时间",etEndTime.getText().toString());
		if(TextUtils.isEmpty(etStartTime.getText().toString().trim())&&TextUtils.isEmpty(etEndTime.getText().toString().trim())) 
		{
			Toast.makeText(Time_choseActivity.this, "请选择时间段", Toast.LENGTH_SHORT).show();
		}else
		{
			
			
			
		time_start = etStartTime.getText().toString();
		
		time_end = etEndTime.getText().toString();
		
		preferences = getSharedPreferences("SpecificName",
				Context.MODE_PRIVATE);
		eString = preferences.getString("specificname", "");
		 Log.e("获取的sn",eString);
		
		//开启线程去获取数据
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				String histories = GetHttp.location_history(eString, time_start, time_end, history_Path);
				Log.e(tag , "haha 再查一遍" + eString+ "w"+ history_Path+"\\"+
				time_start+"\\"
						+time_end);
				Log.e(tag , "haha 得到的历史数据" + histories.length()+ "路径："+history_Path);
				
				Message message = Message.obtain(handler, HISTORY_MESSAGE, histories);
				handler.sendMessage(message);

			}
		});
		thread.start();
		
		showProgressDialog();
		
		}
			
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
