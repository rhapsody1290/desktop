package com.aibeile_diaper.mm.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.aibeile_diaper.mm.activity.R;
import com.aibeile_diaper.mm.view.MyListView;
import com.aibeile_diaper.mm.view.MyListView.IMyListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class HotInforActivity extends Activity implements IMyListViewListener{

	private MyListView mListView;
	private SimpleAdapter mAdapter1;
	private Handler mHandler;
	private ArrayList<HashMap<String, Object>> dlist;
	private int Infosum;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_hotinfor);
		//setContentView(R.layout.fragment_mainpage);		
		/** 下拉刷新，上拉加载 */	
		dlist = new ArrayList<HashMap<String, Object>>();
		mListView = (MyListView) findViewById(R.id.listview);// 你这个listview是在这个layout里面
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		Infosum=0;
		mAdapter1 = new SimpleAdapter(HotInforActivity.this, getData(),
				R.layout.list_items, new String[] { "name", "img", "content" },
				new int[] { R.id.title, R.id.mImage, R.id.content });
		mListView.setAdapter(mAdapter1);
		mListView.setMyListViewListener(this);
		mHandler = new Handler();
		//System.out.println("hi");
		Log.v("tag","debug");
		//Toast.makeText(this, "on down", Toast.LENGTH_SHORT).show();
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			// ��position�����ʱ�����÷�����
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				
				//Toast.makeText(Context, "on down", Toast.LENGTH_SHORT).show();
				System.out.println(position+"被单击啦");
				Intent intentnews = new Intent(HotInforActivity.this, DetailNewsActivity.class);
				intentnews.putExtra("Url",url[position-1]);
				startActivity(intentnews);
		
			}
		});
		mListView.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			// ��position�ѡ��ʱ�����÷�����
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				System.out.println(position + "被选中啦");
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});		
		

	}
	

	private ArrayList<HashMap<String, Object>> getData() {
		
		for (int i = 0; i < data.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", data[i]);
			map.put("content", data1[i]);
			map.put("img", imageIds[i]);
			dlist.add(map);
		}
		//Infosum=Infosum+。。。；
		return dlist;
	}

	/** 停止刷新， */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		String text = mDateFormat.format(System.currentTimeMillis());
		mListView.setRefreshTime(text);
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				dlist.clear();
				Infosum=0;
				getData();
				mListView.setAdapter(mAdapter1);
				onLoad();
			}
		}, 2000);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				mAdapter1.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return false;
	}
	/** 初始化本地数据 */
	String data[] = new String[] { "美国梅奥心脏干细胞技术", "七种隔夜食物含剧毒", "中国首台心衰超滤治疗设备进医院我是" };
	String data1[] = new String[] { "由中国医学国际交流促进会专家工作委员会、北京中华医学会北京分会等6家单位" +
			"共同主办的“缺血性心力衰竭干细胞治疗国际高峰论坛暨C-Cure®技术论证及国际多中心中国区III期临床试验中国首台心衰超滤治疗设备进医院",
			"汤最好的保存方法是汤底不要放盐之类的调味料，煮好汤用干净的勺子勺出当天要喝的，喝不完的，最好用瓦锅存放在冰箱里。" +
			"因为剩汤长时间盛在铝锅、不锈钢锅内，易发生化学反应，应",
			"充血性心力衰竭患者常因心功能失代偿需反复住院治疗，社会及经济负担巨大，而超滤治疗则能帮助心衰患者有效缓解痛苦。" +
			"日前，中国首台心衰超滤设备FQ-16心衰超滤治疗设备进入ffffffffff"
					};
	int[] imageIds=new int[] {R.drawable.newspic1,R.drawable.newspic2,R.drawable.newspic3};
	String url[]=new String[]{"file:///android_asset/plant/index.html",
			"file:///android_asset/plant/index.html",
			"file:///android_asset/plant/index.html"};
	
	
}
