package com.waterfairy.tool.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.FileUtils;
import com.waterfairy.tool.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {
    public static final String TYPE_MP3 = ".mp3";
    public static final String TYPE_M4A = ".m4a";
    public static final String TYPE_WAV = ".wav";
    private static final String TAG = "test";
    private TextView info;
    private EditText editText;
    private List<MusicFile> list;
    private List<String> types;
    private List<MusicFile> sameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initView();
        initData();
    }

    private void initData() {
        types = new ArrayList<>();
        types.add(TYPE_M4A);
        types.add(TYPE_MP3);
        types.add(TYPE_WAV);
    }

    private void initView() {
        info = (TextView) findViewById(R.id.info);
        editText = (EditText) findViewById(R.id.input_music_file);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove_same_music:
                removeSameMusic();
                break;
        }
    }

    private void removeSameMusic() {
        searchMusic();
    }

    private void searchMusic() {
        list = new ArrayList<>();
        sameList = new ArrayList<>();
        String folder = editText.getText().toString();
        final File file = new File(FileUtils.getExtraStoragePath() + "/" + folder);
        if (file.exists()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getFiles(file, 0);
                    searchMusic("DJ舞曲-A9 极品慢嗨");
                    if (list.size() != 0) {
                        Log.i("test", "run: " + list.size());
                        remove();
                        Log.i("test", "run: " + sameList.size());
                    }
                }
            }.start();
        } else {
            ToastUtils.show("没有发现文件夹");
        }
    }

    private void searchMusic(String name) {
        for (int i = 0; i < list.size(); i++) {
            boolean contains = list.get(i).getName().contains(name);
            if (contains) {
                Log.i(TAG, "searchMusic: " + list.get(i).getPath());
            }
        }
    }

    private void remove() {
        for (int i = 0; i < list.size(); i++) {
            MusicFile musicFile = list.get(i);
            for (int j = 0; j < list.size(); j++) {
                if (i != j) {
                    MusicFile musicFile1 = list.get(j);
                    checkSave(musicFile, musicFile1);
                }
            }
        }
        for (int i = 0; i < sameList.size(); i++) {
            MusicFile musicFile = sameList.get(i);
            File file = new File(musicFile.getPath());
            if (file.exists()) {
                file.delete();
                Log.i(TAG, "remove: "+file.getAbsolutePath());
            }
        }
    }

    private void checkSave(MusicFile from, MusicFile to) {
        String fromName = from.getName();
        String toName = to.getName();
        fromName = fromName.substring(0, fromName.length() - 4);
        toName = toName.substring(0, toName.length() - 4);
        if (fromName.equals(toName) || fromName.equals(toName + "1") || (fromName + "1").equals(toName)) {
            if (!from.isHas() && !to.isHas()) {
                MusicFile temp = null;
                if (toName.length() >= fromName.length()) {
                    temp = to;
                } else {
                    temp = from;
                }
                temp.setHas(true);
                if (!sameList.contains(temp)) {
                    sameList.add(temp);
                    Log.i("test", "checkSave: " + temp.getName());
                }
            }
        }
    }

    private void getFiles(File file, int num) {
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File fileTemp = files[i];
                if (fileTemp.isDirectory()) {
                    setInfo(fileTemp.getAbsolutePath());
                    getFiles(fileTemp, num + 1);
                } else {
                    String name = fileTemp.getName();
                    if (name.length() > 4) {
                        String substring = name.substring(name.length() - 4, name.length());
                        for (int j = 0; j < types.size(); j++) {
                            String type = types.get(j);
                            if (substring.equals(type)) {
                                MusicFile musicFile = new MusicFile();
                                musicFile.setName(name);
                                musicFile.setType(type);
                                String path = fileTemp.getAbsolutePath();
                                musicFile.setPath(path);
                                musicFile.setPos(list.size());
                                Log.i("test", "getFiles: " + path);
                                list.add(musicFile);
                                setInfo(path);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setInfo(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                info.setText(content);
            }
        });

    }

    public class MusicFile {


        public MusicFile() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        private String name;
        private String type;
        private String path;
        private boolean isHas;
        private int pos;
        private int samePos;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public int getSamePos() {
            return samePos;
        }

        public void setSamePos(int samePos) {
            this.samePos = samePos;
        }

        public boolean isHas() {
            return isHas;
        }

        public void setHas(boolean has) {
            isHas = has;
        }
    }
}
