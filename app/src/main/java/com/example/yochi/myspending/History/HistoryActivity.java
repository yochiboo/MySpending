package com.example.yochi.myspending.History;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.example.yochi.myspending.InputAmount.InputAmountActivity;
import com.example.yochi.myspending.R;
import com.example.yochi.myspending.Spend.SpendActivity;
import com.example.yochi.myspending.Spend.SpendLog;
import com.example.yochi.myspending.Summary.SummaryFragmentPagerAdapter;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

  public static int REQUESTCODE_EDIT_AMOUNT = 1000;
  public static int REQUESTCODE_EDIT_CATEGORY = 1001;
  public static int REQUESTCODE_EDIT_MEMO = 1002;
  public static int REQUESTCODE_EDIT_PAYMENTDATE = 1003;

  ArrayList<HistoryLog> logs = new ArrayList<HistoryLog>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);

    //フリックで削除できるリスト
    // http://workpiles.com/2015/04/android-swipe-recyclerview/

    // 戻るボタン
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    //フローティングボタン
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showSpendActivity(-1);
      }
    });

    //ページャー
    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    HistoryFragmentPagerAdapter pager = (HistoryFragmentPagerAdapter)viewPager.getAdapter();
    if(pager == null){
      pager = new HistoryFragmentPagerAdapter(getSupportFragmentManager());
      viewPager.setAdapter(pager);
    }else{
      pager.notifyDataSetChanged();
    }

    //起動オプション
    Date currentDate = new Date();
    Intent intent = getIntent();
    if(intent != null){
      int param = intent.getIntExtra("param", 0);
      switch(param){
        // 一日単位履歴
        case 0:
        default:
          currentDate.setTime(intent.getLongExtra("date", (new Date()).getTime()));
          break;
      }
    }

    // カレント位置の設定
    viewPager.setCurrentItem(MyDateUtil.getNumberOfDays(currentDate));

  }

  @Override
  protected void onResume(){
    super.onResume();
    redraw();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_history, menu);
    return true;
  }

  // 遷移先画面の結果受取
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
    if(intent == null){
      // 返却値なし
      return;
    }

    // startActivityForResult()の際に指定した識別コードとの比較
    switch(requestCode){
      // 履歴修正
      case 1000:
        break;
      // 履歴削除
      case 1001:
        break;
      default:
        break;
    }

    // 画面再描画
    redraw();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      // 今日を選択
      case R.id.action_today:
        onSelectToday();
        return true;
      // 日付の選択
      case R.id.action_cal:
        onSelectDate();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void onSelectToday(){
    scrollToPagerCurrentDate(new Date());
  }

  private void onSelectDate(){

    // ページ位置からカレント日付を取得する
    Date currentDate = getPagerCurrentDate();

    // DatePickerDialogの表示
    Calendar cal = Calendar.getInstance();
    cal.setTime(currentDate);
    final DatePickerDialog datePickerDialog = new DatePickerDialog(
      this,
      new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          // 選択された日付のページ位置へスクロールする
          Calendar cal = Calendar.getInstance();
          cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
          scrollToPagerCurrentDate(cal.getTime());
        }
      },
      cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
      }
    });
    datePickerDialog.show();
  }

  private void showSpendActivity(int id){
    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(this, SpendActivity.class);
    if(id > 0){
      intent.putExtra("param", SpendActivity.PARAM_EDIT);
      intent.putExtra("id", id);
    }else{
      intent.putExtra("param", SpendActivity.PARAM_NEW);
      intent.putExtra("date", SpendActivity.PARAM_NEW);
    }
    startActivity(intent);
  }

  // 再描画
  private void redraw(){
    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    HistoryFragmentPagerAdapter pager = (HistoryFragmentPagerAdapter)viewPager.getAdapter();
    pager.notifyDataSetChanged();
  }

  // ページ位置日付の設定
  private void scrollToPagerCurrentDate(Date currentDate){
    // アダプタを初期化してページのカレントを設定する
    // ※アダプタを初期化しないとだんまりになる
    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    viewPager.setAdapter(new HistoryFragmentPagerAdapter(getSupportFragmentManager()));
    viewPager.setCurrentItem(MyDateUtil.getNumberOfDays(currentDate));
  }

  // ページ位置日付の取得
  private Date getPagerCurrentDate(){
    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    Date currentDate = MyDateUtil.addDate(null, viewPager.getCurrentItem());
    return currentDate;
  }

}
