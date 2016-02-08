package com.zxing.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aibeile_diaper.mm.activity.MainActivity;
import com.aibeile_diaper.mm.activity.R;
import com.aibeile_diaper.mm.activity.ScanActivity;
import com.aibeile_diaper.mm.util.FileUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.PerspectiveTransform;
import com.google.zxing.qrcode.QRCodeReader;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.image.RGBLuminanceSource;
import com.zxing.view.ViewfinderView;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
@SuppressLint("NewApi") public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;
	private ImageButton mImageButton;
	
	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private ProgressDialog mProgress;
	private String photo_path;
	private Bitmap scanBitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
	}
	
	

	/**
	 * 扫描二维码图片的方法
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);//最后返回的坐标要乘2
	//	int sampleSize = (int) (options.outHeight / (float) 100);//若除100，则最后返回的坐标要乘4
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);//将图片压缩2倍，防止有过大的图片
	//	scanBitmap = BitmapFactory.decodeFile(path);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		Rect mmrect=CameraManager.get().getFramingRect();      
		System.out.println(mmrect.left+"-----"+mmrect.top+"---"+mmrect.bottom+"---"+mmrect.right+"--"+mmrect.width()+"---"+mmrect.height());      
		String resultString = result.getText();
		Bitmap mbitmap=barcode;
		//FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else 
		{
			ResultPoint[] point = result.getResultPoints();
	 		for(int i=0;i<point.length;i++)
			{System.out.println("point["+i+"] "+point[i].getX()+" "+point[i].getY());}	
	 		float bottomleft_x = point[0].getX();
	 		float bottomleft_y = point[0].getY();
	 		float topleft_x = point[1].getX();
	 		float topleft_y = point[1].getY();
	 		float topright_x = point[2].getX();
	 		float topright_y = point[2].getY();
	 		float bottomright_x=0;
	 		float bottomright_y=0;
	 		float sourceBottomRightX = 34.125f;
	 		float sourceBottomRightY = 34.125f;
	 		if(point.length==3)
	 		{bottomright_x = topright_x+bottomleft_x-topleft_x-3.0f;
	 		 bottomright_y = topright_y+bottomleft_y-topleft_y;	
	 		sourceBottomRightX += 3.0f;
	 		sourceBottomRightY += 3.0f;
	 		}
	 		else{
	 		bottomright_x = point[3].getX();
	 		bottomright_y = point[3].getY();
	 		
	 		}
	 	//	float center_x = (topright_x+topleft_x)/2;//相对于扫描框的坐标
	 	//	float center_y = (topleft_y+bottomleft_y)/2;//相对于扫描框的坐标
	 		float source_center_x = (topright_x+topleft_x)*0.5f+mmrect.left;//相对于屏幕的坐标
	 		float source_center_y = (topleft_y+bottomleft_y)*0.5f+mmrect.top;//相对于屏幕的坐标
	 		float k_size=(topright_x-topleft_x)/(1.3333f*0.762f);
	// 	    mbitmap= Bitmap.createBitmap(barcode, mmrect.left, mmrect.top, mmrect.width(), mmrect.height(),  null, false);// 截取扫描框中的图片	
	 		mbitmap= Bitmap.createBitmap(barcode, (int)(source_center_x-k_size*1.7), (int)(source_center_y-k_size*1.7), (int)(k_size*3.5), (int)(k_size*3.5),  null, false);// 截取所有颜色块的图片
	 		String savedname = DateFormat.format("result"+"yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";
	    	FileUtil.saveBitmap(mbitmap,savedname);
	   //二维码相对于截出来的图的坐标
	        bottomleft_x = bottomleft_x-(source_center_x-k_size*1.7f-mmrect.left);
	        bottomleft_y=bottomleft_y-(source_center_y-k_size*1.7f-mmrect.top);
	        topleft_x = topleft_x-(source_center_x-k_size*1.7f-mmrect.left);
	        topleft_y=topleft_y-(source_center_y-k_size*1.7f-mmrect.top);
	        topright_x = topright_x-(source_center_x-k_size*1.7f-mmrect.left);
	        topright_y = topright_y-(source_center_y-k_size*1.7f-mmrect.top);
	        bottomright_x = bottomright_x-(source_center_x-k_size*1.7f-mmrect.left);
	        bottomright_y=bottomright_y-(source_center_y-k_size*1.7f-mmrect.top);
	  
	 		float offset=0;
	 		float points[] ={4.6875f-offset,4.6875f-offset,
			         20.3125f-offset,4.6875f-offset,
			         35.9375f-offset,4.6875f-offset,
			         51.5625f-offset,4.6875f-offset,
			         4.6875f-offset,20.3125f-offset,
			         51.5625f-offset,20.3125f-offset,
			         4.6875f-offset,35.9375f-offset,
			         51.5625f-offset,35.9375f-offset,
			         4.6875f-offset,51.5625f-offset,
			         20.3125f-offset,51.5625f-offset,
			         35.9375f-offset,51.5625f-offset,
			         51.5625f-offset,51.5625f-offset};
	 		PerspectiveTransform p = PerspectiveTransform.quadrilateralToQuadrilateral(19.125f,19.125f,
	 				                                                                   37.125f,19.125f,
	 				                                                                   sourceBottomRightX,sourceBottomRightY,
	 				                                                                   19.125f,37.125f,
	 				                                                                   topleft_x, topleft_y,
	 				                                                                   topright_x,topright_y,
	 				                                                                   bottomright_x, bottomright_y,
	 				                                                                   bottomleft_x, bottomleft_y);
	 		
	 		p.transformPoints(points);
	 		
	 		for(int i=0;i<points.length;i=i+2)
			{System.out.println(i+" "+points[i] + "," + points[i+1]);}	
	 		int points_count=0;
	 		while((points_count<=22)&&(points[points_count]>0)&&(points[points_count]<mbitmap.getWidth())&&(points[points_count+1]>0)&&(points[points_count+1]<mbitmap.getHeight()))
	 		{
	 			points_count=points_count+2;
	 		}
	 		if(points_count!=24)
	 		{
	 		Toast.makeText(CaptureActivity.this, "error！", Toast.LENGTH_LONG).show();
	 		Intent intent1 = new Intent(CaptureActivity.this,MainActivity.class);
			MainActivity.TAG="B";	
			startActivity(intent1);
	 		
	 		}
	 		else
	 		{///String savedname = DateFormat.format("result"+"yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";
	    	//FileUtil.saveBitmap(mbitmap,savedname);
	    	photo_path=FileUtil.getSDPath() + java.io.File.separator+ "imgtest"+java.io.File.separator+savedname;
	 		System.out.println("count="+points_count);
			Intent resultIntent = new Intent(CaptureActivity.this,ScanActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			bundle.putFloatArray("12_point", points);
			bundle.putFloat("k_size", k_size);
	     
		    bundle.putString("result_savename", photo_path);   
			resultIntent.putExtras(bundle);
			startActivity(resultIntent);
			CaptureActivity.this.finish();
	 		}
		}	
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}
	
	

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private static PerspectiveTransform createTransform(ResultPoint topLeft,
            ResultPoint topRight,
            ResultPoint bottomLeft,
            ResultPoint alignmentPattern,
            int dimension) {
float dimMinusThree = (float) dimension - 3.5f;
float bottomRightX;
float bottomRightY;
float sourceBottomRightX;
float sourceBottomRightY;
if (alignmentPattern != null) {
bottomRightX = alignmentPattern.getX();
bottomRightY = alignmentPattern.getY();
sourceBottomRightX = dimMinusThree - 3.0f;
sourceBottomRightY = sourceBottomRightX;
} else {
// Don't have an alignment pattern, just make up the bottom-right point
bottomRightX = (topRight.getX() - topLeft.getX()) + bottomLeft.getX();
bottomRightY = (topRight.getY() - topLeft.getY()) + bottomLeft.getY();
sourceBottomRightX = dimMinusThree;
sourceBottomRightY = dimMinusThree;
}

return PerspectiveTransform.quadrilateralToQuadrilateral(
3.5f,
3.5f,
dimMinusThree,
3.5f,
sourceBottomRightX,
sourceBottomRightY,
3.5f,
dimMinusThree,
topLeft.getX(),
topLeft.getY(),
topRight.getX(),
topRight.getY(),
bottomRightX,
bottomRightY,
bottomLeft.getX(),
bottomLeft.getY());
}
}
	