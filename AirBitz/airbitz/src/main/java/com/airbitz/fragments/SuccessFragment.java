/**
 * Copyright (c) 2014, Airbitz Inc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms are permitted provided that 
 * the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Redistribution or use of modified source code requires the express written
 *    permission of Airbitz Inc.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the Airbitz Project.
 */

package com.airbitz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbitz.R;
import com.airbitz.activities.NavigationActivity;
/**
 * Created on 2/22/14.
 */
public class SuccessFragment extends BaseFragment {
    public static final String TYPE_SEND = "com.airbitz.fragments.receivedsuccess.send";
    public static final String TYPE_REQUEST = "com.airbitz.fragments.receivedsuccess.request";
    private final String TAG = getClass().getSimpleName();
    Runnable mSendAnimationRunner = new Runnable() {
        int count = 0;

        @Override
        public void run() {
            count++;
            if (count % 3 == 1) {
                mLogoImageView.setImageResource(R.drawable.ico_sending_1);
            } else if (count % 3 == 2) {
                mLogoImageView.setImageResource(R.drawable.ico_sending_2);
            } else if (count % 3 == 0) {
                mLogoImageView.setImageResource(R.drawable.ico_sending_3);
            }
            mHandler.postDelayed(this, 500);
        }
    };
    private TextView mSendingTextView;
    private TextView mTitleTextView;
    private ImageButton mBackButton;
    private ImageView mLogoImageView;
    private Bundle mBundle;
    Runnable mRequestAnimationRunner = new Runnable() {
        int count = 0;

        @Override
        public void run() {
            count++;
            if (count <= 10) {
                if (count % 3 == 1) {
                    mLogoImageView.setImageResource(R.drawable.ico_sending_3);
                } else if (count % 3 == 2) {
                    mLogoImageView.setImageResource(R.drawable.ico_sending_2);
                } else if (count % 3 == 0) {
                    mLogoImageView.setImageResource(R.drawable.ico_sending_1);
                }
                mHandler.postDelayed(this, 500);
            } else {
                mActivity.switchToWallets(mBundle);
                mActivity.resetFragmentThreadToBaseFragment(NavigationActivity.Tabs.REQUEST.ordinal());
                mActivity.showNavBar();
            }
        }
    };
    private Handler mHandler = new Handler();
    private View mView;
    private NavigationActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = this.getArguments();
        if (mBundle == null) {
            Log.d(TAG, "Bundle is null");
        }
        mActivity = (NavigationActivity) getActivity();
        mActivity.hideNavBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_received_success, container, false);
        } else {

            return mView;
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mSendingTextView = (TextView) mView.findViewById(R.id.textview_sending);
        mTitleTextView = (TextView) mView.findViewById(R.id.fragment_category_textview_title);

        mLogoImageView = (ImageView) mView.findViewById(R.id.imageview_logo);

        mBackButton = (ImageButton) mView.findViewById(R.id.layout_airbitz_header_button_back);

        mSendingTextView.setTypeface(NavigationActivity.montserratBoldTypeFace);
        mTitleTextView.setTypeface(NavigationActivity.montserratBoldTypeFace);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        String fromSource = mBundle.getString(WalletsFragment.FROM_SOURCE);
        if (fromSource.contains(TYPE_REQUEST)) {
            mTitleTextView.setText(getString(R.string.request_title));
            mSendingTextView.setText(getString(R.string.received_success_receiving));
            mHandler.post(mRequestAnimationRunner);
        } else if (fromSource.contains(TYPE_SEND)) {
            mTitleTextView.setText(getString(R.string.received_success_sending_title));
            mSendingTextView.setText(getString(R.string.received_success_sending));
            mHandler.post(mSendAnimationRunner);
        }

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mSendAnimationRunner);
        mHandler.removeCallbacks(mRequestAnimationRunner);
    }

}
