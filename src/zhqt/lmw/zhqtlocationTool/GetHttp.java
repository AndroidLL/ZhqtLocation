package zhqt.lmw.zhqtlocationTool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import zhqt.lmw.function.Time_choseActivity;
import zhqt.lmw.zhqtlocation.entity.Location;

import android.util.Log;
import android.widget.Toast;

public class GetHttp  
{
	
	private static String tag = "GetHttp";
/**
 * 检验帐号是否正确
 * @param path
 * @param uString
 * @param pString
 * @return
 */
	public static Boolean getHttpClient(String path, String uString,String pString)
	{

		List<NameValuePair> list = null;
		
		list = getData(uString,pString);
		
		DefaultHttpClient client = new DefaultHttpClient();
		UrlEncodedFormEntity entity = null;
		
		try 
		{

			/**
			 * ģ�⴫ͳ��html��������������д��䣬���ñ���
			 */
			entity = new UrlEncodedFormEntity(list,"utf-8");
			Log.e(tag, "entity = "+ entity.toString());
			
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		//�½�һ��post����
		HttpPost post = new HttpPost(path);
		//����ʵ������
		post.setEntity(entity);
		HttpResponse response = null;
		String strResult = "";
		
		try 
		{
			//��������
			response = client.execute(post);
		} catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		//�����ͳɹ������õ���Ӧ
		Log.i(tag, "状态："+response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 200)
		{
			try 
			{
				//��ȡ���������ع�����json�ַ�������
				strResult = EntityUtils.toString(response.getEntity());
				Log.i(tag, strResult);
				
			} catch (ParseException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			JSONObject jsonObject = null;
			try 
			{
				//��json�ַ���ת��Ϊjson����
				jsonObject = getJSON(strResult);
				
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}

			
			String result;
			try 
			{
				result = jsonObject.getString("result");
				Log.i(tag, result);
				if("ok".equals(result))
				{
					return true;
				}
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			
		}
		
		return false;
		
	}
/**
 * 组装帐号数据
 * @param username
 * @param password
 * @return
 */
	private static List<NameValuePair> getData(String username,String password) 
	{
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair = new BasicNameValuePair("account", username);
		NameValuePair pair2 = new BasicNameValuePair("password", password);
		list.add(pair);
		list.add(pair2);
		return list;
		
	}
	/**
	 * 组装设备数据
	 * @param Equipmentname
	 * @return
	 */
	private static List<NameValuePair> getEquipment(String Equipmentname,String s) 
	{
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair = new BasicNameValuePair(s, Equipmentname);
		list.add(pair);
		
		return list;
		
	}
	
/**
 * 获取设备数据
 * @param name
 * @param equipment_path
 * @return
 */
	public static String Equipment(String name, String equipment_path,String s) 
	{
		List<NameValuePair> list = null;
		list = getEquipment(name,s);
	
		DefaultHttpClient client = new DefaultHttpClient();
		UrlEncodedFormEntity entity = null;
		
		try 
		{

			entity = new UrlEncodedFormEntity(list,"utf-8");
			
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		HttpPost post = new HttpPost(equipment_path);
		post.setEntity(entity);
		HttpResponse response = null;
		String strResult = "";
		
		try 
		{
			response = client.execute(post);
		} catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		Log.i(tag, "=="+response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 200)
		{
			try 
			{
				strResult = EntityUtils.toString(response.getEntity());
				
			} catch (ParseException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		
		return strResult;
	}

	
	private static JSONObject getJSON(String strResult) throws JSONException 
	{
		return new JSONObject(strResult);
	}
	
	/**
	 * 获取分页信息
	 * @param time_start2
	 * @param time_end2
	 * @param pagePath
	 * @param eString
	 * @return
	 */
	public static String location_pages(String time_start2, String time_end2,
			String pagePath, String eString) 
	{
		
		List<NameValuePair> list = null;
		list = getPageMessage(eString,time_start2,time_end2);
	
		DefaultHttpClient client = new DefaultHttpClient();
		UrlEncodedFormEntity entity = null;
		
		try 
		{

			entity = new UrlEncodedFormEntity(list,"utf-8");
			
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		HttpPost post = new HttpPost(pagePath);
		post.setEntity(entity);
		HttpResponse response = null;
		String strResult = "";
		
		try 
		{
			response = client.execute(post);
		} catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		Log.i(tag, "=="+response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 200)
		{
			try 
			{
				strResult = EntityUtils.toString(response.getEntity());
				
			} catch (ParseException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		JSONObject jsonObject = null;
		try 
		{
			jsonObject = new JSONObject(strResult);
			
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		String object = "";
		
		try
		{
			object = jsonObject.getString("count");
			Log.i(tag, object);
			if("error".equals(object))
			{
				return "该段时间内无数据！";
			}
			
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
			
		return object;
	}
	
	/**
	 * 组装分页数据
	 * @param time_start2
	 * @param time_end2
	 * @param pagePath
	 * @param eString
	 * @return
	 */
	private static List<NameValuePair> getPageMessage(String eString,
			String time_start2,String time_end2) 
			{
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair = new BasicNameValuePair("sn", eString);
		NameValuePair pair2 = new BasicNameValuePair("time_begin", time_start2);
		NameValuePair pair3 = new BasicNameValuePair("time_end", time_end2);
		list.add(pair);
		list.add(pair2);
		list.add(pair3);
		
		Log.e("1", "pair"+pair.toString()+"+"+pair.getName()+"+"+pair.getValue());
		Log.e("2", "pair"+pair2.toString()+"+"+pair2.getName()+"+"+pair2.getValue());
		Log.e("3", "pair"+pair3.toString()+"+"+pair3.getName()+"+"+pair3.getValue());
		return list;
	}
	
	/**
	 * 组装history数据
	 * @param time_start
	 * @param time_end
	 * @param eString
	 * @return 
	 */
	private static List<NameValuePair> getHistory(String eString,String time_start,
			String time_end) 
			{
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair1 = new BasicNameValuePair("sn", eString);
		NameValuePair pair2 = new BasicNameValuePair("time_begin", time_start);
		NameValuePair pair3 = new BasicNameValuePair("time_end", time_end);
		list.add(pair1);
		list.add(pair2);
		list.add(pair3);
		Log.e("1", "pair"+pair1.toString()+"+"+pair1.getName()+"+"+pair1.getValue());
		Log.e("2", "pair"+pair2.toString()+"+"+pair2.getName()+"+"+pair2.getValue());
		Log.e("3", "pair"+pair3.toString()+"+"+pair3.getName()+"+"+pair3.getValue());
		return list;
	}
	/**
	 * 获取history 数据
	 * @param time_start
	 * @param time_end
	 * @param historyPath
	 * @param eString
	 * @param i
	 * @return
	 */
	public static String location_history(String eString,String time_start,
			String time_end, String history_Path) 
			{
		List<NameValuePair> list = null;
		
		DefaultHttpClient client = new DefaultHttpClient();
		UrlEncodedFormEntity entity = null;
		list = getHistory(eString, time_start, time_end);
		try 
		{

			entity = new UrlEncodedFormEntity(list,"utf-8");
			
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		HttpPost post = new HttpPost(history_Path);
		post.setEntity(entity);
		HttpResponse response = null;
		String strResult = "";
		
		try 
		{
			response = client.execute(post);
		} catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		Log.i(tag, "=="+response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 200)
		{
			try 
			{
				strResult = EntityUtils.toString(response.getEntity());
				Log.e(time_start, ""+strResult+"\\"+strResult.length());
				
			} catch (ParseException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		return strResult;
	}
	
	
	/**
	 * 设置属性
	 * @param eString
	 * @param msleepreport
	 * @param mgpsmode
	 * @param mcallringmode
	 * @param mtimezone
	 * @param mspeedenable
	 * @param mspeedmax
	 * @param mmoveenable
	 * @param mmovedistance
	 * @param mshockenable
	 * @param bindnumber
	 */
	public static void alter(String eString, String msleepreport,
			String mgpsmode, String mcallringmode, String mtimezone,
			String mspeedenable, String mspeedmax, String mmoveenable,
			String mmovedistance, String mshockenable, String bindnumber) 
	{
		
		
		
		
		
	}
}
