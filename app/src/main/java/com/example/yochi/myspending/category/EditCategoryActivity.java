package com.example.yochi.myspending.category;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.yochi.myspending.dialog.MyConfirmDialogFragment;
import com.example.yochi.myspending.inputAmount.InputAmountActivity;
import com.example.yochi.myspending.inputMemo.InputMemoActivity;
import com.example.yochi.myspending.inputPaymentDate.InputPaymentDateActivity;
import com.example.yochi.myspending.R;
import com.example.yochi.myspending.spend.SpendAdapter;
import com.example.yochi.myspending.spend.SpendLog;
import com.example.yochi.myspending.spend.SpendLogDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class EditCategoryActivity extends AppCompatActivity {

  public static final int PARAM_NEW = 0;
  public static final int PARAM_EDIT = 1;
  public static final String PARAM_CATEGORY = "category";

  private static final String SAVE_STATE = "state";

  /** 編集中カテゴリ */
  Category mCategory = new Category();

  /** モード */
  int mMode = PARAM_NEW;

  /**
   * 生成イベント
   * @param savedInstanceState 状態
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_edit);

    // 戻るボタン設定
    if(getActionBar() != null){
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // カテゴリ名

    // 親カテゴリスピナーイベント設定
    Spinner spinner = (Spinner)findViewById(R.id.spinnerParent);
    spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 選択した親カテゴリをメンバへ保存
      }
    });

    // フローティング保存ボタンイベント設定
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onSaveCategory();
      }
    });

    //パラメータ取得
    Intent intent = getIntent();
    if(intent != null){
      mMode = intent.getIntExtra("param", 0);
      switch(mMode){
        // 編集
        case PARAM_EDIT:
          mCategory = (Category)intent.getSerializableExtra(PARAM_CATEGORY);
          // 削除ボタンの有効化
          break;
        //新規
        case PARAM_NEW:
        default:
          break;
      }
    }

    //状態復元
    // https://developer.android.com/training/basics/activity-lifecycle/recreating.html?hl=ja
    if (savedInstanceState != null) {
      // Restore value of members from saved state
      mCategory = (Category)savedInstanceState.getSerializable(SAVE_STATE);
    }
  }

  /**
   * 再開イベント
   */
  @Override
  protected void onResume(){
    super.onResume();
    redraw();
  }

  /**
   * オプションメニュー作成イベント
   * @param menu オプションメニュー
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // 編集モードの場合、削除ボタンを有効化する
    switch(mMode){
      // 編集
      case PARAM_EDIT:
        // 削除ボタンの有効化
        getMenuInflater().inflate(R.menu.menu_spend, menu);
        break;
      //新規
      case PARAM_NEW:
      default:
        // 何もしない
        break;
    }
    return true;
  }

  /**
   * 状態保存イベント
   * @param savedInstanceState 状態
   */
  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    // カテゴリ名

    // カテゴリ一覧選択位置

    super.onSaveInstanceState(savedInstanceState);
  }

  /**
   * オプションメニュー選択イベント
   * @param selected 選択メニュー
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem selected) {

    switch(selected.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      case R.id.action_delete:
        onConfirmDeleteCategory();
        return true;
    }
    return super.onOptionsItemSelected(selected);
  }

  /**
   * カテゴリ保存イベント
   */
  private void onSaveCategory(){

    SelectCategoryDao dao = new SelectCategoryDao(this);

    switch(mMode){
      // 編集
      case PARAM_EDIT:
        dao.updateCategory(mCategory);
        // 削除ボタンの有効化
        break;
      //新規
      case PARAM_NEW:
      default:
        // カテゴリコードおよび表示順採番
        dao.insertCategory(mCategory);
        break;
    }
    finish();
  }

  /**
   * カテゴリ削除確認イベント
   */
  private void onConfirmDeleteCategory(){
    // 削除確認ダイアログ表示
    MyConfirmDialogFragment dialogFragment = new MyConfirmDialogFragment()
      .setMessage(getString(R.string.msg_confirm_delete))
      .setPositive(getString(R.string.msg_confirm_delete_ok))
      .setNegative(getString(R.string.msg_confirm_delete_cancel));
    String tag = "confirm";
    dialogFragment.show(getFragmentManager(), tag);
  }

  /**
   * カテゴリ削除イベント
   */
  private void onDeleteCategory(){
    SelectCategoryDao dao = new SelectCategoryDao(this);
    dao.deleteCategory(mCategory.getCode());
    finish();
  }

  /**
   * 再描画
   */
  private void redraw(){

    // カテゴリ名
    EditText editText = (EditText) findViewById(R.id.editName);
    editText.setText(mCategory.getName());

    // 親カテゴリ一覧配置
    SelectCategoryDao dao = new SelectCategoryDao(this);
    ArrayList<Category> parents =  dao.selectAllParents();
    Spinner spinner = (Spinner)findViewById(R.id.spinnerParent);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    adapter.add("（なし）");
    for(Category c : parents){
      adapter.add(c.getName());
    }
    spinner.setAdapter(adapter);

    // 親カテゴリ選択位置設定
    if(mCategory.getParentCode() > 0){
      int pos = 1;
      for(Category c : parents){
        if(mCategory.getParentCode().equals(c.getCode())){
          break;
        }
        pos++;
      }
      spinner.setSelection(pos);
    }
  }
}
