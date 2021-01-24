package nms.az.azerlotereya.adapters;

import android.content.Intent;
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

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.EditClassicGameTicketsActivity;
import nms.az.azerlotereya.activities.ClassicGameActivity;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/8/15.
 */
public class LottoGameAdapter extends BaseAdapter {

    private int itemResource;
    private ClassicGameActivity context;
    private List<int[][]> tickets;
    private LayoutInflater inflater;
    private int gameId;
    private static final int BASE_TEXTVIEW_ID = 1000;

    private boolean isRemoving = false;
    private int removingPosition = 0;

    private int rowSize, columnSize, tableCount;

    private LinearLayout childView;

    public LottoGameAdapter(ClassicGameActivity context, int itemResource, List<int[][]> tickets, int gameId) {
        super();
        this.context = context;
        this.itemResource = itemResource;
        this.tickets = tickets;
        inflater = LayoutInflater.from(context);
        this.gameId = gameId;

        initElements();
    }


    private void initElements() {

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
        }


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(itemResource, parent, false);
            holder.container = (LinearLayout) convertView.findViewById(R.id.ticket_container);
            holder.container.addView(Utilities.createLottoView(context, gameId));
            holder.number = (TextView) convertView.findViewById(R.id.ticket_index);
            holder.clear = (ImageButton) convertView.findViewById(R.id.ticket_remove);
            holder.fill = (ImageButton) convertView.findViewById(R.id.ticket_fill);
            holder.edit = (ImageButton) convertView.findViewById(R.id.ticket_edit);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        setLottoNumbers(holder.container, tickets.get(position));
        //set header text as first char in name
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


    private void setLottoNumbers(View view, int[][] ticket) {


        for (int tableIndex = 0; tableIndex < tableCount; tableIndex++) {

            for (int row = 0; row < rowSize; row++) {
                for (int column = 0; column < columnSize; column++) {

                    int textId = BASE_TEXTVIEW_ID + column + ((tableIndex * rowSize + row) * columnSize);

                    TextView text = (TextView) view.findViewById(textId);


                    if (ticket[row + tableIndex * rowSize][column] == 0)
                        text.setText("");
                    else
                        text.setText(String.valueOf(ticket[row + tableIndex * rowSize][column]));
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


                                context.update(Constants.ACTION_TICKET_REMOVE, position);
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
                    context.update(Constants.ACTION_TICKET_REGENERATE, position);

                    break;

                case R.id.ticket_edit:
                    Intent intent = new Intent(context, EditClassicGameTicketsActivity.class);
                    intent.putExtra(Constants.TICKET_COLUMN_SIZE_KEY, columnSize);
                    intent.putExtra(Constants.TICKET_ROW_SIZE_KEY, rowSize);
                    intent.putExtra(Constants.TICKET_TABLE_COUNT_KEY, tableCount);
                    intent.putExtra(Constants.TICKET_POSITION_KEY, position + 1);
                    intent.putExtra(Constants.GAME_ID_KEY, gameId);

                    Gson gson = new Gson();

                    intent.putExtra(Constants.TICKET_KEY, gson.toJson(tickets.get(position)));
/*
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.TICKET_KEY, tickets.get(position));
                    intent.putExtras(bundle);
*/
                    context.startActivityForResult(intent, 1);
                    break;
            }
        }
    }

}


