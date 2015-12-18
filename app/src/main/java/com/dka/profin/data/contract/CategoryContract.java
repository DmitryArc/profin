package com.dka.profin.data.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.dka.profin.common.ColumnMap;
import com.dka.profin.common.ProjectionMap;
import com.dka.profin.data.ProfinProvider;

import java.util.Map;

/**
 * @author Dmitry.Kalyuzhnyi 15.12.2015.
 */
public interface CategoryContract {
    String TABLE_NAME = "_categories";
    String PATH_JOINED = "full";

    Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(ProfinProvider.AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    Uri CONTENT_URI_JOINED = Uri.withAppendedPath(CONTENT_URI, PATH_JOINED);

    String MIME_TYPE = "/vnd." + ProfinProvider.AUTHORITY + "." + TABLE_NAME;
    String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE;
    String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE;

    Map<String, String> PROJ_MAP = new ProjectionMap.Builder()
            .addColumn(BaseColumns._ID, BaseColumns._ID)
            .addColumn(Columns.NAME, Columns.NAME)
            .addColumn(Columns.PROPORTION, Columns.PROPORTION)
            .build().getProjectionMap();

    Map<String, String> PROJ_MAP_JOINED_WITH_COSTS = new ProjectionMap.Builder()
            .addColumn(BaseColumns._ID, TABLE_NAME + "." + BaseColumns._ID)
            .addColumn(Columns.NAME, Columns.NAME)
            .addColumn(Columns.PROPORTION, Columns.PROPORTION)
            .addColumn(MainContract.Columns.SUM, "SUM(" + MainContract.TABLE_NAME + "." + MainContract.Columns.SUM +")")
            .build().getProjectionMap();

    ColumnMap COL_MAP = new ColumnMap.Builder()
            .addColumn(BaseColumns._ID, BaseColumns._ID, ColumnMap.Type.LONG)
            .addColumn(Columns.NAME, Columns.NAME, ColumnMap.Type.STRING)
            .addColumn(Columns.PROPORTION, Columns.PROPORTION, ColumnMap.Type.INTEGER)
            .build();

    String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Columns.NAME + " TEXT NOT NULL, " +
            Columns.PROPORTION + " INTEGER NOT NULL);";

    interface Columns extends BaseColumns {

        String NAME = "name";
        String PROPORTION = "proportion";
    }
}
