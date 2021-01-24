package nms.az.azerlotereya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.models.Draw;

/**
 * Created by anar on 2/21/16.
 */
public class SpinnerDrawAdapter extends BaseAdapter {

    private List<Draw> draws;
    private Context context;
    private ItemHolder holderDropDown, holder;

    public SpinnerDrawAdapter(Context context, List<Draw> draws) {
        this.draws = draws;
        this.context = context;
    }

    @Override
    public int getCount() {
        return draws.size();
    }

    @Override
    public Object getItem(int position) {
        return draws.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            holderDropDown = new ItemHolder();
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_draw_list_item, parent, false);

            holderDropDown.drawNumber = (TextView) view.findViewById(R.id.draw_number_textview);
            holderDropDown.drawTime = (TextView) view.findViewById(R.id.draw_time_textview);

            view.setTag("DROPDOWN");
        }

        holderDropDown.drawNumber.setText(draws.get(position).getNumber());
        holderDropDown.drawTime.setText(draws.get(position).getDate());

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            holder = new ItemHolder();
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_draw_item, parent, false);

            holder.drawNumber = (TextView) view.findViewById(R.id.draw_number);

            view.setTag("NON_DROPDOWN");
        }
        holder.drawNumber.setText(draws.get(position).getNumber());
        return view;
    }

    class ItemHolder {
        TextView drawTime, drawNumber;
    }
}
