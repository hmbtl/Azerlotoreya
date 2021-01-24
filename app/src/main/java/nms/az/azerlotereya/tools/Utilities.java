package nms.az.azerlotereya.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import nms.az.azerlotereya.R;

/**
 * Created by anar on 7/13/15.
 */
public class Utilities {

    private static Random random = new Random();
    private static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public static final int BASE_TEXTVIEW_ID = 1000;

    public static final String LANGUAGE_AZ = "az";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_RU = "ru";



    public static boolean randomBoolean() {
        return Math.random() < 0.5;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static void showDialog(Context context, Integer... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        builder.setTitle(args[0]);
        builder.setMessage(args[1]);
        builder.setPositiveButton(args[2], null);

        if (args.length == 4) {
            builder.setNegativeButton(args[3], null);
        }

        builder.show();
    }

    public static int getMaxRow(int gameId) {

        if (gameId == Constants.GAME_ID_KLASSIK)
            return 9;
        else if (gameId == Constants.GAME_ID_SUPER)
            return 9;
        else if (gameId == Constants.GAME_ID_BINQO)
            return 6;
        else if (gameId == Constants.GAME_ID_5_36)
            return 5;
        else if (gameId == Constants.GAME_ID_6_40)
            return 6;
        return 0;
    }


    public static boolean checkEmail(EditText editText) {
        return editText.getText().toString().matches(EMAIL_REGEX);
    }

    public static String generatePIN() {
        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int) (Math.random() * 9000) + 1000;

        return String.valueOf(randomPIN);
    }


    // Randomly fill tickets by GameId
    public static int[][] fillRandom(int gameId) {

        int rowSize = 0;
        int columnSize = 0;

        int rowNumbersCount, numberBase, generatedNumber;

        SparseBooleanArray usedNumbers = new SparseBooleanArray();


        if (gameId == Constants.GAME_ID_KLASSIK) {
            rowSize = 6;
            columnSize = 9;
        } else if (gameId == Constants.GAME_ID_SUPER) {
            rowSize = 9;
            columnSize = 9;
        } else if (gameId == Constants.GAME_ID_BINQO) {
            rowSize = 5;
            columnSize = 6;
        }

        int maxRandom;

        int ticket[][] = new int[rowSize][columnSize];

        for (int row = 0; row < rowSize; row++) {

            int column = 0;
            rowNumbersCount = 0;

            // If each row already has 5 numbers then skip
            while (rowNumbersCount != 5) {


                // if selected table cell has value which is different than 0 then continue to next cell
                if (ticket[row][column] == 0) {
                    if (Utilities.randomBoolean()) {

                        // get the base number for selected row
                        numberBase = column * 10;

                        // if it is last column then random 10 numbers
                        if (column == columnSize - 1)
                            maxRandom = 10;
                            // else random 9 numbers
                        else
                            maxRandom = 9;


                        // generate new number
                        generatedNumber = (random.nextInt(maxRandom) + numberBase);


                        while (usedNumbers.get(generatedNumber)) {
                            generatedNumber = (random.nextInt(maxRandom) + numberBase);
                        }

                        usedNumbers.put(generatedNumber, true);

                        ticket[row][column] = generatedNumber;


                        if (generatedNumber != 0)
                            rowNumbersCount++;
                    }

                }

                column++;

                if (column == columnSize)
                    column = 0;

            }

        }
        return ticket;
    }


    // Show Message TOAST by resource ID
    public static void showMessageToast(Activity context, int resource) {

        /* LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) context.findViewById(R.id.toast_background));


        ((TextView) layout.findViewById(R.id.toast_message)).setText(resource);

        // Create Custom Toast
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();



        */

        Utilities.showMessageToast(context, context.getResources().getString(resource));


    }


    // Show message Toast by String value
    public static void showMessageToast(Activity context, String message) {

 /*       LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) context.findViewById(R.id.toast_background));


        ((TextView) layout.findViewById(R.id.toast_message)).setText(message);

        // Create Custom Toast
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
*/


        Snackbar snackbar = Snackbar.make(context.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);


        View snackBarView = snackbar.getView();
        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();

    }


    // Get game titles by gameId
    public static String getTitleGameById(int gameId) {

        switch (gameId) {
            case Constants.GAME_ID_6_40:
                return "6/40";
            case Constants.GAME_ID_5_36:
                return "5/36";
            case Constants.GAME_ID_KLASSIK:
                return "Klassik Loto";
            case Constants.GAME_ID_SUPER:
                return "Super Loto";
            case Constants.GAME_ID_BINQO:
                return "Binqo";
            case Constants.GAME_ID_FOUR_PLUS_FOUR:
                return "4+4";
        }

        return "";
    }

    // Get gameID by Titles
    public static int getGameById(String name){
        if (name.equalsIgnoreCase("Binqo"))
            return Constants.GAME_ID_BINQO;
        if (name.equalsIgnoreCase("5/36"))
            return Constants.GAME_ID_5_36;
        if (name.equalsIgnoreCase("6/40"))
            return Constants.GAME_ID_6_40;
        if (name.equalsIgnoreCase("Super Loto"))
            return Constants.GAME_ID_SUPER;
        if (name.equalsIgnoreCase("Klassik Loto"))
            return Constants.GAME_ID_KLASSIK;
        if (name.equalsIgnoreCase("4+4"))
            return Constants.GAME_ID_FOUR_PLUS_FOUR;
        return 0;
    }

    public static int getGameIdByIndex(int index){
        switch(index){
            case 0:
                return Constants.GAME_ID_5_36;
            case 1:
                return Constants.GAME_ID_6_40;
            case 2:
                return Constants.GAME_ID_BINQO;
            case 3:
                return Constants.GAME_ID_SUPER;
            case 4:
                return Constants.GAME_ID_KLASSIK;
            default:
                return Constants.GAME_ID_5_36;
        }

    }


    public static int getGameIndexById(int gameId){
        switch (gameId) {
            case Constants.GAME_ID_6_40:
                return 1;
            case Constants.GAME_ID_5_36:
                return 0;
            case Constants.GAME_ID_KLASSIK:
                return 4;
            case Constants.GAME_ID_SUPER:
                return 3;
            case Constants.GAME_ID_BINQO:
                return 2;
        }
        return 0;
    }


    public static boolean isCorrectName(String name){
        return getGameById(name) != 0;
    }


    // Set each game different colors to show in gameList
    public static int getGameColorById(Context context, int gameId) {

        switch (gameId) {
            case Constants.GAME_ID_6_40:
                return context.getResources().getColor(R.color.alti_qirx);
            case Constants.GAME_ID_5_36:
                return context.getResources().getColor(R.color.bes_otuzalti);
            case Constants.GAME_ID_KLASSIK:
                return context.getResources().getColor(R.color.klassik_lotto);
            case Constants.GAME_ID_SUPER:
                return context.getResources().getColor(R.color.super_lotto);
            case Constants.GAME_ID_BINQO:
                return context.getResources().getColor(R.color.binqo);
            case Constants.GAME_ID_FOUR_PLUS_FOUR:
                return context.getResources().getColor(R.color.four_plus_four);
        }

        return Color.WHITE;
    }

    public static int[][] oneDimensionArrayToTwo(int gameId, int[] ticketOneArray) {

        int rowCount = ticketOneArray.length / 5;

        int columnCount = (gameId == Constants.GAME_ID_BINQO) ? 6 : 9;

        int[][] ticket = new int[rowCount][columnCount];

        int row = 0, column = 0;

        for (int i = 0; i < ticketOneArray.length; i++) {
        }

        return ticket;
    }


    // Generate TicketView for GAMES: Binqo, Super and Klassik
    public static LinearLayout createLottoView(Context context, int gameId) {

        int rowSize, columnSize, tableCount;

        if (gameId == Constants.GAME_ID_BINQO) {
            rowSize = 5;
            columnSize = 6;
            tableCount = 1;
        } else if (gameId == Constants.GAME_ID_SUPER) {
            rowSize = 3;
            columnSize = 9;
            tableCount = 3;
        } else if (gameId == Constants.GAME_ID_KLASSIK) {
            rowSize = 3;
            columnSize = 9;
            tableCount = 2;
        } else {
            rowSize = 0;
            columnSize = 0;
            tableCount = 0;
        }


        float scale = context.getResources().getDisplayMetrics().density;
        int generalPadding = (int) (5 * scale + 0.5f);


        int textPadding = (int) (5 * scale + 0.5f);

        int layoutPadding = (int) (2 * scale + 0.5f);

        int textPaddingBottomTop;

        if (gameId == Constants.GAME_ID_BINQO)
            textPaddingBottomTop = (int) (10 * scale + 0.5f);
        else
            textPaddingBottomTop = (int) (5 * scale + 0.5f);


        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(context);
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        generalContainer.setLayoutParams(generalParams);
        generalContainer.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);

        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {


            // secondary container to keep view with shadow background
            LinearLayout secondaryContainer = new LinearLayout(context);
//            secondaryContainer.setBackgroundResource(R.drawable.shadow_test);
            secondaryContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams secondaryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondaryParams.setMargins(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
            secondaryContainer.setLayoutParams(secondaryParams);


            for (int row = 0; row < rowSize; row++) {
                // create row for each game
                LinearLayout rowContainer = new LinearLayout(context);
                rowContainer.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams rowParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowContainer.setLayoutParams(rowParams);

                for (int column = 0; column < columnSize; column++) {
                    // Create each column for  game
                    TextView columnTextView = new TextView(context);
                    columnTextView.setBackgroundResource(R.drawable.rectangle_without_corner_not_filled);
                    LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    columnParams.weight = 1f;
                    columnTextView.setLayoutParams(columnParams);
                    columnTextView.setGravity(Gravity.CENTER);
                    columnTextView.setTextColor(Color.parseColor("#474747"));
                    columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    columnTextView.setPadding(textPadding, textPaddingBottomTop, textPadding, textPaddingBottomTop);
                    columnTextView.setText("1");


                    int generatedId = BASE_TEXTVIEW_ID + column + ((tableIndex * rowSize + row) * columnSize);

                    columnTextView.setId(generatedId);

                    rowContainer.addView(columnTextView);
                }

                secondaryContainer.addView(rowContainer);
            }

            generalContainer.addView(secondaryContainer);
        }

        return generalContainer;
    }


    public static double getTicketPrice(int gameId) {
        switch (gameId) {
            case Constants.GAME_ID_6_40:
                return 0.25;
            case Constants.GAME_ID_5_36:
                return 0.2;
            case Constants.GAME_ID_KLASSIK:
                return 1;
            case Constants.GAME_ID_SUPER:
                return 1;
            case Constants.GAME_ID_BINQO:
                return 1;
        }

        return 0;
    }

    // Get minimum gameOrder count by GameId
    public static int getMinimumOrder(int gameId) {
        switch (gameId) {
            case Constants.GAME_ID_6_40:
                return 4;
            case Constants.GAME_ID_5_36:
                return 5;
            case Constants.GAME_ID_KLASSIK:
                return 2;
            case Constants.GAME_ID_SUPER:
                return 1;
            case Constants.GAME_ID_FOUR_PLUS_FOUR:
                return 1;
            case Constants.GAME_ID_BINQO:
                return 1;
        }

        return 1;
    }


    // Method to show dialog messages to notify user some actions. We can set title, message, button
    // values here.
    public static void showAlertDialog(Context context, String title, String message, String button) {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_alert_custom);
        dialog.setTitle(title);

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_alert_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_alert_message);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);

        dialogMessage.setText(message);
        dialogButton.setText(button);
        dialogTitle.setText(title);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    // Same as previous method only it is useful to use predefined string values in xml
    public static void showAlertDialog(Context context, int title, int message, int button) {

        Resources resources = context.getResources();
        Utilities.showAlertDialog(context, resources.getString(title), resources.getString(message),
                resources.getString(button));
    }
     /* End of dialog methods */


    // Method to show dialog messages to notify user some actions. We can set title, message, button
    // values here.
    public static void showAlertDialog(Context context, String title, String message, String button, boolean isSuccess) {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_alert_custom);

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_alert_right_button);
        final TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_alert_message);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        final ImageView successIcon = (ImageView) dialog.findViewById(R.id.dialog_icon);
        successIcon.setVisibility(View.VISIBLE);

        if(isSuccess)
                successIcon.setImageResource(R.drawable.ic_success);
        else
                successIcon.setImageResource(R.drawable.ic_fail);

        dialogMessage.setText(message);
        dialogButton.setText(button);
        dialogTitle.setText(title);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    // Same as previous method only it is useful to use predefined string values in xml
    public static void showAlertDialog(Context context, int title, int message, int button, boolean isSuccess) {

        Resources resources = context.getResources();
        Utilities.showAlertDialog(context, resources.getString(title), resources.getString(message),
                resources.getString(button));
    }
     /* End of dialog methods */


    public static String intListToJson(List<int[]> list) {

        return new Gson().toJson(list);
    }

    public static String intArrayToJson(List<int[][]> list, int gameId) {

        List<int[][]> ticket = new LinkedList<>();


        for (int[][] element : list) {

            int indexR = 0;

            int length = 5;

            if(gameId == Constants.GAME_ID_FOUR_PLUS_FOUR){
                length = 4;
            } else {
                length = 5;
            }

            int[][] tColumn = new int[element.length][length];

            for (int[] row : element) {

                int indexC = 0;

                for (int cell : row) {
                    if (cell != 0) {
                        tColumn[indexR][indexC] = cell;
                        indexC++;
                    }
                }
                indexR++;
            }

            ticket.add(tColumn);

        }

        return new Gson().toJson(ticket);
    }



    /*

    public static void pickNumbers(int[] numbers) {
        GameConstants.GAME_SELECTED_NUMBER_LIST.add(numbers);
    }

    public static void pickNumbers(int[] numbers, int position) {
        GameConstants.GAME_SELECTED_NUMBER_LIST.set(position, numbers);
    }*/


    // Generate Tickets View filled with Numbers for Games : Binqo, Klassik, Super
    public static View createTicketViewFromNumbers(Context context, int gameId, int rowCount) {


        int rowSize, columnSize, tableCount;

        // This variable used to convert float to density pixel
        float scale = context.getResources().getDisplayMetrics().density;

        int textPaddingBottomTop = 0;


        boolean isBalls = false;

        switch (gameId) {
            case Constants.GAME_ID_BINQO:
                rowSize = 5;
                columnSize = 6;
                tableCount = 1;
                textPaddingBottomTop = (int) (10 * scale + 0.5f);
                break;
            case Constants.GAME_ID_KLASSIK:
                rowSize = 3;
                columnSize = 9;
                tableCount = 2;
                textPaddingBottomTop = (int) (5 * scale + 0.5f);
                break;
            case Constants.GAME_ID_SUPER:
                rowSize = 3;
                columnSize = 9;
                tableCount = 3;
                textPaddingBottomTop = (int) (5 * scale + 0.5f);
                break;
            case Constants.GAME_ID_5_36:
                rowSize = rowCount;
                columnSize = 5;
                tableCount = 1;
                textPaddingBottomTop = (int) (10 * scale + 0.5f);
                isBalls = true;
                break;
            case Constants.GAME_ID_6_40:
                rowSize = rowCount;
                columnSize = 6;
                tableCount = 1;
                textPaddingBottomTop = (int) (10 * scale + 0.5f);
                isBalls = true;
                break;
            default:
                rowSize = rowCount;
                columnSize = 0;
                tableCount = 1;

        }


        int generalPadding = (int) (5 * scale + 0.5f);

        int textPadding = (int) (5 * scale + 0.5f);

        int layoutPadding = (int) (2 * scale + 0.5f);

        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(context);
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        generalContainer.setLayoutParams(generalParams);
        generalContainer.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);

        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {


            // secondary container to keep view with shadow background
            LinearLayout secondaryContainer = new LinearLayout(context);
            secondaryContainer.setOrientation(LinearLayout.VERTICAL);


            LinearLayout.LayoutParams secondaryParams;

            if (isBalls)
                secondaryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            else
                secondaryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            secondaryParams.setMargins(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
            secondaryContainer.setLayoutParams(secondaryParams);


            for (int row = 0; row < rowSize; row++) {
                // create row for each game
                LinearLayout rowContainer = new LinearLayout(context);
                rowContainer.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams rowParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowContainer.setLayoutParams(rowParams);

                for (int column = 0; column < columnSize; column++) {
                    // Create each column for  game
                    TextView cellTextView = new TextView(context);


                    LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);

                    if (isBalls) {
                        cellTextView.setBackgroundResource(R.drawable.rectangle_without_corner_not_filled);
                        columnParams.weight = 1f;

                    } else {
                        cellTextView.setBackgroundResource(R.drawable.circle);
                    }
                    cellTextView.setLayoutParams(columnParams);
                    cellTextView.setGravity(Gravity.CENTER);
                    cellTextView.setTextColor(Color.parseColor("#474747"));
                    cellTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    cellTextView.setPadding(textPadding, textPaddingBottomTop, textPadding, textPaddingBottomTop);
                    cellTextView.setText("1");


                    int generatedId = BASE_TEXTVIEW_ID + column + ((tableIndex * rowSize + row) * columnSize);


                    cellTextView.setId(generatedId);

                    rowContainer.addView(cellTextView);
                }

                secondaryContainer.addView(rowContainer);
            }

            generalContainer.addView(secondaryContainer);
        }

        return generalContainer;

    }


    public static List<int[]> numbersToBallsNumber(int[][] numbers) {

        List<int[]> result = new LinkedList<>();

        Log.e("size numbers", numbers.length + " ");

        for (int i = 0; i < numbers.length; i++) {
            result.add(numbers[i]);

        }
        return result;
    }


    public static boolean isThisDateValid(String dateToValidate, String dateFromat) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }


    // Change language of the applicaiton by forcly
    // changing the system locale
    public static void setLocale(Activity context, String language){
        Locale mLocale = new Locale(language);
        Locale.setDefault(mLocale);
        Configuration config = context.getBaseContext().getResources().getConfiguration();
        if (!config.locale.equals(mLocale))
        {
            config.locale = mLocale;
            context.getBaseContext().getResources().updateConfiguration(config, null);
        }
        SharedData.setLang(language);
    }

}
