package nms.az.azerlotereya.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.models.Bank;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 3/17/16.
 */
public class WithdrawBalanceFragment extends Fragment {


    private EditText cardNumber, amount, address;
    private Button widthdrawButton;
    private TextView generalInfo, generalCall,balanceWithdrawEdit;
    private MaterialSpinner bankSpinner;
    private ImageButton infoButton;

    private final int MY_PERMISSIONS_REQUEST_CALL = 12;

    private LinearLayout bankContainer,azerpoctContainer;

    private ImageButton pmBank, pmAzerpoct;


    private static final int METHOD_AZERPOCT = 0;
    private static final int METHOD_BANK = 3;
    private int selectedMethod = 0;

    private List<Bank> bankList = new LinkedList<>();

    private Intent callIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NestedScrollView scrollView = (NestedScrollView) inflater.inflate(
                R.layout.fragment_withdraw_balance, container, false);

        callIntent = new Intent(Intent.ACTION_CALL);

        // Load ImageButton to init Selection
        pmAzerpoct = (ImageButton) scrollView.findViewById(R.id.withdraw_method_azerpoct);
        pmBank = (ImageButton) scrollView.findViewById(R.id.withdraw_method_bank);

        // Set onClick Events
        pmAzerpoct.setOnClickListener(onClick);
        pmBank.setOnClickListener(onClick);

        // Load Containers
        azerpoctContainer = (LinearLayout) scrollView.findViewById(R.id.azerpoct_container);
        bankContainer = (LinearLayout) scrollView.findViewById(R.id.azericard_container);

        // Load General Views
        amount = (EditText) scrollView.findViewById(R.id.amount_edit_text);
        widthdrawButton = (Button) scrollView.findViewById(R.id.widthdraw_button);
        generalCall = (TextView) scrollView.findViewById(R.id.general_call);
        infoButton = (ImageButton) scrollView.findViewById(R.id.info_button);
        balanceWithdrawEdit = (TextView) scrollView.findViewById(R.id.balance_withdraw);
        balanceWithdrawEdit.setText(String.valueOf(SharedData.getBalanceWithdraw()));
        generalInfo = (TextView) scrollView.findViewById(R.id.general_rule);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            generalInfo.setText(Html.fromHtml(getResources().getString(R.string.general_rules_text),Html.FROM_HTML_MODE_LEGACY));
        } else {
            generalInfo.setText(Html.fromHtml(getResources().getString(R.string.general_rules_text)));
        }
        widthdrawButton.setOnClickListener(onClick);
        generalCall.setOnClickListener(onClick);
        infoButton.setOnClickListener(onClick);




        // Load Bank Container Views
        cardNumber = (EditText) scrollView.findViewById(R.id.card_number_edit_text);
        bankSpinner = (MaterialSpinner) scrollView.findViewById(R.id.bank_name_spinner);


        // Load Azerpoct Container Views
        address = (EditText) scrollView.findViewById(R.id.azerpoct_address);


        selectPaymentMethod(METHOD_AZERPOCT);

        new BgRequestAsynctask(getActivity(), "GET", "user/balance", null, balanceListener).execute();


        if(!SharedData.isDontShow()){
            showInfoDialog(false);
        }

        return scrollView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadBanks(){

        HashMap<String,String> params = new HashMap<>();
        new BgRequestAsynctask(getActivity(), "GET", "banks/", params, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {

                Log.e("Update adapter", "requested");
                bankSpinner.setItems(bankList);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onData(JSONObject json) throws JSONException {

                bankList.clear();

                JSONArray banksJSON = json.getJSONArray("banks");

                for(int i = 0;i<banksJSON.length();i++){
                    JSONObject bank = banksJSON.getJSONObject(i);

                    String name = bank.getString("bank_name");
                    int id  = bank.getInt("id");
                    bankList.add(new Bank(id, name));
                }

            }
        }).execute();
    }




    private void selectPaymentMethod(int method) {

        String pName = "";
        Resources resources = getResources();


        switch (method) {
            case METHOD_AZERPOCT:

                pmAzerpoct.setSelected(true);
                pmBank.setSelected(false);

                azerpoctContainer.setVisibility(View.VISIBLE);
                bankContainer.setVisibility(View.GONE);
                address.setText(SharedData.getUser().getAddress());

                selectedMethod = METHOD_AZERPOCT;

                break;


            case METHOD_BANK:

                pmAzerpoct.setSelected(false);
                pmBank.setSelected(true);


                azerpoctContainer.setVisibility(View.GONE);
                bankContainer.setVisibility(View.VISIBLE);

                loadBanks();

                selectedMethod = METHOD_BANK;

                break;

        }

    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == widthdrawButton) {
                if (checkAmount(amount)) {
                    // TODO: 3/17/16
                        HashMap<String, String> params = new HashMap<>();

                        int payment_method = 1;
                        String card = "";

                        if(selectedMethod == METHOD_BANK){
                            payment_method = 2;
                            card = cardNumber.getText().toString();
                            if(!checkCard(cardNumber))
                                return;

                            if(Double.parseDouble(amount.getText().toString()) < 20){

                                Utilities.showAlertDialog(getContext(),R.string.withdraw_balance,R.string.balance_withdraw_message, R.string.close);
                                return;
                            }

                        } else {
                            payment_method = 1;
                            card = "";
                        }

                        // Show error message if the amount is higher than withdraw balance
                        if(SharedData.getBalanceWithdraw() < Double.parseDouble(amount.getText().toString())
                                || SharedData.getBalanceWithdraw() == 0.0 ) {
                            Utilities.showAlertDialog(getContext(),R.string.withdraw_balance,R.string.withdraw_error_message, R.string.close);
                            return;
                        }


                        Log.e("BalanceWithdraw",String.valueOf(SharedData.getBalanceWithdraw()));
                        Log.e("Amount",String.valueOf( Double.parseDouble(amount.getText().toString())));

                        params.put("payment_method", String.valueOf(payment_method));
                        params.put("amount", String.valueOf(amount.getText().toString()));
                        params.put("card_number", card);
                        params.put("bank_id",String.valueOf(bankList.get(bankSpinner.getSelectedIndex()).getId()));


                        Log.e("BankParams",params.toString());


                        new BgRequestAsynctask(getActivity(), "POST", "pay/prize", params, new JsonAPIListener() {
                            @Override
                            public void onNull() {

                            }

                            @Override
                            public void onSuccess() {
                                Utilities.showAlertDialog(getActivity(),R.string.success,R.string.withdraw_success_message ,R.string.close);
                                new BgRequestAsynctask(getActivity(), "GET", "user/balance", null, balanceListener).execute();
                            }

                            @Override
                            public void onError() {
                               // Utilities.showAlertDialog(getActivity(),"Xəta" ,"Serverlə əlaqə saxlanıla bilmədi" ,"Bağla");
                            }

                            @Override
                            public void onData(JSONObject json) throws JSONException {
                            }
                        }).execute();

                }
            } else if (v == generalCall) {
                callIntent.setData(Uri.parse("tel:0124903484"));

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {


                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    startActivity(callIntent);
                }
            } else if (v == pmAzerpoct){
                selectPaymentMethod(METHOD_AZERPOCT);
            } else if (v == pmBank){
                selectPaymentMethod(METHOD_BANK);
            } else if (v == infoButton){
                showInfoDialog(true);
            }
        }
    };


    private void showInfoDialog(boolean isDontShow) {
        final AppCompatDialog dialog = new AppCompatDialog(getActivity());
        dialog.setContentView(R.layout.dialog_info);
        dialog.setTitle(R.string.info);

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_alert_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_alert_message);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        final AppCompatCheckBox dialogCheckBox = (AppCompatCheckBox) dialog.findViewById(R.id.dialog_alert_checkbox);

        if(SharedData.isDontShow()){
            dialogCheckBox.setVisibility(View.GONE);
        }

        if(isDontShow){
            dialogCheckBox.setVisibility(View.GONE);
        }

        dialogMessage.setText(R.string.withdraw_information);
        dialogButton.setText(R.string.close);
        dialogTitle.setText(R.string.info);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogCheckBox.isChecked()){
                    SharedData.setDontShow(true);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private boolean checkAmount(EditText amountEdit) {

        if (amountEdit.getText().toString().equals("")) {
            Utilities.showMessageToast(getActivity(), R.string.payment_amount);
            return false;
        }


        double amount = Double.parseDouble(amountEdit.getText().toString());

        if (amount >= 2000) {
            Utilities.showMessageToast(getActivity(), R.string.call_to_loto);
            return false;
        }

        return true;
    }


    private boolean checkCard(EditText cardEdit) {
        if (cardEdit.getText().toString().equals("")) {
            Utilities.showMessageToast(getActivity(), R.string.insert_card);

            return false;

        }

        return true;
    }

    JsonAPIListener balanceListener = new JsonAPIListener() {
        @Override
        public void onNull() {

        }


        @Override
        public void onData(JSONObject json) throws JSONException{
            JSONObject userJSON = json.getJSONObject("user");
            double balance = userJSON.getDouble("balance");
            double balanceWithdraw = userJSON.getDouble("balance_withdraw");
            SharedData.setBalance(balance);
            SharedData.setBalanceWithdraw(balanceWithdraw);
        }

        @Override
        public void onSuccess() {
            balanceWithdrawEdit.setText(String.valueOf(SharedData.getBalanceWithdraw()));
        }

        @Override
        public void onError() {

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
