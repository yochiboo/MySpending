package com.example.yochi.myspending.util;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * Created by yochi on 2017/11/30.
 */
public class MyContext {
  private static MyContext instance = null;
  private Context applicationContext;

  private MyContext(Context applicationContext) {
    this.applicationContext = applicationContext;
  }

  // via https://qiita.com/eimei4coding/items/0585b8240873ec5e9c20
  // publicをつけないのは意図的
  // MyApplicationと同じパッケージにして、このメソッドのアクセスレベルはパッケージローカルとする
  // 念のため意図しないところで呼び出されることを防ぐため
  static void onCreateApplication(Context applicationContext) {
    // Application#onCreateのタイミングでシングルトンが生成される
    instance = new MyContext(applicationContext);
  }

  public static MyContext getInstance() {
    if (instance == null) {
      // こんなことは起きないはず
      throw new RuntimeException("MyContext should be initialized!");
    }
    return instance;
  }

  public static Context getContext() {
    if (instance == null) {
      throw new RuntimeException("MyContext should be initialized!");
    }
    return instance.getApplicationContext();
  }

  public static String getString(int resId) {
    if (instance == null) {
      throw new RuntimeException("MyContext should be initialized!");
    }
    return instance.getApplicationContext().getString(resId);
  }

  public static AssetManager getAssets() {
    if (instance == null) {
      throw new RuntimeException("MyContext should be initialized!");
    }
    return instance.getApplicationContext().getAssets();
  }

  public Context getApplicationContext() {
    return applicationContext;
  }
}

