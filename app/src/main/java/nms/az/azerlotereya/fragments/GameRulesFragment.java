package nms.az.azerlotereya.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 3/14/16.
 */
public class GameRulesFragment extends Fragment {

    private MaterialSpinner gameNameList;
    private int gameId;
    private TextView gameRulesText;
    private List<String> gameList = new LinkedList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NestedScrollView scrollView = (NestedScrollView) inflater.inflate(
                R.layout.fragment_game_rules, container, false);

        gameNameList = (MaterialSpinner) scrollView.findViewById(R.id.game_name_edittext);
        gameRulesText = (TextView) scrollView.findViewById(R.id.game_rules_text);

        Log.e("Should populate","Nothing yet");
        populateSpinner();


        gameNameList.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                setGameRuleOnSelection(item);
            }
        });

        return scrollView;
    }


    private void setGameRuleOnSelection(String gameTitle){

        gameId = Utilities.getGameById(gameTitle);
        String gameRule = "";

        switch (gameId){
            case Constants.GAME_ID_5_36:
                gameRule = getResources().getString(R.string.game_rule_5_36);
                break;
            case Constants.GAME_ID_6_40:
                gameRule = getResources().getString(R.string.game_rule_6_40);
                break;
            case Constants.GAME_ID_BINQO:
                gameRule = getResources().getString(R.string.game_rule_binqo);
                break;
            case Constants.GAME_ID_KLASSIK:
                gameRule = getResources().getString(R.string.game_rule_klassik);
                break;
            case Constants.GAME_ID_SUPER:
                gameRule = getResources().getString(R.string.game_rule_super);
                break;
            case Constants.GAME_ID_FOUR_PLUS_FOUR:
                gameRule = getResources().getString(R.string.game_rule_four_plus_four);
                break;
        }
        gameRulesText.setText(Html.fromHtml(gameRule));
    }

    private void populateSpinner(){
        new BgRequestAsynctask(getActivity(), "GET", "games/lists", null, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {
                gameNameList.setItems(gameList);
                setGameRuleOnSelection(gameList.get(0));
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
                    boolean enabled = gameJSON.getBoolean("enabled");

                    if(enabled ){
                        gameList.add(name);
                    }
                }
            }
        }).execute();


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
