package zhqt.lmw.zhqtlocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zhqt.lmw.function.MainTabActivity;
import zhqt.lmw.function.MapActivity;
import zhqt.lmw.zhqtlocation.entity.Location;
import zhqt.lmw.zhqtlocationTool.Utile;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author develop
 * 
 *    
 */

public class DeviceActivity extends ExpandableListActivity 
{
	 private Handler handler;
	 String  equipmentString;
	 private String tag = "DeviceActivity";
	 /**SharedPreferences中储存数据的路径**/  
	 public final static String DATA_URL = "/data/data/";  
	 public final static String SHARED_MAIN_XML = "main.xml";  
	 SharedPreferences preferences;
	 private List<List<String>> itemList = null;
	 private List<String> groupList = null;
	 private ExpandableListView expList = null;
	 private Intent intent;

	 public static ArrayList<Location> locations;
	 private static final int TRUEDATE = 1000;
	 public  final static String LIST_KEY = "Data"; 
	 private Handler mHandler = new Handler()
	 {
		 @Override
		public void handleMessage(Message msg)
		 {
			 
			 switch(msg.what)
			 {
			 	case TRUEDATE:
			 		ArrayList<Location> data =  (ArrayList<Location>) msg.obj;
			 		True(data);
			 		break;
			 
			 }
			 
		 }
		 
	 };

	 
	protected void True(ArrayList<Location> data) 
	{
		intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("DATA", data);
		intent.setClass(DeviceActivity.this, MapActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	
		preferences = getSharedPreferences("test", Context.MODE_APPEND);
		String eString = preferences.getString("Equipment","");
		Log.e("equipment", ""+eString);
		itemList  =Utile.getTrueString(eString);
		setListAdapter(new IdeasExpandableListAdapter(this));
		
	}
	
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		//clearPreference();
	}
	
/**
 *  删除SharedPreferences文件 
 *  
 */ 
	private void clearPreference() 
	{
	    /** 删除SharedPreferences文件 **/  
        File file = new File(DATA_URL + getPackageName().toString()  
            + "/shared_prefs", SHARED_MAIN_XML);  
        if (file.exists()) {  
            file.delete();  
        }
	}


	public class IdeasExpandableListAdapter extends BaseExpandableListAdapter 
	{

		private Context mContext = null;
		
		public IdeasExpandableListAdapter(Context context)
		{
			this.mContext = context;
			groupList = new ArrayList<String>();
			initData(itemList);
		}

		private void initData(List<List<String>> s)
		{
			int length = s.size();
			Log.e(tag, ""+length);
			for(int i=0 ; i <length; i++)
			{
				groupList.add("默认分组:");
				
			}
			
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}
		
		
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return itemList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return itemList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent)
		{
		
			TextView text = null;
			
			if(convertView == null)
			{
				text = new TextView(mContext);
				
			}else
			{
				text = (TextView)convertView;
			}
			
			String name = groupList.get(groupPosition);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150);
			text.setLayoutParams(lp);
			text.setTextSize(35);
			text.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			text.setPadding(36, 0, 0, 0);
			text.setText(name);
			return text;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent)
		{
			 TextView text = null;
			if(convertView == null)
			{
				text = new TextView(mContext);
			}else
			{
				text = (TextView)convertView;
			}
			// 获取子节点要显示的名称
			   final String name = itemList.get(groupPosition).get(childPosition);
			   // 设置文本视图的相关属性
			   AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
			   ViewGroup.LayoutParams.MATCH_PARENT, 100);
			   text.setLayoutParams(lp);
			   text.setTextSize(20);
			   text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			   text.setPadding(45, 0, 0, 0);
			   text.setText(name);
			   text.setClickable(true);  
			   
			   text.setOnClickListener(new View.OnClickListener() 
			   {
				   
				   @Override
				   public void onClick(View v) 
				   {
					   
						preferences = getSharedPreferences("SpecificName", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("specificname",name);
						editor.commit();
						intent = new Intent();
						intent.setClass(DeviceActivity.this, MainTabActivity.class);
						startActivity(intent);
					   
					   Toast.makeText(mContext, name+"说:不要点我", Toast.LENGTH_SHORT).show();
				   }
			   });
			   return text;
			   
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) 
		{
			return true;
		}
		
		
		
		}

}