package com.aibeile_diaper.mm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainPageInfoActivity extends Activity{


	Intent intentnews;
	String url;
	int strbackimage;
	String strday;
	String strmonth;
	String strweek;
	String strtitle;
	int markid;
	ImageView backimage;
	TextView day;
	TextView month;
	TextView week;
	TextView title;
	Intent dintent;
	TextView progress1;
	TextView progress2;
	TextView progress3;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainpage_info);
		Getdata();
		Initview();	
		backimage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				intentnews = new Intent(MainPageInfoActivity.this, DetailNewsActivity.class);
				intentnews.putExtra("Url",url);
				startActivity(intentnews);
			}
		
		});
	}
	private void Initview() {
		// TODO Auto-generated method stub
		backimage=((ImageView) findViewById(R.id.main_info_backimage));
		backimage.setImageResource(strbackimage);
		day=((TextView) findViewById(R.id.main_info_day));
		day.setText(strday);
		month=((TextView) findViewById(R.id.main_info_month));
		month.setText(strmonth);
		week=((TextView) findViewById(R.id.main_info_week));
		week.setText(strweek);
		title=((TextView) findViewById(R.id.main_info_title));
		title.setText(strtitle);
		progress1=((TextView) findViewById(R.id.main_info_progress1));
		progress2=((TextView) findViewById(R.id.main_info_progress2));
		progress3=((TextView) findViewById(R.id.main_info_progress3));
		if(markid==0)
		{
			progress1.setBackgroundResource(R.drawable.progress_white);
			progress2.setBackgroundResource(R.drawable.progress_grey);
			progress3.setBackgroundResource(R.drawable.progress_grey);
		}
		else if(markid==1)
		{
			progress2.setBackgroundResource(R.drawable.progress_white);
			progress1.setBackgroundResource(R.drawable.progress_grey);
			progress3.setBackgroundResource(R.drawable.progress_grey);
		}
		else
		{
			progress3.setBackgroundResource(R.drawable.progress_white);
			progress2.setBackgroundResource(R.drawable.progress_grey);
			progress1.setBackgroundResource(R.drawable.progress_grey);
		}
				
	}
	private void Getdata() {

		// TODO Auto-generated method stub
		/*url="file:///android_asset/plant/index.html";
		strbackimage=R.drawable.main_info_image;
		strday="14";
		strmonth="OCT";
		strweek="星期三";
		strtitle="美国梅奥心脏干细胞技术";*/

		dintent=getIntent();
		url=dintent.getStringExtra("url");		
		strbackimage=Integer.parseInt(dintent.getStringExtra("backimage"));		
		strday=dintent.getStringExtra("day");
		strmonth=dintent.getStringExtra("month");
		strweek=dintent.getStringExtra("week");
		strtitle=dintent.getStringExtra("title");
		markid=Integer.parseInt(dintent.getStringExtra("id"));

	}


}
