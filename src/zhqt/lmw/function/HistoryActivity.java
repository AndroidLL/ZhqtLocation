package zhqt.lmw.function;


import java.util.ArrayList;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import zhqt.lmw.zhqtlocation.R;
import zhqt.lmw.zhqtlocation.entity.Location;
import zhqt.lmw.zhqtlocationTool.Utile;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HistoryActivity extends FragmentActivity
{
	private AMap aMap;
	private Button replayButton;
	private ArrayList<Location> arrayList = new ArrayList<Location>();
	private Intent intent;
	private SeekBar processbar;
	private Marker marker = null;
	public Handler timer = new Handler();
	public Runnable runnable = null;
	private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
	private ArrayList<LatLng> latlngList_path = new ArrayList<LatLng>();
	private static final LatLng marker1 = new LatLng(39.24426, 100.18322);
	private static final LatLng marker2 = new LatLng(39.24426, 104.18322);
	private static final LatLng marker3 = new LatLng(39.24426, 108.18322);
	private static final LatLng marker4 = new LatLng(39.24426, 112.18322);
	private static final LatLng marker5 = new LatLng(39.24426, 116.18322);
	private static final LatLng marker6 = new LatLng(36.24426, 100.18322);
	private static final LatLng marker7 = new LatLng(36.24426, 104.18322);
	private static final LatLng marker8 = new LatLng(36.24426, 108.18322);
	private static final LatLng marker9 = new LatLng(36.24426, 112.18322);
	private static final LatLng marker10 = new LatLng(36.24426, 116.18322);
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		context = this;
		init();
		float f = 0;
		// 
		for (int i = 0; i < latlngList.size() - 1; i++) 
		{
			f += AMapUtils.calculateLineDistance(latlngList.get(i),
					latlngList.get(i + 1));
		}
		Log.e("float", String.valueOf(f / 1000));
	}
	
	/**
	 * ��ʼ��AMap����
	 */
	private void init() 
	{
		
		
		replayButton = (Button) findViewById(R.id.btn_replay);
		processbar = (SeekBar) findViewById(R.id.process_bar);
		processbar.setSelected(false);
		processbar.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
			}
		});
		processbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{

				
				latlngList_path.clear();
				if (progress != 0)
				{
					for (int i = 0; i < seekBar.getProgress(); i++) 
					{
						latlngList_path.add(latlngList.get(i));
					}
					drawLine(latlngList_path, progress);
				}
			
				try 
				{
					Thread.sleep(100);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{
				
				latlngList_path.clear();
				int current = seekBar.getProgress();
				if (current != 0)
				{
					for (int i = 0; i < seekBar.getProgress(); i++)
					{
						latlngList_path.add(latlngList.get(i));
					}
					drawLine(latlngList_path, current);
				}
			}
		});
		
		runnable = new Runnable()
		{
			@Override
			public void run() 
			{
				handler.sendMessage(Message.obtain(handler, 1));
			}
		};
		if (aMap == null) 
		{
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.maps)).getMap();
			aMap.getUiSettings().setCompassEnabled(false);
			if (aMap != null) 
			{
				setUpMap();
			}
		}
	}

	private void drawLine(ArrayList<LatLng> list,int current)
	{
		aMap.clear();
		LatLng replayGeoPoint = latlngList.get(current - 1);
		if (marker != null) 
		{
			marker.destroy();
		}
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions
		.position(replayGeoPoint)
		.title("起点")
		.snippet(" ")
		.icon(BitmapDescriptorFactory
				.fromBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.car)))
						.anchor(0.5f, 0.5f);
		marker = aMap.addMarker(markerOptions);
		aMap.addMarker(new MarkerOptions()
		.position(latlngList.get(0))
		.title("终点")
		.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(
						getResources(),
						R.drawable.nav_route_result_start_point))));
		if (latlngList_path.size() > 1) {
			PolylineOptions polylineOptions = (new PolylineOptions())
					.addAll(latlngList_path)
					.color(Color.rgb(9, 129, 240)).width(6.0f);
			aMap.addPolyline(polylineOptions);
		}
		if (latlngList_path.size() == latlngList.size()) {
			aMap.addMarker(new MarkerOptions()
					.position(latlngList.get(latlngList.size() - 1))
					.title("�յ�")
					.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
							.decodeResource(
									getResources(),
									R.drawable.nav_route_result_end_point))));
		}
	}
	
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				int curpro = processbar.getProgress();
				if (curpro != processbar.getMax()) {
					processbar.setProgress(curpro + 1);
					timer.postDelayed(runnable, 500);// �ӳ�0.5������ִ��
				} else {
					Button button = (Button) findViewById(R.id.btn_replay);
					button.setText(" �ط� ");// ��ִ�е����һ������ ֹͣ����
				}
			}
		}
	};

	private void setUpMap() 
	{
		//得到数据并娶到latlng数据
		intent = getIntent();
		arrayList = (ArrayList<Location>) intent.getSerializableExtra("History_Data");
		Log.e("",
				"ppppppp" + arrayList.get(0).getAddr()
						+ arrayList.get(1).getAddr());
		latlngList = Utile.getTrack(arrayList);
		latlngList.add(marker1);
		latlngList.add(marker2);
		latlngList.add(marker3);
		latlngList.add(marker4);
		latlngList.add(marker5);
		latlngList.add(marker6);
		latlngList.add(marker7);
		latlngList.add(marker8);
		latlngList.add(marker9);
		latlngList.add(marker10);
		processbar.setMax(latlngList.size());
		aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngList.get(0), 14));
	}

	
	public void btn_replay_click(View v) 
	{
		if (replayButton.getText().toString().trim().equals("回放")) 
		{
			if (latlngList.size() > 0) 
			{
				if (processbar.getProgress() == processbar.getMax()) 
				{
					processbar.setProgress(0);
				}
				replayButton.setText("暂停");
				timer.postDelayed(runnable, 10);
			}
		} else 
		{
			timer.removeCallbacks(runnable);
			replayButton.setText("回放");
		}
	}

}
