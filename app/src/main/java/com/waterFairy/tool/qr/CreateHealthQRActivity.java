package com.waterfairy.tool.qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.waterfairy.tool.R;
import com.waterfairy.tool.dialog.ImgDialog;
import com.waterfairy.tool.utils.FileUtils;
import com.waterfairy.tool.utils.QRCodeUtil;
import com.waterfairy.tool.utils.ToastUtils;

import java.io.File;

public class CreateHealthQRActivity extends AppCompatActivity {
    private static final String TAG = "QE_code";
    private String name;
    private String address;
    private EditText editText;
    private EditText editTextWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creater_health_qr);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        editText = (EditText) findViewById(R.id.input);
        editTextWidth = (EditText) findViewById(R.id.input_width);

    }

    public void onClick(View view) {
        String content = getInput();
        if (TextUtils.isEmpty(content)) return;
        int width = getWidth();

        String string = "*HYL*BP000001-8CDE52FBCCAF";

        String temp = "";
        switch (view.getId()) {
            case R.id.ic:
                //读卡器
                temp = "IC";
                break;
            case R.id.bp1:
                //血压/心率   脉搏波
                temp = "BP";
                break;
            case R.id.bp2:
                //血压/心率   欧姆龙
                temp = "OP";
//                ToastUtils.show("暂不支持");
                break;
            case R.id.bs:
                //血糖
                temp = "BS";
                break;
            case R.id.we:
                //体重
                temp = "WE";
                break;
            case R.id.bo:
                //血氧
                temp = "BO";
                break;
            case R.id.ec:
                //心电
                temp = "EC";
                break;
        }

        address = address.replace(":", "");
        //文件名字
        String name = temp + content + "-" + address;
        //二维码内容
        String output = "*HYL*" + name;

        //图片路径
        String extraPackageFilePath = FileUtils.getExtraPackageFilePath(this, "img/qr/health");
        //判断相同address  是否有保存
        delSameAddressFile(extraPackageFilePath, address);
        //图片文件完整路径(编码)
        name = new String(Base64.encode(name.getBytes(), Base64.DEFAULT));
        String imgPath = extraPackageFilePath + "/" + name + ".jpg";
        //生成二维码
        QRCodeUtil.createQRImage(output, width, null, imgPath);
        //展示
        new ImgDialog(this, width, width, new File(imgPath)).show();
        Log.i(TAG, "onClick: " + output);
    }


    private void delSameAddressFile(String extraPackageFilePath, String address) {
        File file = new File(extraPackageFilePath);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) return;
        for (int i = 0; i < files.length; i++) {
            File fileTemp = files[i];
            String name = fileTemp.getName();
            name = name.replace(".jpg", "");
            name = new String(Base64.decode(name.getBytes(), Base64.DEFAULT));
            String[] split = name.split("-");
            if (split.length == 2) {
                if (split[1].equals(address)) {
                    fileTemp.delete();
                }
            }
            Log.i(TAG, "delSameAddressFile: " + file);
        }
    }

    private int getWidth() {
        String widthStr = editTextWidth.getText().toString();
        if (TextUtils.isEmpty(widthStr)) {
            return 600;
        } else {
            return Integer.parseInt(widthStr);
        }
    }

    private String getInput() {
        String string = editText.getText().toString();
        if (TextUtils.isEmpty(string)) {
            ToastUtils.show("请输入编号");
            return "";
        } else {
            string = Integer.parseInt(string) + "";
        }
        String temp = string;
        for (int i = 0; i < 6 - string.length(); i++) {
            temp = "0" + temp;
        }
        return temp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("查看").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, QRListActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
