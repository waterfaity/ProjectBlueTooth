package com.waterfairy.tool.xml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.waterfairy.tool.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class XMLActivity extends AppCompatActivity {

    private static final String TAG = "xml";
    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pull_write:
                write(persons);
                break;
            case R.id.pull_read:
                readXml();
                break;
        }
    }

    private void write(List<Person> persons) {
        File file = new File(getExternalCacheDir().getAbsolutePath() + "/" + getPackageName() + "/apk");
        boolean canWrite = false;
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) canWrite = true;
        } else {
            canWrite = true;
        }
        if (canWrite) {
            File fileXml = new File(file.getAbsolutePath() + "/versionConfigure.xml");
            if (fileXml.exists()) {
                canWrite = true;
            } else {
                try {
                    fileXml.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    canWrite = false;
                }
            }
            if (canWrite) {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(fileXml);
                    canWrite = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    canWrite = false;
                }
                if (canWrite) {
                    PullPersonParser pullPersonParser = PullPersonParser.getPullPersonParser();
                    try {
                        pullPersonParser.writeXml(persons, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return;
        }

    }

    private void readXml() {
        try {
            InputStream open = getResources().getAssets().open("person.xml");
            PullPersonParser pullPersonParser = PullPersonParser.getPullPersonParser();
            try {
                persons = pullPersonParser.readXml(open);
                Log.i(TAG, "readXml: " + persons.size());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
