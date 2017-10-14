package com.example.yochi.myspending.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    static final String DB = "spend.db";
    static final int DB_VERSION = 1;
    static final String DROP_TABLE = "drop table log;";
    private Context mContext;

    public MySQLiteOpenHelper(Context c) {
        super(c, DB, null, DB_VERSION);
        mContext = c;
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            execSql(db, "sql/createTable");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(DROP_TABLE);
        //onCreate(db);
    }

    /**
     * 引数に指定したassetsフォルダ内のsqlを全て実行します。
     * @param db データベース
     * @param assetsDir assetsフォルダ内のフォルダのパス
     * @throws IOException
     */
    public void execSql(SQLiteDatabase db, String assetsDir) throws IOException {
        AssetManager as = mContext.getResources().getAssets();
        try {
            String files[] = as.list(assetsDir);
            for (int i = 0; i < files.length; i++) {
                String str = readFile(as.open(assetsDir + "/" + files[i]));
                for (String sql: str.split(";")){
                    db.execSQL(sql);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ファイルから文字列を読み込みます。
     * @param is
     * @return ファイルの文字列
     * @throws IOException
     */
    private String readFile(InputStream is) throws IOException{
        BufferedReader br = null;
        try {
            //br = new BufferedReader(new InputStreamReader(is,"SJIS"));
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String str;
            while((str = br.readLine()) != null){
                if(str.startsWith("<!--")){
                    // コメント行は読み飛ばす
                    continue;
                }
                sb.append(str.trim());
            }
            return sb.toString();
        } finally {
            if (br != null) br.close();
        }
    }
}
