package com.waterfairy.tool.regular;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.waterfairy.tool.R;

import java.util.List;

public class RegularActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty(message = "不能为空")
    @Email
    private EditText email;
    @Password(min = 6, message = "")
    @Pattern(message = "密码格式错误", regex = "^1[3|4|5|7|8][0-9]{9}$")
    private EditText password;
    @NotEmpty(message = "不能为空")
    @Pattern(message = "手机号码格式错误", regex = "^1[3|4|5|7|8][0-9]{9}$")
    private EditText mobile;

    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular);
        initView();
        initData();
    }

    private void initData() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initView() {
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "正确", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClick(View view) {
        validator.validate();
    }
}