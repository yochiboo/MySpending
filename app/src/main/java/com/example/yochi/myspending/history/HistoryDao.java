package com.example.yochi.myspending.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yochi.myspending.util.MySQLiteOpenHelper;
import com.example.yochi.myspending.util.MySqlUtil;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by yochi on 2017/07/06.
 */

public class HistoryDao {

  private Context mContext = null;
  private MySQLiteOpenHelper mHelper = null;

  public HistoryDao(Context c) {
    mContext = c;
    mHelper = new MySQLiteOpenHelper(mContext);
  }

  //履歴検索
  public ArrayList<HistoryLog> selectHistories(Timestamp from, Timestamp to){
    ArrayList<HistoryLog> result = new ArrayList<HistoryLog>();
    String sqlFormat = MySqlUtil.getSql(mContext.getAssets(), "sql/history/selectAmount");
    String sql = String.format(sqlFormat, from.getTime(), to.getTime());
    SQLiteDatabase myDb = mHelper.getWritableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    while(c.moveToNext()){
      HistoryLog dto = new HistoryLog();
      dto.setId(c.getInt(c.getColumnIndex("id")));
      dto.setPaymentDate(new Timestamp(c.getLong(c.getColumnIndex("payment_date"))));
      dto.setAmount(c.getInt(c.getColumnIndex("amount")));
      dto.setMemo(c.getString(c.getColumnIndex("memo")));
      dto.setClassCode(c.getInt(c.getColumnIndex("class")));
      dto.setCategoryName(c.getString(c.getColumnIndex("category")));
      result.add(dto);
    }
    c.close();
    myDb.close();
    return result;
  }

  //履歴全期間の支払日カウント
  public int countHistoriesPaymentDate(){
    String sql = MySqlUtil.getSql(mContext.getAssets(), "sql/history/countHistoriesPaymentDate");
    SQLiteDatabase myDb = mHelper.getReadableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    c.moveToNext();
    int count = c.getInt(c.getColumnIndex("count"));
    c.close();
    myDb.close();
    return count;
  }


  //履歴全期間の支払日
  public ArrayList<Timestamp> selectPaymentDates(){
    ArrayList<Timestamp> result = new ArrayList<Timestamp>();
    String sql = MySqlUtil.getSql(mContext.getAssets(), "sql/history/selectPaymentDates");
    SQLiteDatabase myDb = mHelper.getReadableDatabase();
    Cursor c = myDb.rawQuery(sql, null);
    while(c.moveToNext()){
      result.add(new Timestamp(c.getLong(c.getColumnIndex("payment_date"))));
    }
    c.close();
    myDb.close();
    return result;
  }
}
