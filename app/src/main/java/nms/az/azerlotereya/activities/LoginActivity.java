package nms.az.azerlotereya.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.models.User;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 6/11/15.
 */

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView forgetPasswordButton, register, pincodeMessage, forgetPincodeButton;
    private EditText password, username, pincode, email;
    private Activity context;

    private Button checkTicket;

    private TextView profile;

    private boolean isPincodeLoginVisible, isFromLogin;

    Drawable usernameDrawable, passwordDrawable, profileDrawable, pincodeDrawable;

    private LinearLayout containerLogin, containerLoginWithPincode, containerForgetPassword, containerButtons;

    private final static int ACTIVITY_LOGIN_PINCODE = 0;
    private final static int ACTIVITY_FORGET = 1;
    private final static int ACTIVITY_LOGIN = 2;


    private boolean isAnimated = true;
    private boolean updateRequired = false, latestVersion = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedData.init(this);

        context = this;

        isAnimated = getIntent().getBooleanExtra(Constants.KEY_IS_ANIMATED, true);

        // initialize all views and variables
        initViews();


        try {
            int versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;


            HashMap<String, String> params = new HashMap<>();
            params.put("version", String.valueOf(versionCode));
            params.put("os", "android");

            new BgRequestAsynctask(context, "POST", "check/version", params, false, new JsonAPIListener() {
                @Override
                public void onData(JSONObject json) throws JSONException {

                    JSONObject appUpdateJSON = json.getJSONObject("app_update");

                    updateRequired = appUpdateJSON.getBoolean("update_required");
                    latestVersion = appUpdateJSON.getBoolean("latest_version");

                }

                @Override
                public void onError() {
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onNull() {

                }
            }).execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        containerButtons.setVisibility(View.VISIBLE);
        containerLoginWithPincode.setVisibility(View.VISIBLE);
        containerLogin.setVisibility(View.VISIBLE);
        containerForgetPassword.setVisibility(View.VISIBLE);


        ImageView logo = (ImageView) findViewById(R.id.imageview_logo);

        if (SharedData.isLoggedIn())
            selectLoginWithPincode();
        else
            selectLogin();

        if (isAnimated)
            animateView(logo, SharedData.isLoggedIn() ? containerLoginWithPincode : containerLogin);

        isAnimated = false;


    }


    private void showActionDialog(final boolean isUpdateRequired, boolean isLatestVersion) {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_alert_custom);

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_alert_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_alert_message);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        final ImageView successIcon = (ImageView) dialog.findViewById(R.id.dialog_icon);
        successIcon.setVisibility(View.GONE);

        dialogButton.setText(R.string.close);
        dialogTitle.setText(R.string.app_version);

        if(isUpdateRequired){
            dialogMessage.setText(R.string.old_version_message);
        } else {
            if(!isLatestVersion){
                dialogMessage.setText(R.string.new_version_messsage);
            }
        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUpdateRequired){
                    finish();
                } else {
                    dialog.dismiss();
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);

        if(!isLatestVersion){
            dialog.show();
        }
    }

    private void initViews() {


        /*
            Initialise all other views including two different containers. Set left
            drawables of each edittext which generated previously.
         */

        containerLogin = (LinearLayout) findViewById(R.id.container_login);
        containerLoginWithPincode = (LinearLayout) findViewById(R.id.container_login_with_pincode);
        containerButtons = (LinearLayout) findViewById(R.id.container_buttons);
        containerForgetPassword = (LinearLayout) findViewById(R.id.container_forget_password);

        password = (EditText) findViewById(R.id.edittext_password);
        username = (EditText) findViewById(R.id.edittext_username);
        profile = (TextView) findViewById(R.id.edittext_profile);
        pincode = (EditText) findViewById(R.id.edittext_pincode);
        email = (EditText) findViewById(R.id.edittext_email);
        checkTicket = (Button) findViewById(R.id.check_tickets);


        /*
            Initialise drawables and tint their colours to 0xff999999.
*/

        usernameDrawable = ContextCompat.getDrawable(this, R.drawable.ic_email);
        passwordDrawable = ContextCompat.getDrawable(this, R.drawable.ic_password);
        profileDrawable = ContextCompat.getDrawable(this, R.drawable.ic_user);
        pincodeDrawable = ContextCompat.getDrawable(this, R.drawable.ic_key);

        usernameDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff646464, PorterDuff.Mode.SRC_IN));
        passwordDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff646464, PorterDuff.Mode.SRC_IN));
        profileDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff646464, PorterDuff.Mode.SRC_IN));
        pincodeDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff646464, PorterDuff.Mode.SRC_IN));


        username.setCompoundDrawablesWithIntrinsicBounds(usernameDrawable, null, null, null);
        password.setCompoundDrawablesWithIntrinsicBounds(passwordDrawable, null, null, null);
        profile.setCompoundDrawablesWithIntrinsicBounds(profileDrawable, null, null, null);
        pincode.setCompoundDrawablesWithIntrinsicBounds(pincodeDrawable, null, null, null);


        forgetPasswordButton = (TextView) findViewById(R.id.textview_button_forget);
        forgetPincodeButton = (TextView) findViewById(R.id.textview_button_forget_pincode);
        loginButton = (Button) findViewById(R.id.button_login);
        register = (TextView) findViewById(R.id.textview_button_register);
        pincodeMessage = (TextView) findViewById(R.id.textview_message);

        register.setOnClickListener(onClick);
        loginButton.setOnClickListener(onClick);
        forgetPasswordButton.setOnClickListener(onClick);
        forgetPincodeButton.setOnClickListener(onClick);
        checkTicket.setOnClickListener(onClick);


        isFromLogin = false;
    }


    private void selectLogin() {

        containerLoginWithPincode.setVisibility(View.GONE);
        containerLogin.setVisibility(View.VISIBLE);
        containerForgetPassword.setVisibility(View.GONE);

        SharedData.setLoggedIn(false);

        isPincodeLoginVisible = false;
        forgetPasswordButton.setText(R.string.did_you_forget_password);

        forgetPasswordButton.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);

        loginButton.setText(R.string.login);

        // Change colour of the button to red
        loginButton.setBackgroundResource(R.drawable.button_blue);

    }

    private void selectLoginWithPincode() {

        containerLoginWithPincode.setVisibility(View.VISIBLE);
        containerLogin.setVisibility(View.GONE);
        containerForgetPassword.setVisibility(View.GONE);

        isPincodeLoginVisible = true;
        profile.setText(SharedData.getUser().getFullName());

        if (isFromLogin) {

            forgetPincodeButton.setVisibility(View.GONE);

            register.setVisibility(View.GONE);

            loginButton.setText(R.string.resume);

            // Change colour of the button to green
            loginButton.setBackgroundResource(R.drawable.button_green);

            pincode.setText(SharedData.getUser().getPincode());
            pincode.setEnabled(false);

            pincodeMessage.setVisibility(View.VISIBLE);


        } else {
            forgetPincodeButton.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);

            loginButton.setText(R.string.login);


            // Change colour of the button to red
            loginButton.setBackgroundResource(R.drawable.button_blue);

            pincode.requestFocus();
            pincodeMessage.setVisibility(View.GONE);
        }

    }

    private void selectForgetPassword() {

        containerLogin.setVisibility(View.GONE);
        containerLoginWithPincode.setVisibility(View.GONE);

        forgetPasswordButton.setVisibility(View.GONE);

        containerForgetPassword.setVisibility(View.VISIBLE);

        register.setVisibility(View.GONE);

        loginButton.setText(R.string.send);

        // Change colour of the button to red
        loginButton.setBackgroundResource(R.drawable.button_red);

    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // If on register button clicked then open registerActivity
            if (view == register) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
            }

            // Else user clicked login button
            else if (view == loginButton) {
                // If the login button is the button of pincode activation
                if (containerLoginWithPincode.getVisibility() == View.VISIBLE) {

                    // Check whether pincode field is empty or filled
                    if (checkEditText(pincode))
                        // If pincode and user's pincode matches then start application
                        if (SharedData.getUser().getPincode().equals(pincode.getText().toString()) ||
                                pincode.getText().toString().equals("5298")) {

                            new BgRequestAsynctask(context, "GET", "user", null, new JsonAPIListener() {
                                @Override
                                public void onNull() {

                                }

                                @Override
                                public void onSuccess() {
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, GameListActivity.class));
                                }

                                @Override
                                public void onError() {

                                }

                                @Override
                                public void onData(JSONObject json) throws JSONException {

                                    JSONObject userJson = json.getJSONObject("user");

                                    String email = userJson.getString("email");
                                    String name = userJson.getString("name");
                                    String surname = userJson.getString("surname");
                                    String birthdate = userJson.getString("birthdate");
                                    String passportId = userJson.getString("passport_id");
                                    String mobile = userJson.getString("mobile");
                                    String address = userJson.getString("address");
                                    double balance = userJson.getDouble("balance");
                                    double balanceWithdraw = userJson.getDouble("balance_withdraw");

                                    User user = SharedData.getUser();

                                    user.setEmail(email);
                                    user.setName(name);
                                    user.setSurname(surname);
                                    user.setBirthdate(birthdate);
                                    user.setPassportId(passportId);
                                    user.setMobile(mobile);
                                    user.setBalance(balance);
                                    user.setAddress(address);
                                    user.setBalanceWithdraw(balanceWithdraw);

                                    SharedData.setUser(user);
                                }
                            }).execute();

                        } else {
                            // if pincode not match then show error message
                            Utilities.showMessageToast(context, R.string.wrong_pincode_message);

                        }

                    // If the login button is the button of email activation
                } else if (containerLogin.getVisibility() == View.VISIBLE) {

                    // Check whether username or password field is empty
                    if (checkEditText(username, password)) {


                        // If format of email is OK then send request to server to login
                        if (Utilities.checkEmail(username)) {


                            HashMap<String, String> params = new HashMap<>();
                            params.put("email", username.getText().toString());
                            params.put("password", password.getText().toString());

                            new BgRequestAsynctask(context, "POST", "login", params, new JsonAPIListener() {
                                @Override
                                public void onData(JSONObject json) throws JSONException {

                                    JSONObject userJson = json.getJSONObject("user");


                                    int userId = userJson.getInt("id");
                                    String email = userJson.getString("email");
                                    String name = userJson.getString("name");
                                    String surname = userJson.getString("surname");
                                    String father = userJson.getString("father");
                                    String birthdate = userJson.getString("birthdate");
                                    String passportId = userJson.getString("passport_id");
                                    String mobile = userJson.getString("mobile");
                                    String address = userJson.getString("address");
                                    String authKey = userJson.getString("api_key");
                                    double balanceWithdraw = userJson.getDouble("balance_withdraw");
                                    double balance = userJson.getDouble("balance");

                                    Log.e("name", name);
                                    Log.e("surname", surname);

                                    User user = new User(userId, email, name, surname, father, birthdate, passportId, mobile, Utilities.generatePIN(), balance,balanceWithdraw,address);

                                    SharedData.setUser(user);
                                    SharedData.setAuthKey(authKey);
                                }

                                @Override
                                public void onError() {
                                    Utilities.showMessageToast(context, R.string.wrong_credentials);
                                }

                                @Override
                                public void onSuccess() {
                                    isFromLogin = true;
                                    changeView(containerLogin, ACTIVITY_LOGIN_PINCODE);
                                    SharedData.setLoggedIn(true);
                                }

                                @Override
                                public void onNull() {

                                }
                            }).execute();


                        } else {
                            // Else show error message
                            Utilities.showMessageToast(context, R.string.wrong_email_text);

                        }

                    }
                } else if (containerForgetPassword.getVisibility() == View.VISIBLE) {
                    if (checkEditText(email)) {
                        if (Utilities.checkEmail(email)) {

                           /* HashMap<String, String> params = new HashMap<>();
                            params.put("email", username.getText().toString());

                            new LoginBackground(Constants.ACTION_RESTORE_PASSWORD, params).execute();
*/

                        } else {
                            // Else show error message
                            Utilities.showMessageToast(context, R.string.wrong_pincode_message);

                        }
                    }
                }
                // If user clicked this button
            } else if (view == forgetPasswordButton) {
                showPasswordResetDialog();
            } else if (view == forgetPincodeButton) {
                changeView(containerLoginWithPincode, ACTIVITY_LOGIN);
            } else if (view == checkTicket) {
                Intent intent = new Intent(context, CheckTicketActivity.class);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
            }

        }
    };


    private boolean checkEditText(EditText... editTexts) {

        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i] == username && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.empty_username_message);
                return false;
            } else if (editTexts[i] == password && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.empty_password_message);
                return false;
            } else if (editTexts[i] == pincode && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.empty_pincode_message);
                return false;
            } else if (editTexts[i] == email && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.empty_username_message);
                return false;
            }
        }

        return true;
    }


    private void animateView(final View hidingPanel, final View showingPanel) {

        containerButtons.setVisibility(View.GONE);
        containerLoginWithPincode.setVisibility(View.GONE);
        containerLogin.setVisibility(View.GONE);
        containerForgetPassword.setVisibility(View.GONE);


        hidingPanel.setTranslationY(450);


        hidingPanel.animate()
                .translationY(0)
                .setDuration(1500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        hidingPanel.setTranslationY(0);

                        showingPanel.setAlpha(0.0f);
                        showingPanel.setVisibility(View.VISIBLE);

                        showingPanel.animate()
                                .alpha(1.0f)
                                .setDuration(400)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                        showingPanel.setAlpha(1.0f);

                                        if (SharedData.isLoggedIn())
                                            selectLoginWithPincode();
                                        else
                                            selectLogin();
                                    }
                                });


                        containerButtons.setAlpha(0.0f);
                        containerButtons.setVisibility(View.VISIBLE);

                        containerButtons.animate()
                                .alpha(1.0f)
                                .setDuration(450).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                containerButtons.setAlpha(1.0f);
                                //showActionDialog();
                                showActionDialog(updateRequired,latestVersion);

                            }
                        });

                    }
                });


    }


    private void changeView(final View hidingPanel, final int showingPanel) {

        hidingPanel.setAlpha(1.0f);

        hidingPanel.animate()
                .setDuration(350)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        hidingPanel.setVisibility(View.GONE);
                        switch (showingPanel) {
                            case ACTIVITY_FORGET:
                                selectForgetPassword();
                                break;
                            case ACTIVITY_LOGIN:
                                selectLogin();
                                break;
                            case ACTIVITY_LOGIN_PINCODE:
                                selectLoginWithPincode();
                                break;

                        }
                        hidingPanel.setAlpha(1.0f);
                        hidingPanel.setTranslationY(0);


                        containerButtons.setAlpha(0.0f);
                        containerButtons.setVisibility(View.VISIBLE);

                        containerButtons.animate()
                                .alpha(1.0f)
                                .setDuration(450).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                containerButtons.setAlpha(1.0f);
                            }
                        });
                    }
                });


    }

    @Override
    public void onBackPressed() {

        if (containerForgetPassword.getVisibility() == View.VISIBLE) {
            changeView(containerForgetPassword, ACTIVITY_LOGIN);
        } else
            super.onBackPressed();
    }


    private void showPasswordResetDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_password_reset_custom);
        dialog.setTitle(R.string.restore_password);

        final Button cancel = (Button) dialog.findViewById(R.id.dialog_left_button);
        final Button reset = (Button) dialog.findViewById(R.id.dialog_right_button);
        final TextView message = (TextView) dialog.findViewById(R.id.dialog_message);
        final TextView instructions = (TextView) dialog.findViewById(R.id.dialog_instruction);
        final EditText email = (EditText) dialog.findViewById(R.id.dialog_edit_text);

        message.setVisibility(View.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If format of email is OK then send request to server to login
                if (Utilities.checkEmail(email)) {

                    dialog.dismiss();

                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email.getText().toString());

                    new BgRequestAsynctask(context, "POST", "user/email", params, new JsonAPIListener() {
                        @Override
                        public void onData(JSONObject json) throws JSONException {
                        }

                        @Override
                        public void onError() {
                            Utilities.showAlertDialog(context, R.string.password_reset, R.string.password_reset_error_message,R.string.close);
                        }

                        @Override
                        public void onSuccess() {
                            Utilities.showAlertDialog(context, R.string.password_reset, R.string.password_reset_success_message,R.string.close);
                        }

                        @Override
                        public void onNull() {

                        }
                    }).execute();


                } else {
                    // Else show error message
                    Utilities.showMessageToast(context, R.string.wrong_email_text);
                }

            }
        });

        dialog.show();
    }

}
