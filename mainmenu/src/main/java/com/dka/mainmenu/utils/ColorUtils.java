package com.dka.mainmenu.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author Dmitry.Kalyuzhnyi 11.12.2015.
 */
public class ColorUtils {

    public static int getThemeAttrColor(Context context,
                                        int attr) {
        final TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            if (typedValue.type >= TypedValue.TYPE_FIRST_INT && typedValue.type <= TypedValue.TYPE_LAST_INT) {
                return typedValue.data;
            } else if (typedValue.type == TypedValue.TYPE_STRING) {
                return context.getResources().getColor(typedValue.resourceId);
            }
        }
        return 0;
    }

}