package com.aibeile_diaper.mm.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

public class DetailNewsActivity extends Activity {
	WebView wView;
	Intent pIntent;
	String mUrl;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_news);
	    //requestWindowFeature(Window.FEATURE_NO_TITLE);
		pIntent=getIntent();
		mUrl=pIntent.getStringExtra("Url");
		WebView wView = (WebView)findViewById(R.id.view);   
		WebSettings wSet = wView.getSettings();   
		wSet.setJavaScriptEnabled(true);   
		wSet.setUseWideViewPort(true); 
		wSet.setLoadWithOverviewMode(true); 


		wSet.setUseWideViewPort(true);   
		wSet.setSupportZoom(true); //设置可以支持缩放
		wSet.setDefaultZoom(WebSettings.ZoomDensity.FAR);  
		wSet.setBuiltInZoomControls(true);//设置出现缩放工具


		wView.loadUrl(mUrl);  

	}
}
