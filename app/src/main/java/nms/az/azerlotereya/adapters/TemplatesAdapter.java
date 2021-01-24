package nms.az.azerlotereya.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.models.Template;
import nms.az.azerlotereya.tools.Constants;

/**
 * Created by anar on 10/10/15.
 */
public class TemplatesAdapter extends ArrayAdapter<Template> {

    private Activity context;
    private int resource;
    private List<Template> templates;
    private LayoutInflater inflater;
    OnDataChangeListener mOnDataChangeListener;

    ItemHolder holder;

    public TemplatesAdapter(Activity context, int resource, List<Template> templates) {
        super(context, resource, templates);
        this.context = context;
        this.resource = resource;
        this.templates = templates;
        this.inflater = LayoutInflater.from(context);
    }


    public interface OnDataChangeListener {
         void onDataChanged(int action, int index);
    }


    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }


    @Override
    public int getCount() {
        return templates.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ItemHolder();
            convertView = inflater.inflate(resource, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.template_name);
            holder.date = (TextView) convertView.findViewById(R.id.template_date);
            holder.remove = (ImageButton) convertView.findViewById(R.id.template_remove);
            holder.container = (RelativeLayout) convertView.findViewById(R.id.template_container);

            convertView.setTag(holder);
        } else
            holder = (ItemHolder) convertView.getTag();


        holder.name.setText(templates.get(position).getName());
        holder.date.setText(templates.get(position).getDate());

        holder.remove.setOnClickListener(new ActionOnClickListener(convertView, position));
        holder.container.setOnClickListener(new ActionOnClickListener(convertView, position));


        return convertView;
    }


    @Override
    public Template getItem(int position) {
        return templates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ItemHolder {
        ImageButton remove;
        TextView name, date;
        RelativeLayout container;
    }


    private class ActionOnClickListener implements View.OnClickListener {

        private int position;
        private View view;
        private ViewGroup parent;

        public ActionOnClickListener(View view, int position) {
            this.view = view;
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.template_remove:


                    if (mOnDataChangeListener != null) {
                        mOnDataChangeListener.onDataChanged(Constants.ACTION_TEMPLATE_REMOVE, position);
                    }
                    break;


                case R.id.template_container:


                    Log.e(" index ", position + "");

                    if (mOnDataChangeListener != null) {
                        mOnDataChangeListener.onDataChanged(Constants.ACTION_TEMPLATE_UPDATE, position);
                    }

                    break;
            }
        }
    }

}
