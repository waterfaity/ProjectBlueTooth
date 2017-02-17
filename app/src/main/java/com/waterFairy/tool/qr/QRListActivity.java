package com.waterfairy.tool.qr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.dialog.ImgDialog;
import com.waterfairy.tool.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class QRListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<String> list;
    private File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlist);
        initView();
        initData();
    }

    private void initData() {

        String extraPackageFilePath = FileUtils.getExtraPackageFilePath(this, "img/qr/health");
        File file = new File(extraPackageFilePath);
        if (file.exists()) {
            files = file.listFiles();
            list = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                File temp = files[i];
                String name = temp.getName();
                String nameTemp = new String(Base64.decode(name.getBytes(), Base64.DEFAULT));
//                *HYL*BP000001
                String code = nameTemp.split("-")[0];
                list.add(code);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.test_list_item, android.R.id.text1, list);
            mListView.setAdapter(arrayAdapter);
        }


    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = files[position];
        new ImgDialog(this, 600, 600, new File(file.getAbsolutePath())).show();
    }
}
