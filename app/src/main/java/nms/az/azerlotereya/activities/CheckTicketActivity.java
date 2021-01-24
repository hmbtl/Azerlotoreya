package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
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
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by User on 31.12.2015.
 */
public class CheckTicketActivity extends AppCompatActivity {

    private CardView ticketInfoContainer ;
    private Button checkTicketButton;
    private Activity context;

    private EditText drawNumberEdit, ticketNumberEdit;
    private TextView gameNameBar, infoMessage;
    private List<String> gameList = new LinkedList<>();


    private MaterialSpinner gameNameList;

    private int gameId;
    private Gson gson = new Gson();
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_tickets);

        context = this;

        if (getIntent().hasExtra(Constants.GAME_KEY)) {
            game = gson.fromJson(getIntent().getStringExtra(Constants.GAME_KEY), Game.class);
            gameId = game.getGameId();
        } else {
            gameId = Constants.GAME_ID_5_36;
        }

        initToolbar();
        initViews();
    }

    private void initViews(){
        ticketInfoContainer = (CardView) findViewById(R.id.ticket_info);
        ticketInfoContainer.setVisibility(View.GONE);

        checkTicketButton = (Button) findViewById(R.id.check_ticket_button);
        checkTicketButton.setOnClickListener(onClick);

        gameNameList = (MaterialSpinner) findViewById(R.id.game_name_edittext);
        drawNumberEdit = (EditText) findViewById(R.id.draw_number_edit);
        ticketNumberEdit = (EditText) findViewById(R.id.ticket_number_edit);

        gameNameBar = (TextView) findViewById(R.id.info_game_name);
        infoMessage = (TextView) findViewById(R.id.ticket_message);

        populateSpinner();


        gameNameList.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                gameId = Utilities.getGameById(item);
            }
        });

        gameNameBar.setText(Utilities.getTitleGameById(gameId));

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == checkTicketButton){

                if (checkFields()){

                        HashMap<String,String> params = new HashMap<>();
                        params.put("game_id",String.valueOf(gameId));
                        params.put("draw_number",drawNumberEdit.getText().toString());
                        params.put("ticket_number", ticketNumberEdit.getText().toString());
                    Log.e("CheckTicket",params.toString());
                        new BgRequestAsynctask(context, "POST", "tickets/check", params, new JsonAPIListener() {
                            @Override
                            public void onNull() {

                            }

                            @Override
                            public void onSuccess() {
                                ticketInfoContainer.setVisibility(View.VISIBLE);
                                gameNameBar.setText(Utilities.getTitleGameById(gameId));

                                int gameColor = Utilities.getGameColorById(context, gameId);
                                gameNameBar.setBackgroundColor(gameColor);
                            }

                            @Override
                            public void onError() {

                                ticketInfoContainer.setVisibility(View.VISIBLE);
                                gameNameBar.setText(Utilities.getTitleGameById(gameId));

                                int gameColor = Utilities.getGameColorById(context, gameId);
                                gameNameBar.setBackgroundColor(gameColor);

                                infoMessage.setText(R.string.no_tickets_found_message);
                            }

                            @Override
                            public void onData(JSONObject json) throws JSONException {
                                JSONObject ticket_winning = json.getJSONObject("ticket_winning");
                                String winAmount = ticket_winning.getString("win_amount");
                                infoMessage.setText(getResources().getString(R.string.winning_prize,winAmount));

                            }
                        }).execute();

                }

            }
        }
    };


    private boolean checkFields(){
         if (ticketNumberEdit.getText().toString().equals("")){
            Utilities.showMessageToast(context, R.string.fill_ticket_number_message);
            return false;
        } else if (drawNumberEdit.getText().toString().equals("")){
            Utilities.showMessageToast(context, R.string.fill_draw_number_message);
            return false;
        }

        return true;
    }

    private void populateSpinner(){
        new BgRequestAsynctask(this, "GET", "games/lists", null, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {
                gameNameList.setItems(gameList);
                Log.e("Position",gameList.indexOf(Utilities.getTitleGameById(gameId)) + "");
                Log.e("GAMEID",gameId + "");
                gameNameList.setSelectedIndex(gameList.indexOf(Utilities.getTitleGameById(gameId)));
            }

            @Override
            public void onError() {

            }

            @Override
            public void onData(JSONObject json) throws JSONException {
                JSONArray gamesJson = json.getJSONArray("games");

                Log.e("Games",gamesJson.toString());

                for (int i = 0; i < gamesJson.length(); i++) {
                    JSONObject gameJSON = gamesJson.getJSONObject(i);

                    int id = gameJSON.getInt("id");
                    //String name = gameJSON.getString("name");
                    String name = Utilities.getTitleGameById(id);
                    boolean enabled = gameJSON.getBoolean("enabled");

                    if(enabled ){
                        gameList.add(name);
                    }
                }
            }
        }).execute();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.slide_out_bottom);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setTitle(R.string.check_ticket);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
