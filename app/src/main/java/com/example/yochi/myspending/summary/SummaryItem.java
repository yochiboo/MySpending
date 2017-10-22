package com.example.yochi.myspending.summary;

import java.io.Serializable;

/**
 * Created by yochi on 2017/07/06.
 */

public class SummaryItem implements Serializable {

  // カテゴリコード
  private int categoryCode = 0;
  // カテゴリ毎サマリ
  private String categoryName = "";
  // 収支
  private long amount = 0;

  public SummaryItem(){
    super();
  }

  public int getCategoryCode() {
    return categoryCode;
  }

  public void setCategoryCode(int categoryCode) {
    this.categoryCode = categoryCode;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}
