package nms.az.azerlotereya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Random;


/**
 * Created by anar on 7/24/15.
 */
public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder> {

    public Context context;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, numberOfStudents, discountedPrice, price;
        public ImageView image;
        public RatingBar rating;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(0, viewGroup, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


        viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        viewHolder.discountedPrice.setText("AZN " + String.valueOf(course.getDiscountedPrice()));


        Random randomFloat = new Random();
//        viewHolder.rating.setRating(randomFloat.nextFloat() * 5.0f);


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        viewHolder.image.setBackgroundColor(color);


//        if (position == 5) {
//            float scale = context.getResources().getDisplayMetrics().density;
//            int paddingRight = (int) (5 * scale + 0.5f);
//
//            LayoutParams params = new LayoutParams(
//                    LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT
//            );
//
//            viewHolder.cardview.setLa(viewHolder.cardview.getContentPaddingLeft(),
//                    viewHolder.cardview.getContentPaddingTop(), paddingRight, viewHolder.cardview.getPaddingBottom());
//        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}