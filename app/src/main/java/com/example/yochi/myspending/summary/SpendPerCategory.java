package com.example.yochi.myspending.summary;

import com.example.yochi.myspending.spend.SpendLog;

/**
 * Created by yochi on 2017/07/06.
 */

public class SpendPerCategory extends SpendLog {

  // カテゴリ名
  private String categoryName = "";

  public SpendPerCategory(){
    super();
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}
