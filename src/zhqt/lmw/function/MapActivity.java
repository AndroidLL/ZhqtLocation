package zhqt.lmw.function;

import java.util.ArrayList;

import zhqt.lmw.zhqtlocation.R;
import zhqt.lmw.zhqtlocation.entity.Location;
import zhqt.lmw.zhqtlocationTool.GetHttp;
import zhqt.lmw.zhqtlocationTool.Utile;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

/**
 * 
 * @author develop
 * 
 */
public class MapActivity extends Activity {
	private MapView mapView;
	private AMap aMap;
	// 当前轨迹点图案
	private Marker marker = null;
	// 定时器
	private Handler timer = new Handler();
	private Handler handler2;
	private SharedPreferences preferences;
	private String eString;

	private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
	private String EquipmentDetails_path = "http://ttgps.net:8080/JsonWeb/location";

	protected static final int TRUEDATE = 10000;
	protected static final String tag = "MapActivity";
	public Handler mHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case TRUEDATE:
				ArrayList<Location> data = (ArrayList<Location>) msg.obj;

				latlngList = Utile.getTrack(data);
				Log.e(tag, "data" + data.size());
				aMap.setMapType(AMap.MAP_TYPE_NORMAL);
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						latlngList.get(0), 15));

				drawLine(latlngList);

				break;

			}

		}
	};

	/**
	 * 获取经纬度
	 */
	Runnable runnable = new Runnable() 
	{

		@Override
		public void run() 
		{
			preferences = getSharedPreferences("SpecificName",
					Context.MODE_PRIVATE);
			eString = preferences.getString("specificname", "");
			Log.e(tag, eString);
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {

					String equipment = GetHttp.Equipment(eString,
							EquipmentDetails_path, "sn");
					Log.e(tag, equipment);
					ArrayList<Location> arrayList = Utile
							.getLocation(equipment);
					Log.e(tag, "size" + arrayList.size());
					Message message = Message.obtain(mHandler, TRUEDATE,
							arrayList);
					mHandler.sendMessage(message);
				}
			});
			thread.start();
			mHandler.postDelayed(runnable, 3000);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写

		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();

		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		mHandler.postDelayed(runnable, 3000);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		mHandler.removeCallbacks(runnable);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	public void drawLine(ArrayList<LatLng> latlngList) {

		aMap.clear();
		LatLng replayGeoPoint = latlngList.get(0);
		if (marker != null) {
			marker.destroy();
			Log.e(tag, "" + marker.getId());
		}
		// �������λ��
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions
				.position(replayGeoPoint)
				.title("标题")
				.snippet("呜呜呜")
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.car)))
				.anchor(0.5f, 0.5f);
		marker = aMap.addMarker(markerOptions);
		marker.showInfoWindow();

		aMap.addMarker(new MarkerOptions()
				.position(latlngList.get(0))
				.title("开始")
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.nav_route_result_start_point))));

		if (latlngList.size() > 1) {
			PolylineOptions polylineOptions = (new PolylineOptions())
					.addAll(latlngList).color(Color.rgb(9, 129, 240))
					.width(6.0f);
			aMap.addPolyline(polylineOptions);
		}

	}

	/*
	 * private AMap aMap; private String EquipmentDetails_path =
	 * "http://ttgps.net:8080/JsonWeb/location"; private Button button; private
	 * Button button1; private Button button2; private Button button3; private
	 * Marker marker = null;// ��ǰ�켣��ͼ�� public Handler timer = new
	 * Handler();// ��ʱ�� private ArrayList<LatLng> latlngList = new
	 * ArrayList<LatLng>(); private static final String tag = "MapActivity";
	 * private SharedPreferences preferences; private String eString; Context
	 * context;
	 * 
	 * protected static final int TRUEDATE = 10000; public Handler mHandler =
	 * new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { switch (msg.what) {
	 * case TRUEDATE: ArrayList<Location> data = (ArrayList<Location>) msg.obj;
	 * 
	 * latlngList = Utile.getTrack(data); Log.e(tag, "data"+data.size());
	 * aMap.setMapType(AMap.MAP_TYPE_NORMAL);
	 * aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngList.get(0),
	 * 15));
	 * 
	 * drawLine(latlngList); break;
	 * 
	 * }
	 * 
	 * };
	 * 
	 * };
	 * 
	 * Runnable runnable2 = new Runnable() {
	 * 
	 * @Override public void run() { preferences =
	 * getSharedPreferences("SpecificName", Context.MODE_PRIVATE); eString =
	 * preferences.getString("specificname", ""); Log.e(tag,eString); Thread
	 * thread = new Thread(new Runnable() {
	 * 
	 * @Override public void run() {
	 * 
	 * String equipment = GetHttp.Equipment(eString, EquipmentDetails_path,
	 * "sn"); Log.e(tag,equipment); ArrayList<Location> arrayList =
	 * Utile.getLocation(equipment); Log.e(tag,"size"+arrayList.size()); Message
	 * message = Message.obtain(mHandler, TRUEDATE, arrayList);
	 * mHandler.sendMessage(message); } }); thread.start();
	 * mHandler.postDelayed(runnable2, 10000); } };
	 * 
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setContentView(R.layout.map_main);
	 * context = this; init(); float f = 0; for (int i = 0; i <
	 * latlngList.size() - 1; i++) { f +=
	 * AMapUtils.calculateLineDistance(latlngList.get(i), latlngList.get(i +
	 * 1)); } Log.e("float", String.valueOf(f / 1000)); }
	 *//**
	 * btn_replay ��ʼ��AMap����
	 */
	/*
	 * private void init() { button = (Button) findViewById(R.id.radio_button0);
	 * button1 = (Button) findViewById(R.id.radio_button1); button2 = (Button)
	 * findViewById(R.id.radio_button2); button3 = (Button)
	 * findViewById(R.id.radio_button3);
	 * 
	 * if (aMap == null) { aMap = ((SupportMapFragment)
	 * getSupportFragmentManager() .findFragmentById(R.id.map)).getMap();
	 * aMap.getUiSettings().setCompassEnabled(true);
	 * 
	 * if (aMap != null) { setUpMap(); } }
	 * 
	 * button1.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * Intent intent = new Intent(); intent.setClass(getApplicationContext(),
	 * Time_choseActivity.class); startActivity(intent);
	 * 
	 * } });
	 * 
	 * }
	 * 
	 * @Override protected void onPause() { super.onPause();
	 * mHandler.removeCallbacks(runnable2); }
	 * 
	 * private void setUpMap() { button.setOnClickListener(new OnClickListener()
	 * {
	 * 
	 * @Override public void onClick(View v) { mHandler.postDelayed(runnable2,
	 * 10000); Toast.makeText(context, "正在连接...请稍后", Toast.LENGTH_LONG).show();
	 * } });
	 * 
	 * }
	 *//**
	 * 划线
	 * 
	 * @param list
	 * @param current
	 */
	/*
	 * private void drawLine(ArrayList<LatLng> list) { // TODO Auto-generated
	 * method stub aMap.clear(); LatLng replayGeoPoint = latlngList.get(0); if
	 * (marker != null) { marker.destroy(); } // �������λ�� MarkerOptions
	 * markerOptions = new MarkerOptions(); markerOptions
	 * .position(replayGeoPoint) .title("标题") .snippet("片段 ")
	 * .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
	 * .decodeResource(getResources(), R.drawable.car))) .anchor(0.5f, 0.5f);
	 * marker = aMap.addMarker(markerOptions); // ������㿪ʼ aMap.addMarker(new
	 * MarkerOptions() .position(latlngList.get(0)) .title("开始")
	 * .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
	 * .decodeResource(getResources(),
	 * R.drawable.nav_route_result_start_point)))); // ���������� if
	 * (latlngList.size() > 1) { PolylineOptions polylineOptions = (new
	 * PolylineOptions()) .addAll(latlngList).color(Color.rgb(9, 129, 240))
	 * .width(6.0f); aMap.addPolyline(polylineOptions); } }
	 */

}
