package com.dka.profin.fragment;

import android.content.ContentValues;

import com.dka.profin.data.contract.IncomeContract;
import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 30.12.2015.
 */
public class EnterIncomeFragment extends EnterDataFragment {
    public static final String TAG = EnterIncomeFragment.class.getSimpleName();

    @Override
    protected boolean addRecord() {
        final String sumStr = vSum.getText().toString();
        if (!sumStr.isEmpty()) {
            if (mQueryhandler == null) {
                mQueryhandler = new QueryHandler(getContext().getContentResolver());
            }

            final ContentValues cv = new ContentValues();
            cv.put(IncomeContract.Columns.CATEGORY_ID, mCateroryId);
            cv.put(IncomeContract.Columns.SUM, Integer.parseInt(sumStr));
            cv.put(IncomeContract.Columns.DATE_TIME, System.currentTimeMillis());
            mQueryhandler.startInsert(0, null, IncomeContract.CONTENT_URI, cv);

            return true;
        } else {
            return false;
        }
    }
}
