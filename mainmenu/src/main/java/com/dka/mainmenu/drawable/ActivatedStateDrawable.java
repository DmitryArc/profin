package com.dka.mainmenu.drawable;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * @author Dmitry.Kalyuzhnyi 11.12.2015.
 */
public class ActivatedStateDrawable extends StateListDrawable {

    private int mColor;
    private int mActivatedColor;

    public ActivatedStateDrawable(Drawable drawable,
                                  int color,
                                  int activatedColor) {
        mColor = color;
        mActivatedColor = activatedColor;

        addState(new int[]{android.R.attr.state_activated}, drawable);
        addState(new int[]{}, drawable);
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isActivated = false;
        for (int state : states) {
            if (state == android.R.attr.state_activated) {
                isActivated = true;
            }
            if (isActivated) {
                super.setColorFilter(mActivatedColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                super.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return super.onStateChange(states);

    }

    @Override
    public boolean isStateful() {
        return true;
    }

}