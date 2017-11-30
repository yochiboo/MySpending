package com.example.yochi.myspending.util;

import android.app.Application;

public class MyApplication extends Application {
  // Application#onCreateは、ActivityやServiceが生成される前に呼ばれる。
  // だから、ここでシングルトンを生成すれば問題ない
  @Override
  public void onCreate() {
    super.onCreate();
    MyContext.onCreateApplication(getApplicationContext());
  }
}
