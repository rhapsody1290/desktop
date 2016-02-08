package com.aibeile_diaper.mm.activity;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.aibeile_diaper.mm.util.MySort;
import com.aibeile_diaper.mm.util.detect_color;
import com.zxing.activity.CaptureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ScanActivity extends Activity implements OnClickListener {
	private String rgb1[][] = new String[][] { { "����    0", "255", "224", "127" }, { "����    8", "232", "214", "107"},  
            { "����    15", "232", "198", "145" }, { "����    40", "207", "173", "102" }, { "����    70", "184", "151", "115" },{ "��֢    125", "141", "96", "106" }, { "��֢    300", "124", "67", "86" }, { "��֢    500", "117", "90", "120" } };  
	private String rgb2[][] = new String[][] { { "����    ����", "255", "244", "109" }, { "����    ����", "255", "207", "161"},  
            { "��֢    �����̶ȵķ�ɫ", "255", "190", "181" }, { "��֢    �����̶ȵķ�ɫ", "255", "139", "130" }, { "��֢    �����̶ȵķ�ɫ", "220", "88", "79" }};  
	private String rgb3[][] = new String[][] { { "����    3.2", "255", "156", "110" }, { "����    9", "237", "122", "54"},  
            { "����    16", "231", "112", "85" }, { "����    24", "223", "103", "96" }, { "����    32", "202", "87", "81" },{ "��֢    64", "218", "63", "75" }, { "��֢    92", "198", "55", "70" }, { "��֢    128", "200", "34", "73" } };  
	private String rgb4[][] = new String[][] { { "����    ����", "212", "224", "90" }, { "����    ΢��", "162", "207", "98"},  
            { "����    0.3", "135", "199", "101" }, { "����    0.5", "118", "189", "27" }, { "��֢    1", "107", "192", "103" },{ "��֢    3", "117", "164", "96" }, { "��֢    ����20", "94", "145", "93" }};  
	private String rgb5[][] = new String[][] { { "����    5.0", "219", "64", "40" }, { "����    5.5", "229", "73", "50"},  
            { "����    6.0", "224", "126", "38" }, { "����    6.5", "227", "160", "35" }, { "����    7.0", "188", "181", "49" },{ "��֢    7.5", "101", "132", "65" }, { "��֢    8.0", "72", "129", "67" },{ "��֢    8.5", "20", "89", "64" }};  
	private static String rgb6[][] = new String[][] { { "����    ����", "252", "186", "97" }, { "����    10����Ѫ", "252", "186", "97"},  
            { "����    10��Ѫ", "185", "151", "100" }, { "����    25", "141", "155", "107" }, { "��֢    80", "101", "120", "100" },{ "��֢    170", "85", "78", "78" }, { "��֢    250", "75", "51", "82" }};  
	private String rgb7[][] = new String[][] { { "����    1.000", "36", "52", "62" }, { "����    1.005", "58", "52", "61"},  
            { "����    1.010", "67", "87", "69" }, { "����    1.015", "120", "106", "69" }, { "��֢    1.020", "154", "139", "70" },{ "��֢    1.025", "190", "162", "69" }, { "��֢    1.030", "210", "170", "66" }};  
	private String rgb8[][] = new String[][] { { "����    ����", "203", "124", "88" }, { "����    0.5", "208", "114", "105"},  
            { "����    1.5", "180", "49", "70" }, { "����    3.0", "169", "38", "59" }, { "����    4.0", "165", "23", "67" },{ "��֢    8.0", "123", "32", "71" }, { "��֢    12", "122", "13", "60" },{ "��֢    16", "97", "6", "45" }};  
	private String rgb9[][] = new String[][] { { "����    ����", "254", "205", "100" }, { "����    ��+", "253", "204", "125"},  
            { "����    ��++", "196", "124", "92" }, { "��֢    ��+++", "165", "101", "88" }};
	private String rgb10[][] = new String[][] { { "����    ����", "100", "143", "130" }, { "����    5", "63", "130", "94"},  
            { "����    10", "88", "125", "29" }, { "����    15", "73", "110", "74" }, { "��֢    30", "151", "119", "64" },{ "��֢    60", "110", "37", "49" }, { "��֢    110", "86", "20", "48" }};  
	private String rgb11[][] = new String[][] { { "����    ����", "4", "1", "15" }, { "����    0.5", "0", "61", "37"},  
            { "����    1.0", "35", "96", "72" }, { "����    1.4", "56", "154", "72" }, { "��֢    2.0", "193", "203", "31" },{ "��֢    2.8", "212", "183", "40" }, { "��֢    3.6", "199", "159", "55" }};  
	private String rgb12[][] = new String[][] { { "����    1.0", "245", "173", "206" }, { "����    2.5", "22", "135", "182"},  
            { "����    4.0", "206", "99", "146" }, { "����    5.0", "133", "92", "113" }, { "����    6.0", "107", "66", "87" },{ "��֢    7.5", "134", "20", "116" }, { "��֢    12.5", "110", "20", "85" },{ "��֢    15", "79", "0", "54" }};  
	private TextView tv00,tv01,tv02,tv03;
	private TextView tv10,tv11,tv12,tv13;
	private TextView tv20,tv21,tv22,tv23;
	private TextView tv30,tv31,tv32,tv33;
	private TextView tv40,tv41,tv42,tv43;
	private TextView tv50,tv51,tv52,tv53;
	private TextView tv60,tv61,tv62,tv63;
	private TextView tv70,tv71,tv72,tv73;
	private TextView tv80,tv81,tv82,tv83;
	private TextView tv90,tv91,tv92,tv93;
	private TextView tv100,tv101,tv102,tv103;
	private TextView tv110,tv111,tv112,tv113;
	private TextView result_text;
	private Spinner spinner_date;
	private Bitmap result_bitmap;
	private String scanResult,result_savename;
	private ImageView back_button;
	private int lightRed = Color.rgb(Integer.parseInt("ec", 16),
	            Integer.parseInt("2b", 16), Integer.parseInt("4a", 16));// ǳ��ɫ
	private int lightYellow = Color.rgb(Integer.parseInt("fa", 16),Integer.parseInt("dc", 16), Integer.parseInt("00", 16));
	private int lightGreen = Color.rgb(Integer.parseInt("81", 16),
            Integer.parseInt("c3", 16), Integer.parseInt("45", 16));
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.result_scan);
		back_button=(ImageView) findViewById(R.id.header_back_button);
		back_button.setOnClickListener(onClickListener);
		scanResult = (String) getIntent().getSerializableExtra("result");
		result_savename = (String) getIntent().getSerializableExtra("result_savename");
		float points_12[] = getIntent().getFloatArrayExtra("12_point");
		float k_size=getIntent().getFloatExtra("k_size", -1f);
		ArrayList<HashMap<String,String>> mm=detect_color.mdetect_color(result_savename,points_12,k_size);
		 System.out.println("---------����ǰ---------");
		  for (HashMap<String, String> hm : mm)
		  {
		   System.out.println(hm.get("zhibiao") + ":" + hm.get("test_data"));
		  }
		  System.out.println("---------�����---------");
		   Collections.sort(mm, new MySort(false, true, "range"));
		   for (HashMap<String, String> hm : mm)
		   {
		      System.out.println(hm.get("zhibiao") + ":" + hm.get("test_data"));
		    } 
		result_text=(TextView) findViewById(R.id.tv_result);
		HashMap<String,String> temp0=mm.get(0);
		if(temp0.get("range").toString().equals("2"))
		{result_text.setText("��ά��ɨ������"+ scanResult+"\n"+"�����μ�������أ���XXX���ա�");}
	    else if(mm.get(0).get("range").toString().equals("1"))
		{result_text.setText("��ά��ɨ������"+ scanResult+"\n"+"�����μ������΢�쳣����ע��XXXָ�ꡣ");}
		else{result_text.setText("��ά��ɨ������"+ scanResult+"\n"+"�����μ�������ã���������֡�");}
		spinner_date=(Spinner) findViewById(R.id.spinner_date);
	    //��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���    
		List<String> list = new ArrayList<String>(); 
		SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy��MM��dd��");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String date = formatter.format(curDate);
		list.add(date);
		list.add("2015��12��01��"); 
        list.add("2015��11��04��");     
        //�ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);    
        //��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��    
  //      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //���Ĳ�������������ӵ������б���    
        spinner_date.setAdapter(adapter); 
        
		tv00=(TextView) findViewById(R.id.tv00);
		tv01=(TextView) findViewById(R.id.tv01);
		tv02=(TextView) findViewById(R.id.tv02);
		tv03=(TextView) findViewById(R.id.tv03);
		tv10=(TextView) findViewById(R.id.tv10);
		tv11=(TextView) findViewById(R.id.tv11);
		tv12=(TextView) findViewById(R.id.tv12);
		tv13=(TextView) findViewById(R.id.tv13);
		tv20=(TextView) findViewById(R.id.tv20);
		tv21=(TextView) findViewById(R.id.tv21);
		tv22=(TextView) findViewById(R.id.tv22);
		tv23=(TextView) findViewById(R.id.tv23);
		tv30=(TextView) findViewById(R.id.tv30);
		tv31=(TextView) findViewById(R.id.tv31);
		tv32=(TextView) findViewById(R.id.tv32);
		tv33=(TextView) findViewById(R.id.tv33);
		tv40=(TextView) findViewById(R.id.tv40);
		tv41=(TextView) findViewById(R.id.tv41);
		tv42=(TextView) findViewById(R.id.tv42);
		tv43=(TextView) findViewById(R.id.tv43);
		tv50=(TextView) findViewById(R.id.tv50);
		tv51=(TextView) findViewById(R.id.tv51);
		tv52=(TextView) findViewById(R.id.tv52);
		tv53=(TextView) findViewById(R.id.tv53);
		tv60=(TextView) findViewById(R.id.tv60);
		tv61=(TextView) findViewById(R.id.tv61);
		tv62=(TextView) findViewById(R.id.tv62);
		tv63=(TextView) findViewById(R.id.tv63);
		tv70=(TextView) findViewById(R.id.tv70);
		tv71=(TextView) findViewById(R.id.tv71);
		tv72=(TextView) findViewById(R.id.tv72);
		tv73=(TextView) findViewById(R.id.tv73);
		tv80=(TextView) findViewById(R.id.tv80);
		tv81=(TextView) findViewById(R.id.tv81);
		tv82=(TextView) findViewById(R.id.tv82);
		tv83=(TextView) findViewById(R.id.tv83);
		tv90=(TextView) findViewById(R.id.tv90);
		tv91=(TextView) findViewById(R.id.tv91);
		tv92=(TextView) findViewById(R.id.tv92);
		tv93=(TextView) findViewById(R.id.tv93);
		tv100=(TextView) findViewById(R.id.tv100);
		tv101=(TextView) findViewById(R.id.tv101);
		tv102=(TextView) findViewById(R.id.tv102);
		tv103=(TextView) findViewById(R.id.tv103);
		tv110=(TextView) findViewById(R.id.tv110);
		tv111=(TextView) findViewById(R.id.tv111);
		tv112=(TextView) findViewById(R.id.tv112);
		tv113=(TextView) findViewById(R.id.tv113);
		
		
		HashMap<String,String> temp=mm.get(0);
		setdata(temp,tv00,tv01,tv02,tv03);
		temp.clear();
		temp=mm.get(1);
		setdata(temp,tv10,tv11,tv12,tv13);
/*		Drawable tt= getResources().getDrawable(R.drawable.wenhao);
		tt.setBounds(0, 0, tt.getMinimumWidth(), tt.getMinimumHeight());//��������ͼƬ��С��������ʾ
		tv10.setCompoundDrawables(null, null, tt, null);*/
		temp.clear();
		temp=mm.get(2);
		setdata(temp,tv20,tv21,tv22,tv23);
		temp.clear();
		temp=mm.get(3);
		setdata(temp,tv30,tv31,tv32,tv33);
		temp.clear();
		temp=mm.get(4);
		setdata(temp,tv40,tv41,tv42,tv43);
		temp.clear();
		temp=mm.get(5);
		setdata(temp,tv50,tv51,tv52,tv53);
		temp.clear();
		temp=mm.get(6);
		setdata(temp,tv60,tv61,tv62,tv63);
		temp.clear();
		temp=mm.get(7);
		setdata(temp,tv70,tv71,tv72,tv73);
		temp.clear();
		temp=mm.get(8);
		setdata(temp,tv80,tv81,tv82,tv83);
		temp.clear();
		temp=mm.get(9);
		setdata(temp,tv90,tv91,tv92,tv93);
		temp.clear();
		temp=mm.get(10);
		setdata(temp,tv100,tv101,tv102,tv103);
		temp.clear();
		temp=mm.get(11);
		setdata(temp,tv110,tv111,tv112,tv113);

		tv00.setClickable(true);
		tv00.setOnClickListener(this);
		tv01.setClickable(true);
		tv01.setOnClickListener(this) ;
		tv02.setClickable(true);
		tv02.setOnClickListener(this);
		tv03.setClickable(true);
		tv03.setOnClickListener(this) ;
		tv10.setClickable(true);
		tv10.setOnClickListener(this);
		tv11.setClickable(true);
		tv11.setOnClickListener(this) ;
		tv20.setClickable(true);
		tv20.setOnClickListener(this);
		tv21.setClickable(true);
		tv21.setOnClickListener(this) ;
		tv30.setClickable(true);
		tv30.setOnClickListener(this);
		tv31.setClickable(true);
		tv31.setOnClickListener(this) ;
		tv40.setClickable(true);
		tv40.setOnClickListener(this);
		tv41.setClickable(true);
		tv41.setOnClickListener(this) ;
		tv50.setClickable(true);
		tv50.setOnClickListener(this);
		tv51.setClickable(true);
		tv51.setOnClickListener(this) ;
		tv60.setClickable(true);
		tv60.setOnClickListener(this);
		tv61.setClickable(true);
		tv61.setOnClickListener(this) ;
		tv70.setClickable(true);
		tv70.setOnClickListener(this);
		tv71.setClickable(true);
		tv71.setOnClickListener(this) ;
		tv80.setClickable(true);
		tv80.setOnClickListener(this);
		tv81.setClickable(true);
		tv81.setOnClickListener(this) ;
		tv90.setClickable(true);
		tv90.setOnClickListener(this);
		tv91.setClickable(true);
		tv91.setOnClickListener(this) ;
		tv100.setClickable(true);
		tv100.setOnClickListener(this);
		tv101.setClickable(true);
		tv101.setOnClickListener(this) ;
		tv110.setClickable(true);
		tv110.setOnClickListener(this);
		tv111.setClickable(true);
		tv111.setOnClickListener(this) ;
		
	}
	
	 public void onClick(View v) {
	    // TODO Auto-generated method stub
	    switch (v.getId()) {
	    case R.id.tv00:
	    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
	    	Intent intent0 = new Intent(ScanActivity.this,Chart_itemActivity.class);
	    	intent0.putExtra("zhibiao",tv00.getText().toString());
			startActivity(intent0);
	      break;
	    case R.id.tv01:
	    	Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
	      break;
	    case R.id.tv10:
		    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
		    	Intent intent1 = new Intent(ScanActivity.this,Chart_itemActivity.class);
		    	intent1.putExtra("zhibiao",tv10.getText().toString());
				startActivity(intent1);
		  break;
		case R.id.tv11:
		    	Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
	    break;
		case R.id.tv20:
			    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
			    	Intent intent2 = new Intent(ScanActivity.this,Chart_itemActivity.class);
			    	intent2.putExtra("zhibiao",tv20.getText().toString());
					startActivity(intent2);
			      break;
	    case R.id.tv21:
			   Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		break;
	    case R.id.tv30:
				    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
				    	Intent intent3 = new Intent(ScanActivity.this,Chart_itemActivity.class);
				    	intent3.putExtra("zhibiao",tv30.getText().toString());
						startActivity(intent3);
				      break;
	    case R.id.tv31:
				    	Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
				      break;
		case R.id.tv40:
					    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
					Intent intent4 = new Intent(ScanActivity.this,Chart_itemActivity.class);
					  intent4.putExtra("zhibiao",tv40.getText().toString());
				startActivity(intent4);
			break;
	    case R.id.tv41:
			Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		break;
		case R.id.tv50:
						    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
			Intent intent5 = new Intent(ScanActivity.this,Chart_itemActivity.class);
		    intent5.putExtra("zhibiao",tv50.getText().toString());
			startActivity(intent5);
		break;
		case R.id.tv51:
			Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
	    break;
		case R.id.tv60:
							 //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
			Intent intent6 = new Intent(ScanActivity.this,Chart_itemActivity.class);
			intent6.putExtra("zhibiao",tv60.getText().toString());
			startActivity(intent6);
		break;
		case R.id.tv61:
			Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		break;
		case R.id.tv70:
								    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
			Intent intent7 = new Intent(ScanActivity.this,Chart_itemActivity.class);
			intent7.putExtra("zhibiao",tv70.getText().toString());
			startActivity(intent7);
	    break;
		case R.id.tv71:
			Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
			break;
        case R.id.tv80:
									    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
			Intent intent8 = new Intent(ScanActivity.this,Chart_itemActivity.class);
			intent8.putExtra("zhibiao",tv80.getText().toString());
			startActivity(intent8);
			break;
		case R.id.tv81:
		Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		break;
		case R.id.tv90:
										    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
		Intent intent9 = new Intent(ScanActivity.this,Chart_itemActivity.class);
		intent9.putExtra("zhibiao",tv90.getText().toString());
		startActivity(intent9);
		break;
		case R.id.tv91:
	     Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		break;
		case R.id.tv100:
											    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
		Intent intent10 = new Intent(ScanActivity.this,Chart_itemActivity.class);
		intent10.putExtra("zhibiao",tv100.getText().toString());
		startActivity(intent10);
		 break;
		case R.id.tv101:
		Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		break;
		case R.id.tv110:
												    //	Toast.makeText(ScanActivity.this, "���00", Toast.LENGTH_LONG).show();	
		Intent intent11 = new Intent(ScanActivity.this,Chart_itemActivity.class);
		intent11.putExtra("zhibiao",tv110.getText().toString());
		startActivity(intent11);
		break;
	   case R.id.tv111:
		Toast.makeText(ScanActivity.this, "��ת������ָ���������", Toast.LENGTH_LONG).show();	
		 break;
	 
	    default:
	      break;
	    }
	  }

	public void setdata(HashMap<String,String> temp,TextView tv0,TextView tv1,TextView tv2,TextView tv3)
	{tv0.setText(temp.get("zhibiao").toString());
	 tv1.setText(temp.get("test_data").toString());
	 tv2.setText(temp.get("standard_data").toString());	
	if(temp.get("range").toString().equals("0")){
		tv0.setBackgroundColor(lightGreen);
		tv1.setBackgroundColor(lightGreen);
		tv2.setBackgroundColor(lightGreen);
		tv3.setBackgroundColor(lightGreen);	
	}
	else if(temp.get("range").toString().equals("1"))
	{tv0.setBackgroundColor(lightYellow);
	 tv1.setBackgroundColor(lightYellow);
	 tv2.setBackgroundColor(lightYellow);
	 tv3.setBackgroundColor(lightYellow);		
	}
	else
	{tv0.setBackgroundColor(lightRed);
	 tv1.setBackgroundColor(lightRed);
	 tv2.setBackgroundColor(lightRed);
	 tv3.setBackgroundColor(lightRed);	
	}	
	}
	
	 private View.OnClickListener onClickListener=new OnClickListener()
	   {@Override
		public void onClick(View v)
	   {switch(v.getId()) 
			 {case R.id.header_back_button:
					Intent intent1 = new Intent(ScanActivity.this,MainActivity.class);
					MainActivity.TAG="A";	
					startActivity(intent1);
					ScanActivity.this.finish();
					ScanActivity.this.onDestroy();
				break;
			 }
		   
	   }
	   };
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent1 = new Intent(ScanActivity.this,MainActivity.class);
			MainActivity.TAG="A";	
			startActivity(intent1);
			ScanActivity.this.finish();
			ScanActivity.this.onDestroy();	
		} 	
	   return true;
		}

}
