package com.example.yochi.myspending.Summary;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yochi.myspending.History.HistoryActivity;
import com.example.yochi.myspending.R;
import com.example.yochi.myspending.Spend.SpendActivity;
import com.example.yochi.myspending.util.ConvertUtil;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

  private final static int VIEW_TERM_DAY = 0;
  private final static int VIEW_TERM_MONTH = 1;
  
  SummaryDao mDao = new SummaryDao(this);
  Date mCurrentDate = new Date();
  int mViewTerm = VIEW_TERM_DAY;

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //toolbar.inflateMenu(R.menu.menu_main);
    //mDao.dropSpendLog();
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(MenuItem item) {
        // ナビゲーションアイコンクリック時の処理
        switch(item.getItemId()){
          default:
            showAddActivity();
            break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
      }
    });

    // ナビゲーションアイコンの設定、クリック処理
    /*
    toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // ナビゲーションアイコンクリック時の処理
      }
    });
*/

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAddActivity();
      }
    });

    /*
    //サマリが選択された時のイベントを追加
    ListView listView = (ListView) findViewById(R.id.summaryList);
    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SummaryAdapter adapter = (SummaryAdapter)parent.getAdapter();
        Summary summary = adapter.getSummaries().get(position);
      }
    });
*/

    //履歴ボタン
    Button button = (Button)findViewById(R.id.history_button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        SummaryFragmentPagerAdapter pager = (SummaryFragmentPagerAdapter)viewPager.getAdapter();
        Timestamp ts = pager.getSummaries().get(viewPager.getCurrentItem()).getSummaryDate();
        showHistoryActivity(ts);
      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  protected void onResume(){
    super.onResume();
    showTodaySpend(mCurrentDate);

    // 戻るボタン非表示
    ActionBar bar =getActionBar();
    if(bar != null){
      bar.setDisplayHomeAsUpEnabled(false);
    }

  }

  @Override
  protected void onPause(){
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_summary, menu);
    return true;
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    //noinspection SimplifiableIfStatement
    switch (item.getItemId()){
    // 日付の選択
    case R.id.action_cal:
      onSelectDate();
      return true;
    // 表示期間の変更
    case R.id.action_term:
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showAddActivity(){
    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    SummaryFragmentPagerAdapter pager = (SummaryFragmentPagerAdapter)viewPager.getAdapter();
    Timestamp ts = pager.getSummaries().get(viewPager.getCurrentItem()).getSummaryDate();
    mCurrentDate.setTime(ts.getTime());

    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(this, SpendActivity.class);
    intent.putExtra("param", 0);
    intent.putExtra("date", ts.getTime());
    startActivity(intent);
  }

  private void onSelectDate(){
    Calendar cal = Calendar.getInstance();
    cal.setTime(mCurrentDate);
    final int year = cal.get(Calendar.YEAR);
    final int month = cal.get(Calendar.MONTH);
    final int day = cal.get(Calendar.DAY_OF_MONTH);

    final DatePickerDialog datePickerDialog = new DatePickerDialog(
      this,
      new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          Calendar cal = Calendar.getInstance();
          cal.set(year, monthOfYear, dayOfMonth, 0, 0);
          showTodaySpend(cal.getTime());
        }
      },
      year, month, day);
    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
      }
    });
    datePickerDialog.show();
  }

  private void showHistoryActivity(Timestamp ts){
    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(this, HistoryActivity.class);
    intent.putExtra("param", 0);
    intent.putExtra("date", ts.getTime());
    startActivity(intent);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  private void showTodaySpend(Date current){

    ArrayList<Summary> summaries = queryThisMonthAndTodaySummaries();

    //ページャー
    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    SummaryFragmentPagerAdapter pager = (SummaryFragmentPagerAdapter)viewPager.getAdapter();
    if(pager == null){
      pager = new SummaryFragmentPagerAdapter(getSupportFragmentManager(), summaries);
      viewPager.setAdapter(pager);
    }else{
      pager.setSummaries(summaries);
      pager.notifyDataSetChanged();
    }
    //viewPager.setCurrentItem(MyDateUtil.getDayOfMonth(new Timestamp(current.getTime()))-1);

    // ツールバーにカレント年月を表示
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");
    toolbar.setTitle(df.format(current));
    toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
  }

  /**
   * 今月と今日のサマリー検索
   * @return サマリー検索結果
   **/
  private ArrayList<Summary> queryThisMonthAndTodaySummaries(){

    ArrayList<Summary> summaries = new ArrayList<Summary>();
    Timestamp today = new Timestamp((new Date()).getTime());

    Summary thisMonthSummries = ConvertUtil.spendPerCategoryList2Summary(mDao.getThisMonthSpendByCategory());
    thisMonthSummries.setSummaryDate(today);
    summaries.add(thisMonthSummries);

    Summary todaySummaries = ConvertUtil.spendPerCategoryList2Summary(mDao.getTodaySpendByCategory());
    todaySummaries.setSummaryDate(today);
    summaries.add(todaySummaries);

    return summaries;
  }

  private ArrayList<Summary> querySummaries(Date current){

    Timestamp currentTs = new Timestamp(current.getTime());
    List<Timestamp> fromTo = MyDateUtil.getMonthTimestamp(new Timestamp(current.getTime()));
    int lastDay = MyDateUtil.getLastDay(currentTs);

    final long DAY_OF_MILLS = 24*60*60*1000;
    long topTs = fromTo.get(0).getTime();
    ArrayList<Summary> summaries = new ArrayList<Summary>();
    for(int d = 0; d < lastDay; d++){
      Summary summary = new Summary();
      summary.setSummaryDate(new Timestamp(topTs + DAY_OF_MILLS * d));
      summaries.add(summary);
    }

    List<SpendPerCategory> list = mDao.getPeriodNumByDateCategory(fromTo.get(0), fromTo.get(1));
    for(SpendPerCategory s : list){
      if(s.getAmount() <= 0){
        // 支出0円以下は表示しない
        continue;
      }
      SummaryItem item = new SummaryItem();
      item.setCategoryCode(s.getCategoryCode());
      item.setCategoryName(s.getCategoryName());
      item.setAmount(s.getAmount());

      int day = MyDateUtil.getDayOfMonth(s.getPaymentDate());
      Summary summary = summaries.get(day-1);
      ArrayList<SummaryItem> items = summary.getSummaryItems();
      items.add(item);

      summary.setAmount(summary.getAmount() + s.getAmount());
    }

    return summaries;
  }

}
