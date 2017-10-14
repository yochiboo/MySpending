package com.example.yochi.myspending.Spend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yochi.myspending.util.MySQLiteOpenHelper;
import com.example.yochi.myspending.util.MySqlUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yochi on 2017/07/06.
 */

public class SpendLogDao {

  private Context mContext = null;
  private MySQLiteOpenHelper mHelper = null;

  public SpendLogDao(Context c) {
    mContext = c;
    mHelper = new MySQLiteOpenHelper(mContext);
  }

  // ログの記録
  public void saveLog(SpendLog log){
    if(log.getId() < 0){
      // 新規作成
      addLog(log);
    }else{
      // 更新
      updateLog(log);
    }
  }

  // ログ新規作成
  public void addLog(SpendLog log){
    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/spend/addLog");
    String sql = String.format(sqlFormat, log.getPaymentDate().getTime(), log.getAmount(), log.getMemo(), log.getClassCode(), log.getCategoryCode(), log.getWalletCode(), log.getShopCode(), log.getCreateDate().getTime(), log.getUpdateDate().getTime());
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql);
    myDb.close();
  }

  // ログ更新
  public void updateLog(SpendLog log){
    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/spend/updateLog");
    String sql = String.format(sqlFormat, log.getPaymentDate().getTime(), log.getAmount(), log.getMemo(), log.getClassCode(), log.getCategoryCode(), log.getWalletCode(), log.getShopCode(), log.getCreateDate().getTime(), log.getUpdateDate().getTime(), log.getId());
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql);
    myDb.close();
  }

  // ログ削除
  public void dellLog(SpendLog log){
    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/spend/dellLog");
    String sql = String.format(sqlFormat, log.getId());
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    myDb.execSQL(sql);
    myDb.close();
  }

  // ログ取得
  public SpendLog getLog(int id){
    SpendLog log = new SpendLog();

    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/spend/getLog");
    String sql = String.format(sqlFormat, id);

    SQLiteDatabase myDb = mHelper.getReadableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    c.moveToFirst();

    log.setId(id);
    log.setPaymentDate(new Timestamp(c.getLong(c.getColumnIndex("payment_date"))));
    log.setAmount(c.getLong(c.getColumnIndex("amount")));
    log.setMemo(c.getString(c.getColumnIndex("memo")));
    log.setClassCode(c.getInt(c.getColumnIndex("class_code")));
    log.setCategoryCode(c.getInt(c.getColumnIndex("category_code")));
    log.setCategoryName(c.getString(c.getColumnIndex("category_name")));
    log.setWalletCode(c.getInt(c.getColumnIndex("wallet_code")));
    log.setShopCode(c.getInt(c.getColumnIndex("shop_code")));
    log.setCreateDate(new Timestamp(c.getLong(c.getColumnIndex("create_date"))));
    log.setUpdateDate(new Timestamp(c.getLong(c.getColumnIndex("update_date"))));

    c.close();
    myDb.close();

    return log;
  }

  // 期間内支出
  public int getPeriodNum(Timestamp a, Timestamp b){
    String sqlFormat = "select sum(amount) as amount from log where payment_date between %d and %d and class_code=0";
    String sql = String.format(sqlFormat, a.getTime(), b.getTime());
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    c.moveToFirst();
    int amount = c.getInt(c.getColumnIndex("amount"));
    c.close();
    myDb.close();
    return amount;
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

  // カテゴリ毎支出
  public List<SpendLog> getPeriodNumByCategory(Timestamp a, Timestamp b){

    ArrayList<SpendLog> result = new ArrayList<SpendLog>();

    String sqlFormat = "select payment_date, category_code, sum(amount) as amount from log where payment_date between %d and %d and class_code=0 group by payment_date, category_code order by payment_date, category_code";
    String sql = String.format(sqlFormat, a.getTime(), b.getTime());

    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    while(c.moveToNext()){
      SpendLog dto = new SpendLog();
      dto.setPaymentDate(new Timestamp(c.getLong(c.getColumnIndex("payment_date"))));
      dto.setCategoryCode(c.getInt(c.getColumnIndex("category_code")));
      dto.setAmount(c.getInt(c.getColumnIndex("amount")));
      result.add(dto);
    }
    c.close();
    myDb.close();

    return result;
  }

  // 今日のカテゴリ毎支出
  public List<SpendLog> getTodaySpendByCategory(){
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    Calendar tommorow = (Calendar)today.clone();
    tommorow.add(Calendar.DAY_OF_MONTH, 1);
    return getPeriodNumByCategory(new Timestamp(today.getTime().getTime()), new Timestamp(tommorow.getTime().getTime()));
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
