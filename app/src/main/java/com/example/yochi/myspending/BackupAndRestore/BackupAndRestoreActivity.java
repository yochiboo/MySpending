package com.example.yochi.myspending.BackupAndRestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.example.yochi.myspending.R;
import com.example.yochi.myspending.util.MySQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupAndRestoreActivity extends AppCompatActivity {

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_backup);

    // 戻るボタン
    ActionBar actionBar = getSupportActionBar();
    if(actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // バックアップボタン
    // リストアボタン

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

  private void onBackup(){

    String s="";

    String sdDir= Environment.getExternalStorageDirectory().getPath();     //SDカードディレクトリ
    String sdStatus=Environment.getExternalStorageState();           //SDカードの状態を取得
    boolean b=sdStatus.equals(Environment.MEDIA_MOUNTED);     //SDカードの状態
    if(b==false) {  //書込み状態でマウントされていない。
      s+="\n SDメモリが書込み状態でマウントされていません。";
      return;         //ディレクトリ作成失敗
    }

    MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
    SQLiteDatabase db = hlpr.getWritableDatabase();
    String dbFile=db.getPath();   //DBのディレクトリとファイル名

    s+="\n SDカードのパス = " + sdDir;
    s+="\n SDカードの状態 = " + sdStatus;
    s+="\n SQLiteフォルダー = " + dbFile;


    File f = new File(sdDir + "/MySpending");
    b=f.exists();           //SDカードにtestディレクトリがあるか。
    if(b==false) {          //ディレクトリが存在しないので作成。
      b=f.mkdir();    //　sdcard/testディレクトリを作ってみる。
      if(b==false) {
        s+="\n フォルダの作成に失敗しました。";
        return;         //ディレクトリ作成失敗
      }
    }

    filecopy(dbFile , sdDir+"/test.db");  //DBのファイルをSDにコピー
  }

  private void onRestore(){

  }

  //ファイルのコピー（チャネルを使用）
  private void filecopy(String file_src,String file_dist) {
    int err=0;
    File fi = new File(file_src);
    File fo = new File(file_dist);
    try {
      FileInputStream fis = new FileInputStream(fi);
      FileChannel chi = fis.getChannel();

      FileOutputStream fos = new FileOutputStream(fo);
      FileChannel cho = fos.getChannel();

      chi.transferTo(0, chi.size(), cho);
      chi.close();
      cho.close();
    }
    catch (FileNotFoundException e) {
      //s+="\n FileNotFoundException " + e.getMessage();
      err=1;
    }
    catch (IOException e) {
      //s+="\n IOException" + e.getMessage();
      err=2;
    }
    if(err==0) {
      //s+="\n DBをSDカードにコピーしました。";
    }
  }
}
