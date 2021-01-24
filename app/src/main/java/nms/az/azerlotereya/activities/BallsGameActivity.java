package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.NumberGeneratorAdapter;
import nms.az.azerlotereya.adapters.TemplatesAdapter;
import nms.az.azerlotereya.adapters.TemplatesAdapter.OnDataChangeListener;
import nms.az.azerlotereya.customviews.ProgressCustom;
import nms.az.azerlotereya.models.Game;
import nms.az.azerlotereya.models.Template;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.JSONParser;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 7/20/15.
 */
public class BallsGameActivity extends AppCompatActivity {


    private List<TextView> selectedNumbers;
    private ListView list;
    private TextView randomPick, selectNumbersButton, createTemplate, getTemplate;
    Drawable randomPickDrawable, selectionDrawable;
    private NumberGeneratorAdapter adapter;
    private Button buyButton;
    private TextView totalAmount;
    private Activity context;
    double amount;
    private Gson gson = new Gson();

    private Game game;


    private List<int[]> ballNumbers = new LinkedList<>();

    OnDataChangeListener mOnDataChangeListener;

    private int gameId;


    private TemplatesAdapter templatesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_5_36);



        context = this;

        // Initialise toolbar of activity
        initToolbar();

        game = gson.fromJson(getIntent().getStringExtra(Constants.GAME_KEY),Game.class);
        // Get the current selected gameId
        gameId = game.getGameId();

        // Set the title of activity using gameId
        setTitle(game.getName());


        initLayout();


    }


    private void pickrandomNumbers(int gameId) {

        int count = 0;
        int minOrder = 0;
        int size = 0;
        int max = 0;

        switch (gameId) {
            case Constants.GAME_ID_5_36:
                size = 5;
                max = 36;
                minOrder = 5;
                break;
            case Constants.GAME_ID_6_40:
                size = 6;
                max = 40;
                minOrder = 4;
                break;
        }

        if (ballNumbers.size() >= minOrder)
            count = 1;
        else
            count = minOrder - ballNumbers.size();

        for (int c = 0; c < count; c++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.clear();

            int[] generatedNumber = new int[size];

            for (int i = 1; i <= max; i++) {
                list.add(i);
            }

            Collections.shuffle(list);

            for (int i = 0; i < size; i++) {
                generatedNumber[i] = list.get(i);
            }

            ballNumbers.add(generatedNumber);
        }

    }


    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }


    /*
        Initialize Layout views TOOLBAR and BUTTONS
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set toolbar close button on the left of it
        toolbar.setNavigationIcon(R.drawable.ic_back);
        // Close the activity on close button press
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initLayout() {
        totalAmount = (TextView) findViewById(R.id.total_amount);
        totalAmount.setText(getResources().getString(R.string.amount).replace("xxx", "0.00"));

        createTemplate = (TextView) findViewById(R.id.create_template);
        createTemplate.setOnClickListener(onClick);

        getTemplate = (TextView) findViewById(R.id.select_from_template);
        getTemplate.setOnClickListener(onClick);

        list = (ListView) findViewById(R.id.selected_number_list);

        randomPick = (TextView) findViewById(R.id.game_pick_random_numbers);
        selectNumbersButton = (TextView) findViewById(R.id.select_numbers);

        randomPickDrawable = ContextCompat.getDrawable(this, R.drawable.ic_dice);

        randomPickDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff474747, PorterDuff.Mode.MULTIPLY));

        selectionDrawable = ContextCompat.getDrawable(this, R.drawable.ic_number_select);

        selectionDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff474747, PorterDuff.Mode.MULTIPLY));

        randomPick.setCompoundDrawablesWithIntrinsicBounds(randomPickDrawable, null, null, null);
        selectNumbersButton.setCompoundDrawablesWithIntrinsicBounds(selectionDrawable, null, null, null);


        randomPick.setOnClickListener(onClick);
        selectNumbersButton.setOnClickListener(onClick);
        buyButton = (Button) findViewById(R.id.ticket_buy_button);
        buyButton.setOnClickListener(onClick);


        adapter = new NumberGeneratorAdapter(this, R.layout.adapter_item_list_selected_balls,
                ballNumbers, gameId);


        list.setAdapter(adapter);
    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == randomPick) {
                pickrandomNumbers(gameId);
                adapter.notifyDataSetChanged();
                updatePrice();

            } else if (view == selectNumbersButton) {
                Intent intent = new Intent(BallsGameActivity.this, EditBallsGameTicketsActivity.class);
                intent.putExtra(Constants.GAME_ID_KEY, gameId);
                startActivityForResult(intent, 1);
            } else if (view == buyButton) {
                if (ballNumbers.size() < Utilities.getMinimumOrder(gameId))
                    Utilities.showAlertDialog(BallsGameActivity.this, getResources().getString(R.string.minimum_selection),
                            getResources().getString(R.string.minimum_selection_text).replace("xxx", String.valueOf(Utilities.getMinimumOrder(gameId))),
                            getResources().getString(R.string.close));
                else {
                    Intent intent = new Intent(BallsGameActivity.this, PaymentActivity.class);
                    intent.putExtra(Constants.KEY_TOTAL_AMOUNT, adapter.getCount());
                    intent.putExtra(Constants.TICKET_KEY,Utilities.intListToJson(ballNumbers));
                    String gameJSON = gson.toJson(game);
                    intent.putExtra(Constants.GAME_KEY, gameJSON);

                    startActivity(intent);
                    overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                }
            } else if (view == createTemplate) {
                if (ballNumbers.size() < 1)
                    Utilities.showAlertDialog(context, R.string.template_create, R.string.template_create_minimum_error, R.string.close);
                else
                    showActionDialog();
            } else if (view == getTemplate) {
                HashMap<String, String> params = new HashMap<>();
                params.put("game_id", String.valueOf(gameId));
                new LoadTemplateFromDatabase(params).execute();
            }
        }
    };

    public void updatePrice() {
        amount = 0.00;
        amount = adapter.getCount() * game.getCost();

        totalAmount.setText(getResources().getString(R.string.amount).replace("xxx", String.valueOf(Utilities.round(amount, 2))));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int[] result = data.getIntArrayExtra("result");
                if (data.getBooleanExtra("update", false))
                    ballNumbers.set(data.getIntExtra("position", 0), result);
                else
                    ballNumbers.add(result);
                updatePrice();
                adapter.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private void showActionDialog() {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_action_custom);
        dialog.setTitle(R.string.template_ticket);

        final Button dialogLeftButton = (Button) dialog.findViewById(R.id.dialog_action_left_button);
        final Button dialogRightButton = (Button) dialog.findViewById(R.id.dialog_action_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_action_instruction);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        final EditText dialogEditText = (EditText) dialog.findViewById(R.id.dialog_action_edit_text);


        dialogTitle.setText(R.string.template_ticket);

        dialogMessage.setText(R.string.template_create_msg);
        dialogLeftButton.setText(R.string.cancel);
        dialogRightButton.setText(R.string.save);
        dialogEditText.setHint(R.string.template_name);

        dialogLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogEditText.getText().toString().equals("")) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", dialogEditText.getText().toString());
                    params.put("game_id", String.valueOf(gameId));
                    params.put("numbers", Utilities.intListToJson(ballNumbers));
                    new SaveTemplateToDatabase(params).execute();
                    dialog.dismiss();
                } else
                    Utilities.showMessageToast(context, R.string.template_name_text);
            }
        });


        dialog.show();
    }


    // Method to show dialog messages to notify user some actions. We can set title, message, button
    // values here.
    private void showTemplatesDialog(final List<Template> templates) {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_templates);
        dialog.setTitle(R.string.template_tickets);

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_templates_right_button);
        final ListView dialogListView = (ListView) dialog.findViewById(R.id.dialog_list_templates);


        dialogButton.setText(R.string.close);
        templatesAdapter = new TemplatesAdapter(context, R.layout.adapter_item_list_template_2, templates);
        dialogListView.setAdapter(templatesAdapter);


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


        templatesAdapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged(int action, int index) {

                if (action == Constants.ACTION_TEMPLATE_UPDATE) {

                    List<int[]> numbers = Utilities.numbersToBallsNumber(templates.get(index).getNumbers());

                    ballNumbers.clear();
                    ballNumbers.addAll(numbers);

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else if (action == Constants.ACTION_TEMPLATE_REMOVE) {
                    new DeleteTemplateFromDatabase(templates.get(index).getId()).execute();

                    templates.remove(index);
                    templatesAdapter.notifyDataSetChanged();
                }

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.e("size :", ballNumbers.size() + "");
            }
        });


        dialog.show();
    }




    private class LoadTemplateFromDatabase extends AsyncTask<String, String, String> {

        private ProgressCustom pDialog;
        private HashMap<String, String> params;
        private String url;
        private String message;
        private List<Template> templates = new LinkedList<>();


        public LoadTemplateFromDatabase(HashMap<String, String> params) {
            this.params = params;
            this.url = Constants.BASE_URL + "templates";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressCustom(context);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            String status = null;

            try {

                Log.e("url", url);

                JSONObject json = jsonParser.sendGet(url, params);

                Log.e("json", json.toString());
                if (json == null) {
                    return null;
                }


                status = json.getString("status");


                if (status.equals("success")) {


                    JSONArray templatesJson = json.getJSONObject("data").getJSONArray("templates");

                    for (int i = 0; i < templatesJson.length(); i++) {
                        JSONObject templateObject = templatesJson.getJSONObject(i);

                        int id = templateObject.getInt("id");
                        String name = templateObject.getString("name");
                        String date = templateObject.getString("created_at");
                        String numbersJSON = templateObject.getString("numbers");

                        Type listType = new TypeToken<int[][]>() {
                        }.getType();

                        Gson gson = new Gson();
                        int[][] numbers = gson.fromJson(numbersJSON, listType);


                        templates.add(new Template(id, name, numbers, gameId, date));

                    }

                } else
                    message = json.getString("message");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(String status) {

            pDialog.dismiss();

            // If not connected to server
            if (status == null) {
                Utilities.showMessageToast(context, R.string.internet_connection_error);

            } else if (status.equals("error")) {
                Utilities.showMessageToast(context, message);
            } else {
                showTemplatesDialog(templates);
            }
        }
    }


    public class DeleteTemplateFromDatabase extends AsyncTask<String, String, String> {

        private ProgressCustom pDialog;
        private String url;
        private String message;


        public DeleteTemplateFromDatabase(int id) {
            this.url = Constants.BASE_URL + "templates/" + id;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressCustom(context);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            String status = null;

            try {

                Log.e("url", url);

                JSONObject json = jsonParser.sendDelete(url);

                if (json == null) {
                    return null;
                }

                status = json.getString("status");

                message = json.getString("message");

                Log.e("ss", status + " " + message);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(String status) {


            pDialog.dismiss();

            // If not connected to server
            if (status == null) {
                Utilities.showMessageToast(context, R.string.internet_connection_error);


            } else {
                if (status.equals("success")) {
                    Utilities.showMessageToast(context, R.string.template_delete_message);

                } else {
                    Utilities.showMessageToast(context, message);
                }
            }
        }
    }
    private class SaveTemplateToDatabase extends AsyncTask<String, String, String> {

        private ProgressCustom pDialog;
        private HashMap<String, String> params;
        private String url;
        private String message;


        public SaveTemplateToDatabase(HashMap<String, String> params) {
            this.params = params;
            this.url = Constants.BASE_URL + "templates";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressCustom(context);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            String status = null;

            try {


                JSONObject json = jsonParser.sendPost(url, params);
                Log.e("jsonToString", json.toString());


                if (json == null) {
                    return null;
                }

                status = json.getString("status");

                message = json.getString("message");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(String status) {

            pDialog.dismiss();

            // If not connected to server
            if (status == null) {
                Utilities.showMessageToast(context, R.string.internet_connection_error);


            } else {
                if (status.equals("success")) {

                    Utilities.showMessageToast(context, R.string.template_create_message);

                } else {
                    Utilities.showMessageToast(context, message);
                }
            }
        }
    }
}
