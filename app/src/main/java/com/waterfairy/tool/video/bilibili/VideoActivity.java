package com.waterfairy.tool.video.bilibili;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.FileUtils;
import com.waterfairy.tool.utils.ImageUtils;
import com.waterfairy.tool.utils.ToastUtils;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "video";
    private EditText editText, editText1, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit);
        editText1 = (EditText) findViewById(R.id.edit1);
        editText2 = (EditText) findViewById(R.id.edit2);
    }

    public void onClick(View view) {
        String path = editText.getText().toString();
        if (TextUtils.isEmpty(path)) {
            ToastUtils.show("请输入地址");
            return;
        }
        File file = new File(FileUtils.getExtraStoragePath() + "/" + path);
        if (!file.exists()) {
            ToastUtils.show("文件不存在");
            return;
        }

        switch (view.getId()) {
            case R.id.start:

                break;
            case R.id.get_picture:
                getPicture(file);
                break;
            case R.id.start_from_net:
                break;
            case R.id.start_live:

                break;
        }
    }

    private void getPicture(File file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getAbsolutePath());
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        ToastUtils.show("视频时间:" + time + " ms");
        String string = editText1.getText().toString();
        int quality = Integer.parseInt(string);
        Bitmap bitmap = retriever.getFrameAtTime(1 * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        boolean save = ImageUtils.saveBitmap(FileUtils.getExtraStoragePath() + "/" +editText2.getText().toString()+"-"+ quality + ".jpg", bitmap
        , Bitmap.CompressFormat.JPEG,quality);
        Log.i(TAG, "getPicture: " + save);
    }
}
