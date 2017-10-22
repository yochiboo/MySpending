package com.example.yochi.myspending.spend;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by yochi on 2017/07/06.
 */

public class SpendLog implements Serializable {

  // id
  private int id = -1;
  // 支払い日時
  private Timestamp paymentDate;
  // 金額
  private long amount = 0;
  // メモ
  private String memo = "";
  // 分類（収入、支出フラグ）コード
  private int classCode = 0;
  // カテゴリコード
  private int categoryCode = 0;
  // カテゴリ名
  private String categoryName = "未分類";
  // 支払い元、入金先コード
  private int walletCode = 0;
  // 支払い先、入金元コード
  private int shopCode = 0;
  // 作成日時
  private Timestamp createDate = new Timestamp(System.currentTimeMillis());
  // 更新日時
  private Timestamp updateDate = createDate;

  public SpendLog(){
    Calendar today = Calendar.getInstance();
    today.setTimeInMillis(System.currentTimeMillis());
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    paymentDate = new Timestamp(today.getTimeInMillis());
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

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public int getClassCode() {
    return classCode;
  }

  public void setClassCode(int classCode) {
    this.classCode = classCode;
  }

  public int getCategoryCode() {
    return categoryCode;
  }

  public void setCategoryCode(int categoryCode) {
    this.categoryCode = categoryCode;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public int getWalletCode() {
    return walletCode;
  }

  public void setWalletCode(int walletCode) {
    this.walletCode = walletCode;
  }

  public int getShopCode() {
    return shopCode;
  }

  public void setShopCode(int shopCode) {
    this.shopCode = shopCode;
  }

  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  public Timestamp getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Timestamp updateDate) {
    this.updateDate = updateDate;
  }
}
