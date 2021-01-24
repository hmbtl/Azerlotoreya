package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.JSONParser;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/31/15.
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedData.init(this);


        String country = getCountry();

        Log.e("Country", country);

        if (country == null || country.equals(""))
            new CheckCountry(this).execute();
        else if (country.equalsIgnoreCase("az"))
            startApp();
        else
            finish();

    }


    private void startApp() {

        if (SharedData.getLang().equals("en")) {
            Utilities.setLocale(this, Utilities.LANGUAGE_EN);
        } else {
            Utilities.setLocale(this, Utilities.LANGUAGE_RU);
        }

        if (SharedData.isFirstRun())
            startActivity(new Intent(this, IntroActivity.class).
                    putExtra(Constants.RUNNING_INTRO_FROM_WHERE, Constants.RUNNING_FROM_START_ACTIVITY));
        else
            startActivity(new Intent(this, LoginActivity.class));


        finish();
    }

    private String getCountry() {

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();

        return countryCodeValue;
    }


    private class CheckCountry extends AsyncTask<String, String, String> {


        private String url = "http://ip-api.com/json";
        private Activity context;

        public CheckCountry(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            String country = null;

            try {

                JSONObject json = jsonParser.sendRequest("get", "http://ip-api.com/json", null);

                country = json.getString("countryCode").toLowerCase();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
            }

            return country;
        }

        @Override
        protected void onPostExecute(String country) {
            super.onPostExecute(country);
            if (country == null) {
                showInternetDialog();
            } else if (country.equalsIgnoreCase("az")) {
                startApp();
            } else {
                finish();
            }

        }

    }

    private void showInternetDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_alert_custom);
        dialog.setTitle("Internet");

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_alert_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_alert_message);


        assert dialogButton != null;
        assert dialogMessage != null;

        dialogButton.setText(R.string.close);
        dialogMessage.setText(R.string.internet_connection_error);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });


        dialog.show();
    }
}
