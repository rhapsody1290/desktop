package com.aibeile_diaper.mm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class FileUtil {
	/*** 获取sd卡的路径 @return 路径的字符串*/
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
		}
		return sdDir.toString();
	}
	
	public static void saveBitmap(Bitmap bmp,String savedname)
	  {
		   
	  	  FileOutputStream b = null;  
	  	 String IMG_PATH = FileUtil.getSDPath() + java.io.File.separator+ "imgtest";
	  	 System.out.println("IMG_PATH = "+IMG_PATH);
	  	  File path = new File(IMG_PATH);
	    	if (!path.exists()) {path.mkdirs();}
	         try {  
	              b = new FileOutputStream(new File(IMG_PATH, savedname));  
	              bmp.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
	             } catch (FileNotFoundException e) {  
	                 e.printStackTrace();  
	             } finally {  
	               try {  
	                b.flush();  
	                b.close();  
	                } catch (IOException e) {  
	                e.printStackTrace();  
	                }
	             }
		  
	  }
	    
	    /**  
	     * 复制单个文件  
	     * @param oldPath String 原文件路径 如：c:/fqf.txt  
	     * @param newPath String 复制后路径 如：f:/fqf.txt  
	     * @return boolean  
	     */   
	   public static void copyFile(String oldPath, String newPath) {   
	       try {   
	           int bytesum = 0;   
	           int byteread = 0;   
	           File oldfile = new File(oldPath);   
	           if (oldfile.exists()) { //文件存在时   
	               InputStream inStream = new FileInputStream(oldPath); //读入原文件   
	               FileOutputStream fs = new FileOutputStream(newPath);   
	               byte[] buffer = new byte[1444];   
	               int length;   
	               while ( (byteread = inStream.read(buffer)) != -1) {   
	                   bytesum += byteread; //字节数 文件大小   
	                   System.out.println(bytesum);   
	                   fs.write(buffer, 0, byteread);   
	               }   
	               inStream.close();   
	           }   
	       }   
	       catch (Exception e) {   
	           System.out.println("复制单个文件操作出错");   
	           e.printStackTrace();   
	  
	       }   
	  
	   }  
	   
	   @SuppressLint("NewApi") 
	   public static String getPath_content(final Context context, final Uri uri) {  
		   
		    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
		  
		    // DocumentProvider  
		    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
		        // ExternalStorageProvider  
		        if (isExternalStorageDocument(uri)) {  
		            final String docId = DocumentsContract.getDocumentId(uri);  
		            final String[] split = docId.split(":");  
		            final String type = split[0];  
		  
		            if ("primary".equalsIgnoreCase(type)) {  
		                return Environment.getExternalStorageDirectory() + "/" + split[1];  
		            }  
		  
		            // TODO handle non-primary volumes  
		        }  
		        // DownloadsProvider  
		        else if (isDownloadsDocument(uri)) {  
		  
		            final String id = DocumentsContract.getDocumentId(uri);  
		            final Uri contentUri = ContentUris.withAppendedId(  
		                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
		  
		            return getDataColumn(context, contentUri, null, null);  
		        }  
		        // MediaProvider  
		        else if (isMediaDocument(uri)) {  
		            final String docId = DocumentsContract.getDocumentId(uri);  
		            final String[] split = docId.split(":");  
		            final String type = split[0];  
		  
		            Uri contentUri = null;  
		            if ("image".equals(type)) {  
		                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
		            } else if ("video".equals(type)) {  
		                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
		            } else if ("audio".equals(type)) {  
		                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
		            }  
		  
		            final String selection = "_id=?";  
		            final String[] selectionArgs = new String[] {  
		                    split[1]  
		            };  
		  
		            return getDataColumn(context, contentUri, selection, selectionArgs);  
		        }  
		    }  
		    // MediaStore (and general)  
		    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
		  
		        // Return the remote address  
		        if (isGooglePhotosUri(uri))  
		            return uri.getLastPathSegment();  
		  
		        return getDataColumn(context, uri, null, null);  
		    }  
		    // File  
		    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
		        return uri.getPath();  
		    }  
		  
		    return null;  
		}  
		  
		/** 
		 * Get the value of the data column for this Uri. This is useful for 
		 * MediaStore Uris, and other file-based ContentProviders. 
		 * 
		 * @param context The context. 
		 * @param uri The Uri to query. 
		 * @param selection (Optional) Filter used in the query. 
		 * @param selectionArgs (Optional) Selection arguments used in the query. 
		 * @return The value of the _data column, which is typically a file path. 
		 */  
		public static String getDataColumn(Context context, Uri uri, String selection,  
		        String[] selectionArgs) {  
		  
		    Cursor cursor = null;  
		    final String column = "_data";  
		    final String[] projection = {  
		            column  
		    };  
		  
		    try {  
		        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
		                null);  
		        if (cursor != null && cursor.moveToFirst()) {  
		            final int index = cursor.getColumnIndexOrThrow(column);  
		            return cursor.getString(index);  
		        }  
		    } finally {  
		        if (cursor != null)  
		            cursor.close();  
		    }  
		    return null;  
		}  
		  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is ExternalStorageProvider. 
		 */  
		public static boolean isExternalStorageDocument(Uri uri) {  
		    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is DownloadsProvider. 
		 */  
		public static boolean isDownloadsDocument(Uri uri) {  
		    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is MediaProvider. 
		 */  
		public static boolean isMediaDocument(Uri uri) {  
		    return "com.android.providers.media.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is Google Photos. 
		 */  
		public static boolean isGooglePhotosUri(Uri uri) {  
		    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
		}  

}

