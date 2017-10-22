package com.example.yochi.myspending.category;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yochi.myspending.util.MySQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by yochi on 2017/07/06.
 */

public class SelectCategoryDao {

  private Context mContext = null;
  private SQLiteDatabase mydb = null;

  public SelectCategoryDao(Context c) {
    mContext = c;
  }

  // カテゴリ一覧検索
  public ArrayList<Category> selectCategoryList(){

    ArrayList<Category> result = new ArrayList<Category>();

    String sql = getSql("sql/selectCategory/selectAllCategory");
    Cursor c = openDb().rawQuery(sql, null);
    while(c.moveToNext()){
      Category item = CategoryDto.parseCategory(c);
      result.add(item);
    }
    c.close();
    closeDb();

    return result;
  }

  // 指定カテゴリー取得
  public Category getCategory(int id){
    String sql = getSql("sql/selectCategory/getCategory");
    Cursor c = openDb().rawQuery(sql, null);
    c.moveToNext();
    Category item = CategoryDto.parseCategory(c);
    c.close();
    closeDb();
    return item;
  }

  // カテゴリー追加
  public void insertCategory(Category c){
    String format = getSql("sql/selectCategory/insertCategory");
    String sql = String.format(format, c.getCode(), c.getName(), c.getClassCode(), c.getParentCode(), c.getViewOrder(), c.getIconSrc());
    openDb().execSQL(sql);
    closeDb();
  }

  // カテゴリー編集
  public void updateCategory(Category c){
    String format = getSql("sql/selectCategory/updateCategory");
    String sql = String.format(format, c.getName(), c.getClassCode(), c.getParentCode(), c.getViewOrder(), c.getIconSrc(), c.getCode());
    openDb().execSQL(sql);
    closeDb();
  }

  // カテゴリー削除
  public void deleteCategory(int code){
    String format = getSql("sql/selectCategory/deleteCategory");
    String sql = String.format(format, code);
    openDb().execSQL(sql);
    closeDb();
  }

  // カテゴリー順序変更
  public void reorderCategory(ArrayList<Category> list){
    // idをキーとしてorderを更新する
    // これをすべての項目に実施
    // ↑が効率的かどうかも考える
    String sqlFormat = "insert into log (payment_date, amount, memo, class_code, category_code, wallet_code, shop_code, create_date, update_date) values (%d, %d, '%s', %d, %d, %d, %d, %d, %d)";
  }


  private SQLiteDatabase openDb(){
    if(mydb == null){
      MySQLiteOpenHelper helper = new MySQLiteOpenHelper(mContext);
      mydb = helper.getWritableDatabase();
    }
    return mydb;
  }

  private void closeDb(){
    if(mydb != null) {
      mydb.close();
    }
    mydb = null;
  }

  /**
   * 引数に指定したassetsフォルダ内の指定sqlを実行します。
   * @throws IOException
   */
  public String getSql(String sqlName) {
    AssetManager as = mContext.getResources().getAssets();
    try {
      return readFile(as.open(sqlName + ".sql"));
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
  private String readFile(InputStream is) throws IOException{
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
        sb.append(str.replaceAll( " *$", "" ));
      }
      return sb.toString();
    } finally {
      if (br != null) br.close();
    }
  }
}
