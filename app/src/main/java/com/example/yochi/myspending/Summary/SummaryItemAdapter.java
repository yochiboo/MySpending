package com.example.yochi.myspending.Summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yochi.myspending.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SummaryItemAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<SummaryItem> items = null;

    public SummaryItemAdapter(Context c, ArrayList<SummaryItem> list) {
      context = c;
      layoutInflater = LayoutInflater.from(context);
      items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
      return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if(convertView == null){
        convertView = layoutInflater.inflate(R.layout.row_summary_item, null);
      }

      if(items.size() > 0){
        SummaryItem item = items.get(position);
        ((TextView)convertView.findViewById(R.id.name)).setText(item.getCategoryName());
        ((TextView)convertView.findViewById(R.id.value)).setText(String.format("%,3d" + context.getString(R.string.amount_unit), item.getAmount()));
      }

      return convertView;
    }
}
