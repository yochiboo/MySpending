package com.example.yochi.myspending.summary;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by yochi on 2017/07/06.
 */
public class Summary implements Serializable {

  public static final int MODE_MONTHLY = 0;
  public static final int MODE_DAILY = 1;

  // 表示モード（月or日）
  private int mode;
  // 日時
  private Timestamp summaryDate;
  // 収支
  private long amount = 0;
  // カテゴリ毎サマリ
  private ArrayList<SummaryItem> summaryItems = new ArrayList<SummaryItem>();

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }
  public Timestamp getSummaryDate() {
    return summaryDate;
  }

  public void setSummaryDate(Timestamp summaryDate) {
    this.summaryDate = summaryDate;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }


  public ArrayList<SummaryItem> getSummaryItems() {
    return summaryItems;
  }

  public void setSummaryItems(ArrayList<SummaryItem> summaryItems) {
    this.summaryItems = summaryItems;
  }
}
