package com.aibeile_diaper.mm.adapter;



import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CommPagerAdapter extends PagerAdapter{

	private List<View> list;
	
	public CommPagerAdapter(List<View> list){
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
    public void destroyItem(View view, int position, Object arg2) {
        ViewPager pViewPager = ((ViewPager) view);
        pViewPager.removeView(list.get(position));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public Object instantiateItem(View view, int position) {
        ViewPager pViewPager = ((ViewPager) view);
        pViewPager.addView(list.get(position));
        return list.get(position);
    }



    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }
}
