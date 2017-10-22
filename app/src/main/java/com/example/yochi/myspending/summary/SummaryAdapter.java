package com.example.yochi.myspending.summary;

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

public class SummaryAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Summary> summaries;

    public SummaryAdapter(Context c, ArrayList<Summary> list) {
      context = c;
      layoutInflater = LayoutInflater.from(context);
      summaries = list;
    }

    @Override
    public int getCount() {
      return summaries.size();
    }

    @Override
    public Object getItem(int position) {
      return summaries.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if(convertView == null){
        convertView = layoutInflater.inflate(R.layout.row_summary, null);
      }

      Summary s = summaries.get(position);
      SimpleDateFormat df1 = new SimpleDateFormat("dd");
      SimpleDateFormat df2 = new SimpleDateFormat("E");
      SimpleDateFormat df3 = new SimpleDateFormat("MM/yyyy");
      ((TextView)convertView.findViewById(R.id.day)).setText(df1.format(s.getSummaryDate()));
      ((TextView)convertView.findViewById(R.id.dayPart)).setText(df2.format(s.getSummaryDate()));
      ((TextView)convertView.findViewById(R.id.month)).setText(df3.format(s.getSummaryDate()));
      ((TextView)convertView.findViewById(R.id.amount)).setText(String.format("%,3d" + context.getString(R.string.amount_unit), s.getAmount()));

      ListView listView = (ListView) convertView.findViewById(R.id.categoryAmountList);
      listView.setAdapter(new SummaryItemAdapter(context, s.getSummaryItems()));

      return convertView;
    }

  public ArrayList<Summary> getSummaries() {
    return summaries;
  }

  public void setSummaries(ArrayList<Summary> summaries) {
    this.summaries = summaries;
  }
}
