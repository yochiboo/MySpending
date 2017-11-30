package com.example.yochi.myspending.config;

/**
 * Created by yochi on 2017/07/06.
 */

public class Config {

  /** インスタンス */
  Static Config instance = null;

  /** テーマ */
  String thema = "default";
  // 言語
  String locale = "jp";
  // クイック入力の編集

  HashMap<int, String> conf = new HashMap<int, String>();

  static public Config getInstance() {
    if(instance == null){
      // コンフィグの検索
      dao = new ConfigDao();
      conf = ConfigDao.queryAllConfig();
    }
    return instance;
  }

  public String getConfig(int id){
    String val = (Stirng)conf.get(id);
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
