package com.waterfairy.tool.date;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.DateUtils;

public class Str2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str2);
    }
    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.long_time);
        String string = editText.getText().toString();
        string="Fri Jan 17 11:14:45 CST 2016";
        TextView textView = (TextView) findViewById(R.id.date_time);
        String trans = DateUtils.trans(string);
        textView.setText(trans);
    }
}
