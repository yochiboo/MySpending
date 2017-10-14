package com.example.yochi.myspending.History;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;

import com.example.yochi.myspending.Summary.Summary;
import com.example.yochi.myspending.Summary.SummaryFragment;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

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
