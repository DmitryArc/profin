package com.dka.profin.fragment;

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
import com.dka.profin.R;
import com.dka.profin.common.CursorRecyclerViewAdapter;
import com.dka.profin.data.contract.CategoryContract;

/**
 * @author Dmitry.Kalyuzhnyi 14.12.2015.
 */
public class RootFragment extends ContentFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id,
                                         Bundle args) {
        return new CursorLoader(getContext(), CategoryContract.CONTENT_URI, null, null, null, null);
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

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {
        final TextView mName;
        final TextView mProportion;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.tv_cat_name);
            mProportion = (TextView) itemView.findViewById(R.id.tv_cat_proportion);
        }
    }

    private class CategoryAdapter extends CursorRecyclerViewAdapter<CategoryViewHolder> {
        public CategoryAdapter(Context context) {
            super(context, null);
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder viewHolder, Cursor cursor) {
            viewHolder.mName.setText(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.NAME)));
            viewHolder.mProportion.setText(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.PROPORTION)));
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_category, parent, false));
        }
    }
}
