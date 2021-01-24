package nms.az.azerlotereya.adapters;

/**
 * Created by anar on 9/6/15.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.EditClassicGameTicketsActivity;
import nms.az.azerlotereya.activities.ClassicGameActivity;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.Utilities;


/**
 * Created by anar on 7/24/15.
 */
public class ClassicLottoAdapter extends RecyclerView.Adapter<ClassicLottoAdapter.ViewHolder> {

    private static List<int[][]> tickets;
    private static ClassicGameActivity context;
    private static int gameId;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageButton clearButton, fillButton, editButton;
        public TextView ticketNumber;
        public LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);

            clearButton = (ImageButton) itemView.findViewById(R.id.ticket_remove);
            fillButton = (ImageButton) itemView.findViewById(R.id.ticket_fill);
            editButton = (ImageButton) itemView.findViewById(R.id.ticket_edit);
            ticketNumber = (TextView) itemView.findViewById(R.id.ticket_index);
            container = (LinearLayout) itemView.findViewById(R.id.ticket_container);

            container.addView(Utilities.createLottoView(ClassicLottoAdapter.context, ClassicLottoAdapter.gameId));

            editButton.setOnClickListener(this);
            fillButton.setOnClickListener(this);
            clearButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view == editButton) {
                Intent intent = new Intent(context, EditClassicGameTicketsActivity.class);
                intent.putExtra(Constants.TICKET_POSITION_KEY, getAdapterPosition() + 1);
                intent.putExtra(Constants.GAME_ID_KEY, gameId);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TICKET_KEY, ClassicLottoAdapter.tickets.get(getAdapterPosition()));
                intent.putExtras(bundle);

                ClassicLottoAdapter.context.startActivityForResult(intent, 1);
            } else if (view == clearButton) {

                context.update(Constants.ACTION_TICKET_REMOVE, getAdapterPosition());
            } else if (view == fillButton) {
                context.update(Constants.ACTION_TICKET_REMOVE, getAdapterPosition());
            }
        }
    }

    public ClassicLottoAdapter(ClassicGameActivity context, List<int[][]> tickets, int gameId) {
        ClassicLottoAdapter.context = context;
        ClassicLottoAdapter.gameId = gameId;
        ClassicLottoAdapter.tickets = tickets;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_lotto_game_list, viewGroup, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.ticketNumber.setText("bilet #" + (position + 1) + "");

        setLottoNumbers(viewHolder.container, tickets.get(position));
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }


    private void setLottoNumbers(View view, int[][] ticket) {


        int rowSize = ticket.length;
        int columnSize = ticket[0].length;

        for (int row = 0; row < rowSize; row++) {

            for (int column = 0; column < columnSize; column++) {

                int textId = Utilities.BASE_TEXTVIEW_ID + column + (row * columnSize);

                TextView text = (TextView) view.findViewById(textId);


                if (ticket[row][column] == 0)
                    text.setText("");
                else
                    text.setText(String.valueOf(ticket[row][column]));
            }
        }

    }
}