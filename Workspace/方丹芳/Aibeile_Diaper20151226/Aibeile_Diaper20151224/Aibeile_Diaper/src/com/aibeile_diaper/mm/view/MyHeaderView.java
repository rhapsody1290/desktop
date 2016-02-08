package com.aibeile_diaper.mm.view;

import java.net.ContentHandler;

import com.aibeile_diaper.mm.activity.R;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyHeaderView extends LinearLayout {

	public TextView tv=null;
	public LinearLayout headerContentView=null;
	public String [] headcontent=null;
	public Button[] headButtons=null;
	public View.OnClickListener[] onClickListeners=null;
	public ImageButton home_btn;
	public ImageButton setting;
	private ImageView search_iv;
	//private MyBroadcastReciver sendReceiver;
	
	


	public MyHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.header_main, this);  
        
//        IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("hide_search_button");
//		sendReceiver = new MyBroadcastReciver();
//		getContext().registerReceiver(sendReceiver, intentFilter);
        
        search_iv = (ImageView)findViewById(R.id.header_search_button);
        search_iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("show_search_edittext");
				getContext().sendBroadcast(intent);
			}
		});

	}



	
	
	
	
	public MyHeaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_main, this);

	}


	
	
	

	

}
