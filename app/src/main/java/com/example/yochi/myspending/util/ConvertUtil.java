package com.example.yochi.myspending.util;

import com.example.yochi.myspending.summary.SpendPerCategory;
import com.example.yochi.myspending.summary.Summary;
import com.example.yochi.myspending.summary.SummaryItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ConvertUtil
 * Created by yochi on 2017/07/06.
 */
public class ConvertUtil {

  /**
   * List<SpendPerCategory>型をSummary型へ変換する
   * @param list List<SpendPerCategory>型
   * @return Summary型
   */
  public static Summary spendPerCategoryList2Summary(List<SpendPerCategory> list) {

    Summary summary = new Summary();
    if(list == null){
      return summary;
    }

    ArrayList<SummaryItem> items = summary.getSummaryItems();
    long amount = 0;
    Timestamp ts = null;

    for(SpendPerCategory s : list){

      if(s.getAmount() <= 0){
        // 支出0円以下は変換しない
        continue;
      }

      if(ts == null){
        ts = s.getPaymentDate();
        summary.setSummaryDate(ts);
      }

      SummaryItem item = new SummaryItem();
      item.setCategoryCode(s.getCategoryCode());
      item.setCategoryName(s.getCategoryName());
      item.setAmount(s.getAmount());

      items.add(item);

      amount += s.getAmount();
    }
    summary.setAmount(amount);

    return summary;
  }
}
