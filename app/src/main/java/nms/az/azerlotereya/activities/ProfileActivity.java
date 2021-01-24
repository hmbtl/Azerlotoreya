package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.models.User;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/31/15.
 */
public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText nameEditText, surnameEditText, idNumberEditText, phoneEditText,addressEditText;
    private EditText emailEditText,fatherEditText;
    private TextView dateTextView, changePassword;

    private Button saveButton;

    private Activity context;


    private User generalUser;

    private Calendar birthDate, minDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.my_profile);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;

        initLayout();

        new BgRequestAsynctask(context, "GET", "user", null, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {

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
                String father = userJson.getString("father");
                String passportId = userJson.getString("passport_id");
                String mobile = userJson.getString("mobile");
                double balance = userJson.getDouble("balance");

                User user = SharedData.getUser();

                user.setEmail(email);
                user.setName(name);
                user.setSurname(surname);
                user.setBirthdate(birthdate);
                user.setPassportId(passportId);
                user.setMobile(mobile);
                user.setFather(father);
                user.setBalance(balance);

                SharedData.setUser(user);
            }
        }).execute();

    }

    private void initLayout() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        surnameEditText = (EditText) findViewById(R.id.surname_edit_text);
        fatherEditText = (EditText) findViewById(R.id.father_edit_text);
        idNumberEditText = (EditText) findViewById(R.id.id_edit_text);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);

        changePassword = (TextView) findViewById(R.id.change_password);

        dateTextView = (TextView) findViewById(R.id.birthday_picker);


        generalUser = SharedData.getUser();

        nameEditText.setText(generalUser.getName());
        surnameEditText.setText(generalUser.getSurname());
        fatherEditText.setText(generalUser.getFather());
        idNumberEditText.setText(generalUser.getPassportId());
        phoneEditText.setText(generalUser.getMobile());
        addressEditText.setText(generalUser.getAddress());
        emailEditText.setText(generalUser.getEmail());
        dateTextView.setText(generalUser.getBirthdate());

        changePassword.setOnClickListener(onClick);


        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(onClick);


        birthDate = Calendar.getInstance();
        minDate = Calendar.getInstance();


        minDate.set(birthDate.get(Calendar.YEAR) - 18, birthDate.get(Calendar.MONTH), birthDate.get(Calendar.DAY_OF_MONTH));

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        try {
            Date date = format.parse(generalUser.getBirthdate());
            birthDate.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateTextView.setOnClickListener(onClick);

    }

    private boolean checkEditText(EditText... editTexts) {

        for (int i = 0; i < editTexts.length; i++) {


            if (editTexts[i] == nameEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.name_message);
                return false;
            } else if (editTexts[i] == surnameEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.surname_message);
                return false;
            } else if (editTexts[i] == fatherEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.father_message);
                return false;
            }

            if (i == 3 && dateTextView.getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.birthdate_message);
                return false;
            } else if (editTexts[i] == idNumberEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.passport_id_message);
                return false;
            } else if (editTexts[i] == phoneEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.phone_number_message);
                return false;
            }else if (editTexts[i] == addressEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.address_id_message);
                return false;
            } else if (editTexts[i] == emailEditText && editTexts[i].getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.email_message);
                return false;
            }
        }

        return true;
    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == dateTextView) {

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ProfileActivity.this,
                        birthDate.get(Calendar.YEAR),
                        birthDate.get(Calendar.MONTH),
                        birthDate.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(minDate);
                dpd.show(context.getFragmentManager(), "Datepickerdialog");
            } else if (view == saveButton) {
                if (checkEditText(nameEditText, surnameEditText, idNumberEditText, phoneEditText, emailEditText))
                    Log.e("Success", "s");
                {
                    if (!Utilities.checkEmail(emailEditText))
                        Utilities.showMessageToast(context, R.string.wrong_email_text);
                    else {


                        HashMap<String, String> params = new HashMap<>();
                        params.put("name", nameEditText.getText().toString());
                        params.put("surname", surnameEditText.getText().toString());
                        params.put("father", fatherEditText.getText().toString());
                        params.put("email", emailEditText.getText().toString());
                        params.put("mobile", phoneEditText.getText().toString());
                        params.put("address", addressEditText.getText().toString());
                        params.put("passport_id", idNumberEditText.getText().toString());
                        params.put("birthdate", dateTextView.getText().toString());

                        Log.e("Values",params.toString());

                        new BgRequestAsynctask(context, "PUT", "user", params,
                                new JsonAPIListener() {
                                    @Override
                                    public void onData(JSONObject json) {

                                    }

                                    @Override
                                    public void onNull() {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        updateUser();
                                        Utilities.showMessageToast(context, R.string.profile_update_message);
                                       // finish();
                                    }

                                    @Override
                                    public void onError() {

                                    }

                                }).execute();
                    }

                }
            } else if (view == changePassword) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        }
    };


    private void updateUser() {

        String email = emailEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String father = fatherEditText.getText().toString();
        String passportId = idNumberEditText.getText().toString();
        String mobile = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String birth = dateTextView.getText().toString();


        User usr = new User(generalUser.getUserId(), email, name, surname, father, birth, passportId, mobile,
                generalUser.getPincode(), generalUser.getBalance(),generalUser.getBalanceWithdraw(),address);
        SharedData.setUser(usr);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        dateTextView.setText(day + "/" + month + "/" + year);

        birthDate.set(year, month, day);
    }


}
