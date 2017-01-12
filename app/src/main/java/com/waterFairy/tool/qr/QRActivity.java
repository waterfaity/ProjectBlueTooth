package com.waterfairy.tool.qr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.tool.R;

public class QRActivity extends AppCompatActivity {
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
    }
}
