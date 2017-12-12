package com.example.yochi.myspending.summary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yochi.myspending.history.HistoryActivity;
import com.example.yochi.myspending.R;
import com.example.yochi.myspending.util.MyDateUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SummaryFragment extends Fragment {

  Summary mSummary = null;
  View.OnClickListener mClickListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Bundleの値を受け取る際はonCreateメソッド内で行う
    Bundle args = getArguments();
    // Bundleがセットされていなかった時はNullなのでNullチェックをする
    if (args != null) {
      mSummary = (Summary)args.getSerializable("summary");
    }

    //履歴画面への遷移
    mClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showHistoryActivity(mSummary.getSummaryDate());
      }
    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_summary, null);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);

    if(mSummary == null){
      return;
    } else if(mSummary.getSummaryDate() == null){
      return;
    }

    Locale locale = new Locale("ja", "JP", "JP");

    SimpleDateFormat df;

    df = new SimpleDateFormat("yyyy/MM/dd(EEE)", locale);

    // 日付
    TextView textView = (TextView) view.findViewById(R.id.payment_date);
    textView.setText(df.format(mSummary.getSummaryDate()));

    textView = (TextView) view.findViewById(R.id.amount_value);
    textView.setText(String.format("%,3d" + getString(R.string.amount_unit), mSummary.getAmount()));

    // サマリリスト
    ListView listView = (ListView) view.findViewById(R.id.categoryAmountList);
    listView.setAdapter(new SummaryItemAdapter(view.getContext(), mSummary.getSummaryItems()));
  }

  private void showHistoryActivity(Timestamp ts){
    // 遷移先のアクティビティを起動させる
    Intent intent = new Intent(getContext(), HistoryActivity.class);
    intent.putExtra("param", 0);
    intent.putExtra("date", MyDateUtil.cutTime(ts).getTime());
    startActivity(intent);
  }
}
