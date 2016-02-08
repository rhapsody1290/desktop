package com.aibeile_diaper.mm.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.aibeile_diaper.mm.util.FileUtil;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailInformationActivity extends Activity {
  private View head,id,sex,birthday,name,registerdate,telephone,email,address,qinqingzhanghao,qinqingpassword,doctorrecord,disease;
  private ImageView HeadImage,back_button;
  private TextView IdText,SexText,BirthdayText,NameText,RegisterDateText,TelephoneText,EmailText,AddressText,QinqingzhanghaoText,QinqingpasswordText;
  private int HEAD_RESULT=0X01;
  private EditText editInput;
  private LayoutInflater inflater;
  private View textEntryView;
  
  @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		setListener();
	}
   private void initView()
   {head= findViewById(R.id.detail_head);
    id= findViewById(R.id.detail_id);
    sex= findViewById(R.id.detail_sex);
    birthday= findViewById(R.id.detail_birthday);
    name= findViewById(R.id.detail_name);
    registerdate= findViewById(R.id.detail_registerdate);
    telephone= findViewById(R.id.detail_telephone);
    email= findViewById(R.id.detail_email);
    address= findViewById(R.id.detail_address);
    qinqingzhanghao= findViewById(R.id.detail_qinqingzhanghao);
    qinqingpassword= findViewById(R.id.detail_qinqingpassword);
    doctorrecord= findViewById(R.id.detail_doctorrecord);
    disease= findViewById(R.id.detail_disease);
    HeadImage= (ImageView)findViewById(R.id.detail_head_image);
    IdText= (TextView)findViewById(R.id.detail_id_textview);
    SexText= (TextView)findViewById(R.id.detail_sex_textview);
    BirthdayText= (TextView)findViewById(R.id.detail_birthday_textview);
    NameText= (TextView)findViewById(R.id.detail_name_textview);
    RegisterDateText= (TextView)findViewById(R.id.detail_registerdate_textview);
    TelephoneText= (TextView)findViewById(R.id.detail_telephone_textview);
    EmailText= (TextView)findViewById(R.id.detail_email_textview);
    AddressText= (TextView)findViewById(R.id.detail_address_textview);
    QinqingzhanghaoText= (TextView)findViewById(R.id.detail_qinqingzhanghao_textview);
    QinqingpasswordText= (TextView)findViewById(R.id.detail_qinqingpassword_textview);	 
	back_button=(ImageView) findViewById(R.id.header_back_button);
	
  
   }
   private void setListener()
   {head.setOnClickListener(onClickListener);
    id.setOnClickListener(onClickListener);
    sex.setOnClickListener(onClickListener);
    birthday.setOnClickListener(onClickListener);
    name.setOnClickListener(onClickListener);
    registerdate.setOnClickListener(onClickListener);
    telephone.setOnClickListener(onClickListener);
    email.setOnClickListener(onClickListener);
    address.setOnClickListener(onClickListener);
    qinqingzhanghao.setOnClickListener(onClickListener);
    qinqingpassword.setOnClickListener(onClickListener);
    doctorrecord.setOnClickListener(onClickListener);
    disease.setOnClickListener(onClickListener);
    back_button.setOnClickListener(onClickListener);
   }
   private View.OnClickListener onClickListener=new OnClickListener()
   {@Override
	public void onClick(View v)
    {switch(v.getId()) 
	 {case R.id.header_back_button:
			DetailInformationActivity.this.finish();	
			break;
      case R.id.detail_head:
		System.out.println("ͼ��ģʽ����--------->");
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		try {
		  startActivityForResult(Intent.createChooser(intent, "��ѡ��һ���ļ�"),HEAD_RESULT);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(DetailInformationActivity.this, "�밲װ�ļ�������", Toast.LENGTH_SHORT).show();
		}
		break;
	  case R.id.detail_id://���ܸ�
		  Toast.makeText(DetailInformationActivity.this, "������������ģ�", Toast.LENGTH_SHORT).show();
		  break;
	  case R.id.detail_sex://���ܸ�
		  Toast.makeText(DetailInformationActivity.this, "������������ģ�", Toast.LENGTH_SHORT).show();
		  break;
	  case R.id.detail_birthday://���ܸ�
		  Toast.makeText(DetailInformationActivity.this, "������������ģ�", Toast.LENGTH_SHORT).show();
		  break;
	  case R.id.detail_name: 
		  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		  textEntryView = inflater.inflate(R.layout.dialog_edittext, null);  
		  editInput=(EditText)textEntryView.findViewById(R.id.editText);  
		  Builder builder = new AlertDialog.Builder(DetailInformationActivity.this);
		  builder.setTitle("��������");
		  builder.setView(textEntryView);
		  builder.setPositiveButton("����",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which){
              NameText.setText(editInput.getText().toString());
			}
		  });
		  builder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
			  dialog.cancel();//�����Ի���
			}
		  });
		  builder.create().show();
		  break;
	  case R.id.detail_registerdate://���ܸ�
		  Toast.makeText(DetailInformationActivity.this, "������������ģ�", Toast.LENGTH_SHORT).show();
		  break;
	  case R.id.detail_telephone:
		  Toast.makeText(DetailInformationActivity.this, "������������ģ�", Toast.LENGTH_SHORT).show();
		  break;
	  case R.id.detail_email:
		  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		  textEntryView = inflater.inflate(R.layout.dialog_edittext, null);  
		  editInput=(EditText)textEntryView.findViewById(R.id.editText);  
		  Builder builder2 = new AlertDialog.Builder(DetailInformationActivity.this);
		  builder2.setTitle("��������");
		  builder2.setView(textEntryView);
		  builder2.setPositiveButton("����",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which){
              EmailText.setText(editInput.getText().toString());
			}
		  });
		  builder2.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
			  dialog.cancel();//�����Ի���
			}
		  });
		  builder2.create().show();
		  break;
	  case R.id.detail_address:
		  break;
	  case R.id.detail_qinqingzhanghao:
		  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		  textEntryView = inflater.inflate(R.layout.dialog_edittext, null);  
		  editInput=(EditText)textEntryView.findViewById(R.id.editText);  
		  Builder builder3 = new AlertDialog.Builder(DetailInformationActivity.this);
		  builder3.setTitle("���������˺�");
		  builder3.setView(textEntryView);
		  builder3.setPositiveButton("����",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which){
              QinqingzhanghaoText.setText(editInput.getText().toString());
			}
		  });
		  builder3.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
			  dialog.cancel();//�����Ի���
			}
		  });
		  builder3.create().show();
		  break;
	  case R.id.detail_qinqingpassword:
		  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		  textEntryView = inflater.inflate(R.layout.dialog_edittext, null);  
		  editInput=(EditText)textEntryView.findViewById(R.id.editText);  
		  Builder builder4 = new AlertDialog.Builder(DetailInformationActivity.this);
		  builder4.setTitle("���������˺�����");
		  builder4.setView(textEntryView);
		  builder4.setPositiveButton("����",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which){
              QinqingpasswordText.setText(editInput.getText().toString());
			}
		  });
		  builder4.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
			  dialog.cancel();//�����Ի���
			}
		  });
		  builder4.create().show();
		  break;
	  case R.id.detail_doctorrecord:
		  Toast.makeText(DetailInformationActivity.this, "��ת���鿴��ҽ��¼ҳ��", Toast.LENGTH_SHORT).show();
		  break;
	  case R.id.detail_disease:
		  Toast.makeText(DetailInformationActivity.this, "��ת�����˼���ʷҳ��", Toast.LENGTH_SHORT).show();
		  break;
	 }   
    }	      
   };
   
   protected void onActivityResult(int requestCode, int resultCode, Intent data) 
   {if (resultCode == Activity.RESULT_CANCELED)
	return;
	if(requestCode==HEAD_RESULT)	
	{Uri uri = data.getData();
	 System.out.println("uri"+uri);
	 String url = FileUtil.getPath_content(DetailInformationActivity.this, uri);	
	 try {
		Bitmap bitmapSelected = BitmapFactory.decodeStream(getContentResolver().openInputStream(
				Uri.fromFile(new File(url))));
		HeadImage.setImageBitmap(bitmapSelected);
	 } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 }
	}
   }
}
