package com.example.yochi.myspending.spend;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yochi.myspending.R;

import java.text.SimpleDateFormat;

public class SpendAdapter extends BaseAdapter {

  Context context;
  LayoutInflater layoutInflater = null;
  SpendLog item;

  public SpendAdapter(Context c, SpendLog log) {
    context = c;
    layoutInflater = LayoutInflater.from(context);
    item = log;
  }

  @Override
  public int getCount() {
      return 4;
  }

  @Override
  public Object getItem(int position) {
    Object val = null;
    switch(position){
      // 金額
      case 0:
        val = item.getAmount();
        break;
      // カテゴリ
      case 1:
        val = item.getCategoryCode();
        break;
      // メモ
      case 2:
        val = item.getMemo();
        break;
      // 支払い日時
      case 3:
        val = item.getPaymentDate();
        break;
    }
    return val;
  }

  @Override
  public long getItemId(int position) {
      return position;
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    if(convertView == null){
      convertView = layoutInflater.inflate(R.layout.row_spend, null);
    }

    TextView valueText = (TextView)convertView.findViewById(R.id.value);
    String name = "";
    String value = "";

    switch(position){
      // 金額
      case 0:
        name = "金額";
        value = String.format("%,d円", item.getAmount());
        break;
      // カテゴリ
      case 1:
        name = "カテゴリ";
        value = item.getCategoryName();
        break;
      // メモ
      case 2:
        name = "メモ";
        value = item.getMemo();
        break;
      // 支払い日時
      case 3:
        name = "支払い日";
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        value = df.format(item.getPaymentDate());
        break;
    }
    ((TextView)convertView.findViewById(R.id.name)).setText(name);

    TextView v = (TextView)convertView.findViewById(R.id.value);
    v.setText(value);

    return convertView;
  }

  public void setSpendLog(SpendLog v){
    item = v;
  }
  public SpendLog getSpendLog(){
    return(item);
  }
}
