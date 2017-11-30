package com.example.yochi.myspending.config;

import com.example.yochi.myspending.util.MyContext;

import java.util.HashMap;

/**
 * Created by yochi on 2017/07/06.
 */
public class Config {

  /** インスタンス */
  private static Config instance = null;

  /** テーマ */
  String thema = "default";
  // 言語
  String locale = "jp";
  // クイック入力の編集

  private static HashMap<Integer, String> conf = new HashMap<Integer, String>();

  static public Config getInstance() {
    if(instance == null){
      // コンフィグの検索
      ConfigDao dao = new ConfigDao(MyContext.getContext());
      conf = dao.queryAllConfig();
    }
    return instance;
  }

  public String getConfig(int id){
    String val = (String)conf.get(id);
    if(val == null){
      return "";
    }
    return val;
  }

  public void setConfig(int id, String val){
    conf.put(id, val);
  }

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
