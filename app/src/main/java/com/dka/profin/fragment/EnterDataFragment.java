package com.dka.profin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dka.profin.R;
import com.dka.profin.data.contract.MainContract;

/**
 * @author Dmitry.Kalyuzhnyi 22.12.2015.
 */
public abstract class EnterDataFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = EnterDataFragment.class.getSimpleName();
    public static final String EXTRA_CATEGORY_ID = "categoryId";
    public static final String EXTRA_CATEGORY_NAME = "categoryName";

    protected int mCateroryId;
    protected String mCategoryName;

    protected TextView vCategoryName;
    protected TextInputLayout vSumContainer;
    protected EditText vSum;

    protected QueryHandler mQueryhandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueryhandler = new QueryHandler(getContext().getContentResolver());

        if (getArguments() != null) {
            mCateroryId = getArguments().getInt(EXTRA_CATEGORY_ID);
            mCategoryName = getArguments().getString(EXTRA_CATEGORY_NAME);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.enter_data);

        final View content = LayoutInflater.from(getActivity()).inflate(R.layout.fr_enter_data, null, false);
        vCategoryName = (TextView) content.findViewById(R.id.tv_ent_category);
        vCategoryName.setText(mCategoryName);
        vSumContainer = (TextInputLayout) content.findViewById(R.id.l_ent_sumcontainer);
        vSum = (EditText) content.findViewById(R.id.et_ent_value);
        content.findViewById(R.id.btn_ent_save).setOnClickListener(this);
        content.findViewById(R.id.btn_ent_cancel).setOnClickListener(this);

        builder.setView(content);

        return builder.create();
    }

    /**
     * Implementation for {@link DialogInterface.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ent_save:
                if (addRecord()) {
                    dismiss();
                } else {
                    vSumContainer.setError(getString(R.string.sum_empty));
                }
                break;
            default:
                dismiss();
        }
    }

    protected abstract boolean addRecord();

    public class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

}
