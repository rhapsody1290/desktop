package com.aibeile_diaper.mm.view;

import java.util.ArrayList;
import java.util.List;

import com.aibeile_diaper.mm.activity.R;
import com.aibeile_diaper.mm.listener.CommonCallback;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyScrollHeaderView extends LinearLayout {

	private LinearLayout scrollHeaderContainer; 
	private HorizontalScrollView scrollHeaderScroller;
	private List<TextView> mTvList = new ArrayList<TextView>();
	private Context mContext;
	private int   mLength = 0;
	private int   mWidth = 0; 
	private CommonCallback callback;
	public MyScrollHeaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyScrollHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.scroll_header, this);
		scrollHeaderContainer = (LinearLayout) findViewById(R.id.scroll_header_container);
		scrollHeaderScroller = (HorizontalScrollView) findViewById(R.id.scroll_header_scroller);
		mContext = context;
		//addScrollHeaderItem(context,context.getResources().getStringArray(R.array.news_header_strings));
	}



	
	
	public void addScrollHeaderItem(Context context,String[] headerStrs){
		//int width = 0;
		mLength = headerStrs.length;
		if(headerStrs.length <= 4)
		  mWidth = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth()/headerStrs.length;
		else
			mWidth = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth()/4;
		for(int i = 0;i < headerStrs.length;i++){
			View view = LayoutInflater.from(context).inflate(R.layout.scroll_header_tv, null);
			TextView tv = (TextView) view.findViewById(R.id.scroll_header_tv);
			mTvList.add(tv);
			tv.setText(headerStrs[i]);
		    view.setId(i);
			LayoutParams params = new LayoutParams(mWidth,LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
			scrollHeaderContainer.addView(view);
			view.setOnClickListener(onClickListener);
		}
		
	}
	
	
	
	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			setCurrentTab(id);
		}
		
	};
	
	
	public void setCurrentTab(int id){
		TextView tv = mTvList.get(id);
		callback.callback(id);
		tv.setTextColor(mContext.getResources().getColor(R.color.blue));
		
		for(int i = 0;i < mTvList.size();i++){
			if(i != id){
				TextView otherTv = (TextView) mTvList.get(i);
				otherTv.setTextColor(mContext.getResources().getColor(R.color.text_grey));
			}
		}
		int indicatorLeft = id*mWidth;
		int scrollX = (id > 3?indicatorLeft:0) - 3*mWidth;
		
		scrollHeaderScroller.scrollTo(scrollX, 0);
	}

	
	public void setMyScrollCallback(CommonCallback callback){
		
		this.callback = callback;
	}
}
