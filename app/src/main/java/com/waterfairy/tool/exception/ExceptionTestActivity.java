package com.waterfairy.tool.exception;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.tool.R;

public class ExceptionTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_test);
        try {
            ExceptionTest.test();
        } catch (NullPointerException e) {

        }
    }
}
