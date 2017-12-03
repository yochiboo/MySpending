package com.example.yochi.myspending.title;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.yochi.myspending.R;
import com.example.yochi.myspending.category.SelectCategoryActivity;
import com.example.yochi.myspending.history.HistoryActivity;
import com.example.yochi.myspending.spend.SpendActivity;
import com.example.yochi.myspending.summary.SpendPerCategory;
import com.example.yochi.myspending.summary.Summary;
import com.example.yochi.myspending.summary.SummaryActivity;
import com.example.yochi.myspending.summary.SummaryDao;
import com.example.yochi.myspending.summary.SummaryFragmentPagerAdapter;
import com.example.yochi.myspending.summary.SummaryItem;
import com.example.yochi.myspending.util.ConvertUtil;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TitleActivity extends AppCompatActivity {

  /** スレッドUI操作用ハンドラ */
  private Handler mHandler = new Handler();
  /** テキストオブジェクト */
  private Runnable updateText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_title);

    updateText = new Runnable() {
      public void run() {
        showSummaryActivity();
        mHandler.removeCallbacks(updateText);
        finish();
      }
    };
    mHandler.postDelayed(updateText, 1000);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  protected void onResume(){
    super.onResume();
  }

  @Override
  protected void onPause(){
    super.onPause();
  }

  private void showSummaryActivity(){
    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(this, SummaryActivity.class);
    startActivity(intent);
  }

}
