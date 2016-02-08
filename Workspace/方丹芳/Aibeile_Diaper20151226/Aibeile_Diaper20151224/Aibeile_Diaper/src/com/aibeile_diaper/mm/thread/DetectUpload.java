package com.aibeile_diaper.mm.thread;

import java.util.Date;

public class DetectUpload {
	private static String detectuploadurl;
	private DetectUploadThread myThread=new DetectUploadThread();
	public void detectupload(String user_id,Date detect_date,String detect_age_month,String qrcode,String product_id,String detail,String qualified,int score,String value_1)
	{
		for(;;)
		{
			myThread.setUser_id(user_id);
			myThread.setDetect_date(detect_date);
			myThread.setDetect_age_month(detect_age_month);
			myThread.setQrcode(qrcode);
			myThread.setProduct_id(product_id);
			myThread.setDetail(detail);
			myThread.setQualified(qualified);
			myThread.setScore(score);
			myThread.setValue_1(value_1);
			myThread.start();
			
		}
    	
		
	}
		
		
}
