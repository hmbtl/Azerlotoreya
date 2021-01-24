package nms.az.azerlotereya.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.EditFourPlusFourGameActivity;
import nms.az.azerlotereya.activities.FourPlusFourGameActivity;
import nms.az.azerlotereya.tools.Constants;

import static nms.az.azerlotereya.tools.Utilities.BASE_TEXTVIEW_ID;

/**
 * Created by anar on 8/8/15.
 */
public class FourPlusFourGameAdapter extends BaseAdapter {

    private int itemResource;
    private FourPlusFourGameActivity context;
    private List<int[][]> tickets;
    private LayoutInflater inflater;

    private boolean isRemoving = false;
    private int removingPosition = 0;

    private LinearLayout childView;

    public FourPlusFourGameAdapter(FourPlusFourGameActivity context, int itemResource, List<int[][]> tickets) {
        super();
        this.context = context;
        this.itemResource = itemResource;
        this.tickets = tickets;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(itemResource, parent, false);
            holder.container = (LinearLayout) convertView.findViewById(R.id.ticket_container);
            holder.container.addView(createFourPlusFourView(context));
            holder.number = (TextView) convertView.findViewById(R.id.ticket_index);
            holder.clear = (ImageButton) convertView.findViewById(R.id.ticket_remove);
            holder.fill = (ImageButton) convertView.findViewById(R.id.ticket_fill);
            holder.edit = (ImageButton) convertView.findViewById(R.id.ticket_edit);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        //set header text as first char in name
        setLottoNumbers(holder.container, tickets.get(position));
        holder.number.setText("bilet #" + (position + 1) + "");
        holder.clear.setOnClickListener(new ActionOnClickListener(convertView, parent, position));
        holder.fill.setOnClickListener(new ActionOnClickListener(convertView, parent, position));
        holder.edit.setOnClickListener(new ActionOnClickListener(convertView, parent, position));


        if (isRemoving) {
            if (position == removingPosition && removingPosition != tickets.size()) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.listitemup);
                animation.setStartOffset(position * 100);
                convertView.startAnimation(animation);
                isRemoving = false;

            } else if (position + 1 == removingPosition && removingPosition == tickets.size()) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.listitemdown);
                animation.setStartOffset(position * -100);
                convertView.startAnimation(animation);
                isRemoving = false;
            }
        }


        return convertView;
    }


    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public int[][] getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    class HeaderViewHolder {
        ImageButton clear, fill, edit;
        TextView number;
        LinearLayout container;
    }


    private LinearLayout createFourPlusFourView(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;

        int rowSize = 5, columnSize = 4, tableCount = 2;
        int generalPadding = (int) (5 * scale + 0.5f);
        int textPadding = (int) (2 * scale + 0.5f);
        int layoutPadding = (int) (2 * scale + 0.5f);
        int textPaddingBottomTop = (int) (5 * scale + 0.5f);


        // Create general Linear Layout which will keep child views with background set to white
        LinearLayout generalContainer = new LinearLayout(context);
        generalContainer.setBackgroundColor(Color.WHITE);
        generalContainer.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams generalParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        generalContainer.setLayoutParams(generalParams);
        generalContainer.setPadding(generalPadding, generalPadding, generalPadding, generalPadding);

        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {

            // secondary container to keep view with shadow background
            LinearLayout secondaryContainer = new LinearLayout(context);
//            secondaryContainer.setBackgroundResource(R.drawable.shadow_test);
            secondaryContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams secondaryParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondaryParams.weight = 1f;
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
                    columnTextView.setTypeface(null, Typeface.BOLD);
                    columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
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



    private void setLottoNumbers(View view, int[][] ticket){

        for (int tableIndex = 0; tableIndex < 2; tableIndex++) {

            int numberIndex  = 0, limitIndex = 0;
            int[] currentTicket = ticket[tableIndex];

            // Sort current array
            Arrays.sort(currentTicket);

            for (int row = 0; row < 5; row++) {

                for (int column = 0; column < 4; column++) {

                    int textId = BASE_TEXTVIEW_ID + column + ((tableIndex * 5 + row) * 4);

                    TextView text = (TextView) view.findViewById(textId);

                    // Increase numbers in the bpx
                    numberIndex ++;

                    if(limitIndex < 4){
                        if(numberIndex == currentTicket[limitIndex]){
                            limitIndex++;
                            text.setText(String.valueOf(numberIndex));
                        } else {
                            text.setText("");
                        }
                    } else {
                        text.setText("");
                    }


                }
            }
        }
    }



    private class ActionOnClickListener implements View.OnClickListener {

        private int position;
        private View view;
        private ViewGroup parent;

        public ActionOnClickListener(View view, ViewGroup parent, int position) {
            this.view = view;
            this.position = position;
            this.parent = parent;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ticket_remove:

                    if (tickets.size() != 1) {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


                                context.updateList(Constants.ACTION_TICKET_REMOVE, position);
                                isRemoving = true;
                                removingPosition = position;

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        view.startAnimation(animation);

                    }
                    break;
                case R.id.ticket_fill:
                    context.updateList(Constants.ACTION_TICKET_REGENERATE, position);

                    break;

                case R.id.ticket_edit:
                    Intent intent = new Intent(context, EditFourPlusFourGameActivity.class);

                    Gson gson = new Gson();

                    intent.putExtra(Constants.TICKET_KEY,gson.toJson(tickets.get(position)));
                    intent.putExtra(Constants.TICKET_POSITION_KEY, position);
                    intent.putExtra(Constants.TICKET_IS_EDIT, true);

                    context.startActivityForResult(intent, 1);
                    break;
            }
        }
    }

}


