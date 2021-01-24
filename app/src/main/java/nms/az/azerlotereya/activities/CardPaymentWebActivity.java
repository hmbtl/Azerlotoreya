package nms.az.azerlotereya.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.customviews.ProgressCustom;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;

/**
 * Created by anar on 12/23/15.
 */
public class CardPaymentWebActivity extends AppCompatActivity {

    private WebView webView;

    private ProgressCustom dialog;

    private Activity context;
    final String loadUrl = "http:/185.118.50.124:5050/payments/bob/pay";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_azericard);
        initToolbar();

        context = this;

        String amount = getIntent().getStringExtra(Constants.PAYMENT_AMOUNT_KEY);
        String name = getIntent().getStringExtra(Constants.PAYMENT_NAME_KEY);


        name = name.replace(" ", "+");
        String lang = Locale.getDefault().getLanguage();
        amount = String.valueOf(Integer.parseInt(amount) * 100);

        final Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Authorization", SharedData.getAuthKey());

        dialog = new ProgressCustom(this);

        String getData = "?amount=" + amount + "&user=" + name + "&language=" + lang;

        // initialize the browser object
        WebView browser = (WebView) findViewById(R.id.web_azericard);

        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setJavaScriptEnabled(true);

        browser.setWebViewClient(new WebViewClient() {
                                     @SuppressWarnings("deprecation")
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         view.getSettings().setLoadWithOverviewMode(true);
                                         view.getSettings().setUseWideViewPort(true);
                                         Log.e("URL to load", url);

                                         if (url.contains("http://success/")) {
                                             Intent returnIntent = new Intent();
                                             setResult(Activity.RESULT_OK,returnIntent);
                                             finish();
                                         } else if (url.contains("http://fail/")){
                                             Intent returnIntent = new Intent();
                                             returnIntent.putExtra(Constants.PAYMENT_ACTION_CANCEL,true);
                                             setResult(Activity.RESULT_CANCELED,returnIntent);
                                             finish();
                                         }
                                         view.loadUrl(url, extraHeaders);
                                         return true;
                                     }

                                    @TargetApi(Build.VERSION_CODES.N)
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                        String url=request.getUrl().toString();
                                        view.getSettings().setLoadWithOverviewMode(true);
                                        view.getSettings().setUseWideViewPort(true);
                                        Log.e("URL to load", url);

                                        if (url.contains("http://success/")) {
                                            Intent returnIntent = new Intent();
                                            setResult(Activity.RESULT_OK,returnIntent);
                                            finish();
                                        } else if (url.contains("http://fail/")){
                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtra(Constants.PAYMENT_ACTION_CANCEL,true);
                                            setResult(Activity.RESULT_CANCELED,returnIntent);
                                            finish();
                                        }
                                        view.loadUrl(url, extraHeaders);
                                        return true;
                                    }

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         dialog.show();
                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         dialog.dismiss();
                                         CardPaymentWebActivity.this.setTitle(view.getTitle());
                                     }


                                 }

        );


        setTitle(browser.getTitle());


        try {
            // load the url
            browser.loadUrl(loadUrl + getData, extraHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.PAYMENT_ACTION_CANCEL,false);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.slide_out_bottom);
    }

}
