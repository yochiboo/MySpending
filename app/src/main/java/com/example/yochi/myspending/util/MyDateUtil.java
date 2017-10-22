package com.example.yochi.myspending.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yochi on 2017/07/06.
 */

public class MyDateUtil {

  public final static long DAY_PER_MILLS = 1000 * 60 * 60 * 24;

  // 指定日の開始終了タイムスタンプ
  public static ArrayList<Timestamp> getDayTimestamp(Timestamp day){
    ArrayList<Timestamp> result = new ArrayList<Timestamp>();
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(day.getTime());
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    result.add(new Timestamp(date.getTime().getTime()));
    Calendar next = (Calendar)date.clone();
    next.add(Calendar.DAY_OF_MONTH, 1);
    result.add(new Timestamp(next.getTime().getTime()-1));
    return result;
  }

  // 指定月の開始終了タイムスタンプ
  public static ArrayList<Timestamp> getMonthTimestamp(Timestamp day){
    ArrayList<Timestamp> result = new ArrayList<Timestamp>();
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(day.getTime());
    date.set(Calendar.DAY_OF_MONTH, 1);
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    result.add(new Timestamp(date.getTime().getTime()));
    Calendar next = (Calendar)date.clone();
    next.add(Calendar.MONTH, 1);
    result.add(new Timestamp(next.getTime().getTime()-1));
    return result;

  }

  // 月の日数を取得
  public static int getLastDay(Timestamp target){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(target.getTime());
    return cal.getActualMaximum(Calendar.DATE);
  }

  // 日にちを取得
  public static int getDayOfMonth(Timestamp target){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(target.getTime());
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  // 今日の日付か判定
  public static boolean isToday(Timestamp target){
    if(target == null){
      return false;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(target.getTime());
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    Calendar today = Calendar.getInstance();
    today.setTime(new Date());
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    return today.compareTo(cal) == 0;
  }

  // 月の加算
  public static Date addMonth(Date target, int add){
    if(target == null){
      target = new Date();
      target.setTime(0);
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(target);
    cal.add(Calendar.MONTH, add);
    return cal.getTime();
  }

  // 日にちの加算
  public static Date addDate(Date target, int add){
    if(target == null){
      target = new Date();
      target.setTime(0);
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(target);
    cal.add(Calendar.DAY_OF_MONTH, add);
    return cal.getTime();
  }

  // 基準日からの差分日数
  public static int getNumberOfDays(Date target){
    long offset = Calendar.getInstance().getTimeZone().getRawOffset();
    long num = (target.getTime()-offset) / DAY_PER_MILLS;
    if(target.getTime() % DAY_PER_MILLS > 0){
      num++;
    }
    return (int)num;
  }

  // 時刻の削除
  public static Timestamp cutTime(Timestamp target){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(target.getTime());
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return new Timestamp(cal.getTimeInMillis());
  }

}
