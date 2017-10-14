package com.example.yochi.myspending.Config;

import com.example.yochi.myspending.Summary.SummaryItem;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by yochi on 2017/07/06.
 */

public class Config {

  // テーマ
  String thema = "default";
  // 言語
  String locale = "jp";
  // クイック入力の編集

  public String getThema() {
    return thema;
  }

  public void setThema(String thema) {
    this.thema = thema;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }
}
