package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/30/15.
 */
public class PaymentActivity extends AppCompatActivity {

    TextView totalOrder;

    private double totalMoney;
    private String ticket;

    private TextView userBalance, customer, orderNumberTextView;

    private RelativeLayout orderContainer;
    private LinearLayout successContainer;

    private Activity context;

    private Button buyTickets;
    private TextView increaseBalance;

    private boolean orderProccessed = false;
    private Gson gson = new Gson();

    private Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initToolbar();
        initView();

        context = this;

        game = gson.fromJson(getIntent().getStringExtra(Constants.GAME_KEY), Game.class);

        // Get the current selected gameId
        double totalAmount = getIntent().getIntExtra(Constants.KEY_TOTAL_AMOUNT, 0);
        totalMoney = Utilities.round(totalAmount * game.getCost(), 2);

        ticket = getIntent().getStringExtra(Constants.TICKET_KEY);

        customer.setText(SharedData.getFullName());

        totalOrder.setText(Html.fromHtml(totalAmount + " x " + game.getName() + " " + getResources().getString(R.string.tickets_small) + "<br><b>" + totalMoney + " azn</b>"));

        new BgRequestAsynctask(context, "GET", "user/balance", null, balanceListener).execute();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setTitle(R.string.payment);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        userBalance.setText(SharedData.getBalance() + " AZN");
    }

    private void initView() {
        totalOrder = (TextView) findViewById(R.id.total);
        customer = (TextView) findViewById(R.id.customer_name);
        userBalance = (TextView) findViewById(R.id.user_balance);

        buyTickets = (Button) findViewById(R.id.buy_tickets);

        buyTickets.setOnClickListener(onClick);

        increaseBalance = (TextView) findViewById(R.id.button_increase_balance);
        increaseBalance.setOnClickListener(onClick);

        successContainer = (LinearLayout) findViewById(R.id.success_container);
        orderContainer = (RelativeLayout) findViewById(R.id.order_container);

        successContainer.setVisibility(View.GONE);
        orderContainer.setVisibility(View.VISIBLE);

        orderNumberTextView = (TextView) findViewById(R.id.order_number);


    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == buyTickets) {
                if (orderProccessed)
                    onBackPressed();
                else {

                    if (SharedData.getBalance() < totalMoney)
                        Utilities.showAlertDialog(context, R.string.balance, R.string.no_balance_message, R.string.close);
                    else {
                        HashMap<String, String> params = new HashMap<>();


                        double ticketPrice = 0.0;

                        if (game.getGameId() == Constants.GAME_ID_5_36 ||
                                game.getGameId() == Constants.GAME_ID_6_40)
                            ticketPrice = totalMoney;
                        else
                            ticketPrice = game.getCost();

                        params.put("ticket_price", String.valueOf(ticketPrice));
                        params.put("game_id", String.valueOf(game.getGameId()));
                        params.put("draw_number", game.getDraws().get(game.getSelectedDraw()).getNumber());
                        params.put("numbers", ticket);

                        Log.e("Params To Send",params.toString());



                        new BgRequestAsynctask(context, "POST", "orders", params, new JsonAPIListener() {
                            @Override
                            public void onNull() {

                            }

                            @Override
                            public void onSuccess() {

                                orderContainer.setVisibility(View.GONE);
                                successContainer.setVisibility(View.VISIBLE);
                                buyTickets.setText(R.string.close);
                                Utilities.showMessageToast(context, R.string.ticket_buy_message);
                                orderProccessed = true;

                                new BgRequestAsynctask(context, "GET", "user/balance", null, balanceListener).execute();
                            }

                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onData(JSONObject json) throws JSONException {
                                Log.e("Received",json.toString());

                                JSONObject orderJSON = json.getJSONObject("order");

                                String orderNumber = orderJSON.getString("order_number");
                                orderNumberTextView.setText(getResources().getString(R.string.order_number) +" :" + orderNumber);
                            }
                        }).execute();
                    }

                }

            } else if (view == increaseBalance) {
                startActivity(new Intent((PaymentActivity.this), AccountActivity.class));
            }

        }
    };





    JsonAPIListener balanceListener = new JsonAPIListener() {
        @Override
        public void onNull() {

        }


        @Override
        public void onData(JSONObject json) throws JSONException{
            JSONObject userJSON = json.getJSONObject("user");
            double balance = userJSON.getDouble("balance");
            SharedData.setBalance(balance);
        }

        @Override
        public void onSuccess() {
            userBalance.setText(String.valueOf(SharedData.getBalance()) + " AZN");
        }

        @Override
        public void onError() {

        }
    };

}
