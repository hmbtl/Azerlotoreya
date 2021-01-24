package nms.az.azerlotereya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.FourPlusFourGameAdapter;
import nms.az.azerlotereya.customviews.BackAwareEditText;
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 6/13/17.
 */

public class FourPlusFourGameActivity extends AppCompatActivity {


    ListView ticketListView;
    BackAwareEditText numberOfTicketsEditText;
    TextView fillButton, totalAmount;

    private int gameId;
    private Gson gson = new Gson();
    private ImageButton addTicket, removeTicket;
    private Game game;
    private FourPlusFourGameAdapter adapter;
    private List<int[][]> tickets;
    private Button buyButton;
    private LinearLayout addRemoveTicketContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_4_plus_4);

        // Init Toolbar
        initToolbar();

        // Init all view within container
        initLayouts();

        // Get game data from bundle with gson value
        game = gson.fromJson(getIntent().getStringExtra(Constants.GAME_KEY), Game.class);
        // Get the current selected gameId
        gameId = game.getGameId();

        // Set the title of the activity to game name
        setTitle(game.getName());

        // Create a ticket
        initTickets(1);

        adapter = new FourPlusFourGameAdapter(this,
                R.layout.adapter_item_lotto_game_list, tickets);

        ticketListView.setAdapter(adapter);
        fillButton.setOnClickListener(onClick);
        addTicket.setOnClickListener(onClick);
        removeTicket.setOnClickListener(onClick);



        numberOfTicketsEditText.setOnClickListener(onClick);
        numberOfTicketsEditText.setBackPressedListener(new BackAwareEditText.BackPressedListener() {
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




    private void changeStateOfDoneButton(boolean hasFocus) {
        if (hasFocus) {
            fillButton.setVisibility(View.VISIBLE);
            addRemoveTicketContainer.setVisibility(View.GONE);
        } else {
            fillButton.setVisibility(View.GONE);
            addRemoveTicketContainer.setVisibility(View.VISIBLE);
        }
    }


    private void initValues(){

        tickets.add(getRandomTicket());
        tickets.add(getRandomTicket());
        tickets.add(getRandomTicket());
        tickets.add(getRandomTicket());
        tickets.add(getRandomTicket());
        /*
        tickets.add(new int[][] {{1,2,3,4},{5,6,7,8}});
        tickets.add(new int[][] {{12,13,15,20},{1,5,6,2}});
        tickets.add(new int[][] {{10,2,13,4},{5,6,7,8}});
        tickets.add(new int[][] {{9,8,3,4},{1,16,17,18}});
        */

    }

    private void initTickets(int count) {


        boolean isAdding = count > tickets.size();

        int countToAdd = Math.abs(count - tickets.size());

        Log.e(" countToAdd ", "" + countToAdd);
        Log.e(" ticketSize ", "" + tickets.size());

        int ticketSize = tickets.size();

        for (int i = 0; i < countToAdd; i++) {

            if (isAdding)
                tickets.add(getRandomTicket());
            else
                tickets.remove(ticketSize - i - 1);
        }


        totalAmount.setText(getResources().getString(R.string.amount).replace("xxx", Utilities.round(tickets.size(), 2) + ""));


    }

    private void initLayouts() {

        tickets = new LinkedList<>();

        addRemoveTicketContainer = (LinearLayout) findViewById(R.id.container_add_ticket);
        addTicket = (ImageButton) findViewById(R.id.tickets_plus);
        removeTicket = (ImageButton) findViewById(R.id.tickets_minus);
        buyButton = (Button) findViewById(R.id.ticket_buy_button);
        ticketListView = (ListView) findViewById(R.id.tickets_list_view);
        fillButton = (TextView) findViewById(R.id.fill_tickets);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        numberOfTicketsEditText = (BackAwareEditText) findViewById(R.id.number_of_tickets_edit_text);

        numberOfTicketsEditText.clearFocus();
        buyButton.setOnClickListener(onClick);
        fillButton.setVisibility(View.GONE);
        addRemoveTicketContainer.setVisibility(View.VISIBLE);
    }

    public void updateList(int action, int index) {

        switch (action) {
            case Constants.ACTION_TICKET_ADD:
                tickets.add(getRandomTicket());
                break;
            case Constants.ACTION_TICKET_REMOVE:
                tickets.remove(index);
                break;
            case Constants.ACTION_TICKET_REGENERATE:
                tickets.set(index, getRandomTicket());
                break;
        }


        totalAmount.setText(getResources().getString(R.string.amount).replace("xxx", Utilities.round(tickets.size(), 2) + ""));
        numberOfTicketsEditText.setText(tickets.size() + "");
        adapter.notifyDataSetChanged();
    }


    private int[][] getRandomTicket(){
        Random random = new Random();

        ArrayList<Integer> list = new ArrayList<Integer>();

        // Create list with values from 1 to 20
        for(int i = 1; i <= 20;i++){
            list.add(i);
        }

        // Shuffle to get random values;
        Collections.shuffle(list);

        // LabelA int array
        int[] labelA = new int[4];
        // Get unique sequential random numbers
        for (int i = 0; i < 4; i ++){
            labelA[i] = list.get(i);
        }

        // Shuffle to get random values;
        Collections.shuffle(list);

        // LabelB int array
        int[] labelB = new int[4];
        // Get unique sequential random numbers labelB
        for (int i = 0; i < 4; i++){
            labelB[i] = list.get(i);
        }

        int[][] finalTicket  = new int[2][4];
        finalTicket[0] = labelA;
        finalTicket[1] = labelB;

        return finalTicket;
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


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (view == fillButton) {
                initTickets(Integer.parseInt(numberOfTicketsEditText.getText().toString()));
                adapter.notifyDataSetChanged();
            } else if (view == addTicket) {

                // Add new ticket
                updateList(Constants.ACTION_TICKET_ADD, 0);
            } else if (view == removeTicket) {
                if (tickets.size() > 1)
                    // remove the last ticket
                    updateList(Constants.ACTION_TICKET_REMOVE, tickets.size() - 1);
            } else if (view == numberOfTicketsEditText) {
                changeStateOfDoneButton(true);
            } else if (view == buyButton) {
                Intent intent = new Intent(FourPlusFourGameActivity.this, PaymentActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


                Gson gson = new Gson();

                int ticket[][] = gson.fromJson(data.getStringExtra(Constants.TICKET_KEY_UPDATE),int[][].class);
                int position = data.getIntExtra(Constants.TICKET_POSITION_KEY, 0);


                Log.e("POSITION","" + position);

                tickets.set(position, ticket);

                adapter.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
