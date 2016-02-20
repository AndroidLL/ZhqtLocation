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
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment 
{

	private static MapFragment fragment = null;
	public static final int POSITION = 0;
	protected static final String tag = "MapFragment";

	private MapView mapView;
	private AMap aMap;
	private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
	private String EquipmentDetails_path = "http://ttgps.net:8080/JsonWeb/location";
	//当前轨迹点图案
	private Marker marker = null;
	//定时器
	private Handler timer = new Handler();
	private Handler handler2 ;
	private SharedPreferences preferences;
	private String eString;
	public Activity activity;
	private View rootView;
	protected static final int TRUEDATE = 10000;
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
				Log.e(tag, "data"+data.size());
				aMap.setMapType(AMap.MAP_TYPE_NORMAL);
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngList.get(0), 15));
				
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
			preferences = getActivity().getSharedPreferences("SpecificName",
					Context.MODE_PRIVATE);
			eString = preferences.getString("specificname", "");
			Log.e(tag,eString);
			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run() 
				{

					String equipment = GetHttp.Equipment(eString,
							EquipmentDetails_path, "sn");
					Log.e(tag,equipment);
					ArrayList<Location> arrayList = Utile.getLocation(equipment);
					Log.e(tag,"size"+arrayList.size());
					Message message = Message.obtain(mHandler, TRUEDATE,
							arrayList);
					mHandler.sendMessage(message);
				}
			});
			thread.start();
			mHandler.postDelayed(runnable, 5000);
			
		}
	};


/*	public static Fragment newInstance() 
	{
		if (fragment == null) 
		{
			synchronized (MapFragment.class) 
			{
				if (fragment == null) 
				{
					fragment = new MapFragment();
				}
			}
		}
		return fragment;
	}
*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		
			 rootView = inflater.inflate(R.layout.fragment_map, container,false);
			 mapView = (MapView) rootView.findViewById(R.id.mapView);
			 inits();
			
		
		return rootView;
		
		
		
		
		/*if (mapLayout == null) 
		{
			Log.i("sys", "MF onCreateView() null");
			mapLayout = inflater.inflate(R.layout.fragment_map, container,false);
			mapView = (MapView) mapLayout.findViewById(R.id.mapView);
			//此方法必须重写
			mapView.onCreate(savedInstanceState);
			
			float f = 0;
			for (int i = 0; i < latlngList.size() - 1; i++) 
			{
				f += AMapUtils.calculateLineDistance(latlngList.get(i),
						latlngList.get(i + 1));
			}
			Log.e("float", String.valueOf(f / 1000));
			
			
			if (aMap == null) 
			{
				aMap = mapView.getMap();
			}
		} else 
		{
			if (mapLayout.getParent() != null) {
				((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
			}
		}
		return mapLayout;
		*/
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		mapView.onCreate(savedInstanceState);
		
	}

	private void inits() 
	{
		if(aMap == null)
		{
			
			aMap = mapView.getMap();
		}
	}
	

	public void drawLine(ArrayList<LatLng> latlngList)
	{
		
				aMap.clear();
				LatLng replayGeoPoint = latlngList.get(0);
				if (marker != null) 
				{
					marker.destroy();
					Log.e(tag, ""+marker.getId());
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
				
				if (latlngList.size() > 1)
				{
					PolylineOptions polylineOptions = (new PolylineOptions())
							.addAll(latlngList).color(Color.rgb(9, 129, 240))
							.width(6.0f);
					aMap.addPolyline(polylineOptions);
				}
		
		
	}


	@Override
	public void onResume()
	{
		Log.i("sys", "mf onResume");
		super.onResume();
		mapView.onResume();
		mHandler.postDelayed(runnable, 5000);
	
	}

	/**
	 * 方法必须重写 map的生命周期方法
	 */
	@Override
	public void onPause() 
	{
		Log.i("sys", "mf onPause");
		super.onPause();
		mapView.onPause();
		mHandler.removeCallbacks(runnable);
		mapView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 方法必须重写 map的生命周期方法
	 */
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		Log.i("sys", "mf onSaveInstanceState");
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写 map的生命周期方法
	 */
	@Override
	public void onDestroy() 
	{
		Log.i("sys", "mf onDestroy");
		super.onDestroy();
		mapView.onDestroy();
	}


}
