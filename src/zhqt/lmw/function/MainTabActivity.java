package zhqt.lmw.function;


import zhqt.lmw.zhqtlocation.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;

public class  MainTabActivity extends TabActivity implements OnCheckedChangeListener
{
	
	
	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maintabs);
        
        this.mAIntent = new Intent(this,MapActivity.class);
        this.mBIntent = new Intent(this,Time_choseActivity.class);
        this.mCIntent = new Intent(this,FenceActivity.class);
        this.mDIntent = new Intent(this,SettingActivity.class);
        
		((RadioButton) findViewById(R.id.radio_button0))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button1))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button2))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button3))
		.setOnCheckedChangeListener(this);
        setupIntent();
        
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		if(isChecked)
		{
			switch (buttonView.getId())
			{
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.radio_button3:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			}
		}
		
	}
	
	private void setupIntent() 
	{
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", R.string.location,
				R.drawable.tab_weixin_pressed, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", R.string.history,
				R.drawable.tab_address_pressed, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB",
				R.string.electronic_fence, R.drawable.tab_find_frd_pressed,
				this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", R.string.settings,
				R.drawable.tab_settings_pressed, this.mDIntent));


	}
	
	
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content)
	{
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	}
	
	
	
	
}