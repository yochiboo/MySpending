package com.example.yochi.myspending.summary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class SummaryFragmentPagerAdapter extends FragmentStatePagerAdapter {

  Context context;
  LayoutInflater layoutInflater = null;
  ArrayList<Summary> summaries;

  public SummaryFragmentPagerAdapter(FragmentManager fm, ArrayList<Summary> list) {
    super(fm);
    summaries = list;
  }

  @Override
  public Fragment getItem(int i) {

    Fragment fragment = new SummaryFragment();
    Bundle args = new Bundle();
    Summary s = summaries.get(i);
    args.putSerializable("summary", s);
    fragment.setArguments(args);
    return fragment;

  }

  @Override
  public int getCount() {
    return summaries.size();
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return "Page " + position;
  }

  public ArrayList<Summary> getSummaries() {
    return summaries;
  }

  public void setSummaries(ArrayList<Summary> summaries) {
    this.summaries = summaries;
  }
}
