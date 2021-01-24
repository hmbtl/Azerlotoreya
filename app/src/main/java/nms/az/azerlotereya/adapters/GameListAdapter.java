package nms.az.azerlotereya.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.BallsGameActivity;
import nms.az.azerlotereya.activities.CheckTicketActivity;
import nms.az.azerlotereya.activities.FourPlusFourGameActivity;
import nms.az.azerlotereya.activities.ClassicGameActivity;
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.tools.Constants;

/**
 * Created by anar on 11/5/15.
 */

public class GameListAdapter extends ArrayAdapter<Game> {

    private Activity context;
    private int layoutResourceId;
    private List<Game> games;
    private boolean[] animationStates;

    public GameListAdapter(Activity context, int layoutResourceId, List<Game> games) {
        super(context, layoutResourceId, games);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.games = games;
        this.animationStates = new boolean[games.size()];
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        final ItemHolder holder;

        if (row == null) {

            holder = new ItemHolder();

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder.title = (TextView) row.findViewById(R.id.game_title);
            holder.price1 = (TextView) row.findViewById(R.id.total_winning);
            holder.price2 = (TextView) row.findViewById(R.id.total_winning_thousands);
            holder.currency = (TextView) row.findViewById(R.id.total_winning_currency);
            holder.drawInfo = (TextView) row.findViewById(R.id.new_game_date);
            holder.drawTime = (TextView) row.findViewById(R.id.new_game_date_clock);
            holder.playContainer = (RelativeLayout) row.findViewById(R.id.playnow_button_container);
            holder.checkWinners = (RelativeLayout) row.findViewById(R.id.check_winners_button);
            holder.drawsSpinner = (AppCompatSpinner) row.findViewById(R.id.draws_spinner);

            holder.titleContainer = (RelativeLayout) row.findViewById(R.id.game_title_container);

            holder.lastWinnerContainer = (RelativeLayout) row.findViewById(R.id.last_winner_container);


//                if (!animationStates[position]) {
//                    Log.e("TAG", "Animating item no: " + position);
//                    animationStates[position] = true;
//                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.listitemup);
//                    animation.setStartOffset(position * 500);
//                    row.startAnimation(animation);
//                }
            row.setTag(holder);
        } else
            holder = (ItemHolder) row.getTag();


        final Game game = games.get(position);

        int gameColor = context.getResources().getColor(R.color.binqo);

        String winnings = game.getDraws().get(game.getSelectedDraw()).getWinnings();

        int thousands = Integer.parseInt(winnings) / 1000;
        String winning = "";
        if (thousands > 0)
            winning = winnings.substring(winnings.length() - 3, winnings.length());

        Resources resources = context.getResources();

        if (game.getLastWinners() == null)
            holder.lastWinnerContainer.setVisibility(View.INVISIBLE);
        else {
            holder.lastWinnerContainer.setVisibility(View.VISIBLE);
        }

        switch (game.getGameId()) {
            case Constants.GAME_ID_BINQO:
                gameColor = ContextCompat.getColor(context,R.color.binqo);
                break;
            case Constants.GAME_ID_KLASSIK:
                gameColor = ContextCompat.getColor(context,R.color.klassik_lotto);
                break;
            case Constants.GAME_ID_FOUR_PLUS_FOUR:
                gameColor = ContextCompat.getColor(context, R.color.four_plus_four);
                break;
            case Constants.GAME_ID_SUPER:
                gameColor = ContextCompat.getColor(context,R.color.super_lotto);
                break;
            case Constants.GAME_ID_5_36:
                gameColor = ContextCompat.getColor(context,R.color.bes_otuzalti);
                break;
            case Constants.GAME_ID_6_40:
                gameColor = ContextCompat.getColor(context,R.color.alti_qirx);
                break;
        }


        holder.title.setText(game.getName());

        holder.price1.setText(String.valueOf(thousands));
        holder.price2.setText(winning);


        holder.titleContainer.setBackgroundColor(gameColor);
        holder.price1.setTextColor(gameColor);
        holder.price2.setTextColor(gameColor);
        holder.currency.setTextColor(gameColor);
        holder.drawInfo.setText(game.getDraws().get(game.getSelectedDraw()).getInfo());
        holder.drawTime.setText(game.getDraws().get(game.getSelectedDraw()).getTime());

        holder.checkWinners.setBackgroundColor(gameColor);

        holder.playContainer.setOnClickListener(new GameOnClickListener(row, game));
        holder.checkWinners.setOnClickListener(new GameOnClickListener(row, game));


        holder.drawsSpinner.setAdapter(new SpinnerDrawAdapter(context, game.getDraws()));
        holder.drawsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                game.setSelectedDraw(holder.drawsSpinner.getSelectedItemPosition());
                holder.drawInfo.setText(game.getDraws().get(game.getSelectedDraw()).getInfo());
                String winnings = game.getDraws().get(game.getSelectedDraw()).getWinnings();

                int thousands = Integer.parseInt(winnings) / 1000;
                String winning = "";
                if (thousands > 0)
                    winning = winnings.substring(winnings.length() - 3, winnings.length());

                holder.price1.setText(String.valueOf(thousands));
                holder.price2.setText(winning);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/*
        String[] draws = game.getDrawsList();

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, R.layout.spinner_draw_item, draws);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        holder.drawsSpinner.setAdapter(arrayAdapter);
        holder.drawsSpinner.setSelection(0);
        holder.drawsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                game.setSelectedDraw(holder.drawsSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
  */
        return row;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Game getItem(int i) {
        return games.get(i);
    }

    class ItemHolder {
        TextView title, price1, price2, currency, drawInfo,drawTime;
        RelativeLayout playContainer, checkWinners, titleContainer;
        RelativeLayout lastWinnerContainer;
        AppCompatSpinner drawsSpinner;
    }


    private class GameOnClickListener implements View.OnClickListener {

        private Game game;
        private View view;

        public GameOnClickListener(View view, Game game) {
            this.view = view;
            this.game = game;
        }


        @Override
        public void onClick(View v) {

            Gson gson = new Gson();

            switch (v.getId()) {
                case R.id.playnow_button_container:


                    int gameId = game.getGameId();

                    if (gameId == Constants.GAME_ID_5_36 || gameId == Constants.GAME_ID_6_40) {
                        Intent intent = new Intent(context, BallsGameActivity.class);
                        String gameJSON = gson.toJson(game);
                        intent.putExtra(Constants.GAME_KEY, gameJSON);
                        context.startActivity(intent);
                    } else if (gameId == Constants.GAME_ID_FOUR_PLUS_FOUR){
                        Intent intent = new Intent(context, FourPlusFourGameActivity.class);
                        String gameJSON = gson.toJson(game);
                        intent.putExtra(Constants.GAME_KEY, gameJSON);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ClassicGameActivity.class);
                        String gameJSON = gson.toJson(game);
                        intent.putExtra(Constants.GAME_KEY, gameJSON);
                        context.startActivity(intent);
                    }
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    break;
                case R.id.check_winners_button:
                    Intent intent = new Intent(context, CheckTicketActivity.class);
                    String gameJSON = gson.toJson(game);
                    intent.putExtra(Constants.GAME_KEY, gameJSON);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);

            }

        }
    }

}