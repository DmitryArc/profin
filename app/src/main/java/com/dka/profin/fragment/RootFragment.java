package com.dka.profin.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dka.mainmenu.ContentFragment;
import com.dka.profin.CategoryItemClickListener;
import com.dka.profin.R;
import com.dka.profin.common.CursorRecyclerViewAdapter;
import com.dka.profin.data.contract.CategoryContract;
import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 14.12.2015.
 */
public class RootFragment extends ContentFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        CategoryItemClickListener {
    private final static int ROOT_FRAGMENT_LOADER = 0;

    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private QueryHandler mQueryhandler;

    public RootFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_root, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_main);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CategoryAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ROOT_FRAGMENT_LOADER, null, this);
        if(mQueryhandler == null){
            mQueryhandler = new QueryHandler(getContext().getContentResolver());
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id,
                                         Bundle args) {
        return new CursorLoader(getContext(), CategoryContract.CONTENT_URI_JOINED, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && mAdapter.getCursor() != data) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CategoryItemClickListener mCategoryItemClickListener;

        final TextView mName;
        final TextView mProportion;
        final TextView mSum;

        public CategoryViewHolder(View itemView,
                                  CategoryItemClickListener listener) {
            super(itemView);

            mCategoryItemClickListener = listener;
            itemView.setOnClickListener(this);

            mName = (TextView) itemView.findViewById(R.id.tv_cat_name);
            mProportion = (TextView) itemView.findViewById(R.id.tv_cat_proportion);
            mSum = (TextView) itemView.findViewById(R.id.tv_cat_sum);
        }

        @Override
        public void onClick(View v) {
            if (mCategoryItemClickListener != null) {
                mCategoryItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    /**
     * Implementation for {@link CategoryItemClickListener}
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        final Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        int id = cursor.getInt(cursor.getColumnIndex(CategoryContract.Columns._ID));
        if (mQueryhandler != null) {
            final ContentValues cv = new ContentValues();
            cv.put(MainContract.Columns.CATEGORY_ID, id);
            cv.put(MainContract.Columns.SUM, 10);
            cv.put(MainContract.Columns.DATE_TIME, System.currentTimeMillis());
            mQueryhandler.startInsert(0, null, MainContract.CONTENT_URI, cv);
        }
    }

    private class CategoryAdapter extends CursorRecyclerViewAdapter<CategoryViewHolder> {
        public CategoryAdapter(Context context) {
            super(context, null, true);
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder viewHolder, Cursor cursor) {
            viewHolder.mName.setText(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.NAME)));
            viewHolder.mProportion.setText(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.PROPORTION)));
            viewHolder.mSum.setText(cursor.getString(cursor.getColumnIndex(MainContract.Columns.SUM)));

        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_category, parent, false),
                    RootFragment.this);
        }
    }

    public class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

}
