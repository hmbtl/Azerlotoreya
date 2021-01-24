package nms.az.azerlotereya.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.EditBallsGameTicketsActivity;
import nms.az.azerlotereya.activities.BallsGameActivity;
import nms.az.azerlotereya.tools.Constants;

/**
 * Created by anar on 7/20/15.
 */
public class NumberGeneratorAdapter extends ArrayAdapter<int[]> {


    private List<int[]> tickets;
    private int resource;
    private BallsGameActivity context;
    ItemsHolder holder;
    private int gameId;

    public NumberGeneratorAdapter(BallsGameActivity context, int resource, List<int[]> tickets, int gameId) {
        super(context, resource, tickets);
        this.context = context;
        this.resource = resource;
        this.tickets = tickets;
        this.gameId = gameId;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {


        if (row == null) {
            holder = new ItemsHolder();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource, parent, false);

            holder.circle_1 = (TextView) row.findViewById(R.id.ticket_circle_1);
            holder.circle_2 = (TextView) row.findViewById(R.id.ticket_circle_2);
            holder.circle_3 = (TextView) row.findViewById(R.id.ticket_circle_3);
            holder.circle_4 = (TextView) row.findViewById(R.id.ticket_circle_4);
            holder.circle_5 = (TextView) row.findViewById(R.id.ticket_circle_5);
            holder.circle_6 = (TextView) row.findViewById(R.id.ticket_circle_6);

            holder.edit = (ImageButton) row.findViewById(R.id.ticket_numbers_edit);
            holder.remove = (ImageButton) row.findViewById(R.id.ticket_numbers_remove);
            holder.balls = (LinearLayout) row.findViewById(R.id.balls);

            row.setTag(holder);
        } else
            holder = (ItemsHolder) row.getTag();


        int[] numbers = tickets.get(position);

        holder.circle_1.setText(String.valueOf(numbers[0]));
        holder.circle_2.setText(String.valueOf(numbers[1]));
        holder.circle_3.setText(String.valueOf(numbers[2]));
        holder.circle_4.setText(String.valueOf(numbers[3]));
        holder.circle_5.setText(String.valueOf(numbers[4]));


        if (numbers.length > 5)
            holder.circle_6.setText(String.valueOf(numbers[5]));
        else
            holder.circle_6.setVisibility(View.GONE);

        holder.edit.setOnClickListener(new NumberOnClickListener(row, position));
        holder.remove.setOnClickListener(new NumberOnClickListener(row, position));
        holder.balls.setOnClickListener(new NumberOnClickListener(row, position));

        return row;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public int[] getItem(int position) {
        return tickets.get(position);
    }


    static class ItemsHolder {
        TextView circle_1, circle_2, circle_3, circle_4, circle_5, circle_6;
        ImageButton edit, remove;
        LinearLayout balls;
    }

    private class NumberOnClickListener implements View.OnClickListener {

        private int position;
        private View view;

        public NumberOnClickListener(View view, int position) {
            this.view = view;
            this.position = position;
        }

        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.balls:

                    Intent intent = new Intent(context, EditBallsGameTicketsActivity.class);
                    intent.putExtra(Constants.GAME_ID_KEY, gameId);
                    intent.putExtra("numbers_to_edit", tickets.get(position));
                    intent.putExtra("position", position);
                    intent.putExtra("edit", true);
                    context.startActivityForResult(intent, 1);

                    break;
                case R.id.ticket_numbers_remove:
                    tickets.remove(position);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        context.updatePrice();
    }
}
