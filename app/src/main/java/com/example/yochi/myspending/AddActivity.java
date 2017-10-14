package com.example.yochi.myspending;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yochi.myspending.Category.Category;
import com.example.yochi.myspending.Category.SelectCategoryActivity;
import com.example.yochi.myspending.Spend.SpendLog;
import com.example.yochi.myspending.Spend.SpendLogDao;

public class AddActivity extends AppCompatActivity {

  public static int REQUESTCODE_SELECT_CATEGORY = 1001;

  Category selectedCategory = new Category();
  SpendLogDao mDao = new SpendLogDao(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add);

    // 戻るボタン
    ActionBar actionBar = getActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // カテゴリ選択ボタン
    Button button = (Button)findViewById(R.id.category_button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // 遷移先のactivityを指定してintentを作成
        Intent intent = new Intent( view.getContext(), SelectCategoryActivity.class );
        // 遷移先から返却されてくる際の識別コード
        int requestCode = REQUESTCODE_SELECT_CATEGORY;
        // 返却値を考慮したActivityの起動を行う
        startActivityForResult( intent, requestCode );
      }
    });

    Button add = (Button)findViewById(R.id.save_button);
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addSpend();
        Snackbar.make(view, "保存しました。", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
        finish();
      }
    });

    Button del = (Button)findViewById(R.id.cancel_button);
    del.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    // フローティング保存ボタン
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addSpend();
        Snackbar.make(view, "保存しました。", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
        finish();
      }
    });

    // カテゴリ初期表示
    selectedCategory.setCode(0);
    selectedCategory.setName("なし");
    updateCategoryName();

  }

  @Override
  protected void onResume(){
    super.onResume();
    updateCategoryName();
  }

  // 遷移先画面の結果受取
  public void onActivityResult( int requestCode, int resultCode, Intent intent )
  {
    // startActivityForResult()の際に指定した識別コードとの比較
    switch(requestCode){
      // カテゴリ選択
      case 1001:
        if(intent == null){
          // 返却値なし
          return;
        }
        Category c = (Category)intent.getSerializableExtra("selected");
        if(c.getCode() < 0){
          // カテゴリ選択取り消し
          return;
        }
        // 画面＆メンバ変数更新
        selectedCategory = c;
        updateCategoryName();
        break;
      default:
        break;
    }
  }

  private void updateCategoryName(){
    ((EditText) findViewById(R.id.category_name)).setText(selectedCategory.getName());
  }

  private void addSpend(){
    EditText amount = (EditText) findViewById(R.id.amount_value);
    EditText memo = (EditText) findViewById(R.id.memo_value);

    SpendLog log = new SpendLog();
    log.setCategoryCode(selectedCategory.getCode());
    log.setAmount(Integer.parseInt(amount.getText().toString()));
    log.setMemo(memo.getText().toString());

    mDao.saveLog(log);
  }
}
