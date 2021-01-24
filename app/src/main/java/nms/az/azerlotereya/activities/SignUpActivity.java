package nms.az.azerlotereya.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/19/15.
 */
public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private EditText nameEditText, surnameEditText, idNumberEditText, phoneEditText, fatherNameEditText;
    private EditText usernameEditText, passwordEditText, passwordAgainEditText,addressEditText;
    private TextView dateTextView;

    private Button registerButton;

    private final int READ_ACCOUNT_PERM = 1;

    private Activity context;


    private Calendar birthDate, minDate;
    private boolean isAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        context = this;
        birthDate = Calendar.getInstance();
        minDate = Calendar.getInstance();

        minDate.set(birthDate.get(Calendar.YEAR) - 18, birthDate.get(Calendar.MONTH) , birthDate.get(Calendar.DAY_OF_MONTH));
        birthDate.set(minDate.get(Calendar.YEAR), minDate.get(Calendar.MONTH), minDate.get(Calendar.DAY_OF_MONTH));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(getResources().getString(R.string.registration));


        initViews();

        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, Manifest.permission.GET_ACCOUNTS)) {
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, READ_ACCOUNT_PERM);
            } else {
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, READ_ACCOUNT_PERM);
            }
        } else {
           usernameEditText.setText(getMainEmail());
        }


    }

    private String getMainEmail(){
        String mainEmail = "";
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                mainEmail = account.name;
            }
        }

        return mainEmail;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       if(requestCode == READ_ACCOUNT_PERM){
           if (grantResults.length > 0
                   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               usernameEditText.setText(getMainEmail());
               // permission was granted, yay! Do the
           }
       }
    }

    private void initViews() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        surnameEditText = (EditText) findViewById(R.id.surname_edit_text);
        idNumberEditText = (EditText) findViewById(R.id.id_edit_text);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        fatherNameEditText = (EditText) findViewById(R.id.father_edit_text);

        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        usernameEditText = (EditText) findViewById(R.id.account_user_edit_text);
        passwordEditText = (EditText) findViewById(R.id.account_password_edit_text);
        passwordAgainEditText = (EditText) findViewById(R.id.account_password_again_edit_text);


        dateTextView = (TextView) findViewById(R.id.birthday_picker);
        dateTextView.setOnClickListener(onClick);

        registerButton = (Button) findViewById(R.id.button_register);
        registerButton.setOnClickListener(onClick);
        addressEditText.setOnFocusChangeListener(onFocus);
    }


    private boolean checkEditText(EditText... editTexts) {

        for (int i = 0; i < editTexts.length; i++) {

            if (editTexts[i] == nameEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.name_message);
                return false;
            } else if (editTexts[i] == surnameEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.surname_message);
                return false;
            } else if (editTexts[i] == fatherNameEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.surname_message);
                return false;
            }
            if (i == 3 && dateTextView.getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.birthdate_message);
                return false;
            } else if (editTexts[i] == idNumberEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.passport_id_message);
                return false;
            }else if (editTexts[i] == addressEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.address_id_message);
                return false;
            }else if (editTexts[i] == phoneEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.phone_number_message);
                return false;
            } else if (editTexts[i] == usernameEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.email_message);
                return false;
            } else if (editTexts[i] == passwordEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.empty_password_message);
                return false;
            } else if (editTexts[i] == passwordAgainEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(SignUpActivity.this, R.string.password_again_message);
                return false;
            }
        }

        return true;
    }



    View.OnFocusChangeListener onFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(view == addressEditText){
                if(hasFocus){
                    Utilities.showAlertDialog(context,R.string.info,R.string.address_fill_message,R.string.close);
                }
            }
        }
    };


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == dateTextView) {

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SignUpActivity.this,
                        birthDate.get(Calendar.YEAR),
                        birthDate.get(Calendar.MONTH) - 1,
                        birthDate.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(minDate);
                dpd.show(SignUpActivity.this.getFragmentManager(), "Datepickerdialog");
            } else if (view == registerButton) {
                if (checkEditText(nameEditText, surnameEditText, fatherNameEditText ,idNumberEditText, phoneEditText, usernameEditText, passwordEditText, passwordAgainEditText))
                {
                    if (!Utilities.checkEmail(usernameEditText))
                        Utilities.showMessageToast(SignUpActivity.this, R.string.wrong_email_text);
                    else {
                        if (!passwordEditText.getText().toString().equals(passwordAgainEditText.getText().toString()))
                            Utilities.showMessageToast(SignUpActivity.this, R.string.password_match_text);
                        else {


                            showTermsDialog();


                        }
                    }

                }
            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.slide_out_bottom);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        dateTextView.setText(day + "/" + (month + 1) + "/" + year);

        birthDate.set(year, month + 1, day);

    }

    private void showTermsDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_terms);
        dialog.setTitle(R.string.agreement);

        final Button dialogLeftButton = (Button) dialog.findViewById(R.id.dialog_action_left_button);
        final Button dialogRightButton = (Button) dialog.findViewById(R.id.dialog_action_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_action_instruction);


        dialogMessage.setText(Html.fromHtml(getResources().getString(R.string.terms_and_conditions_doc)));
        dialogLeftButton.setText(R.string.cancel);
        dialogRightButton.setText(R.string.accept);

        dialogLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialogRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", nameEditText.getText().toString());
                params.put("surname", surnameEditText.getText().toString());
                params.put("father", fatherNameEditText.getText().toString());
                params.put("email", usernameEditText.getText().toString());
                params.put("password", passwordEditText.getText().toString());
                params.put("mobile", phoneEditText.getText().toString());
                params.put("address", addressEditText.getText().toString());
                params.put("passport_id", idNumberEditText.getText().toString());
                params.put("birthdate", dateTextView.getText().toString());

                new BgRequestAsynctask(context, "POST", "register", params, new JsonAPIListener() {
                    @Override
                    public void onNull() {

                    }

                    @Override
                    public void onSuccess() {
                        SharedData.setLoggedIn(false);
                        Utilities.showMessageToast(context, R.string.user_create_message);
                        finish();
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onData(JSONObject json) throws JSONException {

                    }
                }).execute();
            }
        });


        dialog.show();
    }

}
