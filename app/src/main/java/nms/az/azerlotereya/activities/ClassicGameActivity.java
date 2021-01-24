package nms.az.azerlotereya.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.LottoGameAdapter;
import nms.az.azerlotereya.customviews.BackAwareEditText;
import nms.az.azerlotereya.customviews.BackAwareEditText.BackPressedListener;
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/8/15.
 */
public class ClassicGameActivity extends AppCompatActivity {

    ListView ticketListView;


    BackAwareEditText numberOfTicketsEditText;
    TextView fillButton, totalAmount;


    private int gameId;

    private Game game;
    private LottoGameAdapter adapter;

    private List<int[][]> tickets;
    private Button buyButton;

    private LinearLayout addRemoveTicketContainer;

    private ImageButton addTicket, removeTicket;

    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_klassik_lotto);

        initToolbar();

        // Initialize all views in activity
        initLayouts();


        game = gson.fromJson(getIntent().getStringExtra(Constants.GAME_KEY), Game.class);
        // Get the current selected gameId
        gameId = game.getGameId();

        setTitle(game.getName());

        initTickets(1);

        adapter = new LottoGameAdapter(this,
                R.layout.adapter_item_lotto_game_list, tickets, gameId);

        ticketListView.setAdapter(adapter);
        fillButton.setOnClickListener(onClick);
        addTicket.setOnClickListener(onClick);
        removeTicket.setOnClickListener(onClick);


        numberOfTicketsEditText.setOnClickListener(onClick);
        numberOfTicketsEditText.setBackPressedListener(new BackPressedListener() {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                changeStateOfDoneButton(false);
                numberOfTicketsEditText.clearFocus();
                numberOfTicketsEditText.setText(String.valueOf(tickets.size()));

            }
        });

        numberOfTicketsEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    changeStateOfDoneButton(false);
                    numberOfTicketsEditText.clearFocus();
                    initTickets(Integer.parseInt(numberOfTicketsEditText.getText().toString()));
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void changeStateOfDoneButton(boolean hasFocus) {
        if (hasFocus) {
            fillButton.setVisibility(View.VISIBLE);
            addRemoveTicketContainer.setVisibility(View.GONE);
        } else {
            fillButton.setVisibility(View.GONE);
            addRemoveTicketContainer.setVisibility(View.VISIBLE);
        }
    }


    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (view == fillButton) {
                initTickets(Integer.parseInt(numberOfTicketsEditText.getText().toString()));
                adapter.notifyDataSetChanged();
            } else if (view == addTicket) {

                // Add new ticket
                update(Constants.ACTION_TICKET_ADD, 0);
            } else if (view == removeTicket) {

                if (tickets.size() > 0)
                    // remove the last ticket
                    update(Constants.ACTION_TICKET_REMOVE, tickets.size() - 1);
            } else if (view == numberOfTicketsEditText) {
                changeStateOfDoneButton(true);
            } else if (view == buyButton) {
                Intent intent = new Intent(ClassicGameActivity.this, PaymentActivity.class);
                intent.putExtra(Constants.KEY_TOTAL_AMOUNT, tickets.size());
                intent.putExtra(Constants.TICKET_KEY,Utilities.intArrayToJson(tickets,gameId));

                String gameJSON = gson.toJson(game);
                intent.putExtra(Constants.GAME_KEY, gameJSON);

                startActivity(intent);

                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
            }

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initLayouts() {


        tickets = new LinkedList<>();

        addRemoveTicketContainer = (LinearLayout) findViewById(R.id.container_add_ticket);

        addTicket = (ImageButton) findViewById(R.id.tickets_plus);
        removeTicket = (ImageButton) findViewById(R.id.tickets_minus);

        ticketListView = (ListView) findViewById(R.id.tickets_list_view);
        fillButton = (TextView) findViewById(R.id.fill_tickets);
        totalAmount = (TextView) findViewById(R.id.total_amount);

        numberOfTicketsEditText = (BackAwareEditText) findViewById(R.id.number_of_tickets_edit_text);
        numberOfTicketsEditText.clearFocus();

        buyButton = (Button) findViewById(R.id.ticket_buy_button);
        buyButton.setOnClickListener(onClick);


        fillButton.setVisibility(View.GONE);
        addRemoveTicketContainer.setVisibility(View.VISIBLE);
    }

    private void initTickets(int count) {


        boolean isAdding = count > tickets.size();

        int countToAdd = Math.abs(count - tickets.size());

        Log.e(" countToAdd ", "" + countToAdd);
        Log.e(" ticketSize ", "" + tickets.size());

        int ticketSize = tickets.size();


        for (int i = 0; i < countToAdd; i++) {

            Log.e("err", "ticketsize :" + tickets.size() + " | index : " + i);

            if (isAdding)
                tickets.add(Utilities.fillRandom(gameId));
            else
                tickets.remove(ticketSize - i - 1);
        }


        totalAmount.setText(getResources().getString(R.string.amount).replace("xxx", Utilities.round(tickets.size(), 2) + ""));


    }

    public void update(int action, int index) {


        switch (action) {
            case Constants.ACTION_TICKET_ADD:
                tickets.add(Utilities.fillRandom(gameId));
                break;
            case Constants.ACTION_TICKET_REMOVE:
                tickets.remove(index);
                break;
            case Constants.ACTION_TICKET_REGENERATE:
                tickets.set(index, Utilities.fillRandom(gameId));
                break;
        }


        totalAmount.setText(getResources().getString(R.string.amount).replace("xxx", Utilities.round(tickets.size(), 2) + ""));
        numberOfTicketsEditText.setText(tickets.size() + "");
        adapter.notifyDataSetChanged();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


                Gson gson = new Gson();

                int ticket[][] = gson.fromJson(data.getStringExtra(Constants.TICKET_KEY_UPDATE),int[][].class);
                int position = data.getIntExtra(Constants.TICKET_POSITION_KEY, 0);


                tickets.set(position - 1, ticket);

                adapter.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
