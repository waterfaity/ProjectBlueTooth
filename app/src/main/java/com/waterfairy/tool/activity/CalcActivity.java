package com.waterfairy.tool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.waterfairy.tool.R;

public class CalcActivity extends AppCompatActivity {

    private static final String TAG = "calc";
    private EditText editText;
    private TextView textView;

    private EditText mul, div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        textView = (TextView) findViewById(R.id.text);
        editText = (EditText) findViewById(R.id.edit);
        mul = (EditText) findViewById(R.id.mul);
        div = (EditText) findViewById(R.id.div);
    }

    public void onClick(View view) {
        String string = editText.getText().toString();

        String mulStr = mul.getText().toString();
        String divStr = div.getText().toString();
        double mul = TextUtils.isEmpty(mulStr) ? 1 : Double.parseDouble(mulStr);
        double div = TextUtils.isEmpty(divStr) ? 1 : Double.parseDouble(divStr);
        if (!TextUtils.isEmpty(string)) {
            double v = Double.parseDouble(string);
            double dddd = v / div * mul;
            textView.setText("结果:　" + dddd);
            editText.setText("");
        }

        byte[] data = new byte[11];
        data[0] = (byte) 0xbb;
        data[1] = 99;//电量
        data[2] = 28;//温度
        data[3] = 1;//错误代号
        //总里程
        data[4] = (byte) 0xff;
        data[5] = (byte) 0xff;
        data[6] = (byte) 0xff;
        data[7] = (byte) 0xff;
        //当天
        data[8] = 1;
        data[9] = 1;
        //速度
        data[10] = (byte) 0xff;

        long max =
                ((data[4] & 0xffffffff) << 3 * 8) +
                ((data[5] & 0xffffff) << 2 * 8) +
                ((data[6] & 0xffff) << 1 * 8) +
                ((data[7] & 0xFF) << 0 * 8);
        Log.i(TAG, "onClick: " + ((data[4] & 0xffffffff) << 3 * 8));
        Log.i(TAG, "onClick: " + ((data[5] & 0xffffff) << 2 * 8));
        Log.i(TAG, "onClick: " + ((data[6] & 0xffff) << 1 * 8));
        Log.i(TAG, "onClick: " + ((data[7] & 0xFF) << 0 * 8));


        Log.i(TAG, "onClick: " + max);

//           1011 1111    byte = -1  -->  255
// 0000 0000 1111 1111

//
//        long maxToday = (data[8] << (1 * 8)) + (data[9]);
//        Log.i(TAG, "onClick: " + maxToday);
//
//        Log.i(TAG, "onClick: "+data[10]);
//        Log.i(TAG, "onClick: "+((int)data[10]));
//        Log.i(TAG, "onClick: "+(0xff));


    }
}
