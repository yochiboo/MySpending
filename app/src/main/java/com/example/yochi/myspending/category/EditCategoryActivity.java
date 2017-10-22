package com.example.yochi.myspending.category;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.yochi.myspending.inputAmount.InputAmountActivity;
import com.example.yochi.myspending.inputMemo.InputMemoActivity;
import com.example.yochi.myspending.inputPaymentDate.InputPaymentDateActivity;
import com.example.yochi.myspending.R;
import com.example.yochi.myspending.spend.SpendAdapter;
import com.example.yochi.myspending.spend.SpendLog;
import com.example.yochi.myspending.spend.SpendLogDao;

import java.sql.Timestamp;
import java.util.Calendar;

public class EditCategoryActivity extends AppCompatActivity {

  public static final int PARAM_NEW = 0;
  public static final int PARAM_EDIT = 1;

  public static final int REQUESTCODE_EDIT_AMOUNT = 1000;
  public static final int REQUESTCODE_EDIT_CATEGORY = 1001;
  public static final int REQUESTCODE_EDIT_MEMO = 1002;
  public static final int REQUESTCODE_EDIT_PAYMENTDATE = 1003;

  SpendLog inputSpend = new SpendLog();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_spend);

    // 戻るボタン
    ActionBar actionBar = getActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    SpendAdapter adapter = new SpendAdapter(this, inputSpend);
    ListView listView = (ListView) findViewById(R.id.spendList);
    listView.setAdapter(adapter);

    //リスト項目が選択された時のイベントを追加
    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
          // 金額
          case 0:
            editAmount();
            break;
          // カテゴリ
          case 1:
            editCategory();
            break;
          // メモ
          case 2:
            editMemo();
            break;
          // 支払い日時
          case 3:
            editPaymentDate();
            break;
        }
      }
    });

    //削除ボタン
    Button button = (Button)findViewById(R.id.dell);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dellSpend();
        Snackbar.make(view, "保存しました。", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
        finish();
      }
    });

    // フローティング保存ボタン
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addSpend();
        Snackbar.make(view, getResources().getText(R.string.dell_message), Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
        finish();
      }
    });

    //パラメータ取得
    Intent intent = getIntent();
    if(intent != null){
      int param = intent.getIntExtra("param", 0);
      switch(param){
        // 編集
        case PARAM_EDIT:
          int id = intent.getIntExtra("id", -1);
          if(id > 0){
            //
            SpendLogDao dao = new SpendLogDao(this);
            inputSpend = dao.getLog(id);
          }
          // 削除ボタンの有効化
          ((Button)findViewById(R.id.dell)).setVisibility(View.VISIBLE);
          break;
        //新規
        case PARAM_NEW:
        default:
          Timestamp ts = new Timestamp(intent.getLongExtra("date", 0));
          inputSpend = new SpendLog();
          inputSpend.setPaymentDate(ts);
          break;
      }
    }

    // Check whether we're recreating a previously destroyed instance
    // https://developer.android.com/training/basics/activity-lifecycle/recreating.html?hl=ja
    if (savedInstanceState != null) {
      // Restore value of members from saved state
      inputSpend = (SpendLog)savedInstanceState.getSerializable("inputSpend");
    }
    redraw();
  }

  @Override
  protected void onResume(){
    super.onResume();
    redraw();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    // Save the user's current game state
    savedInstanceState.putSerializable("inputSpend", inputSpend);

    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
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
      // 金額入力
      case REQUESTCODE_EDIT_AMOUNT:
        inputSpend.setAmount(intent.getIntExtra("value", 0));
        break;
      // カテゴリ選択
      case REQUESTCODE_EDIT_CATEGORY:
        Category category = (Category)intent.getSerializableExtra("selected");
        inputSpend.setCategoryCode(category.getCode());
        inputSpend.setCategoryName(category.getName());
        break;
      // メモ入力
      case REQUESTCODE_EDIT_MEMO:
        inputSpend.setMemo(intent.getStringExtra("value"));
        break;
      // 支払い日付入力
      case REQUESTCODE_EDIT_PAYMENTDATE:
        Calendar cal = (Calendar)intent.getSerializableExtra("value");
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        inputSpend.setPaymentDate(new Timestamp(cal.getTime().getTime()));
        break;
      default:
        break;
    }

    // 画面再描画
    redraw();
  }

  // 金額編集
  private void editAmount(){
    Intent intent = new Intent(this, InputAmountActivity.class);
    intent.putExtra("value", inputSpend.getAmount());
    startActivityForResult(intent, REQUESTCODE_EDIT_AMOUNT);
  }

  // カテゴリ編集
  private void editCategory(){
    // 遷移先のactivityを指定してintentを作成
    Intent intent = new Intent(this, SelectCategoryActivity.class );
    // 遷移先から返却されてくる際の識別コード
    int requestCode = REQUESTCODE_EDIT_CATEGORY;
    // 返却値を考慮したActivityの起動を行う
    startActivityForResult( intent, requestCode );
  }

  // メモ編集
  private void editMemo(){
    Intent intent = new Intent(this, InputMemoActivity.class );
    intent.putExtra("value", inputSpend.getMemo());
    int requestCode = REQUESTCODE_EDIT_MEMO;
    startActivityForResult(intent, requestCode);
  }

  // 支払い日時編集
  private void editPaymentDate(){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(inputSpend.getPaymentDate().getTime());
    Intent intent = new Intent(this, InputPaymentDateActivity.class );
    intent.putExtra("value", cal);
    int requestCode = REQUESTCODE_EDIT_PAYMENTDATE;
    startActivityForResult(intent, requestCode);
  }

  // 再描画
  private void redraw(){
    ListView listView = (ListView) findViewById(R.id.spendList);
    SpendAdapter adapter = (SpendAdapter)listView.getAdapter();
    adapter.setSpendLog(inputSpend);
    adapter.notifyDataSetChanged();
  }

  private void addSpend(){
    SpendLogDao dao = new SpendLogDao(this);
    dao.saveLog(inputSpend);
  }

  private void dellSpend(){
    SpendLogDao dao = new SpendLogDao(this);
    dao.dellLog(inputSpend);
  }
}
