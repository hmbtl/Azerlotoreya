package nms.az.azerlotereya.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.activities.LoginActivity;
import nms.az.azerlotereya.tools.Constants;

/**
 * Created by anar on 8/31/15.
 */
public class IntroFragment extends Fragment {


    String title, subtitle;
    int resource, position, runningFrom;

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_SUBTITLE = "EXTRA_SUBTITLE";
    public static final String EXTRA_IMAGE = "EXTRA_IMAGE";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String RUNNING_FROM = "RUNNING_FROM";


    public static final IntroFragment newInstance(String title, String subtile, int resource, int position, int runningFrom) {
        IntroFragment f = new IntroFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_TITLE, title);
        bdl.putString(EXTRA_SUBTITLE, subtile);
        bdl.putInt(EXTRA_IMAGE, resource);
        bdl.putInt(EXTRA_POSITION, position);
        bdl.putInt(RUNNING_FROM, runningFrom);

        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_intro, container, false);


        title = getArguments().getString(EXTRA_TITLE);
        subtitle = getArguments().getString(EXTRA_SUBTITLE);
        resource = getArguments().getInt(EXTRA_IMAGE);
        position = getArguments().getInt(EXTRA_POSITION);
        runningFrom = getArguments().getInt(RUNNING_FROM);


        TextView textViewTitle = (TextView) rootView.findViewById(R.id.intro_title);
        textViewTitle.setText(Html.fromHtml(title));

        TextView textViewSubtitle = (TextView) rootView.findViewById(R.id.intro_subtitle);
        textViewSubtitle.setText(Html.fromHtml(subtitle));

        ImageView imageView = (ImageView) rootView.findViewById(R.id.intro_image);
        imageView.setImageResource(resource);

        TextView closeButton = (TextView) rootView.findViewById(R.id.button_close);

        if (position == 4)
            closeButton.setVisibility(View.VISIBLE);
        else
            closeButton.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (runningFrom == Constants.RUNNING_FROM_START_ACTIVITY) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(Constants.KEY_IS_ANIMATED, false);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    getActivity().finish();
                }
            }
        });


        return rootView;
    }
}
