package com.example.yochi.myspending.Summary;

import com.example.yochi.myspending.Spend.SpendLog;

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
