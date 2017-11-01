package com.example.yochi.myspending.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.yochi.myspending.R;
import com.example.yochi.myspending.spend.SpendLog;
import com.example.yochi.myspending.spend.SpendLogDao;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Timestamp;

public class SelectCategoryActivity extends AppCompatActivity {

  public static final int PARAM_SELECT = 0;
  public static final int PARAM_EDIT = 1;

  SelectCategoryDao mDao = new SelectCategoryDao(this);
  int mMode = PARAM_SELECT;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_select);

    // 戻るボタン
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // カテゴリ並べ替え編集ボタン

    //リスト項目が選択された時のイベントを追加
    ListView listView = (ListView) findViewById(R.id.categoryList);
    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          ListView listView = (ListView) parent;
          Category item = (Category)listView.getItemAtPosition(position);
          switch(mMode){
            // 編集
            case PARAM_EDIT:
              editCategory(item);
              break;
            // 選択
            case PARAM_SELECT:
            default:
              selectCategory(item);
              break;
          }
        }
    });
    
    //リスト項目が長押しされた時のイベントを追加
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
          return false;
        }
    });

    // フローティング保存ボタン
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    //パラメータ取得
    Intent intent = getIntent();
    if(intent != null){
      mMode = intent.getIntExtra("param", 0);
    }
    switch(mMode){
      // 編集
      case PARAM_EDIT:
        break;
      // 選択
      case PARAM_SELECT:
      default:
        // 追加ボタンの非表示化
        fab.setVisibility(View.INVISIBLE);
        break;
    }
  }

  @Override
  protected void onResume(){
    super.onResume();

    // カテゴリ一覧
    ListView listView = (ListView) findViewById(R.id.categoryList);
    CategoryAdapter adapter = (CategoryAdapter)listView.getAdapter();
    if(adapter == null){
      // 初回表示
      adapter = new CategoryAdapter(this, mDao.selectCategoryList());
      listView.setAdapter(adapter);
    }else{
      // 再表示
      adapter.setItems(mDao.selectCategoryList());
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void onAddCategory(){

  }

  private void selectCategory(Category selected){

    // 選択カテゴリの返却
    Intent intent = new Intent();
    intent.putExtra("selected", selected);
    setResult( Activity.RESULT_OK, intent );

    // アクティビティの終了
    finish();

  }

  private void editCategory(Category selected){
    Intent intent = new Intent(this, EditCategoryActivity.class);
    intent.putExtra("param", EditCategoryActivity.PARAM_EDIT);
    intent.putExtra(EditCategoryActivity.PARAM_CATEGORY, selected);
    startActivity(intent);
  }

  private void addCategory(){
    Intent intent = new Intent(this, EditCategoryActivity.class);
    intent.putExtra("param", EditCategoryActivity.PARAM_NEW);
    startActivity(intent);
  }

}
