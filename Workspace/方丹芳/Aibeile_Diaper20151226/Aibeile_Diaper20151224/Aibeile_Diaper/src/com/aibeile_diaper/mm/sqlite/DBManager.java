package com.aibeile_diaper.mm.sqlite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DataBaseHelper helper;
	private SQLiteDatabase db;
	private static File path = new File("/sdcard/YanXuXu");// 创建目录
	private static File file = new File("/sdcard/YanXuXu/YanXuXu_core.db");// 创建文件
	public DBManager(Context context) {
		if (!path.exists()) { // 目录不存在返回false
			   path.mkdirs();// 创建一个目录
			  }
			  if (!file.exists()) { // 文件不存在返回false
				   helper = new DataBaseHelper(context);	
				     try {  
				         helper.createDataBase();  
				     } catch (IOException ioe) {  	   
				       throw new Error("Unable to create database");  
				     }
			  }
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
	}	
	/**** 删除数据表* **/
	public void drop_table(String tablename){
	//	db = helper.getWritableDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
		db.execSQL("DROP TABLE " + tablename);
		System.out.println("删除成功！");
		db.close();
		}
	
	/**** 清空数据表记录* **/
	public void clear_table(String tablename){	
	//	db = helper.getWritableDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
		db.delete(tablename, null, null);
		db.close();
		}
	/***删除某一条记录* **/
	public void delect_record(String table, String whereClause, String[] whereArgs){
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
	//	db = helper.getWritableDatabase();
		db.delete(table, whereClause, whereArgs);
		db.close();
		}
    /***插入某一条记录***/
	public void insert_record(String table,String nullColumnHack,ContentValues values){
	//	db = helper.getWritableDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
		long row_id=db.insert(table, nullColumnHack, values);
		db.close();
	}
	/***更新某一条记录***/
	public void update_record(String table,ContentValues values,String whereClause, String[] whereArgs){
	//	db = helper.getWritableDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
		int row_id=db.update(table, values, whereClause, whereArgs);
		db.close();
	}
	/***查询*****/
/*	public Cursor select_record(String table, String[] columns, String selection, String[] selectionArgs){
	//	db = helper.getReadableDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
		Cursor cursor = db.query(table, columns, selection, selectionArgs,null,null,null);
		return cursor;
	}
*/
	public Cursor select_record(String table, String[] columns, String[] select_condition, String[] select_condition_values,String mark){   
		    Cursor cursor=null;
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
			if(select_condition==null)
			{if(columns==null)
			   {cursor = db.rawQuery("SELECT * FROM " + table, null);}
			else{String str_select_context = toString(columns, ",");	
			     cursor = db.rawQuery("SELECT "+ str_select_context + " FROM " + table, null);}
			}
			else
			{ String str_select_condition = toString(select_condition, mark);
				if(columns==null)
			   {cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + str_select_condition, select_condition_values );}
			 else
			  {
				 String str_select_context = toString(columns, ",");
					cursor = db.rawQuery("SELECT DISTINCT "
							+ str_select_context + " FROM " + table + " WHERE "
							+ str_select_condition, select_condition_values );	
			  }		
			}
		
	//		db.close();加上这句，就会报空指针错误
			return cursor;
		}	
	
  /*** 获取所有没有上传的检测结果，存成***/ 
	public ArrayList<HashMap<String, String>> detect_upload()
	{ArrayList<HashMap<String, String>> dataset = new ArrayList<HashMap<String, String>>();
	 Cursor cursor = select_record("Detect", null, new String[]{"upload_flag LIKE ?"}, new String[]{"1"}, "AND");
	 if (cursor != null) {
	  if (cursor.moveToFirst()) {
		do {
		  HashMap<String, String> temp_dataset = new HashMap<String, String>();
				int numColumn = cursor.getColumnIndex("user_id");
				temp_dataset.put("user_id",cursor.getString(cursor.getColumnIndex("user_id")));
				temp_dataset.put("detect_date",cursor.getString(cursor.getColumnIndex("detect_date")));
				temp_dataset.put("detect_age_month",cursor.getString(cursor.getColumnIndex("detect_age_month")));
				temp_dataset.put("qrcode",cursor.getString(cursor.getColumnIndex("qrcode")));
				temp_dataset.put("product_id",cursor.getString(cursor.getColumnIndex("product_id")));
				temp_dataset.put("detail",cursor.getString(cursor.getColumnIndex("detail")));
				temp_dataset.put("qualified",cursor.getString(cursor.getColumnIndex("qualified")));
				temp_dataset.put("score",cursor.getString(cursor.getColumnIndex("score")));
				temp_dataset.put("qualified",cursor.getString(cursor.getColumnIndex("qualified")));
				for(int i=1;i<=12;i++)
				{	
				Cursor cursor_1 = select_record("DetectItemValue", null, new String[]{"id LIKE ?"}, new String[]{cursor.getString(cursor.getColumnIndex("value_id"+i))}, "AND");
				JSONObject value_1 = new JSONObject();
				  while (cursor_1.moveToNext()){
			       try {
					value_1.put("standard_id", cursor_1.getString(cursor_1.getColumnIndex("standard_id")));
					value_1.put("r", cursor_1.getString(cursor_1.getColumnIndex("value_r")));
					value_1.put("g", cursor_1.getString(cursor_1.getColumnIndex("value_g")));
					value_1.put("b", cursor_1.getString(cursor_1.getColumnIndex("value_b")));
					value_1.put("spot", cursor_1.getString(cursor_1.getColumnIndex("spot")));
				   } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				    }	
		           }
				temp_dataset.put("value_"+i,value_1.toString());
				}
			dataset.add(temp_dataset);	
			} while (cursor.moveToNext());
		}else{					
			System.out.println(" 没有匹配的数据");
		}
	}
	 return dataset;
	}
	/**
	 * 将数组内容转换成字符串形式
	 * **/
	public static String toString(String[] Stringto, String mark) {
		String result = "";
		if (Stringto.length == 0) {
			result = "";
		} else {
			for (int i = 0; i < Stringto.length - 1; i++) {
				result = result + Stringto[i] + " " + mark + " ";
			}
			result = result + Stringto[Stringto.length - 1];
		}
		return result;
	}	
	
	
	
	
	
}
