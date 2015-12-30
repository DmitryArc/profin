package com.dka.profin.data.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.dka.profin.common.ColumnMap;
import com.dka.profin.common.ProjectionMap;
import com.dka.profin.data.ProfinProvider;

import java.util.Map;

/**
 * @author Dmitry.Kalyuzhnyi 30.12.2015.
 */
public interface IncomeContract {
    String TABLE_NAME = "_income";

    Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(ProfinProvider.AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    String MIME_TYPE = "/vnd." + ProfinProvider.AUTHORITY + "." + TABLE_NAME;
    String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE;
    String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE;

    Map<String, String> PROJ_MAP = new ProjectionMap.Builder()
            .addColumn(BaseColumns._ID, BaseColumns._ID)
            .addColumn(Columns.CATEGORY_ID, Columns.CATEGORY_ID)
            .addColumn(Columns.SUM, Columns.SUM)
            .addColumn(Columns.COMMENT, Columns.COMMENT)
            .addColumn(Columns.DATE_TIME, Columns.DATE_TIME)
            .addColumn(Columns.PERIOD, Columns.PERIOD)
            .build().getProjectionMap();

    ColumnMap COL_MAP = new ColumnMap.Builder()
            .addColumn(BaseColumns._ID, BaseColumns._ID, ColumnMap.Type.LONG)
            .addColumn(Columns.CATEGORY_ID, Columns.CATEGORY_ID, ColumnMap.Type.INTEGER)
            .addColumn(Columns.SUM, Columns.SUM, ColumnMap.Type.INTEGER)
            .addColumn(Columns.COMMENT, Columns.COMMENT, ColumnMap.Type.STRING)
            .addColumn(Columns.DATE_TIME, Columns.DATE_TIME, ColumnMap.Type.LONG)
            .addColumn(Columns.PERIOD, Columns.PERIOD, ColumnMap.Type.LONG)
            .build();

    String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Columns.CATEGORY_ID + " INTEGER NOT NULL, " +
            Columns.SUM + " INTEGER, " +
            Columns.COMMENT + " TEXT, " +
            Columns.PERIOD + " INTEGER, " +
            Columns.DATE_TIME + " INTEGER NOT NULL);";

    interface Columns extends BaseColumns {
        String CATEGORY_ID = "incomeCategoryId";
        String SUM = "sum";
        String COMMENT = "comment";
        String DATE_TIME = "dateTime";
        String PERIOD = "period";
    }
}
