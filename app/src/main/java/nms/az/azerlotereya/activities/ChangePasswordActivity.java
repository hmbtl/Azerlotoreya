package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 12/21/15.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword, newPassword, repeatPassword;
    private TextView accountEditText;
    private Button saveButton;

    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.change_password);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initLayout();
    }

    private void initLayout() {
        currentPassword = (EditText) findViewById(R.id.current_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        repeatPassword = (EditText) findViewById(R.id.repeat_password);
        accountEditText = (TextView) findViewById(R.id.account_edit_text);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(onClick);


        accountEditText.setText(SharedData.getFullName());
    }

    private boolean checkEditText(EditText... editTexts) {

        for (EditText editText : editTexts) {

            if (editText == currentPassword && editText.getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.password_old_message);
                return false;
            } else if (editText == newPassword && editText.getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.password_new_message);
                return false;
            } else if (editText == repeatPassword && editText.getText().toString().equals("")) {
                Utilities.showMessageToast(context, R.string.password_new_repeat_message);
                return false;
            }
        }

        return true;
    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == saveButton) {
                if (checkEditText(currentPassword, newPassword, repeatPassword)) {
                    if (newPassword.getText().toString().equalsIgnoreCase(repeatPassword.getText().toString())) {

                        HashMap<String, String> params = new HashMap<>();
                        params.put("password_old", currentPassword.getText().toString());
                        params.put("password_new", newPassword.getText().toString());

                        new BgRequestAsynctask(context, "PUT", "user/password", params,
                                new JsonAPIListener() {


                                    @Override
                                    public void onData(JSONObject json) throws JSONException {

                                    }

                                    @Override
                                    public void onNull() {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        newPassword.setText("");
                                        repeatPassword.setText("");
                                        currentPassword.setText("");
                                        Utilities.showMessageToast(context, R.string.change_password_message);
                                    }

                                    @Override
                                    public void onError() {

                                    }

                                }).execute();
                    } else
                        Utilities.showMessageToast(context, R.string.password_match_text);
                }
            }
        }
    };


}
