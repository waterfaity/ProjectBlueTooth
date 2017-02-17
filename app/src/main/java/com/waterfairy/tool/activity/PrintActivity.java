package com.waterfairy.tool.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.FileUtils;

import java.io.File;
import java.net.URL;

public class PrintActivity extends AppCompatActivity {

    private TextView support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        printSystem();
//        printViaGoogleCloudPrintApp(Uri.fromFile(new File(FileUtils.getExtraStoragePath()+"/jj.jpg")));
    }

    private void printSystem() {
        support = (TextView) findViewById(R.id.support);
        boolean b = PrintHelper.systemSupportsPrint();
        if (b) {
            support.setText("系统支持打印机");
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher);
            PrintHelper printHelper = new PrintHelper(this);
            printHelper.printBitmap("droids.jpg", bitmap);
        } else {
            support.setText("系统不支持打印机");
        }
    }


    private void printViaGoogleCloudPrintApp(Uri content) {

        Intent printIntent = new Intent(Intent.ACTION_SEND);

        printIntent.setPackage("com.google.android.apps.cloudprint");

        printIntent.setType("image/*");

        printIntent.putExtra(Intent.EXTRA_TITLE, "Print Test Title");

        printIntent.putExtra(Intent.EXTRA_STREAM, content);

        startActivity(printIntent);

    }
}
