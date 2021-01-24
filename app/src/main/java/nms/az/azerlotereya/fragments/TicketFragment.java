package nms.az.azerlotereya.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.adapters.MyTicketsRecyclerAdapter;
import nms.az.azerlotereya.asynctasks.BgRequestAsynctask;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.interfaces.RecyclerItemClickListener;
import nms.az.azerlotereya.models.Ticket;
import nms.az.azerlotereya.tools.Constants;

/**
 * Created by anar on 9/8/15.
 */
public class TicketFragment extends Fragment {

    public final static String STATUS_KEY = "POSITION_KEY";
    private Gson gson = new Gson();
    private int status = 1;
    private MyTicketsRecyclerAdapter recyclerAdapter;
    private List<Ticket> ticketList = new LinkedList<>();
    private Random rand = new Random();
    private String randomValue = "";

    public TicketFragment(){

    }

    public static TicketFragment createInstance(int status) {
        TicketFragment fragment = new TicketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(STATUS_KEY, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_tickets, container, false);

        randomValue = "--" + String.valueOf(rand.nextInt());


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new MyTicketsRecyclerAdapter(getActivity(), status, ticketList);
        recyclerView.setAdapter(recyclerAdapter);

        status = getArguments().getInt(STATUS_KEY);
        Log.e(randomValue, "Status: " + status );

        HashMap<String,String> params = new HashMap<>();
        params.put("status",String.valueOf(status));

        new BgRequestAsynctask(getActivity(), "GET", "tickets/", params, new JsonAPIListener() {
            @Override
            public void onNull() {

            }

            @Override
            public void onSuccess() {

                recyclerView.getAdapter().notifyDataSetChanged();
                Log.e(randomValue ,"onSuccess Values Updated");
                Log.e(randomValue ,"Count of list: " + ticketList.size());
                Log.e(randomValue, "Adapater Count : " + recyclerAdapter.getItemCount());
                Log.e(randomValue, "Adapater Count from getAdapter : " + recyclerView.getAdapter().getItemCount());

            }

            @Override
            public void onError() {

            }

            @Override
            public void onData(JSONObject json) throws JSONException {

                ticketList.clear();
                JSONArray ticketsJSON = json.getJSONArray("tickets");

                Log.e("TICKET",ticketsJSON.toString());

                for(int i = 0;i<ticketsJSON.length();i++){
                    JSONObject ticket = ticketsJSON.getJSONObject(i);

                    String ticketsNumber = ticket.getString("ticket_number");

                    String numbersStr = ticket.getString("numbers");
                    int[][] numbers = gson.fromJson(numbersStr, int[][].class);

                    int gameId = ticket.getInt("game_id");
                    String createdAt = ticket.getString("created_at");
                    String barcode = ticket.getString("barcode");
                    double cost = ticket.getDouble("cost");
                    String drawNumber = ticket.getString("draw_number");
                    double winning = 0;


                    ticketList.add(new Ticket(ticketsNumber,numbers,gameId,drawNumber,createdAt,barcode,cost,winning));
                }

            }
        }).execute();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Ticket ticket =  ticketList.get(position);

                        createTicketViewFromNumbers(ticket.getTicketNumber(), ticket.getGameId(), ticket.getNumbers());

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        return recyclerView;
    }


    private LinearLayout createClassicTicketView(int rowSize, int columnSize, int tableCount, int[][] numbers){
        float scale = getActivity().getResources().getDisplayMetrics().density;
        int generalPadding = (int) (5 * scale + 0.5f);
        int textPadding = (int) (5 * scale + 0.5f);
        int layoutPadding = (int) (2 * scale + 0.5f);
        int textPaddingBottomTop = (int) (5 * scale + 0.5f);

        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(getActivity());
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        generalContainer.setLayoutParams(generalParams);
        generalContainer.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);

        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {
            // secondary container to keep view with shadow background
            LinearLayout secondaryContainer = new LinearLayout(getActivity());
            secondaryContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams secondaryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondaryParams.setMargins(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
            secondaryContainer.setLayoutParams(secondaryParams);

            int numberIndex  = 0, limitIndex = 0;

            for (int row = 0; row < rowSize; row++) {
                // create row for each game
                LinearLayout rowContainer = new LinearLayout(getActivity());
                rowContainer.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams rowParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowContainer.setLayoutParams(rowParams);
                limitIndex = 0;

                for (int column = 0; column < columnSize; column++) {
                    // Create each column for  game
                    TextView columnTextView = new TextView(getActivity());
                    columnTextView.setBackgroundResource(R.drawable.rectangle_without_corner_not_filled);
                    LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    columnParams.weight = 1f;
                    columnTextView.setLayoutParams(columnParams);
                    columnTextView.setGravity(Gravity.CENTER);
                    columnTextView.setTextColor(Color.parseColor("#474747"));
                    columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    columnTextView.setMinEms(2);
                    columnTextView.setPadding(textPadding, textPaddingBottomTop, textPadding, textPaddingBottomTop);
                    columnTextView.setText(" ");


                    int[] currentRow = numbers[row + tableIndex * rowSize];

                    if(limitIndex < currentRow.length){
                        int currentColumn = Math.abs(currentRow[limitIndex]/10);
                        if(currentColumn == column){
                            columnTextView.setText(String.valueOf(currentRow[limitIndex]));
                            limitIndex++;
                        }
                    }

                    rowContainer.addView(columnTextView);
                }

                secondaryContainer.addView(rowContainer);
            }

            generalContainer.addView(secondaryContainer);
        }

        return generalContainer;
    }

    private LinearLayout createFourPlusFourView(int[][] numbers){
        float scale = getActivity().getResources().getDisplayMetrics().density;

        int rowSize = 5, columnSize = 4, tableCount = 2;
        int generalPadding = (int) (5 * scale + 0.5f);
        int textPadding = (int) (2 * scale + 0.5f);
        int layoutPadding = (int) (2 * scale + 0.5f);
        int textPaddingBottomTop = (int) (5 * scale + 0.5f);

        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(getActivity());
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        generalContainer.setLayoutParams(generalParams);
        generalContainer.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);

        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {

            // secondary container to keep view with shadow background
            LinearLayout secondaryContainer = new LinearLayout(getActivity());
//            secondaryContainer.setBackgroundResource(R.drawable.shadow_test);
            secondaryContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams secondaryParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondaryParams.weight = 1f;
            secondaryParams.setMargins(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
            secondaryContainer.setLayoutParams(secondaryParams);

            int numberIndex  = 0, limitIndex = 0;
            int[] currentTicket = numbers[tableIndex];

            // Sort current array
            Arrays.sort(currentTicket);

            for (int row = 0; row < rowSize; row++) {
                // create row for each game
                LinearLayout rowContainer = new LinearLayout(getActivity());
                rowContainer.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams rowParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowContainer.setLayoutParams(rowParams);

                for (int column = 0; column < columnSize; column++) {
                    // Create each column for  game
                    TextView columnTextView = new TextView(getActivity());
                    columnTextView.setBackgroundResource(R.drawable.rectangle_without_corner_not_filled);
                    LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    columnParams.weight = 1f;
                    columnTextView.setLayoutParams(columnParams);
                    columnTextView.setGravity(Gravity.CENTER);
                    columnTextView.setTextColor(Color.parseColor("#474747"));
                    columnTextView.setTypeface(null, Typeface.BOLD);
                    columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    columnTextView.setPadding(textPadding, textPaddingBottomTop, textPadding, textPaddingBottomTop);
                    columnTextView.setMinEms(2);
                    columnTextView.setText("  ");
                    // Increase numbers in the bpx
                    numberIndex ++;

                    if(limitIndex < 4){
                        if(numberIndex == currentTicket[limitIndex]){
                            limitIndex++;
                            columnTextView.setText(String.valueOf(numberIndex));
                        }
                    }

                    rowContainer.addView(columnTextView);
                }

                secondaryContainer.addView(rowContainer);
            }

            generalContainer.addView(secondaryContainer);
        }

        return generalContainer;
    }

    private LinearLayout createBallsTicketView(int[][] numbers){
        float scale = getActivity().getResources().getDisplayMetrics().density;

        int generalPadding = (int) (5 * scale + 0.5f);
        int textPadding = (int) (2 * scale + 0.5f);
        int layoutPadding = (int) (2 * scale + 0.5f);
        int textPaddingBottomTop = (int) (5 * scale + 0.5f);
        int circleSize = (int) (40 * scale + 0.5f);
        int scrollSize = (int) (250 * scale + 0.5f);


        if(numbers.length < 6){
            scrollSize = (int) (numbers.length * ((47 * scale) + 0.5f));
        }

        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(getActivity());
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        generalContainer.setLayoutParams(generalParams);


        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.setBackgroundColor(Color.WHITE);
        ViewGroup.LayoutParams scrollParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, scrollSize);
        scrollView.setLayoutParams(scrollParams);
        scrollView.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);


        // secondary container to keep view with shadow background
        LinearLayout secondaryContainer = new LinearLayout(getActivity());
        secondaryContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams secondaryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        secondaryParams.setMargins(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
        secondaryContainer.setLayoutParams(secondaryParams);


        for (int row = 0; row < numbers.length; row++) {
            // create row for each game
            LinearLayout rowContainer = new LinearLayout(getActivity());
            rowContainer.setOrientation(LinearLayout.HORIZONTAL);
            rowContainer.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams rowParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowContainer.setLayoutParams(rowParams);

            for (int column = 0; column < numbers[row].length; column++) {
                // Create each column for  game
                TextView columnTextView = new TextView(getActivity());
                columnTextView.setBackgroundResource(R.drawable.circle);
                LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(circleSize, circleSize);
                columnParams.setMargins( layoutPadding,layoutPadding,layoutPadding,layoutPadding);
                columnTextView.setLayoutParams(columnParams);
                columnTextView.setGravity(Gravity.CENTER);
                columnTextView.setTextColor(Color.parseColor("#474747"));
                columnTextView.setTypeface(null, Typeface.BOLD);
                columnTextView.setMinEms(2);
                columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                columnTextView.setText("");



                columnTextView.setText(String.valueOf(numbers[row][column]));


                rowContainer.addView(columnTextView);
            }



            secondaryContainer.addView(rowContainer);
        }
        scrollView.addView(secondaryContainer);
        generalContainer.addView(scrollView);
        return generalContainer;
    }


    private void createTicketViewFromNumbers(String ticketNumber, int gameId, int[][] numbers){

        LinearLayout ticketView = new LinearLayout(getActivity());

        if (gameId == Constants.GAME_ID_BINQO) {
            ticketView = createClassicTicketView(5, 6, 1, numbers);
        } else if (gameId == Constants.GAME_ID_SUPER) {
            ticketView = createClassicTicketView(3, 9, 3, numbers);
        } else if (gameId == Constants.GAME_ID_KLASSIK) {
            ticketView = createClassicTicketView(3, 9, 2, numbers);
        } else if(gameId == Constants.GAME_ID_5_36 || gameId == Constants.GAME_ID_6_40){
            ticketView = createBallsTicketView(numbers);
        } else if(gameId == Constants.GAME_ID_FOUR_PLUS_FOUR){
            ticketView = createFourPlusFourView(numbers);
        }


        String ticketTitle = getResources().getString(R.string.ticket) + " #" + ticketNumber;

        //return generalContainer;
        final AppCompatDialog dialog = new AppCompatDialog(getActivity());
        dialog.setContentView(R.layout.dialog_ticket);
        dialog.setTitle(ticketTitle);
        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_action_right_button);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        final LinearLayout dialogContainer = (LinearLayout) dialog.findViewById(R.id.dialog_container);
        dialogTitle.setText(ticketTitle);

        dialogContainer.addView(ticketView);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

}
