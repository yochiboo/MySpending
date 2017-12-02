package com.example.yochi.myspending.util;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yochi on 2017/07/06.
 */

public class MySqlUtil {

  /**
   * 引数に指定したassetsフォルダ内の指定sqlを実行します。
   * @throws IOException
   */
  public static String getSql(AssetManager assets, String sqlName) {
    try {
      return readFile(assets.open(sqlName + ".sql"));
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 引数に指定したassetsフォルダ内の指定sqlの内容を取得する
   * @throws IOException
   */
  public static String getSql(String sqlName) {
    try {
      return readFile(MyContext.getAssets().open(sqlName + ".sql"));
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * ファイルから文字列を読み込みます。
   * @param is
   * @return ファイルの文字列
   * @throws IOException
   */
  private static String readFile(InputStream is) throws IOException{
    BufferedReader br = null;
    try {
      //br = new BufferedReader(new InputStreamReader(is,"SJIS"));
      br = new BufferedReader(new InputStreamReader(is));

      StringBuilder sb = new StringBuilder();
      String str;
      while((str = br.readLine()) != null){
        if(str.startsWith("<!--")){
          // コメント行は読み飛ばす
          continue;
        }
        sb.append(" " + str.trim());
      }
      return sb.toString();
    } finally {
      if (br != null) br.close();
    }
  }
}
