package com.dka.mainmenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dka.mainmenu.drawable.ActivatedStateDrawable;
import com.dka.mainmenu.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Dmitry.Kalyuzhnyi 08.12.2015.
 */
public class DrawerFragment extends Fragment implements AdapterView.OnItemClickListener, OnBackPressListener, DrawerLayout.DrawerListener {

    private static final String ITEM_ARRAY = "ITEM_ARRAY";

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private MainMenuListener mMainMenuListener;
    private int mCurrentSelectedPosition;
    private int mPendingPositon;
    private DrawerLayout.DrawerListener mDrawerListenerDelegate;
    private ArrayList<MainMenuItem> mItemsArray;
    private MainMenuAdapter mAdapter;
    private TextView mHeaderTitle;
    private TextView mHeaderSubtitle;

    public static DrawerFragment newInstance(MainMenuItem[] itemArray) {
        final DrawerFragment fragment = new DrawerFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ITEM_ARRAY, new ArrayList<>(Arrays.asList(itemArray)));
        fragment.setArguments(args);
        return fragment;
    }

    public static void selectMenuItem(FragmentManager fm,
                                      int menuItemId) {
        final DrawerFragment drawerFragment = (DrawerFragment) fm.findFragmentById(R.id.mainMenuFragment);
        drawerFragment.mCurrentSelectedPosition = getMenuPosition(menuItemId, drawerFragment.mItemsArray);
        drawerFragment.mPendingPositon = drawerFragment.mCurrentSelectedPosition;
        drawerFragment.checkCurrentListItem();
    }

    public static void setUp(FragmentManager fm,
                             DrawerLayout drawerLayout,
                             int initMenuItemId,
                             MainMenuItem[] mainMenuItems,
                             DrawerLayout.DrawerListener listenerDelegate) {
        DrawerFragment drawerFragment = (DrawerFragment) fm.findFragmentById(R.id.mainMenuFragment);
        if (drawerFragment == null) {
            drawerFragment = DrawerFragment.newInstance(mainMenuItems);
            fm.beginTransaction().add(R.id.mainMenuFragment, drawerFragment).commit();
        }
        drawerFragment.setUp(drawerLayout, initMenuItemId, listenerDelegate, mainMenuItems);
    }

    private void setUp(DrawerLayout drawerLayout,
                       int initMenuItemId,
                       DrawerLayout.DrawerListener listenerDelegate,
                       MainMenuItem[] mainMenuItems) {
        mCurrentSelectedPosition = getMenuPosition(initMenuItemId, new ArrayList<>(Arrays.asList(mainMenuItems)));
        mPendingPositon = mCurrentSelectedPosition;
        checkCurrentListItem();
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.shadow_left, GravityCompat.START);
        mDrawerListenerDelegate = listenerDelegate;
        drawerLayout.setDrawerListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mItemsArray = getArguments().getParcelableArrayList(ITEM_ARRAY);
        if (!MainMenuListener.class.isInstance(context)) {
            throw new ClassCastException("Activity must implement OnMainMenuItemClickListener.");
        }
        mMainMenuListener = (MainMenuListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fr_main_menu, container, false);
        mListView.setOnItemClickListener(this);
        final View header = LayoutInflater.from(getActivity()).inflate(R.layout.view_menu_header, mListView, false);
        mHeaderTitle = (TextView) header.findViewById(R.id.tv_header_title);
        mHeaderSubtitle = (TextView) header.findViewById(R.id.tv_header_subtitle);
        if (mMainMenuListener != null) {
            header.findViewById(R.id.header_bg).setBackgroundResource(mMainMenuListener.getHeaderBackgroundResource());
        }
        mListView.addHeaderView(header, null, false);
        mAdapter = new MainMenuAdapter();
        mListView.setAdapter(mAdapter);
        checkCurrentListItem();
        return mListView;
    }

    private void checkCurrentListItem() {
        if (mListView != null) {
            mListView.setItemChecked(mCurrentSelectedPosition + mListView.getHeaderViewsCount(), true);
        }
    }

    private void selectItem(int position) {
        if (mCurrentSelectedPosition != position) {
            mPendingPositon = position;
        }
        if (!mItemsArray.get(position).disabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void handleItemClick(final MainMenuItem menuItem){
        mListView.setItemChecked(mCurrentSelectedPosition + mListView.getHeaderViewsCount(), true);
        if (mMainMenuListener != null) {
            mMainMenuListener.onMainMenuItemClick(menuItem.titleResId, null);
        }
        if (!menuItem.disabled) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainMenuListener = null;
    }

    private static int getMenuPosition(int menuItemid,
                                       ArrayList<MainMenuItem> mainMenuItems) {
        for (int i = 0; i < mainMenuItems.size(); i++) {
            if (mainMenuItems.get(i).titleResId == menuItemid) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {
        final int originalPosition = position - mListView.getHeaderViewsCount();
        final MainMenuItem menuItem = mItemsArray.get(originalPosition);
        if (menuItem.selectable) {
            selectItem(originalPosition);
        } else {
            handleItemClick(menuItem);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public void onDrawerSlide(View drawerView,
                              float slideOffset) {
        mDrawerListenerDelegate.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerListenerDelegate.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerListenerDelegate.onDrawerClosed(drawerView);
        if (mCurrentSelectedPosition != mPendingPositon) {
            mCurrentSelectedPosition = mPendingPositon;
            if (mMainMenuListener != null) {
                mMainMenuListener.onMainMenuItemSelect(mItemsArray.get(mCurrentSelectedPosition).titleResId, null);
            }
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        mDrawerListenerDelegate.onDrawerStateChanged(newState);
    }

    public void setCounter(@StringRes int menuItemId,
                           int counter) {
        mItemsArray.get(getMenuPosition(menuItemId, mItemsArray)).counter = counter;
        mAdapter.notifyDataSetChanged();
    }

    public void setHeaderTitle(String title) {
        this.mHeaderTitle.setText(title);
    }

    public void setHeaderSubtitle(String subtitle) {
        this.mHeaderSubtitle.setText(subtitle);
    }

    public interface MainMenuListener {
        void onMainMenuItemSelect(@StringRes int menuItemTitleResId,
                                  Bundle args);

        void onMainMenuItemClick(@StringRes int menuItemTitleResId,
                                 Bundle args);

        int getHeaderBackgroundResource();
    }

    public static class MainMenuItem implements Parcelable {

        public static final Creator<MainMenuItem> CREATOR = new Creator<MainMenuItem>() {
            @Override
            public MainMenuItem createFromParcel(Parcel source) {
                return new MainMenuItem(source);
            }

            @Override
            public MainMenuItem[] newArray(int size) {
                return new MainMenuItem[size];
            }
        };

        private final int titleResId;
        private final int iconResId;
        private final boolean selectable;
        private boolean disabled;
        private int counter;

        public MainMenuItem(@StringRes int titleResId,
                            @DrawableRes int iconResId,
                            boolean selectable) {
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.selectable = selectable;
            this.disabled = false;
        }

        public MainMenuItem(@StringRes int titleResId,
                            @DrawableRes int iconResId) {
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.selectable = true;
            this.disabled = false;
        }

        private MainMenuItem(Parcel source) {
            titleResId = source.readInt();
            iconResId = source.readInt();
            selectable = source.readByte() == 1;
            disabled = source.readByte() == 1;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest,
                                  int flags) {
            dest.writeInt(titleResId);
            dest.writeInt(iconResId);
            dest.writeByte((byte) (selectable ? 1 : 0));
            dest.writeByte((byte) (disabled ? 1 : 0));
        }

        public MainMenuItem setDisabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }
    }

    static class ViewHolder {
        TextView title;
        TextView counter;
    }

    private class MainMenuAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;

        private MainMenuAdapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return mItemsArray.size();
        }

        @Override
        public MainMenuItem getItem(int position) {
            return mItemsArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mItemsArray.get(position).titleResId;
        }

        @Override
        public View getView(int position,
                            View convertView,
                            ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.view_menu_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.mainMenuTitle);
                viewHolder.counter = (TextView) convertView.findViewById(R.id.mainMenuCounter);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final MainMenuItem menuItem = mItemsArray.get(position);

            viewHolder.title.setEnabled(!menuItem.disabled);
            viewHolder.title.setText(menuItem.titleResId);
            final Drawable iconDrawable = ContextCompat.getDrawable(getActivity(), menuItem.iconResId);
            viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(
                    new ActivatedStateDrawable(iconDrawable,
                            !menuItem.disabled ? Color.GRAY : Color.LTGRAY,
                            ColorUtils.getThemeAttrColor(getActivity(), R.attr.colorAccent)),
                    null, null, null);

            if (menuItem.counter > 0) {
                viewHolder.counter.setText(String.valueOf(menuItem.counter));
            } else {
                viewHolder.counter.setText("");
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}