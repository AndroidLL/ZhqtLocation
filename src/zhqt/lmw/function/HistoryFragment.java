package zhqt.lmw.function;

import java.util.ArrayList;
import java.util.Calendar;

import zhqt.lmw.zhqtlocation.R;
import zhqt.lmw.zhqtlocation.entity.Location;
import zhqt.lmw.zhqtlocationTool.Utile;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HistoryFragment extends Fragment 
{
	
	public MapView mapView;
	public AMap aMap = null;
	public Activity activity;
	private EditText etStartTime; 
    private EditText etEndTime; 
    public  String time_start = null;
    public String time_end = null;
    public String eString;
    private Button btn_submit;
	String history_Path = "http://ttgps.net:8080/JsonWeb/history";
	protected static final int PAGES_MESSAGE = 1000;
	protected static final int HISTORY_MESSAGE = 2000;
	protected String tag = "Time_choseActivity";
	private SharedPreferences preferences;
	private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
    
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
		View view = inflater.inflate(R.layout.fragment_history, container,false);
		mapView = (MapView) view.findViewById(R.id.mapView);
		
		return view;
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
		init();
	}

	private void init() 
	{
		if(aMap == null)
		{
			aMap = mapView.getMap();
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		mapView.onResume();
		
	}

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

	
}
