package com.waterfairy.tool.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.waterfairy.tool.R;
import com.waterfairy.tool.popup.basepopup.BasePopupWindow;

/**
 * Created by water_fairy on 2016/12/20.
 */

public class UpdateVersionPopup extends BasePopupWindow {
    public UpdateVersionPopup(Activity context) {
        super(context);
    }

    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    @Override
    protected View getClickToDismissView() {
        return null;
    }

    @Override
    public View getPopupView() {
        return LayoutInflater.from(mContext).inflate(R.layout.popup_update_version, null);
    }

    @Override
    public View getAnimaView() {
        return null;
    }
}
