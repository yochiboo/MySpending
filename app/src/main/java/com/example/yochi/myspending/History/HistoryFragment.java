package com.example.yochi.myspending.History;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yochi.myspending.R;
import com.example.yochi.myspending.Spend.SpendActivity;
import com.example.yochi.myspending.Summary.Summary;
import com.example.yochi.myspending.Summary.SummaryItemAdapter;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryFragment extends Fragment {

  Date mPaymentDate = new Date();
  View.OnClickListener mClickListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Bundleの値を受け取る際はonCreateメソッド内で行う
    Bundle args = getArguments();
    // Bundleがセットされていなかった時はNullなのでNullチェックをする
    if (args != null) {
      mPaymentDate = (Date)args.getSerializable("date");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_history, null);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);

    //リスト項目が選択された時のイベントを追加
    ListView listView = (ListView) view.findViewById(R.id.historyList);
    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //履歴の編集
        HistoryAdapter adapter = (HistoryAdapter)parent.getAdapter();
        ArrayList<HistoryLog> histories = adapter.getHistiries();
        HistoryLog history = histories.get(position);
        showSpendActivity(view, history.getId());
      }
    });

    redraw(view);
  }


  private void showSpendActivity(View view, int id){
    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(view.getContext(), SpendActivity.class);
    if(id > 0){
      intent.putExtra("param", SpendActivity.PARAM_EDIT);
      intent.putExtra("id", id);
    }else{
      intent.putExtra("param", SpendActivity.PARAM_NEW);
    }
    startActivity(intent);
  }

  // 再描画
  private void redraw(View view){

    HistoryDao dao = new HistoryDao(view.getContext());
    ArrayList<Timestamp> fromTo = MyDateUtil.getDayTimestamp(new Timestamp(mPaymentDate.getTime()));
    ArrayList<HistoryLog> logs = dao.selectHistories(fromTo.get(0), fromTo.get(1));
    long amount = 0;
    for(HistoryLog log : logs){
      amount += log.getAmount();
    }

    Locale locale = new Locale("ja", "JP", "JP");
    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd(EEE)", locale);

    TextView textView = (TextView) view.findViewById(R.id.payment_date);
    textView.setText(df.format(mPaymentDate));

    textView = (TextView) view.findViewById(R.id.amount_value);
    textView.setText(String.format("%,3d" + getString(R.string.amount_unit), amount));

    ListView listView = (ListView) view.findViewById(R.id.historyList);
    HistoryAdapter adapter = (HistoryAdapter)listView.getAdapter();
    if(adapter == null){
      adapter = new HistoryAdapter(view.getContext(), logs);
      listView.setAdapter(adapter);
    }else{
      adapter.setHistiries(logs);
      adapter.notifyDataSetChanged();
    }
  }
}
