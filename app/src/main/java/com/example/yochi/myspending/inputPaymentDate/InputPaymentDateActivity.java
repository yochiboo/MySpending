package com.example.yochi.myspending.inputPaymentDate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import java.util.Calendar;

public class InputPaymentDateActivity extends AppCompatActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    Calendar cal = (Calendar)intent.getSerializableExtra("value");
    final int year = cal.get(Calendar.YEAR);
    final int month = cal.get(Calendar.MONTH);
    final int day = cal.get(Calendar.DAY_OF_MONTH);

    final DatePickerDialog datePickerDialog = new DatePickerDialog(
      this,
      new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          Calendar cal = Calendar.getInstance();
          cal.set(year, monthOfYear, dayOfMonth, 0, 0);
          Intent intent = new Intent();
          intent.putExtra("value", cal);
          setResult(Activity.RESULT_OK, intent);
          // アクティビティの終了
          finish();
        }
      },
      year, month, day);
    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        // キャンセルされたときの処理
        finish();
      }
    });
    datePickerDialog.show();
  }
}
