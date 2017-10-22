package com.example.yochi.myspending.Summary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yochi.myspending.util.MyDateUtil;
import com.example.yochi.myspending.util.MySQLiteOpenHelper;
import com.example.yochi.myspending.util.MySqlUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yochi on 2017/07/06.
 */

public class SummaryDao {

  private Context mContext = null;
  private MySQLiteOpenHelper mHelper = null;

  public SummaryDao(Context c) {
    mContext = c;
    mHelper = new MySQLiteOpenHelper(c);
  }

  // 日にち単位支出
  public int getPeriodNum(Timestamp a, Timestamp b){
    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/summary/selectAmountGroupByDate");
    String sql = String.format(sqlFormat, a.getTime(), b.getTime());

    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    c.moveToFirst();
    int amount = c.getInt(c.getColumnIndex("amount"));
    c.close();
    myDb.close();
    return amount;
  }

  /**
   * 今月の支出
   */
  public int getThisMonthSpend(){
    Calendar today = Calendar.getInstance();
    ArrayList<Timestamp> thisMonthFromTo = MyDateUtil.getMonthTimestamp(new Timestamp(today.getTimeInMillis()));
    return getPeriodNum(thisMonthFromTo.get(0), thisMonthFromTo.get(1));
  }

  // 今日の支出
  public int getTodaySpend(){
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    Calendar tommorow = (Calendar)today.clone();
    tommorow.add(Calendar.DAY_OF_MONTH, 1);
    return getPeriodNum(new Timestamp(today.getTime().getTime()), new Timestamp(tommorow.getTime().getTime()));
  }

  // 昨日の支出
  public int getYesterDaySpend(){
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    Calendar yesterDay = (Calendar)today.clone();
    yesterDay.add(Calendar.DAY_OF_MONTH, -1);
    return getPeriodNum(new Timestamp(yesterDay.getTime().getTime()), new Timestamp(today.getTime().getTime()));
  }

  // 日毎カテゴリ毎支出
  public List<SpendPerCategory> getPeriodNumByDateCategory(Timestamp a, Timestamp b){

    ArrayList<SpendPerCategory> result = new ArrayList<SpendPerCategory>();

    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/summary/selectAmountGroupByDateCategory");
    String sql = String.format(sqlFormat, a.getTime(), b.getTime());

    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    while(c.moveToNext()){
      SpendPerCategory dto = new SpendPerCategory();
      dto.setPaymentDate(new Timestamp(c.getLong(c.getColumnIndex("payment_date"))));
      dto.setCategoryCode(c.getInt(c.getColumnIndex("category_code")));
      dto.setCategoryName(c.getString(c.getColumnIndex("category_name")));
      dto.setAmount(c.getInt(c.getColumnIndex("amount")));
      result.add(dto);
    }
    c.close();
    myDb.close();
    return result;
  }

  // 今日のカテゴリ毎支出
  public List<SpendPerCategory> getTodaySpendByCategory(){
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    Calendar tommorow = (Calendar)today.clone();
    tommorow.add(Calendar.DAY_OF_MONTH, 1);
    return getPeriodNumByDateCategory(new Timestamp(today.getTime().getTime()), new Timestamp(tommorow.getTime().getTime()));
  }

  /**
   * カテゴリ毎支出
   */
  public List<SpendPerCategory> getPeriodNumByCategory(Timestamp a, Timestamp b){

    ArrayList<SpendPerCategory> result = new ArrayList<SpendPerCategory>();

    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/summary/selectAmountGroupByCategory");
    String sql = String.format(sqlFormat, a.getTime(), b.getTime());

    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    while(c.moveToNext()){
      SpendPerCategory dto = new SpendPerCategory();
      dto.setPaymentDate(new Timestamp(c.getLong(c.getColumnIndex("payment_date"))));
      dto.setCategoryCode(c.getInt(c.getColumnIndex("category_code")));
      dto.setCategoryName(c.getString(c.getColumnIndex("category_name")));
      dto.setAmount(c.getInt(c.getColumnIndex("amount")));
      result.add(dto);
    }
    c.close();
    myDb.close();
    return result;
  }

  /**
   * 今月のカテゴリ毎支出
   */
  public List<SpendPerCategory> getThisMonthSpendByCategory(){
    Calendar today = Calendar.getInstance();
    ArrayList<Timestamp> thisMonthFromTo = MyDateUtil.getMonthTimestamp(new Timestamp(today.getTimeInMillis()));
    return getPeriodNumByCategory(thisMonthFromTo.get(0), thisMonthFromTo.get(1));
  }

  /**
   * 指定月のカテゴリ毎支出
   */
  public List<SpendPerCategory> getMonthSpendByCategory(Date target){
    Calendar today = Calendar.getInstance();
    ArrayList<Timestamp> thisMonthFromTo = MyDateUtil.getMonthTimestamp(new Timestamp(today.getTimeInMillis()));
    return getPeriodNumByCategory(thisMonthFromTo.get(0), thisMonthFromTo.get(1));
  }

  // 直近ログの削除
  public void delPrevLog(){
    String sql = "delete from log where create_date = (select MAX(create_date) as date from log)";
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql);
    myDb.close();
  }

  // Logテーブル有無判定
  public Boolean isExistMyTable(){
    return isExistTable("log");
  }

  // テーブル有無判定
  public Boolean isExistTable(String tableName){
    String sqlFormat = "select count(*) as count from sqlite_master where type = 'table' and name = '%s'";

    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(String.format(sqlFormat, tableName), null);
    c.moveToFirst();
    int count = c.getInt(c.getColumnIndex("count"));
    c.close();
    myDb.close();

    return count > 0?true:false;
  }

  // テーブル作成
  public void createMyTable(){
    String sql = "create table log (_id integer primary key autoincrement, payment_date numeric not null, amount numeric, memo text, class_code numeric, category_code numeric, wallet_code numeric, shop_code numeric, create_date numeric, update_date numeric);";
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql);
    myDb.close();
  }

  // テーブル削除
  public void dropSpendLog(){
    String sql = "drop table log";
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql);
    myDb.close();
  }
}
