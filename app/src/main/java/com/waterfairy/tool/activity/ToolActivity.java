package com.waterfairy.tool.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.waterfairy.tool.R;
import com.waterfairy.tool.music.MusicActivity;

public class ToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_remove_same:
                startActivity(new Intent(this, MusicActivity.class));
                break;
        }
    }
}
