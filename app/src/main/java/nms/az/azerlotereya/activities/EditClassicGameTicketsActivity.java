package nms.az.azerlotereya.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.customviews.ProgressCustom;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/20/15.
 */
public class EditClassicGameTicketsActivity extends AppCompatActivity {


    private static final int BASE_TEXTVIEW_ID = 1000;
    private int[][] ticket;
    private int tableCount, rowSize, columnSize, ticketPosition;
    TextView randomPick, clearCells;
    Drawable randomPickDrawable, clearCellsDrawable;
    int gameId;
    private Button acceptButton;
    private EditText[][] cells;
    private LinearLayout[] rowContainers;
    private boolean isGenerating = false;
    private List<EditText> errorCells = new LinkedList<>();
    private List<LinearLayout> errorRows = new LinkedList<>();
    private EditText[][] errCell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_edit);


        // Get the ticket from intent parsed with GSON
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra(Constants.TICKET_KEY);
        ticket = gson.fromJson(strObj, int[][].class);

        // Initialize views of activity
        initViews();
    }


    private void initViews() {
        tableCount = getIntent().getIntExtra(Constants.TICKET_TABLE_COUNT_KEY, 1);
        rowSize = getIntent().getIntExtra(Constants.TICKET_ROW_SIZE_KEY, 3);
        columnSize = getIntent().getIntExtra(Constants.TICKET_COLUMN_SIZE_KEY, 9);
        ticketPosition = getIntent().getIntExtra(Constants.TICKET_POSITION_KEY, 1);
        gameId = getIntent().getIntExtra(Constants.GAME_ID_KEY, 0);


        randomPick = (TextView) findViewById(R.id.button_regenerate_ticket_cells);
        clearCells = (TextView) findViewById(R.id.button_clear_ticket_cells);

        randomPickDrawable = ContextCompat.getDrawable(this, R.drawable.ic_dice);

        randomPickDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff474747, PorterDuff.Mode.MULTIPLY));

        clearCellsDrawable = ContextCompat.getDrawable(this, R.drawable.ic_erase);

        clearCellsDrawable.setColorFilter(new
                PorterDuffColorFilter(0xff474747, PorterDuff.Mode.MULTIPLY));

        randomPick.setCompoundDrawablesWithIntrinsicBounds(randomPickDrawable, null, null, null);
        clearCells.setCompoundDrawablesWithIntrinsicBounds(clearCellsDrawable, null, null, null);

        acceptButton = (Button) findViewById(R.id.select_numbers_okay);
        acceptButton.setOnClickListener(onClick);

        CardView cardView = (CardView) findViewById(R.id.card_view);
        TextView title = (TextView) findViewById(R.id.ticket_title);

        title.setText(title.getText().toString().replace("xxx", String.valueOf(ticketPosition)));


        // add created view to cardview
        cardView.addView(createLottoView());


        randomPick.setOnClickListener(onClick);
        clearCells.setOnClickListener(onClick);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == randomPick) {
                new UpdateCells(EditClassicGameTicketsActivity.this, Constants.ACTION_TICKET_REGENERATE).execute();

            } else if (view == clearCells) {
                new UpdateCells(EditClassicGameTicketsActivity.this, Constants.ACTION_TICKET_CLEAR).execute();
            } else if (view == acceptButton) {

                checkFull();

                if (checkTicketErrors())
                    Utilities.showAlertDialog(EditClassicGameTicketsActivity.this,
                            R.string.wrong, R.string.error_in_ticket, R.string.close);

                else {
                    Intent returnIntent = new Intent();

                    Gson gson = new Gson();

                    returnIntent.putExtra(Constants.TICKET_KEY_UPDATE,gson.toJson(getTicket()));

                    returnIntent.putExtra(Constants.TICKET_POSITION_KEY, ticketPosition);

                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }

        }
    };


    // Create loto view based on gameId and numbers
    private LinearLayout createLottoView() {

        float scale = getResources().getDisplayMetrics().density;
        int generalPadding = (int) (10 * scale + 0.5f);


        int textPadding = (int) (5 * scale + 0.5f);

        int textPaddingBottomTop;

        if (gameId == Constants.GAME_ID_BINQO)
            textPaddingBottomTop = (int) (10 * scale + 0.5f);
        else
            textPaddingBottomTop = (int) (5 * scale + 0.5f);

        cells = new EditText[rowSize * tableCount][columnSize];
        errCell = new EditText[rowSize * tableCount][columnSize];

        rowContainers = new LinearLayout[rowSize * tableCount];


        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(this);
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        generalContainer.setLayoutParams(generalParams);
        generalContainer.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);

        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {


            // secondary container to keep view with shadow background
            LinearLayout secondaryContainer = new LinearLayout(this);
//            secondaryContainer.setBackgroundResource(R.drawable.shadow_test);
            secondaryContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams secondaryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondaryParams.setMargins(textPadding, textPadding, textPadding, textPadding);
            secondaryContainer.setLayoutParams(secondaryParams);


            for (int row = 0; row < rowSize; row++) {

                int newRow = row + rowSize * tableIndex;


                // create row for each game
                LinearLayout rowContainer = new LinearLayout(this);
                rowContainer.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams rowParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowContainer.setLayoutParams(rowParams);


                for (int column = 0; column < columnSize; column++) {
                    // Create each column for  game
                    EditText editTextCell = new EditText(this);
                    editTextCell.setBackgroundResource(R.drawable.ticket_cell);
                    LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    columnParams.weight = 1f;
                    editTextCell.setLayoutParams(columnParams);
                    editTextCell.setGravity(Gravity.CENTER);
                    editTextCell.setTextColor(Color.parseColor("#474747"));
                    editTextCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    editTextCell.setPadding(textPadding, textPaddingBottomTop, textPadding, textPaddingBottomTop);
                    editTextCell.setSingleLine();
                    editTextCell.setMaxEms(2);
                    editTextCell.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    int maxLength = 2;

                    if (column == 0)
                        maxLength = 1;


                    // set max filter to each column
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);

                    editTextCell.setFilters(fArray);


                    if (ticket[row + tableIndex * rowSize][column] == 0)
                        editTextCell.setText("");
                    else
                        editTextCell
                                .setText(String.valueOf(ticket[row + tableIndex * rowSize][column]));


                    int generatedId = BASE_TEXTVIEW_ID + column + (newRow * columnSize);


                    editTextCell.setId(generatedId);
                    editTextCell.addTextChangedListener(new TextListener(newRow, column));

                    cells[newRow][column] = editTextCell;

                    rowContainer.addView(editTextCell);
                }

                rowContainers[newRow] = rowContainer;

                secondaryContainer.addView(rowContainer);
            }

            generalContainer.addView(secondaryContainer);
        }

        return generalContainer;
    }


    private int[][] getTicket() {

        int newRow = rowSize * tableCount;

        int tempTicket[][] = new int[newRow][columnSize];


        for (int row = 0; row < newRow; row++) {
            for (int column = 0; column < columnSize; column++) {


                if (cells[row][column].getText().toString().equals(""))
                    tempTicket[row][column] = 0;
                else
                    tempTicket[row][column] = Integer.parseInt(cells[row][column].getText().toString());

            }
        }

        return tempTicket;
    }



    // Check the ticket has any errors
    private boolean checkTicketErrors() {
        Log.e("errors:", errorRows.size() + " , " + errorCells.size());
        return errorRows.size() != 0 || errorCells.size() != 0;
    }


    // Check if the ticket exceeded its limit
    private void checkFull() {

        int row = rowSize * tableCount;

        for (int i = 0; i < row; i++) {
            checkTicketRow(i);
        }

    }

    // Remove all the errors from the ticket
    // by chaning background color to default
    private void removeAllErrors() {
        for (LinearLayout error : errorRows)
            error.setBackgroundColor(Color.TRANSPARENT);

        for (EditText error : errorCells)
            error.setBackgroundResource(R.drawable.ticket_cell);

        errorRows.clear();
        errorCells.clear();


    }


    class TextListener implements TextWatcher {

        private int row, column;

        public TextListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }


        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if (!isGenerating) {
                removeAllErrors();
                checkTicketRow(row);
                checkTicketColumn();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void checkCell() {

        int row = rowSize * tableCount;


        for (int column = 0; column < columnSize; column++) {
            for (int i = 0; i < row; i++) {

                if (!cells[i][column].getText().toString().equals("")) {

                    int number = Integer.parseInt(cells[i][column].getText().toString());

                    if (number > 9) {
                        int min = column * 10;
                        int max = min + 9;

                        if (column == columnSize - 1)
                            max++;

                        if (number >= min && number <= max) {
                            cells[i][column].setBackgroundResource(R.drawable.ticket_cell);
                            errorCells.remove(cells[i][column]);
                        } else {
                            Utilities.showAlertDialog(EditClassicGameTicketsActivity.this, R.string.wrong, R.string.error_in_cell, R.string.close);
                            cells[i][column].setText("");
                        }

                    } else if (column > 0) {
                        cells[i][column].setBackgroundResource(R.drawable.ticket_cell_error);
                        errorCells.add(cells[i][column]);
                    } else if (number == 0) {
                        Utilities.showAlertDialog(EditClassicGameTicketsActivity.this, R.string.wrong, R.string.error_in_cell, R.string.close);
                        cells[i][column].setText("");
                    }

                }
            }
        }
    }


    private void checkTicketRow(int row) {

        int columnCount = 0;

        for (int i = 0; i < columnSize; i++) {
            if (!cells[row][i].getText().toString().equals(""))
                columnCount++;
        }

        if (columnCount == 5) {
            rowContainers[row].setBackgroundColor(Color.TRANSPARENT);
            errorRows.remove(rowContainers[row]);
        } else {
            rowContainers[row].setBackgroundColor(ContextCompat.getColor(this, R.color.row_error_color));
            errorRows.add(rowContainers[row]);
        }

    }

    private void checkTicketColumn() {
        checkCell();
        checkDuplicate();

    }


    private void checkDuplicate() {

        int row = rowSize * tableCount;


        Set<Integer> set = new HashSet<Integer>();


        for (int column = 0; column < columnSize; column++) {
            set.clear();

            for (int i = 0; i < row; i++)
                for (int k = i + 1; k < row; k++) {

                    if (cells[k][column].getText().toString().equals(""))
                        continue;

                    if (k != i && cells[k][column].getText().toString().equals(cells[i][column].getText().toString())) {

                        set.add(i);
                        set.add(k);

                    }
                }


            for (int i = 0; i < row; i++) {

                if (set.contains(i)) {
                    cells[i][column].setBackgroundResource(R.drawable.ticket_cell_error);
                    errorCells.add(cells[i][column]);
                }

            }
        }


    }


    class UpdateCells extends AsyncTask<Integer, Integer, Integer> {

        private int action;
        private ProgressCustom progress;
        private Context context;
        private long sleepTime;

        public UpdateCells(Context context, int action) {
            this.action = action;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            removeAllErrors();
            isGenerating = true;

            progress = new ProgressCustom(context);
            progress.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progress.dismiss();
            isGenerating = false;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int[][] randomTicket = new int[rowSize][columnSize];
            if (action == Constants.ACTION_TICKET_REGENERATE) {
                randomTicket = Utilities.fillRandom(gameId);
                sleepTime = 10;
            } else {
                sleepTime = 0;
            }


            for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {

                for (int row = 0; row < rowSize; row++) {
                    for (int column = 0; column < columnSize; column++) {

                        int textId = BASE_TEXTVIEW_ID + column + ((tableIndex * rowSize + row) * columnSize);


                        if (action == Constants.ACTION_TICKET_REGENERATE &&
                                randomTicket[row + tableIndex * rowSize][column] != 0)
                            publishProgress(textId, randomTicket[row + tableIndex * rowSize][column]);
                        else
                            publishProgress(textId, 0);

                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            TextView text = (EditText) findViewById(values[0]);
            if (values[1] == 0)
                text.setText("");
            else
                text.setText(String.valueOf(values[1]));
        }
    }

}
