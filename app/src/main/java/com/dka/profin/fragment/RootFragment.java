package com.dka.profin.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

    public RootFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
    }


    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id,
                                         Bundle args) {
        return new CursorLoader(getContext(), CategoryContract.CONTENT_URI_JOINED, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, @Nullable Cursor data) {
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

        @NonNull
        final TextView mName;
        @NonNull
        final TextView mProportion;
        @NonNull
        final TextView mSum;

        public CategoryViewHolder(@NonNull View itemView,
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
        final int id = cursor.getInt(cursor.getColumnIndex(CategoryContract.Columns._ID));
        final String name = cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.NAME));

        DialogFragment dialogFragment = new EnterDataFragment();
        final Bundle args = new Bundle();
        args.putInt(EnterDataFragment.EXTRA_CATEGORY_ID, id);
        args.putString(EnterDataFragment.EXTRA_CATEGORY_NAME, name);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), EnterDataFragment.TAG);
    }

    private class CategoryAdapter extends CursorRecyclerViewAdapter<CategoryViewHolder> {
        public CategoryAdapter(Context context) {
            super(context, null, true);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder viewHolder, @NonNull Cursor cursor) {
            viewHolder.mName.setText(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.NAME)));
            viewHolder.mProportion.setText(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.PROPORTION)));
            viewHolder.mSum.setText(cursor.getString(cursor.getColumnIndex(MainContract.Columns.SUM)));

        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_category, parent, false),
                    RootFragment.this);
        }
    }

}
