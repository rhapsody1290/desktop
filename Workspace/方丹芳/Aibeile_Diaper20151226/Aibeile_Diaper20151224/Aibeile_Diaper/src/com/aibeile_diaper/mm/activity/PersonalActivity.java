package com.aibeile_diaper.mm.activity;

import com.aibeile_diaper.mm.view.CircularImage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity {
	private View personal_data,personal_notice,personal_information,personal_help,personal_about,personal_feedback;
	private CircularImage avatar;
	private TextView personal_id,personal_sex,personal_birthday,personal_score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_personal);
		initView();
		SetListener();
	}
	public void initView()
	{   
		avatar =  (CircularImage) findViewById(R.id.avatar);
		personal_id = (TextView) findViewById(R.id.personal_id);
		personal_sex = (TextView) findViewById(R.id.personal_sex);
		personal_birthday = (TextView) findViewById(R.id.personal_birthday);
		personal_score = (TextView) findViewById(R.id.personal_score);
		personal_data = findViewById(R.id.personal_data);
		personal_notice = findViewById(R.id.personal_notice);
		personal_information = findViewById(R.id.personal_information);
		personal_help = findViewById(R.id.personal_help);
		personal_about = findViewById(R.id.personal_about);
		personal_feedback = findViewById(R.id.personal_feedback);
	
	}
	private void SetListener()
	{
		avatar.setOnClickListener(onClickListener);
		personal_data.setOnClickListener(onClickListener);
		personal_notice.setOnClickListener(onClickListener);
		personal_information.setOnClickListener(onClickListener);
		personal_help.setOnClickListener(onClickListener);
		personal_about.setOnClickListener(onClickListener);
		personal_feedback.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener=new OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.avatar:
				Toast.makeText(getApplicationContext(), "头像",Toast.LENGTH_SHORT).show();
				break;
			case R.id.personal_data: 
				Toast.makeText(getApplicationContext(), "历史数据",Toast.LENGTH_SHORT).show();
				break;
			case R.id.personal_notice:
				Toast.makeText(getApplicationContext(), "下次检测提醒",Toast.LENGTH_SHORT).show();
				break;
			case R.id.personal_information:
				Toast.makeText(getApplicationContext(), "个人详细信息",Toast.LENGTH_SHORT).show();
				Intent intent3 = new Intent(PersonalActivity.this, DetailInformationActivity.class);
			    startActivity(intent3);
				break;
			case R.id.personal_help:
				Toast.makeText(getApplicationContext(), "帮助",Toast.LENGTH_SHORT).show();
				break;
			case R.id.personal_about:
				Toast.makeText(getApplicationContext(), "关于",Toast.LENGTH_SHORT).show();
				break;
			case R.id.personal_feedback:
				Toast.makeText(getApplicationContext(), "反馈",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
};


}
