package zhqt.lmw.zhqtlocationTool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import zhqt.lmw.zhqtlocation.entity.Location;

public class Utile 
{
	
	private ProgressDialog progDialog = null;// 搜索时进度条

	/**
	 * 树状图转换
	 * @param eString
	 * @return List<List<String>>
	 */
	public static List<List<String>> getTrueString(String eString) 
	{
		// [["100000","101252"],["101252"]]
		// List<List<String>>

		List<List<String>> equipment = new ArrayList<List<String>>();
		Gson gson = new Gson();

		Type type = new TypeToken<ArrayList<List<String>>>() 
				{
		}.getType();

		equipment = gson.fromJson(eString, type);

		return equipment;
	}

	/**
	 * json 转换ArrayList<Location>
	 * @param eString
	 * @return ArrayList<Location>---location
	 */
	public static ArrayList<Location> getLocation(String eString)
	{
		
		ArrayList<Location> rs = new ArrayList<Location>();
		
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<Location>>() {}.getType();  
		  
        rs=gson.fromJson(eString, type);  
        
        
        for(Location o:rs)
        {  

                Log.e("Utile", "维度="+o.getLat());  

        }  
		
		
		return rs;
		
	}
	/**
	 * json 转换ArrayList<String>
	 * @param eString
	 * @return
	 */
	public static ArrayList<String> getString(String eString)
	{
		
		ArrayList<String> rs = new ArrayList<String>();
		
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<String>>() {}.getType();  
		
		rs=gson.fromJson(eString, type);  
		
		
		for(String o:rs)
		{  
			
			Log.e("Utile", "String内容是"+o.toString());  
			
		}  
		
		return rs;
		
	}

	/**
	 * 获取经纬度
	 * @param locationData ArrayList
	 * @return ArrayList<LatLng>
	 */
	public static ArrayList<LatLng> getTrack(ArrayList<Location> locationData)
	{
		ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
		
		for(int i = locationData.size() ; i>0; i--)
		{
		
			LatLng latLng = new LatLng(locationData.get(i-1).getLat(), locationData.get(i-1).getLon());
			
			latlngList.add(latLng);
		}
		
		return latlngList;
	}
	
	/**
	 * 显示进度框
	 */
	private void showProgressDialog(Context context) 
	{
		if (progDialog == null)
			progDialog = new ProgressDialog(context);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在连接中...");
		progDialog.show();
	}

	
	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog()
	{
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
	
}
