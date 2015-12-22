package com.dka.profin;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dka.profin.data.contract.CategoryContract;
import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 17.12.2015.
 */
public class Test {
    public static void setTestData(@NonNull Context context){
        for(int i = 0; i < 3; i++) {
            final ContentValues cv = new ContentValues();
            cv.put(CategoryContract.Columns.NAME, "Category " + (i + 1));
            cv.put(CategoryContract.Columns.PROPORTION, (i + 1) * 15);
            final Uri insert = context.getContentResolver().insert(CategoryContract.CONTENT_URI, cv);
            Log.d(">>>", "Created category with ID " + ContentUris.parseId(insert));
        }

        for(int i = 0; i < 15; i++) {
            final ContentValues cv = new ContentValues();
            cv.put(MainContract.Columns.DATE_TIME, System.currentTimeMillis());
            cv.put(MainContract.Columns.SUM, (i+1) * 10);
            cv.put(MainContract.Columns.COMMENT, "Comment " + (i+1));
            cv.put(MainContract.Columns.CATEGORY_ID, (i/5+1));
            final Uri insert = context.getContentResolver().insert(MainContract.CONTENT_URI, cv);
            Log.d(">>>", "Created expenditure with ID " + ContentUris.parseId(insert));
        }
    }

}
