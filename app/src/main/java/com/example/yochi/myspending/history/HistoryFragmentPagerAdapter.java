package com.example.yochi.myspending.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.yochi.myspending.util.MyDateUtil;

public class HistoryFragmentPagerAdapter extends FragmentStatePagerAdapter {

  public HistoryFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {

    // Timestamp型の起点（1970/01/01 00:00）をgetItem(0)とする
    Fragment fragment = new HistoryFragment();
    Bundle args = new Bundle();
    args.putSerializable("date", MyDateUtil.addDate(null, i));
    fragment.setArguments(args);
    return fragment;

  }

  @Override
  public int getCount() {
    return Integer.MAX_VALUE;
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return "Page " + position;
  }
}
