package com.dka.profin.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dka.profin.BuildConfig;
import com.dka.profin.common.ColumnMap;
import com.dka.profin.data.contract.CategoryContract;
import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 15.12.2015.
 */
public class ProfinProvider extends ContentProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public static final String SELECTION_BY_ID_PATTERN = "(%s." + BaseColumns._ID + "=%d)";

    public static final int MAIN_DIR = 101;
    public static final int MAIN_ITEM = 102;
    public static final int CATEGORY_DIR = 201;
    public static final int CATEGORY_ITEM = 202;
    public static final int CATEGORY_DIR_JOINED = 301;
    public static final int CATEGORY_ITEM_JOINED = 302;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, MainContract.TABLE_NAME, MAIN_DIR);
        sUriMatcher.addURI(AUTHORITY, MainContract.TABLE_NAME + "/#", MAIN_ITEM);
        sUriMatcher.addURI(AUTHORITY, CategoryContract.TABLE_NAME, CATEGORY_DIR);
        sUriMatcher.addURI(AUTHORITY, CategoryContract.TABLE_NAME + "/#", CATEGORY_ITEM);
        sUriMatcher.addURI(AUTHORITY, CategoryContract.TABLE_NAME + "/" + CategoryContract.PATH_JOINED, CATEGORY_DIR_JOINED);
        sUriMatcher.addURI(AUTHORITY, CategoryContract.TABLE_NAME + "/" + CategoryContract.PATH_JOINED + "/#", CATEGORY_ITEM_JOINED);
    }

    @Nullable
    private volatile ProfinDatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProfinDatabaseHelper(getContext());
        return mDbHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        final String table;
        final boolean isItemUriType;
        final int uriType = sUriMatcher.match(uri);
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String groupBy = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            queryBuilder.setStrict(true);
        }

        switch (uriType) {
            case MAIN_DIR:
            case MAIN_ITEM:
                table = MainContract.TABLE_NAME;
                queryBuilder.setTables(table);
                queryBuilder.setProjectionMap(MainContract.PROJ_MAP);

                if(uriType == MAIN_ITEM){
                    isItemUriType = true;
                } else {
                    isItemUriType = false;
                }
                break;

            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                table = CategoryContract.TABLE_NAME;
                queryBuilder.setTables(table);
                queryBuilder.setProjectionMap(CategoryContract.PROJ_MAP);

                if(uriType == CATEGORY_ITEM){
                    isItemUriType = true;
                } else {
                    isItemUriType = false;
                }
                break;

            case CATEGORY_DIR_JOINED:
            case CATEGORY_ITEM_JOINED:
                StringBuilder sb = new StringBuilder();
                sb.append(CategoryContract.TABLE_NAME);
                sb.append(" LEFT OUTER JOIN ");
                sb.append(MainContract.TABLE_NAME);
                sb.append(" ON (");
                sb.append(CategoryContract.TABLE_NAME);
                sb.append('.');
                sb.append(CategoryContract.Columns._ID);
                sb.append(" = ");
                sb.append(MainContract.TABLE_NAME);
                sb.append('.');
                sb.append(MainContract.Columns.CATEGORY_ID);
                sb.append(")");
                table = sb.toString();
                groupBy = MainContract.Columns.CATEGORY_ID;
                queryBuilder.setTables(table);
                queryBuilder.setProjectionMap(CategoryContract.PROJ_MAP_JOINED_WITH_COSTS);

                if(uriType == CATEGORY_ITEM){
                    isItemUriType = true;
                } else {
                    isItemUriType = false;
                }
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(isItemUriType){
            final long row = ContentUris.parseId(uri);
            if (row >= 0) {
                queryBuilder.appendWhere(String.format(SELECTION_BY_ID_PATTERN, table, row));
            }
        }

        final Cursor cursor = queryBuilder.query(mDbHelper.getReadableDatabase(), projection, selection, selectionArgs, groupBy, null, sortOrder);
        if(cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MAIN_DIR:
                return MainContract.CONTENT_TYPE_DIR;
            case MAIN_ITEM:
                return MainContract.CONTENT_TYPE_ITEM;
            case CATEGORY_DIR:
                return CategoryContract.CONTENT_TYPE_DIR;
            case CATEGORY_ITEM:
                return CategoryContract.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @NonNull ContentValues values) {
        final String table;
        final ColumnMap columnMap;
        final int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case MAIN_DIR:
                table = MainContract.TABLE_NAME;
                columnMap = MainContract.COL_MAP;
                break;
            case CATEGORY_DIR:
                table = CategoryContract.TABLE_NAME;
                columnMap = CategoryContract.COL_MAP;
                break;

            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        final long row = mDbHelper.getWritableDatabase().insert(table, null, columnMap.translateCols(values));
        if (row > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null, false);
                getContext().getContentResolver().notifyChange(CategoryContract.CONTENT_URI_JOINED, null, false);
            }
            return ContentUris.withAppendedId(uri, row);
        } else {
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri,
                      String selection,
                      String[] selectionArgs) {
        final String table;
        final int uriType = sUriMatcher.match(uri);
        final boolean isItemUriType;

        switch (uriType) {
            case MAIN_DIR:
                table = MainContract.TABLE_NAME;
                isItemUriType = false;
                break;
            case MAIN_ITEM:
                table = MainContract.TABLE_NAME;
                isItemUriType = true;
                break;
            case CATEGORY_DIR:
                table = CategoryContract.TABLE_NAME;
                isItemUriType = false;
                break;
            case CATEGORY_ITEM:
                table = CategoryContract.TABLE_NAME;
                isItemUriType = true;
                break;

            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        if (isItemUriType) {
            selection = setRowSelection(table, uri, selection);
        }

        int deleted = mDbHelper.getWritableDatabase().delete(table, selection, selectionArgs);

        if (deleted > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
            getContext().getContentResolver().notifyChange(CategoryContract.CONTENT_URI_JOINED, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @NonNull ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        final String table;
        final ColumnMap columnMap;
        final int uriType = sUriMatcher.match(uri);
        final boolean isItemUriType;

        switch (uriType) {
            case MAIN_DIR:
            case MAIN_ITEM:
                table = MainContract.TABLE_NAME;
                columnMap = MainContract.COL_MAP;

                if(uriType == MAIN_ITEM){
                    isItemUriType = true;
                } else {
                    isItemUriType = false;
                }
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                table = CategoryContract.TABLE_NAME;
                columnMap = CategoryContract.COL_MAP;

                if(uriType == MAIN_ITEM){
                    isItemUriType = true;
                } else {
                    isItemUriType = false;
                }
                break;

            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        if(isItemUriType){
            selection = setRowSelection(table, uri, selection);
        }

        final int updated = mDbHelper.getWritableDatabase().update(table, columnMap.translateCols(values), selection,
                selectionArgs);

        if (updated > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null, false);
            getContext().getContentResolver().notifyChange(CategoryContract.CONTENT_URI_JOINED, null, false);
        }

        return updated;
    }

    private static String setRowSelection(String table,
                                          Uri uri,
                                          String selection) {
        return DatabaseUtils.concatenateWhere(String.format(SELECTION_BY_ID_PATTERN, table, ContentUris.parseId(uri)),
                selection);
    }

}
