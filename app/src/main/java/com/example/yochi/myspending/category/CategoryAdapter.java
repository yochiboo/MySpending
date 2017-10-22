package com.example.yochi.myspending.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yochi.myspending.R;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Category> items;

    public CategoryAdapter(Context c, ArrayList<Category> list) {
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
        return items.get(position).getCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if(convertView == null){
        convertView = layoutInflater.inflate(R.layout.row_category, null);
      }
      Category item = items.get(position);
      String name = "";
      if(item.getParentCode() > 0){
        name += "  ";
      }
      name += item.getName();

      int parentId = items.get(position).getParentCode();
      ((TextView)convertView.findViewById(R.id.name)).setText(name);
      return convertView;
    }

  public ArrayList<Category> getItems() {
    return items;
  }

  public void setItems(ArrayList<Category> items) {
    this.items = items;
  }
}
