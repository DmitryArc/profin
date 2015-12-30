package com.dka.profin.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.dka.profin.data.contract.CategoryContract;
import com.dka.profin.data.contract.IncomeContract;
import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 15.12.2015.
 */
public class ProfinDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "profin.db";
    public static final int DATABASE_VERSION = 1;

    public ProfinDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(MainContract.CREATE_TABLE_QUERY);
        db.execSQL(CategoryContract.CREATE_TABLE_QUERY);
        db.execSQL(IncomeContract.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        // NOP yet
    }

}
