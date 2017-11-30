package com.example.yochi.myspending.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yochi.myspending.util.MySQLiteOpenHelper;
import com.example.yochi.myspending.util.MySqlUtil;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * コンフィグテーブルアクセス
 */
public class ConfigDao {

  private Context mContext = null;
  private MySQLiteOpenHelper mHelper = null;

  /**
   * コンストラクタ
   * @param c Contextインスタンス
   */
  public ConfigDao(Context c) {
    mContext = c;
    mHelper = new MySQLiteOpenHelper(mContext);
  }

  /**
   * 全コンフィグ検索
   */
  public HashMap<int, String> queryAllConfig(){
    HashMap<int, String> result = new HashMap<int, String>();
    String sql = MySqlUtil.getSql(mContext.getAssets(), "sql/config/queryAllConfig");
    SQLiteDatabase myDb = mHelper.getReadableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    while(c.moveToNext()){
      int id = c.getInt(c.getColumnIndex("code"));
      String value = c.getString(c.getColumnIndex("value"));
      result.put(id, value);
    }
    c.close();
    myDb.close();
    return result;
  }

  /**
   * コンフィグ初期化
   */
  public void initializeConfig(){
    String sql = MySqlUtil.getSql(mContext.getAssets(), "sql/config/initializeConfig");
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql, null);
    myDb.close();
  }

  /**
   * 指定コンフィグ保存
   * @param id コンフィグID
   * @param value コンフィグ値
   */
  public void saveConfig(int id, String value){

    SQLiteDatabase myDb = mHelper.getWritableDatabase();

    // コンフィグ有無判定
    String sqlFormat;
    if(isExist(myDb, id)){
      // コンフィグあり→更新SQL
      sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/config/updateConfig");
    }else{
      // コンフィグあり→新規SQL
      sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/config/insertConfig");
    }
    String sql = String.format(sqlFormat, id, value);
    myDb.execSQL(sql, null);
    myDb.close();
    return result;
  }

  /**
   * 指定コンフィグ有無判定
   * @param db SQLiteDatabaseインスタンス
   * @param id コンフィグID
   */
  public boolean isExist(SQLiteDatabase db, int id){

    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/config/isExist";
    String sql = String.format(sqlFormat, id, value);

    Cursor c = db.rawQuery(sql, null);
    while(c.moveToNext()){
      int exist = c.getInt(c.getColumnIndex("exist"));
    }
    c.close();

    if(exist > 0){
      retun true;
    }
    return falise;
  }
}
