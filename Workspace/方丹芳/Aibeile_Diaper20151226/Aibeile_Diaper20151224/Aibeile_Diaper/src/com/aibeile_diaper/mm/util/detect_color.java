package com.aibeile_diaper.mm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zxing.activity.rgb;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;



public class detect_color {
	private static String rgb1[][] = new String[][] { { "正常     0", "255", "224", "127" }, { "正常    8", "232", "214", "107"},  
            { "低量    15", "232", "198", "145" }, { "低量    40", "207", "173", "102" }, { "低量    70", "184", "151", "115" },{ "重症    125", "141", "96", "106" }, { "重症    300", "124", "67", "86" }, { "重症    500", "117", "90", "120" } };  
	private static String rgb2[][] = new String[][] { { "正常    阴性", "255", "244", "109" }, { "低量    阳性", "255", "207", "161"},  
            { "重症    其他程度的粉色", "255", "190", "181" }, { "重症    其他程度的粉色", "255", "139", "130" }, { "重症    其他程度的粉色", "220", "88", "79" }};  
	private static String rgb3[][] = new String[][] { { "正常    3.2", "255", "156", "110" }, { "正常    9", "237", "122", "54"},  
            { "低量    16", "231", "112", "85" }, { "低量    24", "223", "103", "96" }, { "低量    32", "202", "87", "81" },{ "重症    64", "218", "63", "75" }, { "重症    92", "198", "55", "70" }, { "重症    128", "200", "34", "73" } };  
	private static String rgb4[][] = new String[][] { { "正常    阴性", "212", "224", "90" }, { "正常    微量", "162", "207", "98"},  
            { "低量    0.3", "135", "199", "101" }, { "低量    0.5", "118", "189", "27" }, { "重症    1", "107", "192", "103" },{ "重症    3", "117", "164", "96" }, { "重症    大于20", "94", "145", "93" }};  
	private static String rgb5[][] = new String[][] { { "正常    5.0", "219", "64", "40" }, { "正常    5.5", "229", "73", "50"},  
            { "低量    6.0", "224", "126", "38" }, { "低量    6.5", "227", "160", "35" }, { "低量    7.0", "188", "181", "49" },{ "重症    7.5", "101", "132", "65" }, { "重症    8.0", "72", "129", "67" },{ "重症    8.5", "20", "89", "64" }};  
	private static String rgb6[][] = new String[][] { { "正常    阴性", "252", "186", "97" }, { "低量    10非溶血", "252", "186", "97"},  
            { "低量    10溶血", "185", "151", "100" }, { "低量    25", "141", "155", "107" }, { "重症    80", "101", "120", "100" },{ "重症    170", "85", "78", "78" }, { "重症    250", "75", "51", "82" }};  
	private static String rgb7[][] = new String[][] { { "正常    1.000", "36", "52", "62" }, { "正常    1.005", "58", "52", "61"},  
            { "低量    1.010", "67", "87", "69" }, { "低量    1.015", "120", "106", "69" }, { "重症    1.020", "154", "139", "70" },{ "重症    1.025", "190", "162", "69" }, { "重症    1.030", "210", "170", "66" }};  
	private static String rgb8[][] = new String[][] { { "正常    阴性", "203", "124", "88" }, { "正常    0.5", "208", "114", "105"},  
            { "低量    1.5", "180", "49", "70" }, { "低量    3.0", "169", "38", "59" }, { "低量    4.0", "165", "23", "67" },{ "重症    8.0", "123", "32", "71" }, { "重症    12", "122", "13", "60" },{ "重症    16", "97", "6", "45" }};  
	private static String rgb9[][] = new String[][] { { "正常    阴性", "254", "205", "100" }, { "低量    低+", "253", "204", "125"},  
            { "低量    中++", "196", "124", "92" }, { "重症    高+++", "165", "101", "88" }};
	private static String rgb10[][] = new String[][] { { "正常    阴性", "100", "143", "130" }, { "正常    5", "63", "130", "94"},  
            { "低量    10", "88", "125", "29" }, { "低量    15", "73", "110", "74" }, { "重症    30", "151", "119", "64" },{ "重症    60", "110", "37", "49" }, { "重症    110", "86", "20", "48" }};  
	private static String rgb11[][] = new String[][] { { "正常    阴性", "4", "1", "15" }, { "正常    0.5", "0", "61", "37"},  
            { "低量    1.0", "35", "96", "72" }, { "低量    1.4", "56", "154", "72" }, { "重症    2.0", "193", "203", "31" },{ "重症    2.8", "212", "183", "40" }, { "重症    3.6", "199", "159", "55" }};  
	private static String rgb12[][] = new String[][] { { "正常    1.0", "245", "173", "206" }, { "正常    2.5", "22", "135", "182"},  
            { "低量    4.0", "206", "99", "146" }, { "低量    5.0", "133", "92", "113" }, { "低量    6.0", "107", "66", "87" },{ "重症    7.5", "134", "20", "116" }, { "重症    12.5", "110", "20", "85" },{ "重症    15", "79", "0", "54" }};  
	private static int image_6_key=0;
	private static String result_savename;
	private static float[] points_12;
	private static float k_size;
	public static ArrayList<HashMap<String,String>> mdetect_color(String bitmap_name,float[] points,float size)
	{
		ArrayList<HashMap<String,String>> color_result=new  ArrayList<HashMap<String,String>>();
		color_result.clear();
		result_savename=bitmap_name;
		points_12=points;
		k_size=size;
		Bitmap result_bitmap = BitmapFactory.decodeFile(result_savename);
		Bitmap white_bitmap= Bitmap.createBitmap(result_bitmap.getWidth(), result_bitmap.getHeight(), Config.ARGB_8888);
		//白平衡！！！！！
		try {
			white_bitmap=WhiteBalance.getWhiteBalance(result_savename);
			//ImageIO.write(white, "JPEG", new File(FileUtil.getSDPath() + java.io.File.separator+ "imgtest"+"/white.jpg"));
			FileUtil.saveBitmap(white_bitmap, "white.jpg");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   List<rgb> result_colo =new ArrayList<rgb>();
			for(int i=0;i<points_12.length;i=i+2)
			{System.out.println(i+" "+(int)(points_12[i]) + "," + (int)(points_12[i+1]));	
			//边缘检测！！！！！
			//获取每个色块的颜色  
	 			int pixel = white_bitmap.getPixel((int)(points_12[i]),(int)(points_12[i+1]));	 			
				rgb temp=new rgb();
				temp.r= Color.red(pixel);
				temp.b = Color.blue(pixel);
				temp.g = Color.green(pixel);
				result_colo.add(temp);
			}	
	 		//色块6的处理
	 		 List<rgb> list_rgb = new ArrayList<rgb>();
	          int mArrayColorLengh = (int) ((1+0.5*k_size)* (1+0.5*k_size));  
	          int[] mArrayColor = new int[mArrayColorLengh]; 
	          int count=0;
	          for (int x = (int)(points_12[10]-0.1*k_size); x < (int)(points_12[10]+0.1*k_size); x++)
	          {  
	          for (int y = (int)(points_12[11]-0.1*k_size); y < (int)(points_12[11]+0.1*k_size); y++) 
	          {  
	        	//获取    
					rgb temp=new rgb();
					temp.r= Color.red(white_bitmap.getPixel(x,y));
					temp.b = Color.blue(white_bitmap.getPixel(x,y));
					temp.g = Color.green(white_bitmap.getPixel(x,y)); 
					list_rgb.add(temp);
					mArrayColor[count]=(temp.r-result_colo.get(5).r)*(temp.r-result_colo.get(5).r)+(temp.b-result_colo.get(5).b)*(temp.b-result_colo.get(5).b)+(temp.g-result_colo.get(5).g)*(temp.g-result_colo.get(5).g);
	                count++;
	          }
	          }
	          int max = mArrayColor[0];
	          for(int x=1; x<mArrayColor.length; x++)
	          {
	           if(mArrayColor[x]>max)
	            max = mArrayColor[x];
	          }
	          System.out.println("max+++++++++"+max);
	          if(max<1000)
	          {image_6_key=0;}
	          else{image_6_key=1;}
	    HashMap<String,String> zhibiao1=getColorName(result_colo.get(0).r, result_colo.get(0).g, result_colo.get(0).b, rgb1); 
	    HashMap<String,String> zhibiao2=getColorName(result_colo.get(1).r, result_colo.get(1).g, result_colo.get(1).b, rgb2);
	    HashMap<String,String> zhibiao3=getColorName(result_colo.get(2).r, result_colo.get(2).g, result_colo.get(2).b, rgb3);
	    HashMap<String,String> zhibiao4=getColorName(result_colo.get(3).r, result_colo.get(3).g, result_colo.get(3).b, rgb4); 
	    HashMap<String,String> zhibiao5=getColorName(result_colo.get(4).r, result_colo.get(4).g, result_colo.get(4).b, rgb5);
	    HashMap<String,String> zhibiao6=getColorName(result_colo.get(5).r, result_colo.get(5).g, result_colo.get(5).b, rgb6);
	    HashMap<String,String> zhibiao7=getColorName(result_colo.get(6).r, result_colo.get(6).g, result_colo.get(6).b, rgb7); 
	    HashMap<String,String> zhibiao8=getColorName(result_colo.get(7).r, result_colo.get(7).g, result_colo.get(7).b, rgb8);
	    HashMap<String,String> zhibiao9=getColorName(result_colo.get(8).r, result_colo.get(8).g, result_colo.get(8).b, rgb9);
	    HashMap<String,String> zhibiao10=getColorName(result_colo.get(9).r, result_colo.get(9).g, result_colo.get(9).b, rgb10); 
	    HashMap<String,String> zhibiao11=getColorName(result_colo.get(10).r, result_colo.get(10).g, result_colo.get(10).b, rgb11);
	    HashMap<String,String> zhibiao12=getColorName(result_colo.get(11).r, result_colo.get(11).g, result_colo.get(11).b, rgb12);
		color_result.add(zhibiao1);
		color_result.add(zhibiao2);
		color_result.add(zhibiao3);
		color_result.add(zhibiao4);
		color_result.add(zhibiao5);
		color_result.add(zhibiao6);
		color_result.add(zhibiao7);
		color_result.add(zhibiao8);
		color_result.add(zhibiao9);
		color_result.add(zhibiao10);
		color_result.add(zhibiao11);
		color_result.add(zhibiao12);
	    return color_result;
	}
	
	  public static HashMap<String,String> getColorName(int r, int g, int b, String[][] src) {  
	        int minRGB = 765;  
	        int min =0;
	 //       System.out.println("==========颜色列表========");  
	        for (int i=0;i<src.length;i++) {  
	       //     System.err.println(src[i]);  
	            int tempR = Math.abs(r - Integer.parseInt(src[i][1]));  
	            int tempG = Math.abs(g - Integer.parseInt(src[i][2]));  
	            int tempB = Math.abs(b - Integer.parseInt(src[i][3]));  
	            int tempRGB = (tempR + tempG + tempB) / 3;  
	            if (tempRGB <= minRGB) {  
	                minRGB = tempRGB;  
	                min=i;
	            } 
	        }
	        if((src.equals(rgb6))&((min==0)||(min==1))) 
	        {
	          if(image_6_key==0){min=0;}
	          else if(image_6_key==1){min=1;}
	        } 
	        HashMap<String,String> result=new HashMap<String,String>();
	        result.clear();
	        if(src==rgb1){ result.put("zhibiao", "白细胞");}
	        else if(src==rgb2){ result.put("zhibiao", "亚硝酸盐");}
	        else if(src==rgb3){result.put("zhibiao", "尿胆原");}
	        else if(src==rgb4){result.put("zhibiao", "蛋白质");}
	        else if(src==rgb5){result.put("zhibiao", "PH");}
	        else if(src==rgb6){result.put("zhibiao", "潜血");}
	        else if(src==rgb7){result.put("zhibiao", "尿比重");}
	        else if(src==rgb8){result.put("zhibiao", "酮体");}
	        else if(src==rgb9){result.put("zhibiao", "胆红素");}
	        else if(src==rgb10){result.put("zhibiao", "葡萄糖");}
	        else if(src==rgb11){result.put("zhibiao", "抗坏血酸");}
	        else if(src==rgb12){result.put("zhibiao", "钙");}

	        result.put("test_data", src[min][0]);
	        result.put("standard_data", src[0][0]);
	        if((src[min][0].indexOf("重症") != -1)){result.put("range", "2");}
 			else if((src[min][0].indexOf("低量") != -1)){result.put("range", "1");}
 			else{result.put("range", "0");}
	       
	        return result;  
	    }  


}
