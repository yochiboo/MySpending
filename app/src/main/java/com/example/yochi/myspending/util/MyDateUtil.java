package com.example.yochi.myspending.util;

import org.jetbrains.annotations.Contract;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * MyDateUtil
 * Created by yochi on 2017/07/06.
 */
public class MyDateUtil {

  private final static long DAY_PER_MILLS = 1000 * 60 * 60 * 24;

  /**
   * 指定時刻の日付開始終了タイムスタンプ
   * @param target 指定時刻
   * @return 開始終了タイムスタンプ
   */
  public static ArrayList<Timestamp> getDayTimestamp(Timestamp target){
    ArrayList<Timestamp> result = new ArrayList<>();
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(target.getTime());
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

  /**
   * 指定時刻の月開始終了タイムスタンプ
   * @param target 指定時刻
   * @return 開始終了タイムスタンプ
   */
  public static ArrayList<Timestamp> getMonthTimestamp(Timestamp target){
    ArrayList<Timestamp> result = new ArrayList<>();
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(target.getTime());
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

  /**
   * 指定時刻の月の日数を取得
   * @param target 指定時刻
   * @return 日数
   */
  public static int getLastDay(Timestamp target){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(target.getTime());
    return cal.getActualMaximum(Calendar.DATE);
  }

  /**
   * 指定時刻の日にちを取得
   * @param target 指定時刻
   * @return 日にち
   */
  public static int getDayOfMonth(Timestamp target){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(target.getTime());
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * 今日の日付か判定
   * @param target 判定日付
   * @return 判定結果
   */
  @Contract("null -> false")
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

  /**
   * 月の加算
   * @param target 加算対象
   * @param add 加算月
   * @return 加算結果
   */
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

  /**
   * 日にちの加算
   * @param target 加算対象
   * @param add 加算日
   * @return 加算結果
   */
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

  /**
   * 基準日からの差分日数
   * @param target 日数計算対象日付
   * @return 日数
   */
  public static int getNumberOfDays(Date target){
    long offset = Calendar.getInstance().getTimeZone().getRawOffset();
    long num = (target.getTime()-offset) / DAY_PER_MILLS;
    if(target.getTime() % DAY_PER_MILLS > 0){
      num++;
    }
    return (int)num;
  }

  /**
   * 時刻の削除
   * @param target 削除対象
   * @return 削除後時刻
   */
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
