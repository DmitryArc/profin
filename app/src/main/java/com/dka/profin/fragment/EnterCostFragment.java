package com.dka.profin.fragment;

import android.content.ContentValues;
import android.net.Uri;

import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 30.12.2015.
 */
public class EnterCostFragment extends EnterDataFragment {
    public static final String TAG = EnterCostFragment.class.getSimpleName();

    @Override
    protected boolean addRecord() {
        final String sumStr = vSum.getText().toString();
        if (!sumStr.isEmpty()) {
            if (mQueryhandler == null) {
                mQueryhandler = new QueryHandler(getContext().getContentResolver());
            }

            final ContentValues cv = new ContentValues();
            cv.put(MainContract.Columns.CATEGORY_ID, mCateroryId);
            cv.put(MainContract.Columns.SUM, Integer.parseInt(sumStr));
            cv.put(MainContract.Columns.DATE_TIME, System.currentTimeMillis());
            mQueryhandler.startInsert(0, null, MainContract.CONTENT_URI, cv);

            return true;
        } else {
            return false;
        }
    }
}
