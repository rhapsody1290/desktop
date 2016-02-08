package com.aibeile_diaper.mm.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;



public class WhiteBalance {

	public static Bitmap getWhiteBalance(String image) throws Exception {    
		double avgB=0;//B通道的均值
		double avgG=0;//G通道的均值
		double avgR=0;//R通道的均值
		double BSUM=0;
		double GSUM=0;
		double RSUM=0;
		
		//File file = new File(image);
		Bitmap blue = BitmapFactory.decodeFile(image);
		
        //BufferedImage bi = null;  
		
		double bmaxave,gmaxave,rmaxave;
		rmaxave = calWhite(blue, 0);
		gmaxave = calWhite(blue, 1);
		bmaxave = calWhite(blue, 2);
		double maxmax = max(rmaxave,gmaxave,bmaxave);
	    System.out.println(maxmax);
		double bgain,ggain,rgain;
		bgain=maxmax/bmaxave;
		ggain=maxmax/gmaxave;
		rgain=maxmax/rmaxave;
		System.out.println(rgain);
		System.out.println(ggain);
		System.out.println(bgain);
		Bitmap white = Bitmap.createBitmap(blue.getWidth(), blue.getHeight(), Config.ARGB_8888);
		for (int i=0;i<white.getWidth();i++){
			for (int j=0;j<white.getHeight();j++){
				int rgb = blue.getPixel(i, j);
				
			//	Color getcolor = new Color(rgb);
				int r = Color.red(rgb); 
				//System.out.print(r + " ");
		        int g = Color.green(rgb);
		       // System.out.print(g + " " );
		        int b = Color.blue(rgb);
		      //  System.out.print(b);
		      //  System.out.println();
		        int r_int = (int)(r * rgain);
		        int g_int = (int)(g * ggain);
		        int b_int = (int)(b * bgain);
		        
		        if(r_int > 255)
		        	r_int = 255; 
		        if(g_int > 255)
		        	g_int = 255;
		        if(b_int > 255)
		        	b_int = 255;
		        
		        
		            
		        white.setPixel(i, j, Color.rgb(r_int, g_int, b_int));
		        }
			}
		//ImageIO.write(white, "JPEG", new File("D:/white.jpg"));
		return white;
    }  
	public static double calWhite(Bitmap bi, int channel){
		int valueHist[] = new int[256];
		int value = 0;
		int valueMax = 255;
        double sum = 0;
        double ave = 0;        
        for(int i = 0; i < bi.getWidth(); i++){
        	for(int j = 0; j < bi.getHeight(); j++){
        		int rgb = bi.getPixel(i, j);
        		
        		switch(channel){
        		case 0:
        			value =Color.red(rgb); 
        			break;
        		case 1:
        			value =Color.green(rgb);  
        			break;
        		case 2:
        			value =Color.blue(rgb);
        			break;
        		}
        	    valueHist[value]++;
        	}
        }
        
        for (int k=255; k >= 0; k--){
        	sum+=valueHist[k];
    	    if (sum>bi.getHeight()*bi.getWidth()/10){
    	    	break;
    	    	}
    	    valueMax--;
    	    }
        sum=0;
        double divisor = 0;
        for (int i = valueMax; i<256; i++){
        	sum+=valueHist[i]*i;
        	divisor += valueHist[i];
    	}
        ave=sum/divisor;
       // System.out.println("channel = " + channel + "ave = " + ave);
        return ave;
        }
	public static double max(double r, double g, double b){
		double max = 0;
		if(r >= g && r >= b)
			max = r;
		if(g >= r && g >= b )
			max = g;
		else 
			max = b;
		return max;
	}
}
