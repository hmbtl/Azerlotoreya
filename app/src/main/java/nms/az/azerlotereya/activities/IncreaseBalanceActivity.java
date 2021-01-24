package nms.az.azerlotereya.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 10/12/15.
 */
public class IncreaseBalanceActivity extends AppCompatActivity {

    private ImageButton pmAzeriCard, pmPortManat, pmMilliON, pmEManat;

    private TextView pInstruction, pInfoDetailed, pUserId;

    private LinearLayout azericardContainer;

    private EditText amountEditText;

    private Button checkout;

    private static final char space = ' ';

    private Activity context;


    private LinearLayout otherMethodContainer;

    private static final int METHOD_AZERICARD = 0;
    private static final int METHOD_PORTMANAT = 1;
    private static final int METHOD_MILLION = 2;
    private static final int METHOD_EMANAT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_increase_balance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(R.string.increase_balance);

        context = this;

        initViews();

        selectPaymentMethod(METHOD_AZERICARD);



/*

        recyclerViewCoursesOnSale = (RecyclerView) view.findViewById(R.id.recycler_view_courses_on_sale);
        recyclerViewCoursesOnSale.setHasFixedSize(true);
        LinearLayoutManager lmCoursesSale
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCoursesOnSale.setLayoutManager(lmCoursesSale);
        recyclerViewCoursesOnSale.setAdapter(new CourseGridAdapter(getActivity(), Utilities.getCourses(getActivity(), 0)));
        coursesOnSaleButton = (TextView) view.findViewById(R.id.button_see_all_courses_on_sale);
        coursesOnSaleButton.setOnClickListener(onClick);

  */
    }


    private void initViews() {

        otherMethodContainer = (LinearLayout) findViewById(R.id.other_method_container);

        pmAzeriCard = (ImageButton) findViewById(R.id.payment_method_azericard);
        pmPortManat = (ImageButton) findViewById(R.id.payment_method_portmanat);
        pmMilliON = (ImageButton) findViewById(R.id.payment_method_million);
        pmEManat = (ImageButton) findViewById(R.id.payment_method_emanat);


        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
        azericardContainer = (LinearLayout) findViewById(R.id.azericard_container);


        pmAzeriCard.setOnClickListener(onClick);
        pmPortManat.setOnClickListener(onClick);
        pmMilliON.setOnClickListener(onClick);
        pmEManat.setOnClickListener(onClick);

        pInstruction = (TextView) findViewById(R.id.other_payment_instruction);
        pInfoDetailed = (TextView) findViewById(R.id.payment_info);
        pUserId = (TextView) findViewById(R.id.user_id_textview);


        checkout = (Button) findViewById(R.id.checkout_button);
        checkout.setOnClickListener(onClick);


    }


    private void selectPaymentMethod(int method) {

        String pName = "";
        Resources resources = getResources();

        otherMethodContainer.setVisibility(View.VISIBLE);
        azericardContainer.setVisibility(View.GONE);

        checkout.setVisibility(View.GONE);


        switch (method) {
            case METHOD_EMANAT:

                pName = "eManat";
                pmAzeriCard.setSelected(false);
                pmEManat.setSelected(true);
                pmPortManat.setSelected(false);
                pmMilliON.setSelected(false);



                break;
            case METHOD_PORTMANAT:


                pName = "portManat";

                pmAzeriCard.setSelected(false);
                pmEManat.setSelected(false);
                pmPortManat.setSelected(true);
                pmMilliON.setSelected(false);


                break;
            case METHOD_MILLION:

                pName = "MilliÖn";

                pmAzeriCard.setSelected(false);
                pmEManat.setSelected(false);
                pmPortManat.setSelected(false);
                pmMilliON.setSelected(true);



                break;

            case METHOD_AZERICARD:


                pName = "Azericard";

                pmAzeriCard.setSelected(true);
                pmEManat.setSelected(false);
                pmPortManat.setSelected(false);
                pmMilliON.setSelected(false);


                checkout.setVisibility(View.VISIBLE);
                otherMethodContainer.setVisibility(View.GONE);
                azericardContainer.setVisibility(View.VISIBLE);

                return;

        }

        pInstruction.setText(resources.getString(R.string.payment_general_instruction).replace("xxx", pName));
        pInfoDetailed.setText(resources.getString(R.string.payment_general_info).replace("xxx", pName));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.abc_fade_in, R.anim.slide_out_bottom);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == pmAzeriCard) {
                selectPaymentMethod(METHOD_AZERICARD);

            } else if (view == pmPortManat) {

                selectPaymentMethod(METHOD_PORTMANAT);

            } else if (view == pmEManat) {

                selectPaymentMethod(METHOD_EMANAT);

            } else if (view == pmMilliON) {

                selectPaymentMethod(METHOD_MILLION);
            } else if (view == checkout) {

                if (!amountEditText.getText().toString().equals("")) {
                    Intent intent = new Intent(IncreaseBalanceActivity.this, CardPaymentWebActivity.class);
                    intent.putExtra(Constants.PAYMENT_NAME_KEY,SharedData.getFullName());
                    intent.putExtra(Constants.PAYMENT_AMOUNT_KEY,amountEditText.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                }else
                    Utilities.showMessageToast(context,R.string.payment_amount);
            }

        }
    };


}
