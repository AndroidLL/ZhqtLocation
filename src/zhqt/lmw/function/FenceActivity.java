package zhqt.lmw.function;

import zhqt.lmw.zhqtlocation.R;
import zhqt.lmw.zhqtlocation.SysApplication;
import android.app.Activity;
import android.os.Bundle;

public class FenceActivity extends Activity
{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.fence);
	
	}
	
	
}
