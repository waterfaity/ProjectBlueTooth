package com.waterfairy.tool.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.tool.R;
import com.waterfairy.tool.databinding.ActivityDataBindingBinding;
import com.waterfairy.tool.databinding.User;

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding);

        ActivityDataBindingBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        User user = new User("xiaoming");
        dataBinding.setUser(user);

    }
}
