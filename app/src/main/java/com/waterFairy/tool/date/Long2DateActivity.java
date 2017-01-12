package com.waterfairy.tool.date;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.DateUtils;

public class Long2DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long2_date);
    }

    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.long_time);
        String string = editText.getText().toString();
        long date = 0;
        if (TextUtils.isEmpty(string)) date = 0;
        else date = Long.parseLong(string);

        TextView textView = (TextView) findViewById(R.id.date_time);
        String trans = DateUtils.trans(date);
        textView.setText(trans);
    }
}
