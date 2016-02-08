/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.aibeile_diaper.mm.activity.R;
import com.google.zxing.ResultPoint;
import com.zxing.camera.CameraManager;
  
  
/** 
 * This view is overlaid on top of the camera preview. It adds the viewfinder 
 * rectangle and partial transparency outside it, as well as the laser scanner 
 * animation and result points. 
 *  
 */  
public final class ViewfinderView extends View {  
	
    private static final String TAG = "log";  
    /** 
     * ˢ�½����ʱ�� 
     */  
    private static final long ANIMATION_DELAY = 10L;  
    private static final int OPAQUE = 0xFF;  
  
    /** 
     * �ĸ���ɫ�߽Ƕ�Ӧ�ĳ��� 
     */  
    private int ScreenRate;  
      
    /** 
     * �ĸ���ɫ�߽Ƕ�Ӧ�Ŀ�� 
     */  
    private static final int CORNER_WIDTH = 10;  
    /** 
     * ɨ����е��м��ߵĿ�� 
     */  
    private static final int MIDDLE_LINE_WIDTH = 6;  
      
    /** 
     * ɨ����е��м��ߵ���ɨ������ҵļ�϶ 
     */  
    private static final int MIDDLE_LINE_PADDING = 5;  
      
    /** 
     * �м�������ÿ��ˢ���ƶ��ľ��� 
     */  
    private static final int SPEEN_DISTANCE = 5;  
      
    /** 
     * �ֻ�����Ļ�ܶ� 
     */  
    private static float density;  
    /** 
     * �����С 
     */  
    private static final int TEXT_SIZE = 16;  
    /** 
     * �������ɨ�������ľ��� 
     */  
    private static final int TEXT_PADDING_TOP = 30;  
      
    /** 
     * ���ʶ�������� 
     */  
    private Paint paint;  
      
    /** 
     * �м们���ߵ����λ�� 
     */  
    private int slideTop;  
      
    /** 
     * �м们���ߵ���׶�λ�� 
     */  
    private int slideBottom;  
    private Drawable lineDrawable;// ����ͼƬ��Ϊɨ����
      
    private Bitmap resultBitmap;  
    private final int maskColor;  
    private final int resultColor;  
      
    private final int resultPointColor;  
    private Collection<ResultPoint> possibleResultPoints;  
    private Collection<ResultPoint> lastPossibleResultPoints;  
  
    boolean isFirst;  
      
    public ViewfinderView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        lineDrawable = getResources().getDrawable(R.drawable.zx_code_line);
        density = context.getResources().getDisplayMetrics().density;  
        //������ת����dp  
        ScreenRate = (int)(20 * density);  
        paint = new Paint();  
        Resources resources = getResources();  
        maskColor = resources.getColor(R.color.viewfinder_mask);  
        resultColor = resources.getColor(R.color.result_view);  
  
        resultPointColor = resources.getColor(R.color.possible_result_points);  
        possibleResultPoints = new HashSet<ResultPoint>(5);  
    }  
  
    @Override  
    public void onDraw(Canvas canvas) {  
        //�м��ɨ�����Ҫ�޸�ɨ���Ĵ�С��ȥCameraManager�����޸�  
        Rect frame = CameraManager.get().getFramingRect();  
        Rect frame2 = CameraManager.get().getFramingRect2();  
        if (frame == null) {  
            return;  
        }  
          
        //��ʼ���м��߻��������ϱߺ����±�  
        if(!isFirst){  
            isFirst = true;  
            slideTop = frame2.top;  
            slideBottom = frame2.bottom;  
        }  
          
        //��ȡ��Ļ�Ŀ�͸�  
        int width = canvas.getWidth();  
        int height = canvas.getHeight();  
  
        paint.setColor(resultBitmap != null ? resultColor : maskColor);  
          
        //����ɨ����������Ӱ���֣����ĸ����֣�ɨ�������浽��Ļ���棬ɨ�������浽��Ļ����  
        //ɨ��������浽��Ļ��ߣ�ɨ�����ұߵ���Ļ�ұ�  
        canvas.drawRect(0, 0, width, frame.top, paint);  
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);  
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,  
                paint);  
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);  
          
          
  
        if (resultBitmap != null) {  
            // Draw the opaque result bitmap over the scanning rectangle  
            paint.setAlpha(OPAQUE);  
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);  
        } else {  
  
            //��ɨ�����ϵĽǣ��ܹ�8������  
            paint.setColor(Color.GREEN);  
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,  
                    frame.top + CORNER_WIDTH, paint);  
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top  
                    + ScreenRate, paint);  
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,  
                    frame.top + CORNER_WIDTH, paint);  
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top  
                    + ScreenRate, paint);  
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left  
                    + ScreenRate, frame.bottom, paint);  
            canvas.drawRect(frame.left, frame.bottom - ScreenRate,  
                    frame.left + CORNER_WIDTH, frame.bottom, paint);  
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,  
                    frame.right, frame.bottom, paint);  
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,  
                    frame.right, frame.bottom, paint); 
            
            //��smallɨ�����ϵĽǣ��ܹ�8������  
            paint.setColor(Color.GREEN);  
            canvas.drawRect(frame2.left, frame2.top, frame2.left + ScreenRate/2,  
                    frame2.top + CORNER_WIDTH/2, paint);  
            canvas.drawRect(frame2.left, frame2.top, frame2.left + CORNER_WIDTH/2, frame2.top  
                    + ScreenRate/2, paint);  
            canvas.drawRect(frame2.right - ScreenRate/2, frame2.top, frame2.right,  
                    frame2.top + CORNER_WIDTH/2, paint);  
            canvas.drawRect(frame2.right - CORNER_WIDTH/2, frame2.top, frame2.right, frame2.top  
                    + ScreenRate/2, paint);  
            canvas.drawRect(frame2.left, frame2.bottom - CORNER_WIDTH/2, frame2.left  
                    + ScreenRate/2, frame2.bottom, paint);  
            canvas.drawRect(frame2.left, frame2.bottom - ScreenRate/2,  
                    frame2.left + CORNER_WIDTH/2, frame2.bottom, paint);  
            canvas.drawRect(frame2.right - ScreenRate/2, frame2.bottom - CORNER_WIDTH/2,  
                    frame2.right, frame2.bottom, paint);  
            canvas.drawRect(frame2.right - CORNER_WIDTH/2, frame2.bottom - ScreenRate/2,  
                    frame2.right, frame2.bottom, paint);  
  
            //���ڲ�ɨ�����ϵĽǣ��ܹ�8������  
     /*      paint.setColor(Color.GREEN);
           int frame2_width=frame.width()/3;
 	       int frame2_height=frame.height()/3;		
 	       int leftOffset = frame.left+(frame.width() - width)/2;                                                                                                                                                                                                       
 	       int topOffset = frame.top+(frame.height() - height) / 2;
       	 //���Ͻ�
             canvas.drawRect(leftOffset,topOffset, leftOffset + 15,topOffset+ 5,paint);
         	 canvas.drawRect(leftOffset,topOffset, leftOffset + 5,topOffset+ 15,paint);
          	 //���Ͻ�
          	 canvas.drawRect(leftOffset+frame2_width- 30,topOffset, leftOffset+frame2_width,topOffset + 10,paint);
          	 canvas.drawRect(leftOffset+frame2_width- 10,topOffset, leftOffset+frame2_width,topOffset + 30,paint);
          	 //���½�
          	 canvas.drawRect(leftOffset,topOffset+frame2_height - 10,leftOffset + 30,topOffset+frame2_height,paint);
          	 canvas.drawRect(leftOffset,topOffset+frame2_height - 30,leftOffset + 10,topOffset+frame2_height,paint);
          	 //���½�
          	 canvas.drawRect(leftOffset+frame2_width- 30,topOffset+frame2_height - 10,leftOffset+frame2_width,topOffset+frame2_height, paint);
          	 canvas.drawRect(leftOffset+frame2_width- 10,topOffset+frame2_height - 30,leftOffset+frame2_width,topOffset+frame2_height, paint); 
          
            */
              
            //�����м����,ÿ��ˢ�½��棬�м���������ƶ�SPEEN_DISTANCE  
            slideTop += SPEEN_DISTANCE;  
            if(slideTop >= frame2.bottom){  
                slideTop = frame2.top;  
            }  
            canvas.drawRect(frame2.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/2, frame2.right - MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/2, paint);  
     
            //��ɨ����������  
            paint.setColor(Color.WHITE);  
            paint.setTextSize(TEXT_SIZE * density);  
            paint.setAlpha(0x40);  
            paint.setTypeface(Typeface.create("System", Typeface.BOLD));  
            String text="����ά���ɫ�������ڣ����Զ�ɨ��";
            canvas.drawText(text, frame.left, (float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);  
              
              
  
            Collection<ResultPoint> currentPossible = possibleResultPoints;  
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;  
            if (currentPossible.isEmpty()) {  
                lastPossibleResultPoints = null;  
            } else {  
                possibleResultPoints = new HashSet<ResultPoint>(5);  
                lastPossibleResultPoints = currentPossible;  
                paint.setAlpha(OPAQUE);  
                paint.setColor(resultPointColor);  
                for (ResultPoint point : currentPossible) {  
                    canvas.drawCircle(frame.left + point.getX(), frame.top  
                            + point.getY(), 6.0f, paint);  
                }  
            }  
            if (currentLast != null) {  
                paint.setAlpha(OPAQUE / 2);  
                paint.setColor(resultPointColor);  
                for (ResultPoint point : currentLast) {  
                    canvas.drawCircle(frame.left + point.getX(), frame.top  
                            + point.getY(), 3.0f, paint);  
                }  
            }  
  
              
            //ֻˢ��ɨ�������ݣ������ط���ˢ��  
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,  
                    frame.right, frame.bottom);  
              
        }  
    }  
  
    public void drawViewfinder() {  
        resultBitmap = null;  
        invalidate();  
    }  
  
    /** 
     * Draw a bitmap with the result points highlighted instead of the live 
     * scanning display. 
     *  
     * @param barcode 
     *            An image of the decoded barcode. 
     */  
    public void drawResultBitmap(Bitmap barcode) {  
        resultBitmap = barcode;  
        invalidate();  
    }  
  
    public void addPossibleResultPoint(ResultPoint point) {  
        possibleResultPoints.add(point);  
    }  
  
}  