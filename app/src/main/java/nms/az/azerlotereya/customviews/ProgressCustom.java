package nms.az.azerlotereya.customviews;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import nms.az.azerlotereya.R;

/**
 * Created by anar on 12/21/15.
 */
public class ProgressCustom extends ProgressDialog {


    private TextView dialogMessage;

    public ProgressCustom(Context context) {
        super(context);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_custom);
        setCancelable(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }


    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }


}
