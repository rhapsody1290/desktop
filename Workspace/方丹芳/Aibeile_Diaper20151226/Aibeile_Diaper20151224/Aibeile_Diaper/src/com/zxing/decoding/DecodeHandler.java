/*
 * Copyright (C) 2010 ZXing authors
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

package com.zxing.decoding;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabWidget;

import com.aibeile_diaper.mm.activity.MainActivity;
import com.aibeile_diaper.mm.activity.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.zxing.activity.CaptureActivity;
import com.zxing.camera.CameraManager;
import com.zxing.camera.PlanarYUVLuminanceSource;

@SuppressLint("NewApi") final class DecodeHandler extends Handler {

  private static final String TAG = DecodeHandler.class.getSimpleName();

  private final CaptureActivity activity;
  private final MultiFormatReader multiFormatReader;

  DecodeHandler(CaptureActivity activity, Hashtable<DecodeHintType, Object> hints) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    this.activity = activity;
  }

  @Override
  public void handleMessage(Message message) {
    switch (message.what) {
      case R.id.decode:
        //Log.d(TAG, "Got decode message");
        decode((byte[]) message.obj, message.arg1, message.arg2);
        break;
      case R.id.quit:
        Looper.myLooper().quit();
        break;
    }
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
   * reuse the same reader objects from one decode to the next.
   *
   * @param data   The YUV preview frame.
   * @param width  The width of the preview frame.
   * @param height The height of the preview frame.
   */
  private void decode(byte[] data, int width, int height) {
    long start = System.currentTimeMillis();
    Result rawResult = null;
    int data_w=width;
    int data_h=height;
    
    //modify here
    byte[] rotatedData = new byte[data.length];
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++)
            rotatedData[x * height + height - y - 1] = data[x + y * width];
    }
    int tmp = width; // Here we are swapping, that's the difference to #11
    width = height;
    height = tmp;
    
    PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
 //   BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
    try {
      rawResult = multiFormatReader.decodeWithState(bitmap);
    } catch (ReaderException re) {
      // continue
    } finally {
      multiFormatReader.reset();
    }

    if (rawResult != null) {
    	   long end = System.currentTimeMillis();
    	      Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
    	      Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, rawResult);
    	      Bundle bundle = new Bundle();
    	   //   bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
    	      //��Դ��
    	      int w = data_w;  //���
    	      int h = data_h;
    	      YuvImage image = new YuvImage(data, ImageFormat.NV21, w, h, null);
    	      ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
    	      if(!image.compressToJpeg(new Rect(0, 0, w, h), 100, os)){
    	         System.out.println("error1");
    	      }
    	      byte[] tmp1 = os.toByteArray();
    	      Bitmap bmp = BitmapFactory.decodeByteArray(tmp1, 0,tmp1.length); 
    	      // ����ķ�����Ҫ�����ǰ�ͼƬתһ���Ƕȣ�Ҳ���ԷŴ���С��  
    	      Matrix m = new Matrix();  
    	      m.setRotate(90); // ��תangle��  
    	      bmp= Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),  m, true);// ��������ͼƬ  
    	      bundle.putParcelable(DecodeThread.BARCODE_BITMAP, bmp);   
    	      //��Դ��
    	      message.setData(bundle);
    	      Log.d(TAG, "Sending decode succeeded message...");
    	      message.sendToTarget();
    } else {
      Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
      message.sendToTarget();
    }
  }

}
