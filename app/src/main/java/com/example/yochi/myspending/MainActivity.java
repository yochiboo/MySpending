package com.example.yochi.myspending;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.yochi.myspending.Spend.SpendActivity;
import com.example.yochi.myspending.Summary.SummaryDao;

public class MainActivity extends AppCompatActivity {

  SummaryDao mDao = new SummaryDao(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //mDao.dropSpendLog();
    showTodaySpend();

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAddActivity();
      }
    });

    Button del = (Button)findViewById(R.id.undo_button);
    del.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mDao.delPrevLog();
        Snackbar.make(view, "削除しました。", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
        showTodaySpend();
      }
    });

  }

  @Override
  protected void onResume(){
    super.onResume();
    showTodaySpend();

    // 戻るボタン非表示
    ActionBar bar =getActionBar();
    if(bar != null){
      bar.setDisplayHomeAsUpEnabled(false);
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showAddActivity(){
    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(this, SpendActivity.class);
    startActivity(intent);
  }

  private void showTodaySpend(){
  }
}
