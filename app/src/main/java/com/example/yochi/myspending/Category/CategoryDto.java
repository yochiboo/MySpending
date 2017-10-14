package com.example.yochi.myspending.Category;

import android.database.Cursor;

/**
 * Created by yochi on 2017/07/06.
 */

public class CategoryDto extends Category {

  public static Category parseCategory(Cursor c){
    Category item = new Category();

    int columnIndex = c.getColumnIndex("code");
    if(columnIndex >= 0){
      item.setCode(c.getInt(columnIndex));
    }

    columnIndex = c.getColumnIndex("name");
    if(columnIndex >= 0){
      item.setName(c.getString(columnIndex));
    }

    columnIndex = c.getColumnIndex("class_code");
    if(columnIndex >= 0){
      item.setClassCode(c.getInt(columnIndex));
    }

    columnIndex = c.getColumnIndex("parent_code");
    if(columnIndex >= 0){
      item.setParentCode(c.getInt(columnIndex));
    }

    columnIndex = c.getColumnIndex("view_order");
    if(columnIndex >= 0){
      item.setViewOrder(c.getInt(columnIndex));
    }

    columnIndex = c.getColumnIndex("icon_src");
    if(columnIndex >= 0){
      item.setIconSrc(c.getString(columnIndex));
    }

    return item;
  }

}
