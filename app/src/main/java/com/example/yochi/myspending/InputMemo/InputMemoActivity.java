package com.example.yochi.myspending.InputMemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.yochi.myspending.MainActivity;
import com.example.yochi.myspending.R;

public class InputMemoActivity extends AppCompatActivity {

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input_memo);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if(toolbar != null){
      toolbar.setTitle("hoge");

      toolbar.setNavigationIcon(R.drawable.ic_done_black_24dp);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Toast.makeText(InputMemoActivity.this,"back click!!",Toast.LENGTH_LONG).show();
        }
      });
    }

    // 戻るボタン
    ActionBar actionBar = getSupportActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //
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
        return false;
      }
    });

    Intent intent = getIntent();
    if(intent != null){
      String memo = intent.getStringExtra("value");
      editText.setText(memo);
      editText.setSelection(memo.length());
    }

    // キーボード表示
    // 参考：http://qiita.com/kimihiro_n/items/0a2890757128fcf36771
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId()) {
      case android.R.id.home:
        inputEnd();
        finish();
        return true;
      case R.id.action_done:
        inputEnd();
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void inputEnd(){

    Intent intent = new Intent();
    Editable value = ((EditText)findViewById(R.id.value)).getText();
    intent.putExtra("value", String.valueOf(value));
    setResult( Activity.RESULT_OK, intent );

  }

}
