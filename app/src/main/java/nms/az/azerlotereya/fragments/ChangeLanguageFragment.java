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
public class ChangeLanguageFragment extends Fragment {


    String title, subtitle;
    int resource, position, runningFrom;


    public static ChangeLanguageFragment newInstance() {
        ChangeLanguageFragment f = new ChangeLanguageFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_intro, container, false);





        return rootView;
    }
}
