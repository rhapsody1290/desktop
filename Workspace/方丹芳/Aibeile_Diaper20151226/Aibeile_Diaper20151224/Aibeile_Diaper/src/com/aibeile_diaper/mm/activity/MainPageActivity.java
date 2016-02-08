package com.aibeile_diaper.mm.activity;

import java.util.ArrayList;
import java.util.List;

import com.aibeile_diaper.mm.adapter.CommPagerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class MainPageActivity extends Activity {

	//资讯显示
	private ViewPager mViewInfo;
	private LocalActivityManager mActManagerInfo;
	private List<View> mViewListInfo = new ArrayList<View>();
	private MainPageInfoActivity maininfo1;
	private MainPageInfoActivity maininfo2;
	private MainPageInfoActivity maininfo3;
	int[] strbackimage;
	String[] strday;
	String[] strmonth;
	String[] strweek;
	String[] strtitle;
	String[] url;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_mainpage);

		//资讯初始化

		mActManagerInfo = new LocalActivityManager(this,true);
		mActManagerInfo.dispatchCreate(savedInstanceState);
		Infoget();
		Infoinit();


	}

	private void Infoget() {
		// TODO Auto-generated method stub
		System.out.println(1);
		url=new String[]{"file:///android_asset/plant/index.html",
				"file:///android_asset/plant/index.html",
		"file:///android_asset/plant/index.html"};
		strbackimage=new int[]{R.drawable.main_info_image,
				R.drawable.newspic2,R.drawable.newspic3};
		strday=new String[]{"14","08","15"};
		strmonth=new String[]{"OCT","MAY","JUL"};
		strweek=new String[]{"星期三","星期六","星期日"};
		strtitle=new String[]{"美国梅奥心脏干细胞技术","七种隔夜食物含剧毒", "中国首台心衰超滤治疗设备进医院我是"};

	}

	private void Infoinit() {
		// TODO Auto-generated method stub
		mViewInfo = (ViewPager) findViewById(R.id.info_viewpager);
		Intent info_intent1=new Intent(this,MainPageInfoActivity.class);
		Infoto(info_intent1,0);
		Intent info_intent2=new Intent(this,MainPageInfoActivity.class);
		Infoto(info_intent2,1);
		Intent info_intent3=new Intent(this,MainPageInfoActivity.class);
		Infoto(info_intent3,2);
		mViewListInfo.add(getView("maininfo1",info_intent1));
		mViewListInfo.add(getView("maininfo2",info_intent2));
		mViewListInfo.add(getView("maininfo3",info_intent3));
		CommPagerAdapter adapter = new CommPagerAdapter(mViewListInfo);
		mViewInfo.setAdapter(adapter);
		mViewInfo.setCurrentItem(0);
	}

	private void Infoto(Intent info_intent0, int i) {
		// TODO Auto-generated method stub
		info_intent0.putExtra("url",url[i]);
		info_intent0.putExtra("backimage",Integer.toString(strbackimage[i]));
		info_intent0.putExtra("day",strday[i]);
		info_intent0.putExtra("month",strmonth[i]);
		info_intent0.putExtra("week",strweek[i]);
		info_intent0.putExtra("title",strtitle[i]);	
		info_intent0.putExtra("id",Integer.toString(i));	
	}

	private View getView(String id,Intent intent){
		return mActManagerInfo.startActivity(id, intent).getDecorView();
	}


}
