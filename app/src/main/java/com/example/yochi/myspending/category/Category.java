package com.example.yochi.myspending.category;

import java.io.Serializable;

/**
 * Created by yochi on 2017/07/06.
 */

public class Category implements Serializable {

  // カテゴリコード
  private Integer Code = 0;
  // カテゴリ名
  private String Name = "なし";
  // 分類コード
  private Integer classCode = 0;
  // 親カテゴリコード
  private Integer parentCode = 0;
  // 表示順
  private Integer viewOrder = 0;
  // アイコン
  private String iconSrc = null;

  public Integer getCode() {
    return Code;
  }

  public void setCode(Integer code) {
    Code = code;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public Integer getClassCode() {
    return classCode;
  }

  public void setClassCode(Integer classCode) {
    this.classCode = classCode;
  }

  public Integer getParentCode() {
    return parentCode;
  }

  public void setParentCode(Integer parentCode) {
    this.parentCode = parentCode;
  }

  public Integer getViewOrder() { return viewOrder; }

  public void setViewOrder(Integer viewOrder) { this.viewOrder = viewOrder; }

  public String getIconSrc() {
    return iconSrc;
  }

  public void setIconSrc(String iconSrc) {
    this.iconSrc = iconSrc;
  }
}
