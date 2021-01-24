package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

import static nms.az.azerlotereya.R.id.number;

/**
 * Created by anar on 6/13/17.
 */

public class EditFourPlusFourGameActivity extends Activity {

    GridView tableAGridView, tableBGridView;
    List<String> numbers = new ArrayList<>();
    NumberAdapter tableANumberAdapter, tableBNumberAdapter;
    boolean isEdit = false;
    int[][] numbersToEdit;
    int returningPosition = 0;
    private Button saveButton;
    private Gson gson = new Gson();


    int tableACheckedCount, tableBCheckedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_4_plus_4);

        tableAGridView = (GridView) findViewById(R.id.table_a_number_grid);
        tableBGridView = (GridView) findViewById(R.id.table_b_number_grid);
        saveButton = (Button) findViewById(R.id.select_numbers_okay);
        saveButton.setOnClickListener(onClick);

        for(int i = 1; i <= 20; i++){
            numbers.add(String.valueOf(i));
        }

        tableAGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        tableBGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        tableANumberAdapter = new NumberAdapter(this, R.layout.adapter_item_grid_number_selection, numbers);
        tableBNumberAdapter = new NumberAdapter(this, R.layout.adapter_item_grid_number_selection, numbers);

        tableAGridView.setAdapter(tableANumberAdapter);
        tableBGridView.setAdapter(tableBNumberAdapter);


        // is it editing current one or creating new
        isEdit = getIntent().getBooleanExtra(Constants.TICKET_IS_EDIT, false);

        if (isEdit) {
            numbersToEdit = gson.fromJson(getIntent().getStringExtra(Constants.TICKET_KEY), int[][].class);
            returningPosition = getIntent().getIntExtra(Constants.TICKET_POSITION_KEY, 0);

            Log.e("POSITION ON EDIT","" + returningPosition);

            for (int index : numbersToEdit[0]) {
                tableAGridView.setItemChecked(index - 1, true);
            }

            for (int index : numbersToEdit[1]) {
                tableBGridView.setItemChecked(index - 1, true);
            }

        }


        tableAGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (tableAGridView.getCheckedItemCount() <= 4) {
                    if (tableAGridView.isItemChecked(i))
                        tableAGridView.setItemChecked(i, true);
                    else
                        tableAGridView.setItemChecked(i, false);
                } else
                    tableAGridView.setItemChecked(i, false);

                tableACheckedCount = tableAGridView.getCheckedItemCount();
            }
        });


        tableBGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (tableBGridView.getCheckedItemCount() <= 4) {
                    if (tableBGridView.isItemChecked(i))
                        tableBGridView.setItemChecked(i, true);
                    else
                        tableBGridView.setItemChecked(i, false);
                } else
                    tableBGridView.setItemChecked(i, false);

                tableBCheckedCount = tableBGridView.getCheckedItemCount();
            }
        });

    }



    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view == saveButton){
                if(tableAGridView.getCheckedItemCount() >= 4 && tableBGridView.getCheckedItemCount() >= 4){

                    SparseBooleanArray tableAPositions = tableAGridView.getCheckedItemPositions();

                    int[] tableACheckedNumbers = new int[4];
                    int count = 0;

                    for (int i = 0; i < tableAPositions.size(); i++) {
                        if (tableAPositions.valueAt(i)) {
                            tableACheckedNumbers[count] = tableAPositions.keyAt(i) + 1;
                            count++;
                        }

                        if (count == 4)
                            break;
                    }

                    SparseBooleanArray tableBPositions = tableBGridView.getCheckedItemPositions();

                    int[] tableBCheckedNumbers = new int[4];
                    count = 0;

                    for (int i = 0; i < tableBPositions.size(); i++) {
                        if (tableBPositions.valueAt(i)) {
                            tableBCheckedNumbers[count] = tableBPositions.keyAt(i) + 1;
                            count++;
                        }

                        if (count == 4)
                            break;
                    }


                    Intent returnIntent = new Intent();

                    numbersToEdit[0] = tableACheckedNumbers;
                    numbersToEdit[1] = tableBCheckedNumbers;
                    returnIntent.putExtra(Constants.TICKET_KEY_UPDATE, gson.toJson(numbersToEdit));

                    if (isEdit) {
                        returnIntent.putExtra(Constants.TICKET_IS_UPDATE, true);
                        returnIntent.putExtra(Constants.TICKET_POSITION_KEY, returningPosition);
                    }

                    setResult(RESULT_OK, returnIntent);
                    finish();


                } else {
                    Utilities.showAlertDialog(EditFourPlusFourGameActivity.this,
                            R.string.wrong, R.string.error_ticket_ball_size, R.string.close);
                }
            }

        }
    };


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

                holder.number = (TextView) row.findViewById(number);

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
