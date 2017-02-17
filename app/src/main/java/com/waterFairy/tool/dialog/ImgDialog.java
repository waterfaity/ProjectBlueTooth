package com.waterfairy.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.print.PrintHelper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.waterfairy.tool.R;

import java.io.File;

/**
 * Created by water_fairy on 2016/12/5.
 */

public class ImgDialog extends Dialog {
    public ImgDialog(Context context, int width, int height, File file) {
        super(context, R.style.dialog);
        LinearLayout linearLayout = new LinearLayout(context);
        ImageView imageView = new ImageView(context);
        linearLayout.addView(imageView);
        linearLayout.setGravity(Gravity.CENTER);
        Picasso.with(context).load(file).resize(width, height).into(imageView);
        setCancelable(true);
        setContentView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
