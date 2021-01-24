package nms.az.azerlotereya.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.CardPaymentWebActivity;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 3/17/16.
 */
public class IncreaseBalanceFragment extends Fragment {


    private ImageButton pmAzeriCard, pmEManat;


    private LinearLayout azericardContainer;
    private TextView lotocardSoon;

    private EditText amountEditText, numberEditText;

    private Button checkout;

    private static final char space = ' ';

    private Activity context;


    private LinearLayout otherMethodContainer;

    private static final int METHOD_AZERICARD = 0;
    private static final int METHOD_LOTOCARD = 3;

    static final int WEB_ACTIVITY_REQUEST = 1;

    private String amount;
    private String transactionId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NestedScrollView scrollView = (NestedScrollView) inflater.inflate(
                R.layout.fragment_increase_balance, container, false);

        context = getActivity();

        otherMethodContainer = (LinearLayout) scrollView.findViewById(R.id.other_method_container);

        pmAzeriCard = (ImageButton) scrollView.findViewById(R.id.payment_method_azericard);
        pmEManat = (ImageButton) scrollView.findViewById(R.id.payment_method_emanat);

        lotocardSoon = (TextView) scrollView.findViewById(R.id.other_method_soon);

        azericardContainer = (LinearLayout) scrollView.findViewById(R.id.azericard_container);
        amountEditText = (EditText) scrollView.findViewById(R.id.amount_edit_text);


        numberEditText = (EditText) scrollView.findViewById(R.id.lotocard_number);

        pmAzeriCard.setOnClickListener(onClick);
        pmEManat.setOnClickListener(onClick);


        checkout = (Button) scrollView.findViewById(R.id.checkout_button);
        checkout.setOnClickListener(onClick);

        selectPaymentMethod(METHOD_AZERICARD);

        return scrollView;
    }


    private void selectPaymentMethod(int method) {

        String pName = "";
        Resources resources = getResources();


        switch (method) {
            case METHOD_LOTOCARD:

                pName = "Lotokart";
                pmAzeriCard.setSelected(false);
                pmEManat.setSelected(true);
                pmEManat.setColorFilter(ContextCompat.getColor(context,R.color.payment_selected), PorterDuff.Mode.MULTIPLY);

                /*

                otherMethodContainer.setVisibility(View.VISIBLE);
                */
                lotocardSoon.setVisibility(View.VISIBLE);
                azericardContainer.setVisibility(View.GONE);
                checkout.setVisibility(View.GONE);

                break;


            case METHOD_AZERICARD:



                pName = "Bank of Baku";
                /*


                otherMethodContainer.setVisibility(View.GONE);
                */
                pmEManat.setSelected(false);
                pmEManat.clearColorFilter();
                pmAzeriCard.setSelected(true);


                azericardContainer.setVisibility(View.VISIBLE);
                lotocardSoon.setVisibility(View.GONE);
                checkout.setVisibility(View.VISIBLE);

                break;

        }

    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == pmAzeriCard) {
                selectPaymentMethod(METHOD_AZERICARD);


            } else if (view == pmEManat) {

                selectPaymentMethod(METHOD_LOTOCARD);

            } else if (view == checkout) {

                if (azericardContainer.getVisibility() == View.VISIBLE) {
                    if (!amountEditText.getText().toString().equals("")) {
                        Intent intent = new Intent(getActivity(), CardPaymentWebActivity.class);
                        intent.putExtra(Constants.PAYMENT_NAME_KEY, SharedData.getFullName());
                        intent.putExtra(Constants.PAYMENT_AMOUNT_KEY, amountEditText.getText().toString());
                        startActivityForResult(intent, WEB_ACTIVITY_REQUEST);
                        getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                    } else
                        Utilities.showMessageToast(context, R.string.payment_amount);
                } else if (otherMethodContainer.getVisibility() == View.VISIBLE) {
                    if (!numberEditText.getText().toString().equals("")) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("card_number", numberEditText.getText().toString());

                        new BgRequestAsynctask(context, "POST", "payments/lotocard", params, new JsonAPIListener() {
                            @Override
                            public void onData(JSONObject json) throws JSONException {

                                JSONObject paymentJson = json.getJSONObject("payment_lotocard");

                                amount = paymentJson.getString("amount");
                                transactionId = paymentJson.getString("transaction_id");

                            }

                            @Override
                            public void onError() {
                                Utilities.showAlertDialog(context, R.string.payment,
                                        R.string.error_lotocard,
                                        R.string.close, false);
                            }

                            @Override
                            public void onSuccess() {
                                Utilities.showAlertDialog(context, getResources().getString(R.string.payment),
                                        getResources().getString(R.string.success_lotocard).replace("xxx", amount).
                                                replace("yyy", transactionId),
                                        getResources().getString(R.string.close), true);
                                updateUserBalance();
                            }

                            @Override
                            public void onNull() {

                            }
                        }).execute();


                    } else
                        Utilities.showMessageToast(context, R.string.payment_amount);
                }
            }

        }
    };


    private void updateUserBalance() {
        new BgRequestAsynctask(context, "GET", "user/balance", null, new JsonAPIListener() {
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
                JSONObject userJSON = json.getJSONObject("user");
                double balance = userJSON.getDouble("balance");
                SharedData.setBalance(balance);
            }
        }).execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEB_ACTIVITY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Utilities.showAlertDialog(context, R.string.payment, R.string.payment_success, R.string.close, true);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                if (data.getBooleanExtra(Constants.PAYMENT_ACTION_CANCEL, false))
                    Utilities.showAlertDialog(context, R.string.payment, R.string.payment_fail, R.string.close, false);
            }
            updateUserBalance();
        }
    }
}
