package com.airbitz.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbitz.R;
import com.airbitz.activities.NavigationActivity;
import com.airbitz.models.FragmentSourceEnum;

/**
 * Created by matt on 6/18/14.
 */
public class SuccessFragment extends Fragment {

    private ImageButton mBackButton;
    private ImageButton mHelpButton;

    private Bundle bundle;

    private TextView mFiatTextView;
    private TextView mBitcoinTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
    }

    private View mView;
    @Override public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parentViewGroup = (ViewGroup) mView.getParent();
        if( null != parentViewGroup ) {
            parentViewGroup.removeView( mView );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView!=null)
            return mView;
        mView = inflater.inflate(R.layout.fragment_success, container, false);

        mBackButton = (ImageButton) mView.findViewById(R.id.button_back);
        mHelpButton = (ImageButton) mView.findViewById(R.id.button_help);
        mFiatTextView = (TextView) mView.findViewById(R.id.textview_fiat);
        mBitcoinTextView = (TextView) mView.findViewById(R.id.textview_bitcoin);

        mBitcoinTextView.setText(bundle.getString("bitcoin_value"));
        mFiatTextView.setText(bundle.getString("fiat_value"));

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return mView;
    }

    class ArtificialDelay extends AsyncTask<Void, Integer, Boolean> {
        public ArtificialDelay(){

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //Fragment frag = new TransactionDetailFragment();
            ((NavigationActivity) getActivity()).switchToWallets(FragmentSourceEnum.REQUEST, bundle);
            //((NavigationActivity) getActivity()).pushFragment(frag);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        new ArtificialDelay().execute();
    }

}