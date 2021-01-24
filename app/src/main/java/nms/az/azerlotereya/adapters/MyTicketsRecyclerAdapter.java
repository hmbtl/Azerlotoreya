package nms.az.azerlotereya.adapters;

/**
 * Created by anar on 9/6/15.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.models.Ticket;
import nms.az.azerlotereya.tools.Utilities;


/**
 * Created by anar on 7/24/15.
 */
public class MyTicketsRecyclerAdapter extends RecyclerView.Adapter<MyTicketsRecyclerAdapter.ViewHolder> {

    private  List<Ticket> tickets;
    private  Context context;
    private  int ticketType;
    private static int gameId;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public TextView ticketNumber, drawNumber, createdAt, ticketPrice, gameName, priceLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            ticketNumber = (TextView) itemView.findViewById(R.id.ticket_id);
            drawNumber =(TextView) itemView.findViewById(R.id.draw_number);
            gameName = (TextView)itemView.findViewById(R.id.ticket_game_name);
            ticketPrice =(TextView) itemView.findViewById(R.id.ticket_price);
            createdAt = (TextView)itemView.findViewById(R.id.ticket_created_at);
            priceLabel = (TextView)itemView.findViewById(R.id.label_ticket_price);

        }

        @Override
        public void onClick(View view) {
         /*   if (view == editButton) {
                Intent intent = new Intent(context, EditLottoGameTicketsActivity.class);
                intent.putExtra(Constants.TICKET_POSITION_KEY, getAdapterPosition() + 1);
                intent.putExtra(Constants.GAME_ID_KEY, gameId);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TICKET_KEY, MyTicketsRecyclerAdapter.tickets.get(getAdapterPosition()));
                intent.putExtras(bundle);

                MyTicketsRecyclerAdapter.context.startActivityForResult(intent, 1);
            } else if (view == clearButton) {

                context.update(Constants.ACTION_TICKET_REMOVE, getAdapterPosition());
            } else if (view == fillButton) {
                context.update(Constants.ACTION_TICKET_REMOVE, getAdapterPosition());
            }*/
        }
    }

    public MyTicketsRecyclerAdapter(Context mContext, int ticketStatus, List<Ticket> mTickets) {
        context = mContext;
        tickets = mTickets;
        ticketType = ticketStatus;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_recycler_my_tickets, viewGroup, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Ticket ticket = tickets.get(position);
        viewHolder.ticketNumber.setText("Bilet #" + ticket.getTicketNumber() );

        viewHolder.gameName.setText(Utilities.getTitleGameById(ticket.getGameId()));
        viewHolder.gameName.setBackgroundColor(Utilities.getGameColorById(
                context,ticket.getGameId()));

        if(ticketType == 3){
            viewHolder.priceLabel.setText(R.string.winning);
        } else {
            viewHolder.priceLabel.setText(R.string.price);
        }

        viewHolder.drawNumber.setText("tiraj #" + ticket.getDrawNumber() );
        viewHolder.createdAt.setText(ticket.getCreatedAt() );
        viewHolder.ticketPrice.setText(ticket.getCost() + " AZN" );
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateList(List<Ticket> mTickets){
        tickets.clear();
        tickets.addAll(mTickets);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }


}