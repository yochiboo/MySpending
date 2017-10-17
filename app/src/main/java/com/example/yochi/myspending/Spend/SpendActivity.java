package com.example.yochi.myspending.Spend;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.yochi.myspending.Category.Category;
import com.example.yochi.myspending.Dialog.MyConfirmDialogFragment;
import com.example.yochi.myspending.InputAmount.InputAmountActivity;
import com.example.yochi.myspending.InputMemo.InputMemoActivity;
import com.example.yochi.myspending.InputPaymentDate.InputPaymentDateActivity;
import com.example.yochi.myspending.R;
import com.example.yochi.myspending.Category.SelectCategoryActivity;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Timestamp;
import java.util.Calendar;

public class SpendActivity extends AppCompatActivity
  implements MyConfirmDialogFragment.NoticeDialogListener{

  public static final int PARAM_NEW = 0;
  public static final int PARAM_EDIT = 1;
  private static final int PARAM_QUICK = 2;

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
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    SpendAdapter adapter = new SpendAdapter(this, inputSpend);
    ListView listView = (ListView) findViewById(R.id.spendList);
    listView.setAdapter(adapter);

    // NumPad
    initializeNumPad();

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
        finish();
      }
    });

    // フローティング保存ボタン
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addSpend();
        finish();
      }
    });

    //パラメータ取得
    Intent intent = getIntent();
    if(intent != null){
      int param = intent.getIntExtra("param", 0);
      switch(param){
        // クイック入力
        case PARAM_QUICK:
          break;
        // 編集
        case PARAM_EDIT:
          int id = intent.getIntExtra("id", -1);
          if(id > 0){
            //
            SpendLogDao dao = new SpendLogDao(this);
            inputSpend = dao.getLog(id);
            // 支払日の時刻削除（保険）
            inputSpend.setPaymentDate(MyDateUtil.cutTime(inputSpend.getPaymentDate()));
          }
          // 削除ボタンの有効化
          ((Button)findViewById(R.id.dell)).setVisibility(View.VISIBLE);
          break;
        //新規
        case PARAM_NEW:
        default:
          Timestamp ts = MyDateUtil.cutTime(new Timestamp(intent.getLongExtra("date", 0)));
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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    //パラメータ取得
    Intent intent = getIntent();
    if(intent != null){
      int param = intent.getIntExtra("param", 0);
      switch(param){
        // 編集
        case PARAM_EDIT:
          // 削除ボタンの有効化
          getMenuInflater().inflate(R.menu.menu_spend, menu);
          break;
        //新規
        case PARAM_NEW:
          // クイック入力
        case PARAM_QUICK:
        default:
          break;
      }
    }
    return true;
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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      case R.id.action_delete:
        OnConfirmDelteSpend();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // データ削除の確認
  private void OnConfirmDelteSpend(){
    MyConfirmDialogFragment dialogFragment = new MyConfirmDialogFragment()
      .setMessage(getString(R.string.msg_confirm_delete))
      .setPositive(getString(R.string.msg_confirm_delete_ok))
      .setNegative(getString(R.string.msg_confirm_delete_cancel));
    String tag = "confirm";
    dialogFragment.show(getFragmentManager(), tag);
  }

  // The dialog fragment receives a reference to this Activity through the
  // Fragment.onAttach() callback, which it uses to call the following methods
  // defined by the NoticeDialogFragment.NoticeDialogListener interface
  @Override
  public void onDialogPositiveClick(DialogFragment dialog) {
    // User touched the dialog's positive button
    dellSpend();
    finish();
  }

  @Override
  public void onDialogNegativeClick(DialogFragment dialog) {
    // User touched the dialog's negative button
    // 何もしない
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

  // NumPad選択イベント処理
  View.OnClickListener mNumPadClickListerner = new View.OnClickListener() {
    @Override
    public void onClick(View view) {

      long amount = inputSpend.getAmount();
      switch(view.getId()){
        /*
        case R.id.num00:
          amount *= 100;
          break;
          */
        case R.id.num0:
          amount *= 10;
          break;
        case R.id.num1:
          amount *= 10;
          amount += 1;
          break;
        case R.id.num2:
          amount *= 10;
          amount += 2;
          break;
        case R.id.num3:
          amount *= 10;
          amount += 3;
          break;
        case R.id.num4:
          amount *= 10;
          amount += 4;
          break;
        case R.id.num5:
          amount *= 10;
          amount += 5;
          break;
        case  R.id.num6:
          amount *= 10;
          amount += 6;
          break;
        case R.id.num7:
          amount *= 10;
          amount += 7;
          break;
        case R.id.num8:
          amount *= 10;
          amount += 8;
          break;
        case R.id.num9:
          amount *= 10;
          amount += 9;
          break;
      }

      inputSpend.setAmount(amount);

      redraw();
    }
  };
  // NumPad選択イベント処理
  View.OnClickListener mNumPadBsClickListerner = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      inputSpend.setAmount(inputSpend.getAmount()/10);
      redraw();
    }
  };
  // NumPad選択イベント処理
  View.OnClickListener mNumPadClearClickListerner = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      inputSpend.setAmount(0);
      redraw();
    }
  };
  // NumPad選択イベント処理
  View.OnClickListener mNumPadControlClickListerner = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    }
  };
  // NumPad選択イベント設定
  private void initializeNumPad(){

    // 数字ボタン
    int numPadButtonIds[] = {
      R.id.num0,//R.id.num00,
      R.id.num1,R.id.num2,R.id.num3,
      R.id.num4,R.id.num5, R.id.num6,
      R.id.num7,R.id.num8,R.id.num9,
      };
    for(int id : numPadButtonIds){
      ((Button)findViewById(id)).setOnClickListener(mNumPadClickListerner);
    }

    // 制御ボタン
    ((Button)findViewById(R.id.bs)).setOnClickListener(mNumPadBsClickListerner);
    int numPadControlButtonIds[] = {
      //R.id.ce,
      R.id.clr,
    };
    for(int id : numPadControlButtonIds){
      ((Button)findViewById(id)).setOnClickListener(mNumPadClearClickListerner);
    }
  }
}
