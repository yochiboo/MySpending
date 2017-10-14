package com.example.yochi.myspending.InputAmount;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yochi.myspending.R;

public class InputAmountActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input);

    // 戻るボタン
    ActionBar actionBar = getActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // カテゴリ選択ボタン
    EditText editText = (EditText)findViewById(R.id.value);
    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event == null){
          if(actionId == EditorInfo.IME_ACTION_DONE){
            // ソフトキーボードを隠す
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            inputEnd();
            return true;
          }
        }
        else if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
          if(event.getAction() == KeyEvent.ACTION_UP) {
            // ソフトキーボードを隠す
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
          }
          inputEnd();
          return true;
        }
        return false;
      }
    });

    Intent intent = getIntent();
    if(intent != null){
      int value = intent.getIntExtra("value", 0);
      editText.setText(String.valueOf(value));
    }

    // キーボード表示
    // 参考：http://qiita.com/kimihiro_n/items/0a2890757128fcf36771
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
  }

  private void inputEnd(){

    // 選択カテゴリの返却
    Intent intent = new Intent();
    Editable value = ((EditText)findViewById(R.id.value)).getText();
    intent.putExtra("value", Integer.valueOf(String.valueOf(value)));
    setResult( Activity.RESULT_OK, intent );

    // アクティビティの終了
    finish();

  }

}
