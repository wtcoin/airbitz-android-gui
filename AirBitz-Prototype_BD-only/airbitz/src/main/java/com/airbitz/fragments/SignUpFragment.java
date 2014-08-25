package com.airbitz.fragments;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbitz.AirbitzApplication;
import com.airbitz.R;
import com.airbitz.activities.NavigationActivity;
import com.airbitz.api.CoreAPI;
import com.airbitz.api.SWIGTYPE_p_void;
import com.airbitz.api.core;
import com.airbitz.api.tABC_CC;
import com.airbitz.api.tABC_Error;
import com.airbitz.api.tABC_PasswordRule;
import com.airbitz.api.tABC_RequestResults;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created on 2/10/14.
 */
public class SignUpFragment extends Fragment implements NavigationActivity.OnBackPress {

    public static final int DOLLAR_CURRENCY_NUMBER = 840;
    public static final int MIN_PIN_LENGTH = 4;
    private RelativeLayout mParentLayout;

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordConfirmationEditText;
    private EditText mPasswordForPINEditText;
    private EditText mWithdrawalPinEditText;
    private TextView mWithdrawalLabel;
    private Button mNextButton;
    private boolean mGoodPassword = false;
    private int mMode = SIGNUP;

    private TextView mTitleTextView;

    private LinearLayout mPopupContainer;
    private ImageView mSwitchImage1;
    private ImageView mSwitchImage2;
    private ImageView mSwitchImage3;
    private ImageView mSwitchImage4;
    private ImageView mSwitchImage5;
    private TextView mQuestion1;
    private TextView mQuestion2;
    private TextView mQuestion3;
    private TextView mQuestion4;
    private TextView mQuestion5;
    private TextView mTimeTextView;

    private View mUserNameRedRingCover;
    private View mPinRedRingCover;
    private View mDummyFocus;


    private static final String specialChar = "~`!@#$%^&*()-_+=,.?/<>:;'][{}|\\\"";
    private static final String passwordPattern = ".*[" + Pattern.quote(specialChar) + "].*";

    private CreateAccountTask mAuthTask;

    private CreateFirstWalletTask mCreateFirstWalletTask;

    public static String KEY_USERNAME = "KEY_USERNAME";
    public static String MODE = "com.airbitz.signup.mode";
    public static int SIGNUP = 0;
    public static int CHANGE_PASSWORD=1;
    public static int CHANGE_PASSWORD_VIA_QUESTIONS = 2;
    public static int CHANGE_PIN = 3;

    private CoreAPI mCoreAPI;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCoreAPI = CoreAPI.getApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_signup, container, false);

        mParentLayout = (RelativeLayout) mView.findViewById(R.id.activity_signup_parent_layout);
        mNextButton = (Button) mView.findViewById(R.id.activity_signup_next_button);

        mUserNameRedRingCover = mView.findViewById(R.id.activity_signup_username_redring);
        mDummyFocus = mView.findViewById(R.id.activity_signup_dummy_focus);

        mUserNameEditText = (EditText) mView.findViewById(R.id.activity_signup_username_edittext);
        mPasswordEditText = (EditText) mView.findViewById(R.id.activity_signup_password_edittext);
        mPasswordConfirmationEditText = (EditText) mView.findViewById(R.id.activity_signup_repassword_edittext);
        mWithdrawalPinEditText = (EditText) mView.findViewById(R.id.activity_signup_withdrawal_edittext);
        mTitleTextView = (TextView) mView.findViewById(R.id.activity_signup_title_textview);
        TextView mHintTextView = (TextView) mView.findViewById(R.id.activity_signup_password_help);
        mWithdrawalLabel = (TextView) mView.findViewById(R.id.activity_signup_withdrawal_textview);

        mTitleTextView.setTypeface(NavigationActivity.montserratBoldTypeFace);
        mUserNameEditText.setTypeface(NavigationActivity.helveticaNeueTypeFace);
        mHintTextView.setTypeface(NavigationActivity.latoRegularTypeFace);
        mPasswordEditText.setTypeface(NavigationActivity.helveticaNeueTypeFace);
        mPasswordConfirmationEditText.setTypeface(NavigationActivity.helveticaNeueTypeFace);
        mWithdrawalLabel.setTypeface(NavigationActivity.montserratRegularTypeFace);
        mWithdrawalPinEditText.setTypeface(NavigationActivity.helveticaNeueTypeFace);

        ImageButton mBackButton = (ImageButton) mView.findViewById(R.id.activity_signup_back_button);

        mSwitchImage1 = (ImageView) mView.findViewById(R.id.activity_signup_switch_image_1);
        mSwitchImage2 = (ImageView) mView.findViewById(R.id.activity_signup_switch_image_2);
        mSwitchImage3 = (ImageView) mView.findViewById(R.id.activity_signup_switch_image_3);
        mSwitchImage4 = (ImageView) mView.findViewById(R.id.activity_signup_switch_image_4);
        mSwitchImage5 = (ImageView) mView.findViewById(R.id.activity_signup_switch_image_5);
        mQuestion1 = (TextView) mView.findViewById(R.id.activity_signup_switch_text_1);
        mQuestion2 = (TextView) mView.findViewById(R.id.activity_signup_switch_text_2);
        mQuestion3 = (TextView) mView.findViewById(R.id.activity_signup_switch_text_3);
        mQuestion4 = (TextView) mView.findViewById(R.id.activity_signup_switch_text_4);
        mQuestion5 = (TextView) mView.findViewById(R.id.activity_signup_switch_text_5);

        mTimeTextView = (TextView) mView.findViewById(R.id.activity_signup_time_textview);
        mTimeTextView.setTypeface(NavigationActivity.latoRegularTypeFace);

        mPopupContainer = (LinearLayout) mView.findViewById(R.id.activity_signup_popup_layout);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext();
            }
        });

        mWithdrawalPinEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        final TextWatcher mPINTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>=4) {
                    ((NavigationActivity)getActivity()).hideSoftKeyboard(mWithdrawalPinEditText);
                    mParentLayout.requestFocus();
                }
            }
        };
        mWithdrawalPinEditText.addTextChangedListener(mPINTextWatcher);

        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (mUserNameEditText.getText().toString().length() < 3 || mUserNameEditText.getText().toString().trim().length() < 3) {
                    mUserNameRedRingCover.setVisibility(View.VISIBLE);
                } else {
                    mUserNameRedRingCover.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String password = mPasswordEditText.getText().toString();
                mGoodPassword = checkPasswordRules(password);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    LayoutTransition lt = new LayoutTransition();
                    Animator animator = ObjectAnimator.ofFloat(null,"translationY",-(getResources().getDimension(R.dimen.activity_signup_popup_height)),0);
                    lt.setAnimator(LayoutTransition.APPEARING,animator);
                    lt.setStartDelay(LayoutTransition.APPEARING,0);
                    lt.setDuration(300);
                    mParentLayout.setLayoutTransition(lt);
                    mPopupContainer.setVisibility(View.VISIBLE);
                }else{
                    LayoutTransition lt = new LayoutTransition();
                    Animator animator = ObjectAnimator.ofFloat(null,"translationY",0,-(getResources().getDimension(R.dimen.activity_signup_popup_height)));
                    lt.setAnimator(LayoutTransition.DISAPPEARING,animator);
                    lt.setStartDelay(LayoutTransition.DISAPPEARING,0);
                    lt.setDuration(300);
                    mParentLayout.setLayoutTransition(lt);
                    mPopupContainer.setVisibility(View.GONE);
                }
            }
        });

        setupUI(getArguments());

        mUserNameEditText.requestFocus();
        
        return mView;
    }

    private void setupUI(Bundle bundle) {
        if(bundle==null)
            return;
        //Hide some elements if this is not a fresh signup
        mMode = bundle.getInt(MODE);
        if(mMode==CHANGE_PASSWORD) {
            // Reuse mUserNameEditText for old mPassword too
            mUserNameEditText.setHint(getResources().getString(R.string.activity_signup_old_password));
            mUserNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordEditText.setHint(getResources().getString(R.string.activity_signup_new_password));
            mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordConfirmationEditText.setHint(getResources().getString(R.string.activity_signup_new_password_confirm));
            mPasswordConfirmationEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mNextButton.setText(getResources().getString(R.string.string_done));
            // change title
            mTitleTextView.setText(R.string.activity_signup_title_change_password);
            // hide PIN
            mWithdrawalPinEditText.setVisibility(View.GONE);
            mWithdrawalLabel.setVisibility(View.GONE);
        } else if(mMode==CHANGE_PASSWORD_VIA_QUESTIONS) {
            // change mUsername label, title
            mTitleTextView.setText(R.string.activity_signup_title_change_password_via_questions);
            mPasswordEditText.setHint(getResources().getString(R.string.activity_signup_new_password));
            mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordConfirmationEditText.setHint(getResources().getString(R.string.activity_signup_new_password_confirm));
            mPasswordConfirmationEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mNextButton.setText(getResources().getString(R.string.string_done));
            // hide mUsername
            mUserNameEditText.setVisibility(View.GONE);
        } else if(mMode==CHANGE_PIN) {
            // hide both mPassword fields
            mPasswordForPINEditText = mUserNameEditText;
            mPasswordForPINEditText.setHint(getResources().getString(R.string.activity_signup_password_hint));
            mPasswordForPINEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordEditText.setVisibility(View.GONE);
            mPasswordConfirmationEditText.setVisibility(View.GONE);
            mWithdrawalPinEditText.setHint(getResources().getString(R.string.activity_signup_new_pin));
            mNextButton.setText(getResources().getString(R.string.string_done));
            // change title
            mTitleTextView.setText(R.string.activity_signup_title_change_pin);
        }
    }

    // checks the mPassword against the mPassword rules
    // returns YES if new mPassword fields are good, NO if the new mPassword fields failed the checks
    // if the new mPassword fields are bad, an appropriate message box is displayed
    // note: this function is aware of the 'mode' of the view controller and will check and display appropriately
    private boolean checkPasswordRules(String password) {
        List<tABC_PasswordRule> rules = mCoreAPI.GetPasswordRules(password);

        if(rules.isEmpty()) {
            return false;
        }

        boolean bNewPasswordFieldsAreValid = true;
        for (int i = 0; i < rules.size(); i++)
        {
            tABC_PasswordRule pRule = rules.get(i);
            boolean passed = pRule.getBPassed();
            String description = pRule.getSzDescription();
            if (!passed)
            {
                bNewPasswordFieldsAreValid = false;
            }
            //TODO variable length list of items instead of fixed # of items
            int resource = passed ? R.drawable.green_check : R.drawable.red_x;
            mQuestion1.setText(description);
            switch(i) {
                case 0:
                    mSwitchImage1.setImageResource(resource);
                    break;
                case 1:
                    mSwitchImage2.setImageResource(resource);
                    break;
                case 2:
                    mSwitchImage3.setImageResource(resource);
                    break;
                case 3:
                    mSwitchImage4.setImageResource(resource);
                    break;
                case 4:
                    mSwitchImage5.setImageResource(resource);
                    break;
                default:
                    break;
            }
        }
        mTimeTextView.setText(GetCrackString(mCoreAPI.GetPasswordSecondsToCrack(password)));
        return bNewPasswordFieldsAreValid;
    }

    private void goNext() {
        // if they entered a valid mUsername or old mPassword
        if (userNameFieldIsValid() && newPasswordFieldsAreValid() && pinFieldIsValid()) {
            // if we are signing up a new account
            if (mMode == SIGNUP) {
                attemptSignUp();
            } else {
                mChangeTask = new ChangeTask();
                mChangeTask.execute((Void) null);
            }
        }
    }

    // checks the mUsername field (non-blank or matches old mPassword depending on the mode)
    // returns YES if field is good
    // if the field is bad, an appropriate message box is displayed
    // note: this function is aware of the 'mode' of the view controller and will check and display appropriately
    private boolean userNameFieldIsValid()
    {
        boolean bUserNameFieldIsValid = true;

        // if we are signing up for a new account
        if (mMode == SIGNUP)
        {
            // if nothing was entered
            if (mUserNameEditText.getText().toString().length() == 0)
            {
                bUserNameFieldIsValid = false;
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), getResources().getString(R.string.activity_signup_enter_username));
            }
        }
        else if (mMode != CHANGE_PASSWORD_VIA_QUESTIONS) // the user name field is used for the old mPassword in this case
        {
            // if the mPassword is wrong
            if (!AirbitzApplication.getPassword().equals(mUserNameEditText.getText().toString()))
            {
                bUserNameFieldIsValid = false;
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), getResources().getString(R.string.activity_signup_incorrect_password));
            }
        }
        return bUserNameFieldIsValid;
    }

    // checks the mPassword against the mPassword rules
    // returns YES if new mPassword fields are good, NO if the new mPassword fields failed the checks
    // if the new mPassword fields are bad, an appropriate message box is displayed
    // note: this function is aware of the 'mode' of the view controller and will check and display appropriately
    private boolean newPasswordFieldsAreValid()
    {
        boolean bNewPasswordFieldsAreValid = true;

        // if we are signing up for a new account or changing our mPassword
        if ((mMode!=CHANGE_PIN))
        {
            if (!mGoodPassword)
            {
                bNewPasswordFieldsAreValid = false;
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), getResources().getString(R.string.activity_signup_insufficient_password));
            }
            else if (!mPasswordConfirmationEditText.getText().toString().equals(mPasswordEditText.getText().toString()))
            {
                bNewPasswordFieldsAreValid = false;
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), getResources().getString(R.string.activity_signup_passwords_dont_match));
            }
        }

        return bNewPasswordFieldsAreValid;
    }

    // checks the pin field
    // returns YES if field is good
    // if the field is bad, an appropriate message box is displayed
    // note: this function is aware of the 'mode' of the view controller and will check and display appropriately
    private boolean pinFieldIsValid()
    {
        boolean bpinNameFieldIsValid = true;

        // if we are signing up for a new account
        if (mMode != CHANGE_PASSWORD)
        {
            // if the pin isn't long enough
            if (mWithdrawalPinEditText.getText().toString().length() < MIN_PIN_LENGTH)
            {
                bpinNameFieldIsValid = false;
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), getResources().getString(R.string.activity_signup_insufficient_pin));
            }
        }

        return bpinNameFieldIsValid;
    }


    private String GetCrackString(double secondsToCrack) {
        String crackString = getResources().getString(R.string.activity_signup_time_to_crack);
        if(secondsToCrack < 60.0)
        {
            crackString += String.format("%.2f ", secondsToCrack);
            crackString += getResources().getString(R.string.activity_signup_seconds);
        }
        else if(secondsToCrack < 3600)
        {
            crackString += String.format("%.2f ", secondsToCrack / 60.0);
            crackString += getResources().getString(R.string.activity_signup_minutes);
        }
        else if(secondsToCrack < 86400)
        {
            crackString += String.format("%.2f ", secondsToCrack / 3600.0);
            crackString += getResources().getString(R.string.activity_signup_hours);
        }
        else if(secondsToCrack < 604800)
        {
            crackString += String.format("%.2f ", secondsToCrack / 86400.0);
            crackString += getResources().getString(R.string.activity_signup_days);
        }
        else if(secondsToCrack < 2419200)
        {
            crackString += String.format("%.2f ", secondsToCrack / 604800.0);
            crackString += getResources().getString(R.string.activity_signup_weeks);
        }
        else if(secondsToCrack < 29030400)
        {
            crackString += String.format("%.2f ", secondsToCrack / 2419200.0);
            crackString += getResources().getString(R.string.activity_signup_months);
        }
        else
        {
            crackString += String.format("%.2f ", secondsToCrack / 29030400.0);
            crackString += getResources().getString(R.string.activity_signup_years);
        }
        return crackString;
    }

    /**
     * Represents an asynchronous account creation task
     */
    private ChangeTask mChangeTask;

    @Override
    public void onBackPress() {
        ((NavigationActivity)getActivity()).popFragment();
        ((NavigationActivity) getActivity()).showNavBar();
    }

    public class ChangeTask extends AsyncTask<Void, Void, Boolean> {
        tABC_CC success;

        @Override
        protected void onPreExecute() {
            ((NavigationActivity)getActivity()).showModalProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (mMode == CHANGE_PASSWORD) {
                mCoreAPI.stopWatchers();
                success = mCoreAPI.ChangePassword(mPasswordEditText.getText().toString());
                mCoreAPI.startWatchers();
            }
            else if (mMode == CHANGE_PASSWORD_VIA_QUESTIONS) {
                success = mCoreAPI.ChangePasswordWithRecoveryAnswers(mPasswordEditText.getText().toString(),
                        mWithdrawalPinEditText.getText().toString(), "");
            }
            else {
                success = mCoreAPI.SetUserPIN(mWithdrawalPinEditText.getText().toString());
            }

            return success == tABC_CC.ABC_CC_Ok;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            ((NavigationActivity)getActivity()).showModalProgress(false);
            if (success) {
                if (mMode == CHANGE_PASSWORD) {
                    ((NavigationActivity)getActivity()).ShowMessageDialogBackPress(getResources().getString(R.string.activity_signup_password_change_title), getResources().getString(R.string.activity_signup_password_change_good));
                } else if (mMode == CHANGE_PASSWORD_VIA_QUESTIONS) {
                    ((NavigationActivity)getActivity()).ShowMessageDialogBackPress(getResources().getString(R.string.activity_signup_password_change_title), getResources().getString(R.string.activity_signup_password_change_via_questions_good));
                } else {
                    ((NavigationActivity)getActivity()).ShowMessageDialogBackPress(getResources().getString(R.string.activity_signup_pin_change_title), getResources().getString(R.string.activity_signup_pin_change_good));
                }
            } else {
                if (mMode == CHANGE_PASSWORD) {
                    ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_password_change_title), getResources().getString(R.string.activity_signup_password_change_bad));
                } else if (mMode == CHANGE_PASSWORD_VIA_QUESTIONS) {
                    ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_password_change_title), getResources().getString(R.string.activity_signup_password_change_via_questions_bad));
                } else {
                    ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_pin_change_title), getResources().getString(R.string.activity_signup_pin_change_bad));
                }
            }
        }

        @Override
        protected void onCancelled() {
            mChangeTask = null;
            ((NavigationActivity)getActivity()).showModalProgress(false);
        }
    }

    /**
     * Represents an asynchronous account creation task
     */
    public class CreateAccountTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mPin;
        private String mFailureReason;
        tABC_Error pError = new tABC_Error();
        tABC_RequestResults pData = new tABC_RequestResults();
        SWIGTYPE_p_void pVoid = core.requestResultsp_to_voidp(pData);

        CreateAccountTask(String email, String password, String pin) {
            mUsername = email;
            mPassword = password;
            mPin = pin;
            ((NavigationActivity)getActivity()).showModalProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            tABC_CC code = core.ABC_CreateAccount(mUsername, mPassword, mPin, null, pVoid, pError);
            mFailureReason = pError.getSzDescription();
            return code == tABC_CC.ABC_CC_Ok;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            ((NavigationActivity)getActivity()).showModalProgress(false);
            if (success) {
                mCreateFirstWalletTask = new CreateFirstWalletTask(mUsername, mPassword);
                mCreateFirstWalletTask.execute((Void) null);
            } else {
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), mFailureReason);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            ((NavigationActivity)getActivity()).showModalProgress(false);
        }
    }

    /**
     * Represents an asynchronous creation of the first wallet
     */
    public class CreateFirstWalletTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername, mPassword;

        CreateFirstWalletTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            ((NavigationActivity)getActivity()).showModalProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String walletName = getResources().getString(R.string.activity_recovery_first_wallet_name);
            return mCoreAPI.createWallet(mUsername, mPassword, walletName, DOLLAR_CURRENCY_NUMBER);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCreateFirstWalletTask = null;
            ((NavigationActivity)getActivity()).showModalProgress(false);
            if (!success) {
                ((NavigationActivity)getActivity()).ShowOkMessageDialog(getResources().getString(R.string.activity_signup_failed), getResources().getString(R.string.activity_signup_create_wallet_fail));
            } else {
                AirbitzApplication.Login(mUsername, mPassword);
                CreateDefaultCategories();

                Fragment frag = new PasswordRecoveryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PasswordRecoveryFragment.MODE, PasswordRecoveryFragment.SIGN_UP);
                frag.setArguments(bundle);
                ((NavigationActivity) getActivity()).popFragment();
                ((NavigationActivity) getActivity()).pushFragment(frag, NavigationActivity.Tabs.BD.ordinal());
            }
        }

        @Override
        protected void onCancelled() {
            mCreateFirstWalletTask = null;
            ((NavigationActivity)getActivity()).showModalProgress(false);
        }
    }

    private void CreateDefaultCategories() {
        String[] defaults = getResources().getStringArray(R.array.category_defaults);

        for(String cat : defaults)
            mCoreAPI.addCategory(cat);

        List<String> cats = mCoreAPI.loadCategories();
        if(cats.size()==0 || cats.get(0).equals(defaults)) {
            Log.d("SignupActivity", "Category creation failed");
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String pin = mWithdrawalPinEditText.getText().toString();

        // Reset errors.
        mPasswordEditText.setError(null);
        mUserNameEditText.setError(null);
        mPasswordConfirmationEditText.setError(null);
        mWithdrawalPinEditText.setError(null);

        mAuthTask = new CreateAccountTask(username, password, pin);
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}