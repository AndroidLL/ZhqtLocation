package zhqt.lmw.zhqtlocation;

import java.util.ArrayList;

import zhqt.lmw.zhqtlocation.entity.Location;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestBundle extends Activity 
{
	
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		ArrayList<Location> serInfo = (ArrayList<Location>) getIntent().getSerializableExtra("DATA"); 
		
		//text = (TextView)findViewById(R.id.ss);
		Log.e("", "这次传过来没=="+ serInfo.size());
	}

}
