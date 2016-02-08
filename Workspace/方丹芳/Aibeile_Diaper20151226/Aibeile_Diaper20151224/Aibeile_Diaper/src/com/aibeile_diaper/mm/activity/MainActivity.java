package com.aibeile_diaper.mm.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.aibeile_diaper.mm.sqlite.DBManager;
import com.aibeile_diaper.mm.thread.DetectUpload;
import com.aibeile_diaper.mm.thread.DetectUploadThread;
import com.aibeile_diaper.mm.thread.MyThreadRefreshDatabase;
import com.zxing.activity.CaptureActivity;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabWidget;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity implements OnCheckedChangeListener{
	public static String TAG="A";
	private TabHost mTabHost;
	private RadioButton radio_mainpage,radio_information , radio_scan ,radio_personal ;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	private DBManager mydb;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
   //     setContentView(R.layout.main_2);
        mydb=new DBManager(this);
        this.mAIntent = new Intent(this,MainPageActivity.class);
        this.mBIntent = new Intent(this,InformationActivity.class);
        this.mCIntent = new Intent(this,CaptureActivity.class);
        this.mDIntent = new Intent(this,PersonalActivity.class);
        radio_mainpage=((RadioButton) findViewById(R.id.main_tab_main));
        radio_information=((RadioButton) findViewById(R.id.main_tab_information));
        radio_scan=((RadioButton) findViewById(R.id.main_tab_scan));
        radio_personal=((RadioButton) findViewById(R.id.main_tab_personal));
      
        this.mTabHost = getTabHost();
        if(TAG.equals("A")){
        radio_mainpage.setChecked(true);
    //    this.mTabHost.setCurrentTabByTag("A_TAB");
        }
        else if(TAG.equals("B"))
        {radio_information.setChecked(true);
         this.mTabHost.setCurrentTabByTag("B_TAB");}
        else if(TAG.equals("C"))
        {radio_scan.setChecked(true);
         this.mTabHost.setCurrentTabByTag("C_TAB");}
        else if(TAG.equals("D"))
        {radio_personal.setChecked(true); 
         this.mTabHost.setCurrentTabByTag("D_TAB"); 
        }
        else{System.out.println("error!!!!");}
	
        setupIntent();
        radio_mainpage.setOnCheckedChangeListener(this);
        radio_information.setOnCheckedChangeListener(this);
        radio_scan.setOnCheckedChangeListener(this);
        radio_personal.setOnCheckedChangeListener(this);
  //      requestDatabase();
   //     testdetectupload();
        
    }
    
    private void testdetectupload() {  
    	ArrayList<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();
    	dataset=mydb.detect_upload();
    	System.out.println("dataset"+dataset);
        DetectUploadThread myThread=new DetectUploadThread();
        myThread.setDataset(dataset);
		myThread.start();
    
        	
  }

    private void requestDatabase() {
	/*	MyThreadRefreshDatabase mThread=new MyThreadRefreshDatabase();
		new Thread(mThread).start();*/   	
	}
    
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.main_tab_main:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.main_tab_information:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.main_tab_scan:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.main_tab_personal:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			}
		}
		
	}
	
	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec("A_TAB", "��ҳ", this.mAIntent));
		localTabHost.addTab(buildTabSpec("B_TAB", "��Ѷ", this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB", "ɨ��",this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB","����", this.mDIntent));
	}
	
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel).setContent(content);
	}
}