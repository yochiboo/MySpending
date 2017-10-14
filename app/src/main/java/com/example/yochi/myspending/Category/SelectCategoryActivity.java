package com.example.yochi.myspending.Category;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yochi.myspending.R;

public class SelectCategoryActivity extends AppCompatActivity {

  SelectCategoryDao mDao = new SelectCategoryDao(this);

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
          selectCategory(item);
        }
    });
    
    //リスト項目が長押しされた時のイベントを追加
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
          return false;
        }
    });
/*
    Button del = (Button)findViewById(R.id.cancel_button);
    del.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
*/
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

  private void selectCategory(Category selected){

    // 選択カテゴリの返却
    Intent intent = new Intent();
    intent.putExtra("selected", selected);
    setResult( Activity.RESULT_OK, intent );

    // アクティビティの終了
    finish();

  }
}
