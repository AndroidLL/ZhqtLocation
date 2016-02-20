package zhqt.lmw.zhqtlocationTool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UserService 
{
	
	/**
	 * С��: ��connection����Ĳ��������ǰ�����׼���÷����ڴ滺����,
	 * ������getInputStream()ʱ,�Ž�http�����䵽��������.
	 * Ȼ��,�������˻᷵��״̬,����״̬��������һ���ж�,����500,404,200.
	 */
	
	private static String tag = "MainActivity";

	public static boolean check(String userName, String password)
			throws Exception 
	{

		String path = "http://ttgps.net:8090/vmui.php/Public/appcheckLogin";

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("account", userName);
		jsonObject.put("password",password );
		
		
		
		
		String content = String.valueOf(jsonObject);
		
		try
		{
			Log.e(tag, "JSONObject = " + content);
			return sendRequest(path, content);

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;

	}

	private static boolean sendRequest(String path, String  content) throws IOException 
	{
		
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Log.e(tag, "connection = " +connection);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setReadTimeout(5000);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
		connection.connect();
		
		DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream()); 
		
		dataOutputStream.write(content.getBytes());
		Log.e(tag, "step = "+ content.getBytes());
		dataOutputStream.flush();
		dataOutputStream.close();
		
		InputStream inputStream = connection.getInputStream();
		int code = connection.getResponseCode() ;
		if(code == 200)
		{
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			//���뻺����
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String retData = null; 
			String responseData = "";
				// ��ȡ�������˷��ص�����
			while ((retData = bufferedReader.readLine()) != null) 
			{
					responseData += retData;
				
			}	
			
			try 
			{
				JSONObject object = new JSONObject(responseData);
				JSONObject succObject = object.getJSONObject("account");
				String success = succObject.getString("reslut");
				bufferedReader.close();
				
				if(success == "ok")
				{
					return true;
				}
				
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		
		return false;
		
		/*
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		//�����Ƿ��httpUrlConnection ����.
		connection.setDoInput(true);
		//�����Ƿ���httpUrlConnection���,��Ϊ��post����,��������http�е�������
		connection.setDoOutput(true);
		connection.setReadTimeout(5000);
		connection.setRequestMethod("POST");
		// ����Ҫʹ�û���
		connection.setUseCaches(false);
		�趨��������������ǿ����л���Java����,������趨
		 *�ڴ������л�������ʱ,��web����Ĭ�ϵĲ�����������ʱ
		 *�ᱨjava.io.EOFException��һ��
		 * 
		 
		connection.setRequestProperty("Content-type","application/x-java-serialized-object");
		connection.connect();
		//ͨ����������󹹽��������������,��ʵ����������л��Ķ���
		DataOutputStream dop = new DataOutputStream(
				connection.getOutputStream());
		//����������д������,��Щ���ݽ������ڴ滺����
		dop.write(jsonObject.toString().getBytes(encode));
		//ˢ�¶��������
		dop.flush();
		dop.close();
		
		InputStream inputStream = connection.getInputStream();
		int code = connection.getResponseCode();
		Log.e(tag, "result = " + code);

		if (code == 200) 
		{
			//������getInputStream()ʱ,�Ż��׼���õ�http������ʽ���͵�������
			InputStreamReader reader = new InputStreamReader(
					inputStream);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String retData = null; 
			String responseData = null;
			// ��ȡ�������˷��ص�����
			while ((retData = bufferedReader.readLine()) != null) 
			{
				responseData += retData;
			}

			return true;

		}

		return false;

	*/
	}
}
