package nms.az.azerlotereya.adapters;

/**
 * Created by anar on 9/6/15.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.models.Transaction;


/**
 * Created by anar on 7/24/15.
 */
public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder> {

    private  List<Transaction> transactions;
    private  Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView transDate, transType, transMethod, transAmount,transCardNumber, transOrderNumber;
        public ImageView transIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            transAmount = (TextView) itemView.findViewById(R.id.trans_amount);
            transDate =(TextView) itemView.findViewById(R.id.trans_date);
            transIcon = (ImageView) itemView.findViewById(R.id.trans_icon);
            transMethod =(TextView) itemView.findViewById(R.id.trans_method);
            transType = (TextView)itemView.findViewById(R.id.trans_type);
            transOrderNumber = (TextView)itemView.findViewById(R.id.trans_order_number);
            transCardNumber = (TextView)itemView.findViewById(R.id.trans_card_number);

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

    public TransactionRecyclerAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_transaction, viewGroup, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Transaction transaction = transactions.get(position);
        String[] methods = context.getResources().getStringArray(R.array.transaction_method_array);
        String[] types = context.getResources().getStringArray(R.array.transaction_type_array);
        TypedArray images = context.getResources().obtainTypedArray(R.array.transaction_type_icon_array);
        viewHolder.transType.setText(types[transaction.getType() - 1]);
        if(transaction.getType() == 1 || transaction.getType() == 4) {
            viewHolder.transMethod.setVisibility(View.VISIBLE);
            viewHolder.transMethod.setText(methods[transaction.getMethod() - 1]);
        } else {
            viewHolder.transMethod.setVisibility(View.INVISIBLE);
        }
        viewHolder.transDate.setText(transaction.getDate());
        viewHolder.transAmount.setText(String.valueOf(transaction.getAmount()));

        if(transaction.getOrderNumber().equals("null") || transaction.getOrderNumber() != null){
            viewHolder.transOrderNumber.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.transOrderNumber.setText(transaction.getOrderNumber());
        }

        viewHolder.transOrderNumber.setText(transaction.getOrderNumber());
        if (!transaction.getCardNumber().equals("") || transaction.getCardNumber() != null  ){
            if(transaction.getCardNumber().equals("null")){
                viewHolder.transCardNumber.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.transCardNumber.setVisibility(View.VISIBLE);
                viewHolder.transCardNumber.setText(transaction.getCardNumber());
            }
        }
        else{
            viewHolder.transCardNumber.setText("");
            viewHolder.transCardNumber.setVisibility(View.INVISIBLE);
        }
        viewHolder.transIcon.setImageResource(images.getResourceId(transaction.getType() - 1,0));
        images.recycle();

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


}