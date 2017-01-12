package com.waterfairy.tool.update;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.okhttp.manager.OkHttpManager;
import com.waterfairy.tool.R;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        UpdateManager updateManager = UpdateManager.getInstance();
        updateManager.checkVersion(this, "shopping");
    }
}
