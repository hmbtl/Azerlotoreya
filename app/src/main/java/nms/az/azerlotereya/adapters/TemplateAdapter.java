package nms.az.azerlotereya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.TemplatesActivity;
import nms.az.azerlotereya.models.Template;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 8/8/15.
 */
public class TemplateAdapter extends BaseAdapter {

    private int itemResource;
    private TemplatesActivity context;
    private List<Template> templates;
    private LayoutInflater inflater;
    private int gameId;
    private static final int BASE_TEXTVIEW_ID = 1000;

    private boolean isRemoving = false;
    private int removingPosition = 0;

    private int rowSize, columnSize, tableCount;

    private LinearLayout childView;

    private int maxRow;

    public TemplateAdapter(TemplatesActivity context, int itemResource, List<Template> templates, int gameId) {
        super();
        this.context = context;
        this.itemResource = itemResource;
        this.templates = templates;
        inflater = LayoutInflater.from(context);
        this.gameId = gameId;
        this.maxRow = maxRowCount();

    }


    private int maxRowCount() {


        int maxRow = 0;

        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).getNumbers().length > maxRow)
                maxRow = templates.get(i).getNumbers().length;
        }
        return maxRow;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;

        if (convertView == null) {

            View view = Utilities.createTicketViewFromNumbers(context, gameId, maxRow);

            holder = new HeaderViewHolder();
            convertView = inflater.inflate(itemResource, parent, false);
            holder.container = (LinearLayout) convertView.findViewById(R.id.ticket_container);
            holder.container.addView(view);
            holder.number = (TextView) convertView.findViewById(R.id.ticket_index);
            holder.clear = (ImageButton) convertView.findViewById(R.id.ticket_remove);
            holder.fill = (ImageButton) convertView.findViewById(R.id.ticket_fill);
            holder.edit = (ImageButton) convertView.findViewById(R.id.ticket_edit);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        setLottoNumbers(holder.container, templates.get(position).getNumbers());
        //set header text as first char in name
        holder.number.setText("bilet #" + (position + 1) + "");
        holder.clear.setOnClickListener(new ActionOnClickListener(convertView, parent, position));
        holder.fill.setOnClickListener(new ActionOnClickListener(convertView, parent, position));
        holder.edit.setOnClickListener(new ActionOnClickListener(convertView, parent, position));


        if (isRemoving) {
            if (position == removingPosition && removingPosition != templates.size()) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.listitemup);
                animation.setStartOffset(position * 100);
                convertView.startAnimation(animation);
                isRemoving = false;

            } else if (position + 1 == removingPosition && removingPosition == templates.size()) {
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
        return templates.size();
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

                    if (templates.size() != 1) {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


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

            }
        }
    }

}


