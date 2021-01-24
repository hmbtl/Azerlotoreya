package nms.az.azerlotereya.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.TransactionRecyclerAdapter;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.RecyclerItemClickListener;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.models.Transaction;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 9/8/15.
 */
public class TransactionFragment extends Fragment {

    public final static String POSITION_KEY = "POSITION_KEY";
    private String paymentType = "fail";

    private TransactionRecyclerAdapter recyclerAdapter;

    private List<Transaction> transactionList = new LinkedList<>();

    public static TransactionFragment createInstance(int position) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_KEY, position);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_transactions, container, false);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int position = getArguments().getInt(TransactionFragment.POSITION_KEY, 0);
            if(position == 0)
                paymentType = "fail";
            else
                paymentType = "success";


        HashMap<String,String> params = new HashMap<>();

        Log.e("Test Rresult","" + paymentType);

        recyclerAdapter = new TransactionRecyclerAdapter(getActivity(), transactionList);
        recyclerView.setAdapter(recyclerAdapter);

        params.put("payment_type",paymentType);

        new BgRequestAsynctask(getActivity(), "GET", "payments/", params, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {

                Log.e("Update adapter", "requested");

                recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onError() {

            }

            @Override
            public void onData(JSONObject json) throws JSONException {
                transactionList.clear();


                JSONArray transactionsJSON = json.getJSONArray("payments");

                for(int i = 0;i<transactionsJSON.length();i++){
                    JSONObject transactionJSON = transactionsJSON.getJSONObject(i);

                    int type = transactionJSON.getInt("transaction_type");
                    int method = transactionJSON.getInt("transaction_method");
                    double  amount = transactionJSON.getDouble("amount");
                    String  date = transactionJSON.getString("date");
                    String  orderNumber = transactionJSON.getString("order_number");
                    String  cardNumber = transactionJSON.getString("card_number");

                    transactionList.add(new Transaction(type,method,amount, orderNumber, cardNumber, date));
                }

            }
        }).execute();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                           Transaction transaction =  transactionList.get(position);
                           if(paymentType.equals("fail")){
                               if(transaction.getMethod() == 4){
                                   Utilities.showAlertDialog(getActivity(),R.string.info,
                                           R.string.transaction_error_card, R.string.close);
                               } else {
                                   Utilities.showAlertDialog(getActivity(),R.string.info,
                                           R.string.transaction_error_azerpost, R.string.close);
                               }
                           }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        return recyclerView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {

        transactionList.clear();




    }


}
