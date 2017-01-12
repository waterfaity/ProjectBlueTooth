package com.waterfairy.tool.date;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.waterfairy.tool.R;

public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.long_date:
                startActivity(new Intent(this, Long2DateActivity.class));
                break;
            case R.id.str_date:
                startActivity(new Intent(this, Str2Activity.class));
                break;
        }
    }
}
