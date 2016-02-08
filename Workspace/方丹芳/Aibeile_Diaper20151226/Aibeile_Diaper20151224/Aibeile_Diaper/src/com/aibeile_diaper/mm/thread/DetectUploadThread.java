package com.aibeile_diaper.mm.thread;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.aibeile_diaper.mm.util.Url;

import android.util.Log;

public class DetectUploadThread extends Thread 
{
	private ArrayList<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> getDataset() {
		return dataset;
	}

	public void setDataset(ArrayList<HashMap<String, String>> dataset) {
		this.dataset = dataset;
	}

	private String user_id,detect_age_month,qrcode,product_id,detail,qualified,value_1;
	public String getDetect_age_month() {
		return detect_age_month;
	}

	public void setDetect_age_month(String detect_age_month) {
		this.detect_age_month = detect_age_month;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getQualified() {
		return qualified;
	}

	public void setQualified(String qualified) {
		this.qualified = qualified;
	}

	public String getValue_1() {
		return value_1;
	}

	public void setValue_1(String value_1) {
		this.value_1 = value_1;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getDetect_date() {
		return detect_date;
	}

	public void setDetect_date(Date detect_date) {
		this.detect_date = detect_date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	private int score;
    private Date detect_date;
    private String flag;
    private String url;
    @Override
	public void run() {
		Log.v("DetectUploadThread", "start");
		for(int i=0;(i<dataset.size())&&(flag.equals("success"));i++)
		{url=Url.DETECTURL;
		 url+="user_id="+dataset.get(i).get("user_id").toString();
		 url+="&"+"detect_date="+dataset.get(i).get("datect_date").toString();
		 url+="&"+"detect_age_month="+dataset.get(i).get("datect_age_month").toString();
		 url+="&"+"qrcode="+dataset.get(i).get("qrcode").toString();
		 url+="&"+"product_id="+dataset.get(i).get("product_id").toString();
		 url+="&"+"detail="+dataset.get(i).get("detail").toString();
		 url+="&"+"qualified="+dataset.get(i).get("qualified").toString();
		 url+="&"+"score="+dataset.get(i).get("score").toString();
		 url+="&"+"value_1="+dataset.get(i).get("value_1").toString();
		 url+="&"+"value_2="+dataset.get(i).get("value_2").toString();
		 url+="&"+"value_3="+dataset.get(i).get("value_3").toString();
		 url+="&"+"value_4="+dataset.get(i).get("value_4").toString();
		 url+="&"+"value_5="+dataset.get(i).get("value_5").toString();
		 url+="&"+"value_6="+dataset.get(i).get("value_6").toString();
		 url+="&"+"value_7="+dataset.get(i).get("value_7").toString();
		 url+="&"+"value_8="+dataset.get(i).get("value_8").toString();
		 url+="&"+"value_9="+dataset.get(i).get("value_9").toString();
		 url+="&"+"value_10="+dataset.get(i).get("value_10").toString();
		 url+="&"+"value_11="+dataset.get(i).get("value_11").toString();
		 url+="&"+"value_12="+dataset.get(i).get("value_12").toString();
		 URL myurl;
			try {
				myurl = new URL(url);
				Log.v("DetectUploadThread", url);
				URLConnection con;
				con = myurl.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				flag=getListItems(input);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
    public String getListItems(InputStream input)
	{
		String result = null;
		Reader reader;
		BufferedReader bufferedReader=null;
		try {
			reader = new InputStreamReader(input, "UTF-8");
			bufferedReader = new BufferedReader(reader);
	        String str = null;  
	        StringBuffer sb = new StringBuffer();  
	        while ((str = bufferedReader.readLine()) != null) {  
	            sb.append(str);  
	        }  
	        Log.v("DetectUploadThread", sb.toString());
	        String strTemp=sb.toString();
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        String codeString=obj.getString("code");
	        Log.v("DetectUploadThread",codeString);
	        String messageString=obj.getString("message");
	        if (codeString.equals("200")) {
	        	Log.v("DetectUploadThread", "parse json success");
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
	        	JSONObject array = (JSONObject)tempJsonObject.get("Customer");
	        	result="success";		       
			}
	       else{result="failer";}	        
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//¹Ø±ÕÊäÈëÁ÷
		finally
		{if(bufferedReader !=null){
		  try {bufferedReader.close();	
		   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		  }
			
		}
		return result;
	}	
	
}
