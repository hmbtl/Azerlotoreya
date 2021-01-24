package nms.az.azerlotereya.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import nms.az.azerlotereya.R;

/**
 * Created by anar on 3/15/16.
 */
public class InfoActivity extends AppCompatActivity {


    private TextView phoneCallAC, phoneCallBK, phoneCallNR;
    private TextView emailText;

    private Activity context;

    private final int MY_PERMISSIONS_REQUEST_CALL = 12;

    private String phone;

    private Intent callIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        initToolbar();
        initViews();

        callIntent = new Intent(Intent.ACTION_CALL);
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.about_us);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        phone = "0503188821";

        context = this;

        phoneCallAC = (TextView) findViewById(R.id.phone_1);
        phoneCallBK = (TextView) findViewById(R.id.phone_2);
        phoneCallNR = (TextView) findViewById(R.id.phone_3);
        emailText = (TextView) findViewById(R.id.email_send);


        phoneCallAC.setOnClickListener(onClickCall);
        phoneCallBK.setOnClickListener(onClickCall);
        phoneCallNR.setOnClickListener(onClickCall);


        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "info@mobileair.az", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }

    View.OnClickListener onClickCall = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (v == phoneCallAC) {
                phone = "0503188821";
            } else if (v == phoneCallBK) {
                phone = "0553188821";
            } else if (v == phoneCallNR) {
                phone = "0773188821";
            }


            callIntent.setData(Uri.parse("tel:" + phone));

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {


                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
                startActivity(callIntent);
            }

        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
