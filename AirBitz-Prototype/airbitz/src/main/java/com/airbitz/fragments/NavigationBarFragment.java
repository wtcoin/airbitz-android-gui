package com.airbitz.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbitz.R;
import com.airbitz.activities.BusinessDirectoryActivity;
import com.airbitz.activities.DirectoryDetailActivity;
import com.airbitz.activities.DisplayActivity;
import com.airbitz.activities.ExportActivity;
import com.airbitz.activities.ExportSavingOptionActivity;
import com.airbitz.activities.ImportActivity;
import com.airbitz.activities.MapBusinessDirectoryActivity;
import com.airbitz.activities.NavigationActivity;
import com.airbitz.activities.OfflineWalletActivity;
import com.airbitz.activities.ReceivedSuccessActivity;
import com.airbitz.activities.RequestActivity;
import com.airbitz.activities.SecurityActivity;
import com.airbitz.activities.SendActivity;
import com.airbitz.activities.SendConfirmationActivity;
import com.airbitz.activities.SettingActivity;
import com.airbitz.activities.TransactionActivity;
import com.airbitz.activities.TransactionDetailActivity;
import com.airbitz.activities.WalletActivity;
import com.airbitz.activities.WalletPasswordActivity;

/**
 * Created by Thomas Baker on 4/22/14.
 */
public class NavigationBarFragment extends Fragment {

    private ImageView mDirectoryButton;
    private ImageView mSendButton;
    private ImageView mRequestButton;
    private ImageView mWalletButton;
    private ImageView mSettingButton;
    private ImageView mPopupDirectoryButton;
    private ImageView mPopupSendButton;
    private ImageView mPopupRequestButton;
    private ImageView mPopupWalletButton;
    private ImageView mPopupSettingButton;
    private static ImageView mCurrentActivityButton;
    private View mView, mButtons;
    private int selectedTab = 0;

    //Callbacks for containing Activity to implement
    public interface OnScreenSelectedListener {
        public void onScreenSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        mButtons = (View) mView.findViewById(R.id.normal_navigation_bar);

        mDirectoryButton = (ImageView) mView.findViewById(R.id.directory_button);
        mSendButton = (ImageView) mView.findViewById(R.id.send_button);
        mRequestButton = (ImageView) mView.findViewById(R.id.request_button);
        mWalletButton = (ImageView) mView.findViewById(R.id.wallet_button);
        mSettingButton = (ImageView) mView.findViewById(R.id.setting_button);
        mPopupDirectoryButton = (ImageView) mView.findViewById(R.id.popup_directory_button);
        mPopupSendButton = (ImageView) mView.findViewById(R.id.popup_send_button);
        mPopupRequestButton = (ImageView) mView.findViewById(R.id.popup_request_button);
        mPopupWalletButton = (ImageView) mView.findViewById(R.id.popup_wallet_button);
        mPopupSettingButton = (ImageView) mView.findViewById(R.id.popup_setting_button);

        mButtons.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        displayPopup(event);
                        checkTabs(event);
                        return true;
                    case MotionEvent.ACTION_UP:
                        clearPopups();
                        return true;
                    default:
                        return false;
                }
            }
        });

        return mView;
    }

    private int mLastTab = 0;
    private void checkTabs(MotionEvent ev) {
        selectedTab = getTabNum(ev);
        if(selectedTab==-1)
            return;

        if(mLastTab!=selectedTab) {
            selectTab(selectedTab);
            unselectTab(mLastTab);
            mLastTab = selectedTab;
            ((NavigationActivity)this.getActivity()).onScreenSelected(selectedTab);
        }
    }

    private int getTabNum(MotionEvent ev) {
        if(isOverView(ev.getRawX(), ev.getRawY(), mDirectoryButton)) {
            return 0;
        } else if(isOverView(ev.getRawX(), ev.getRawY(), mRequestButton)) {
            return 1;
        } else if(isOverView(ev.getRawX(), ev.getRawY(), mSendButton)) {
            return 2;
        } else if(isOverView(ev.getRawX(), ev.getRawY(), mWalletButton)) {
            return 3;
        } else if(isOverView(ev.getRawX(), ev.getRawY(), mSettingButton)) {
            return 4;
        }
        else return -1;
    }


    public void selectTab(int position) {
        switch (position) {
            case 0:
                mDirectoryButton.setImageResource(R.drawable.ico_tab_directory_white);
                mDirectoryButton.setBackgroundResource(R.drawable.bg_selected_tab);
                break;
            case 1:
                mRequestButton.setImageResource(R.drawable.ico_tab_request_white);
                mRequestButton.setBackgroundResource(R.drawable.bg_selected_tab);
                break;
            case 2:
                mSendButton.setImageResource(R.drawable.ico_tab_send_white);
                mSendButton.setBackgroundResource(R.drawable.bg_selected_tab);
                break;
            case 3:
                mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
                mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
                break;
            case 4:
                mSettingButton.setImageResource(R.drawable.ico_tab_settings_white);
                mSettingButton.setBackgroundResource(R.drawable.bg_selected_tab);
                break;
            default:
                break;
        }
    }

    private void unselectTab(int position) {
        switch (position) {
            case 0:
                mDirectoryButton.setImageResource(R.drawable.ico_tab_directory);
                mDirectoryButton.setBackgroundResource(R.drawable.bg_tab_bar);
                break;
            case 1:
                mRequestButton.setImageResource(R.drawable.ico_tab_request);
                mRequestButton.setBackgroundResource(R.drawable.bg_tab_bar);
                break;
            case 2:
                mSendButton.setImageResource(R.drawable.ico_tab_send);
                mSendButton.setBackgroundResource(R.drawable.bg_tab_bar);
                break;
            case 3:
                mWalletButton.setImageResource(R.drawable.ico_tab_wallet);
                mWalletButton.setBackgroundResource(R.drawable.bg_tab_bar);
                break;
            case 4:
                mSettingButton.setImageResource(R.drawable.ico_tab_settings);
                mSettingButton.setBackgroundResource(R.drawable.bg_tab_bar);
                break;
            default:
                break;
        }
    }

    private void clearPopups() {
        mPopupDirectoryButton.setVisibility(View.INVISIBLE);
        mPopupRequestButton.setVisibility(View.INVISIBLE);
        mPopupSendButton.setVisibility(View.INVISIBLE);
        mPopupWalletButton.setVisibility(View.INVISIBLE);
        mPopupSettingButton.setVisibility(View.INVISIBLE);
    }

    private void displayPopup(MotionEvent ev) {
        clearPopups();
        if(isOverView(ev.getRawX(), ev.getRawY(), mDirectoryButton)) {
            mPopupDirectoryButton.setVisibility(View.VISIBLE);
        }
        if(isOverView(ev.getRawX(), ev.getRawY(), mRequestButton)) {
            mPopupRequestButton.setVisibility(View.VISIBLE);
        }
        if(isOverView(ev.getRawX(), ev.getRawY(), mSendButton)) {
            mPopupSendButton.setVisibility(View.VISIBLE);
        }
        if(isOverView(ev.getRawX(), ev.getRawY(), mWalletButton)) {
            mPopupWalletButton.setVisibility(View.VISIBLE);
        }
        if(isOverView(ev.getRawX(), ev.getRawY(), mSettingButton)) {
            mPopupSettingButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Determines if given points are inside view
     * @param x - x coordinate of point
     * @param y - y coordinate of point
     * @param view - view object to compare
     * @return true if the points are within view bounds, false otherwise
     */
    private boolean isOverView(float x, float y, View view){
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if(( x > viewX && x < (viewX + view.getWidth())) &&
                ( y > viewY && y < (viewY + view.getHeight()))){
            return true;
        } else {
            return false;
        }
    }

//    public void setActivityRelatedButtonToPressedState() {
//
//        if (getActivity().getClass().toString().equalsIgnoreCase(BusinessDirectoryActivity.class.toString())) {
//            mDirectoryButton.setImageResource(R.drawable.ico_tab_directory_white);
//            mDirectoryButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mDirectoryButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(MapBusinessDirectoryActivity.class.toString())) {
//            mDirectoryButton.setImageResource(R.drawable.ico_tab_directory_white);
//            mDirectoryButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mDirectoryButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(DirectoryDetailActivity.class.toString())) {
//            mDirectoryButton.setImageResource(R.drawable.ico_tab_directory_white);
//            mDirectoryButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mDirectoryButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(RequestActivity.class.toString())) {
//
//            mRequestButton.setImageResource(R.drawable.ico_tab_request_white);
//            mRequestButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mRequestButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(ImportActivity.class.toString())) {
//
//            mRequestButton.setImageResource(R.drawable.ico_tab_request_white);
//            mRequestButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mRequestButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(WalletPasswordActivity.class.toString())) {
//
//            mRequestButton.setImageResource(R.drawable.ico_tab_request_white);
//            mRequestButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mRequestButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(SendActivity.class.toString())) {
//
//            mSendButton.setImageResource(R.drawable.ico_tab_send_white);
//            mSendButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mSendButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(SendConfirmationActivity.class.toString())) {
//
//            mSendButton.setImageResource(R.drawable.ico_tab_send_white);
//            mSendButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mSendButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(ReceivedSuccessActivity.class.toString())) {
//
//            mSendButton.setImageResource(R.drawable.ico_tab_send_white);
//            mSendButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mSendButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(WalletActivity.class.toString())) {
//
//            mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
//            mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mWalletButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(SettingActivity.class.toString())) {
//
//            mSettingButton.setImageResource(R.drawable.ico_tab_settings_white);
//            mSettingButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mSettingButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(DisplayActivity.class.toString())) {
//
//            mSettingButton.setImageResource(R.drawable.ico_tab_settings_white);
//            mSettingButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mSettingButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(SecurityActivity.class.toString())) {
//
//            mSettingButton.setImageResource(R.drawable.ico_tab_settings_white);
//            mSettingButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mSettingButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(TransactionActivity.class.toString())) {
//            mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
//            mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mWalletButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(TransactionDetailActivity.class.toString())) {
//            mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
//            mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mWalletButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(ExportActivity.class.toString())) {
//            mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
//            mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mWalletButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(ExportSavingOptionActivity.class.toString())) {
//            mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
//            mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mWalletButton;
//        } else if (getActivity().getClass().toString().equalsIgnoreCase(OfflineWalletActivity.class.toString())) {
//            mWalletButton.setImageResource(R.drawable.ico_tab_wallet_white);
//            mWalletButton.setBackgroundResource(R.drawable.bg_selected_tab);
//            mCurrentActivityButton = mWalletButton;
//        }
//    }

    public void initializeElements() {

        mDirectoryButton.setImageResource(R.drawable.ico_tab_directory);
        mDirectoryButton.setBackgroundResource(R.drawable.bg_tab_bar);
        mDirectoryButton.invalidate();
        mSendButton.setImageResource(R.drawable.ico_tab_send);
        mSendButton.setBackgroundResource(R.drawable.bg_tab_bar);
        mSendButton.invalidate();
        mRequestButton.setImageResource(R.drawable.ico_tab_request);
        mRequestButton.setBackgroundResource(R.drawable.bg_tab_bar);
        mRequestButton.invalidate();
        mWalletButton.setImageResource(R.drawable.ico_tab_wallet);
        mWalletButton.setBackgroundResource(R.drawable.bg_tab_bar);
        mWalletButton.invalidate();
        mSettingButton.setImageResource(R.drawable.ico_tab_settings);
        mSettingButton.setBackgroundResource(R.drawable.bg_tab_bar);
        mSettingButton.invalidate();

        clearPopups();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeElements();
        selectTab(selectedTab);
    }
}