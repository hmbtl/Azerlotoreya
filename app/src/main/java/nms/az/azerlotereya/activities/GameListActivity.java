package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.GameListAdapter;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.models.Draw;
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 6/13/15.
 */
public class GameListActivity extends AppCompatActivity {

    private List<Game> games = new LinkedList<>();
    private DrawerLayout drawerLayout;
    private GameListAdapter adapter;

    private Activity context;

    private ListView list;

    private TextView userName, userBalance, increaseBalance;
    private Button profileButton, ticketButton, rulesButton, introButton, exitButton, aboutButton, transactionsButton;

    private TextView languageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_secondary);


        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.games));

        languageButton = (TextView) toolbar.findViewById(R.id.language_button);
        languageButton.setOnClickListener(onClick);

        if (SharedData.getLang().equals("en")){
            languageButton.setText("RUS");
        } else {
            languageButton.setText("AZE");
        }


        initLayout();

        adapter = new GameListAdapter(this, R.layout.adapter_item_list_game, games);

        list.setAdapter(adapter);


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }



    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == profileButton) {
                Intent intent = new Intent(GameListActivity.this, ProfileActivity.class);
                startActivity(intent);

            } else if (view == ticketButton) {
                startActivity(new Intent(GameListActivity.this, MyTicketsActivity.class));

            } else if (view == rulesButton) {
                Intent intentRules = new Intent(GameListActivity.this, RulesActivity.class);
                startActivity(intentRules);

            } else if (view == aboutButton) {
                startActivity(new Intent(GameListActivity.this, InfoActivity.class));

            }else if (view == transactionsButton) {
                startActivity(new Intent(GameListActivity.this, TransactionsActivity.class));

            }else if (view == exitButton) {

                Intent intent = new Intent(GameListActivity.this, LoginActivity.class);
                intent.putExtra(Constants.KEY_IS_ANIMATED, false);
                startActivity(intent);
                SharedData.setLoggedIn(false);
                finish();
            } else if (view == introButton) {
                startActivity(new Intent(GameListActivity.this, IntroActivity.class).
                        putExtra(Constants.RUNNING_INTRO_FROM_WHERE, Constants.RUNNING_FROM_MENU));
            } else if (view == increaseBalance) {
                startActivity(new Intent((GameListActivity.this), AccountActivity.class));
            } else if (view == languageButton){
                if(SharedData.getLang().equals("en")){
                    languageButton.setText("AZE");
                    SharedData.setLang("ru");
                    Utilities.setLocale(GameListActivity.this, Utilities.LANGUAGE_RU);
                } else {
                    languageButton.setText("RUS");
                    SharedData.setLang("en");
                    Utilities.setLocale(GameListActivity.this, Utilities.LANGUAGE_EN);
                }

                overridePendingTransition(0, 0);
                Intent i = getIntent();
                startActivity(i);
                finish();

            }
        }
    };


    private void initLayout() {


        profileButton = (Button) findViewById(R.id.button_my_profile);
        ticketButton = (Button) findViewById(R.id.button_my_tickets);
        rulesButton = (Button) findViewById(R.id.button_rules);
        transactionsButton = (Button) findViewById(R.id.button_transactions);
        aboutButton = (Button) findViewById(R.id.button_about_us);
        exitButton = (Button) findViewById(R.id.button_exit);
        introButton = (Button) findViewById(R.id.button_intro);
        increaseBalance = (TextView) findViewById(R.id.button_increase_balance);


        userBalance = (TextView) findViewById(R.id.user_balance);
        userName = (TextView) findViewById(R.id.textview_user_name);


        profileButton.setOnClickListener(onClick);
        ticketButton.setOnClickListener(onClick);
        rulesButton.setOnClickListener(onClick);
        aboutButton.setOnClickListener(onClick);
        exitButton.setOnClickListener(onClick);
        introButton.setOnClickListener(onClick);
        increaseBalance.setOnClickListener(onClick);
        transactionsButton.setOnClickListener(onClick);


        list = (ListView) findViewById(R.id.game_list);


    }


    @Override
    protected void onResume() {
        super.onResume();

        games.clear();

        new BgRequestAsynctask(context, "GET", "games2", null, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }

            @Override
            public void onData(JSONObject json) throws JSONException {
                JSONArray gamesJson = json.getJSONArray("games");

                for (int i = 0; i < gamesJson.length(); i++) {
                    JSONObject gameJSON = gamesJson.getJSONObject(i);


                    int id = gameJSON.getInt("id");
                    String name = gameJSON.getString("name");
                    double cost = gameJSON.getDouble("cost");
                    boolean gameStatus = gameJSON.getBoolean("status");
                    boolean enabled = gameJSON.getBoolean("enabled");

                    if(enabled) {

                        JSONArray drawsJSON = gameJSON.getJSONArray("draws");

                        List<Draw> draws = new LinkedList<Draw>();
                        draws.clear();

                        for (int j = 0; j < drawsJSON.length(); j++) {
                            JSONObject drawJSON = drawsJSON.getJSONObject(j);

                            String number = drawJSON.getString("draw_number");
                            String info = drawJSON.getString("draw_info");
                            String date = drawJSON.getString("draw_date");
                            String time = drawJSON.getString("draw_time");
                            String winnings = drawJSON.getString("winnings");
                            boolean drawStatus = drawJSON.getBoolean("draw_status");

                            Resources res = getResources();

                            JSONObject remainingDateJSON = drawJSON.getJSONObject("draw_remaining_dates");
                            int remainingDays = remainingDateJSON.getInt("days");
                            int remainingHours = remainingDateJSON.getInt("hours");
                            int remainingMinutes = remainingDateJSON.getInt("minutes");
                            if(remainingDays != 0){
                                String dayString = res.getQuantityString(R.plurals.day, remainingDays, remainingDays);
                                info = res.getString(R.string.next_draw_info, dayString);
                            } else if(remainingHours != 0) {
                                String hourString = res.getQuantityString(R.plurals.hour, remainingHours + 1, remainingHours + 1);
                                info = res.getString(R.string.next_draw_info, hourString);
                            } else if(remainingMinutes != 0) {
                                String minuteString = res.getQuantityString(R.plurals.minute, remainingMinutes, remainingMinutes);
                                info = res.getString(R.string.next_draw_info, minuteString);
                            }

                            winnings = String.valueOf(winnings);

                            draws.add(new Draw(number, date, time, info, winnings, drawStatus));
                        }

/*
                    String drawNumber = gameJSON.getString("draw_number");
                    String drawDate = gameJSON.getString("draw_date_str");
                    String drawInfo = gameJSON.getString("draw_info");
                    boolean drawStatus = gameJSON.getInt("draw_status") == 1;
                    String winnings = gameJSON.getString("winnings");
*/


                        Game game = new Game(id, name, cost, gameStatus, enabled, draws);

                        game.setSelectedDraw(0);
                        games.add(game);
                    }
                }
            }
        }).execute();





        userName.setText(SharedData.getFullName());
        userBalance.setText(String.valueOf(SharedData.getBalance()));
    }

}


