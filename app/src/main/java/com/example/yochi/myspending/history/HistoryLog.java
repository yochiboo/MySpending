package com.example.yochi.myspending.history;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yochi on 2017/07/06.
 */

public class HistoryLog implements Serializable {

  // ID
  private int id;
  // 支払い日時
  private Timestamp paymentDate;
  // 金額
  private int amount;
  // メモ
  private String memo;
  // 分類コード
  private int classCode;
  // カテゴリ名
  private String categoryName;

  public HistoryLog(){
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Timestamp getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Timestamp paymentDate) {
    this.paymentDate = paymentDate;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public int getClassCode() {
    return classCode;
  }

  public void setClassCode(int classCode) {
    this.classCode = classCode;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}
