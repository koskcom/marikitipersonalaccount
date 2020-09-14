package bsl.co.ke.fundsmanagementapi.ui.common.menu.slidingrootnav.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import bsl.co.ke.fundsmanagementapi.ui.common.menu.slidingrootnav.SlidingRootNavLayout;


/**
 * Created by yarolegovich on 26.03.2017.
 */

public class HiddenMenuClickConsumer extends View {

    private SlidingRootNavLayout menuHost;

    public HiddenMenuClickConsumer(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return menuHost.isMenuClosed();
    }

    public void setMenuHost(SlidingRootNavLayout layout) {
        this.menuHost = layout;
    }
}
