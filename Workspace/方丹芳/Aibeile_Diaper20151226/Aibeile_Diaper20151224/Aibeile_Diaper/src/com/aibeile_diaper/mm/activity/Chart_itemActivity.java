package com.aibeile_diaper.mm.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aibeile_diaper.mm.view.Mychart;

public class Chart_itemActivity extends Activity {
	private ImageView miaoshu;
	private Mychart my;
	private TextView zhibiao_name;
	private Spinner chart_num;
	private List<String> list = new ArrayList<String>();   
	private ImageView back_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chart_item);
		Intent it=getIntent();
		back_button=(ImageView) findViewById(R.id.header_back_button);
		back_button.setOnClickListener(onClickListener);
		miaoshu=(ImageView) findViewById(R.id.chart_item_miaoshu);
		my=(Mychart) findViewById(R.id.chart_item_chartview); 
		zhibiao_name=(TextView) findViewById(R.id.chart_item_zhibiao);
		chart_num=(Spinner) findViewById(R.id.chart_item_num);
		zhibiao_name.setText(it.getCharSequenceExtra("zhibiao").toString());
	    //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项    
        list.add("最近十次");    
        list.add("最近二十次"); 
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);    
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
  //      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        chart_num.setAdapter(adapter); 
        
		Drawable tt= getResources().getDrawable(R.drawable.niaobizhong);
		tt.setBounds(0, 0, tt.getMinimumWidth(), tt.getMinimumHeight());//必须设置图片大小，否则不显示
		miaoshu.setImageDrawable(tt);
	     int numberOfX = 6;// 默认X轴放6个值
	        int numberOfY = 6;// 默认Y轴放5个值（越多显示的值越精细）
	        List<List<Float>> pointList = new ArrayList<List<Float>>();
	        List<String> titleXList = new ArrayList<String>();
	        List<String> titleYList = new ArrayList<String>();
	        List<String> rangeYList = new ArrayList<String>();
	        List<Integer> lineColorList = new ArrayList<Integer>();
	        lineColorList.add(Color.WHITE);
	        // TODO 测试//titlexlist与point数量一致，否则报错,titleylist数量与numofy
	            List<Float> pointInList = new ArrayList<Float>();
	            for (int j = 0; j < 6; j++) { 
	                titleXList.add("12." + j + "1");
	            }
	            pointInList.add(0f);
	            pointInList.add(0.5f);
	            pointInList.add(1.5f);
	            pointInList.add(3.0f);
	            pointInList.add(4.0f);
	            pointInList.add(8.0f);
	        pointList.add(pointInList);    
	            titleYList.add("0");
	            titleYList.add("0.5");
	            titleYList.add("1.5");
	            titleYList.add("3.0");
	            titleYList.add("4.0");
	            titleYList.add("8.0");
	         rangeYList.add("0");
	         rangeYList.add("正常");
	         rangeYList.add("正常");
	         rangeYList.add("低量");
	         rangeYList.add("重症");
	         rangeYList.add("重症");
	         
	   
	    
	        my.setNumberOfX(6);
	        my.setNumberOfY(6);
	        my.setTitleXList(titleXList);
	        my.setTitleYList(titleYList);
	        my.setRangeYList(rangeYList);
	        my.setLineColorList(lineColorList);
	        my.setPointList(pointList);
	        my=new Mychart(this);
	}
	 private ImageView.OnClickListener onClickListener=new OnClickListener()
	   {@Override
		public void onClick(View v)
	   {switch(v.getId()) 
			 {case R.id.header_back_button:
				Chart_itemActivity.this.finish();	
				break;
			 }
		   
	   }
	   };
    

}
