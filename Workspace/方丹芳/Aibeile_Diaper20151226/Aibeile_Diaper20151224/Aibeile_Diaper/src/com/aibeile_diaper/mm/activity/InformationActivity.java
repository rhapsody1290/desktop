package com.aibeile_diaper.mm.activity;


import java.util.ArrayList;
import java.util.List;

import com.aibeile_diaper.mm.adapter.CommPagerAdapter;
import com.aibeile_diaper.mm.listener.CommonCallback;
import com.aibeile_diaper.mm.view.MyScrollHeaderView;
import com.aibeile_diaper.mm.view.MyViewPager;

import android.os.Bundle;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;

public class InformationActivity extends Activity{

	private MyScrollHeaderView mScrollHeader;
	private MyViewPager mViewPager;
	private LocalActivityManager mActManager;
	private List<View> mViewList = new ArrayList<View>();
	private HotInforActivity hotinfo;
	private HourNewsActivity hournews;
	private NearFocusActivity nearfocus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_information);

		mActManager = new LocalActivityManager(this,true);
		mActManager.dispatchCreate(savedInstanceState);

		mScrollHeader = (MyScrollHeaderView)findViewById(R.id.information_scroll_header);
		mScrollHeader.addScrollHeaderItem(this,getResources().getStringArray(R.array.news_header_strings));
		
		mViewPager = (MyViewPager) findViewById(R.id.fragment_viewpager);
		mViewList.add(getView("hotinfo",new Intent(this,HotInforActivity.class)));
		mViewList.add(getView("hournews",new Intent(this,HourNewsActivity.class)));
		mViewList.add(getView("hournews",new Intent(this,HourNewsActivity.class)));
		//mViewList.add(getView("nearfocus",new Intent(this,NearFocusFragment.class)));
		CommPagerAdapter adapter = new CommPagerAdapter(mViewList);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mScrollHeader.setMyScrollCallback(new CommonCallback(){
			

			@Override
			public void callback(Object obj) {
				// TODO Auto-generated method stub
				System.out.println("开始0123");
				mViewPager.setPagingEnabled(false);
				int id = (Integer) obj;
				mViewPager.setCurrentItem(id);
				mViewPager.setPagingEnabled(true);
				System.out.println("开始了");

			}
			
		});
		mScrollHeader.setCurrentTab(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mScrollHeader.setCurrentTab(position);
			}

		});

	}

	private View getView(String id,Intent intent){
		return mActManager.startActivity(id, intent).getDecorView();
	}



}
