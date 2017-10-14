package com.example.yochi.myspending.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yochi.myspending.R;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

  Context context;
  LayoutInflater layoutInflater = null;
  ArrayList<HistoryLog> histiries;

  public HistoryAdapter(Context c, ArrayList<HistoryLog> list) {
    context = c;
    layoutInflater = LayoutInflater.from(context);
    histiries = list;
  }

  @Override
  public int getCount() {
      return histiries.size();
  }

  @Override
  public Object getItem(int position) {
    return histiries.get(position);
  }

  @Override
  public long getItemId(int position) {
      return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    if(convertView == null){
      convertView = layoutInflater.inflate(R.layout.row_history, null);
    }

    HistoryLog log = histiries.get(position);
    ((TextView)convertView.findViewById(R.id.name)).setText(log.getCategoryName());
    ((TextView)convertView.findViewById(R.id.value)).setText(String.format("%,3d" + context.getString(R.string.amount_unit), log.getAmount()));

    return convertView;
  }

  public ArrayList<HistoryLog> getHistiries() {
    return histiries;
  }

  public void setHistiries(ArrayList<HistoryLog> histiries) {
    this.histiries = histiries;
  }
}
