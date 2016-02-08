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
	    //��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���    
        list.add("���ʮ��");    
        list.add("�����ʮ��"); 
        //�ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);    
        //��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��    
  //      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //���Ĳ�������������ӵ������б���    
        chart_num.setAdapter(adapter); 
        
		Drawable tt= getResources().getDrawable(R.drawable.niaobizhong);
		tt.setBounds(0, 0, tt.getMinimumWidth(), tt.getMinimumHeight());//��������ͼƬ��С��������ʾ
		miaoshu.setImageDrawable(tt);
	     int numberOfX = 6;// Ĭ��X���6��ֵ
	        int numberOfY = 6;// Ĭ��Y���5��ֵ��Խ����ʾ��ֵԽ��ϸ��
	        List<List<Float>> pointList = new ArrayList<List<Float>>();
	        List<String> titleXList = new ArrayList<String>();
	        List<String> titleYList = new ArrayList<String>();
	        List<String> rangeYList = new ArrayList<String>();
	        List<Integer> lineColorList = new ArrayList<Integer>();
	        lineColorList.add(Color.WHITE);
	        // TODO ����//titlexlist��point����һ�£����򱨴�,titleylist������numofy
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
	         rangeYList.add("����");
	         rangeYList.add("����");
	         rangeYList.add("����");
	         rangeYList.add("��֢");
	         rangeYList.add("��֢");
	         
	   
	    
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
