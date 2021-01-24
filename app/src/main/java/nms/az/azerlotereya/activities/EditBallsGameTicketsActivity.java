package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 7/7/15.
 */
public class EditBallsGameTicketsActivity extends Activity {

    GridView numberSelectGrid;
    List<String> numbers;

    private int gameId,checkedItemCount,returningPosition;
    private int circleCount = 5;
    private NumberAdapter adapter;
    private Button okayButton;
    private boolean isEdit;
    private boolean hasError = false;

    TextView description;
    int[] startingNumbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loto_balls);


        // get current id of the game
        gameId = getIntent().getIntExtra(Constants.GAME_ID_KEY, -1);

        // if it is 6/40 game than set circle selection to 6
        if (gameId == Constants.GAME_ID_6_40)
            circleCount = 6;


        // Initialize all the views in the layout
        initLayout();

        // Populate numbers to tickets
        populateNumbers();


        // is it editing current one or creating new
        isEdit = getIntent().getBooleanExtra("edit", false);

        // Depending on the game id change the column count
        if (gameId == Constants.GAME_ID_6_40)
            numberSelectGrid.setNumColumns(7);
        else
            numberSelectGrid.setNumColumns(6);
        // Set choice mode to multiple to select more than one cell
        // in the grid view
        numberSelectGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        // Initialize adapter with view and numbers
        adapter = new NumberAdapter(this, R.layout.adapter_item_grid_number_selection, numbers);
        numberSelectGrid.setAdapter(adapter);

        // If it is editing the get the position of the ticket in list
        // Populate the ticket with numbers from editing ticket
        if (isEdit) {
            startingNumbers = getIntent().getIntArrayExtra("numbers_to_edit");
            returningPosition = getIntent().getIntExtra("position", 0);

            for (int startingNumber : startingNumbers) {
                numberSelectGrid.setItemChecked(startingNumber - 1, true);
            }

        }

        // Get the checked number count from gridView
        // to make sure it is not exceeded its limit
        checkedItemCount = numberSelectGrid.getCheckedItemCount();

        numberSelectGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkedItemCount < circleCount) {
                    // If the grid already checked then
                    // on the next click set false
                    if (numberSelectGrid.isItemChecked(i))
                        numberSelectGrid.setItemChecked(i, true);
                    else
                        numberSelectGrid.setItemChecked(i, false);
                } else
                    numberSelectGrid.setItemChecked(i, false);
                // Update checked cells count
                checkedItemCount = numberSelectGrid.getCheckedItemCount();
            }
        });


    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == okayButton) {
                if (checkedItemCount >= circleCount) {
                    SparseBooleanArray checkedItemPositions = numberSelectGrid.getCheckedItemPositions();

                    int[] checkedNumbers = new int[circleCount];
                    int count = 0;

                    for (int i = 0; i < checkedItemPositions.size(); i++) {
                        if (checkedItemPositions.valueAt(i)) {
                            checkedNumbers[count] = checkedItemPositions.keyAt(i) + 1;
                            count++;
                        }

                        if (count == circleCount)
                            break;
                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", checkedNumbers);

                    if (isEdit) {
                        returnIntent.putExtra("update", true);
                        returnIntent.putExtra("position", returningPosition);
                    }

                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    Utilities.showAlertDialog(EditBallsGameTicketsActivity.this,
                            R.string.wrong, R.string.error_ticket_ball_size, R.string.close);
                }


            }
        }
    };


    private void initLayout() {
        okayButton = (Button) findViewById(R.id.select_numbers_okay);
        okayButton.setOnClickListener(onClick);

        description = (TextView) findViewById(R.id.selection_description);

        String messageDescription = description.getText().toString();

        messageDescription = messageDescription.replace("xxx", "<font color='black'><b>xxx</b></font>")
                .replace("xxx", String.valueOf(circleCount));

        description.setText(Html.fromHtml(messageDescription));


        numberSelectGrid = (GridView) findViewById(R.id.number_grid);
    }


    private void populateNumbers() {
        numbers = new ArrayList<>();

        int value = (circleCount == 5) ? 36 : 40;
        for (int i = 1; i <= value; i++) {
            numbers.add(String.valueOf(i));
        }
    }


    private class NumberAdapter extends ArrayAdapter<String> {

        private Context context;
        private List<String> numbers;
        private int layoutResourceId;

        public NumberAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);

            this.context = context;
            this.layoutResourceId = resource;
            this.numbers = objects;
        }


        @Override
        public int getCount() {
            return numbers.size();
        }

        @Override
        public View getView(int position, View row, ViewGroup parent) {
            ItemHolder holder;

            if (row == null) {

                holder = new ItemHolder();

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layoutResourceId, parent, false);

                holder.number = (TextView) row.findViewById(R.id.number);

                row.setTag(holder);
            } else
                holder = (ItemHolder) row.getTag();

            holder.number.setText(numbers.get(position));

            return row;
        }

        @Override
        public long getItemId(int i) {
            return Integer.parseInt(numbers.get(i));
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public String getItem(int i) {
            return numbers.get(i);
        }

        class ItemHolder {
            TextView number;
        }
    }


}
