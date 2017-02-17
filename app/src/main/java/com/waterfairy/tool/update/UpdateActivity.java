package com.waterfairy.tool.update;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.ToastUtils;

public class UpdateActivity extends AppCompatActivity implements UpdateManager.OnUpdateCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

    }

    @Override
    public void onUpdate(final boolean update) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(update + "");
            }
        });

    }

    public void onClick(View view) {
        String saveName = "com.huizetime";
        String key = "";
        switch (view.getId()) {
            case R.id.evaluation:
                key = "evaluation";
                break;
            case R.id.shopping:
                key = "shopping";
                break;
            case R.id.store:
                key = "store";
                break;
            case R.id.health:
                key = "health";
                break;
            case R.id.sign:
                key = "sign";
                break;
            case R.id.basketball:
                key = "basketball";
                break;
            case R.id.basketballtv:
                key = "basketballtv";
                break;
            case R.id.get:
                EditText editText1 = (EditText) findViewById(R.id.edit1);
                EditText editText2 = (EditText) findViewById(R.id.edit2);
                saveName = editText1.getText().toString();
                key = editText2.getText().toString();
                break;

        }
        saveName = saveName+"." + key;
        UpdateManager updateManager = UpdateManager.getInstance();
        updateManager.setSaveName(saveName).checkVersion(this, key).setOnUpdateCallback(this);

    }
}
