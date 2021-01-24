package nms.az.azerlotereya.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.fragments.IntroFragment;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.SharedData;

/**
 * Created by anar on 8/24/15.
 */
public class IntroActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private int runningFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        SharedData.setFirstRun(false);

        runningFrom = getIntent().getIntExtra(Constants.RUNNING_INTRO_FROM_WHERE, Constants.RUNNING_FROM_START_ACTIVITY);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    int intro1;

                    if(SharedData.getLang().equals("en"))
                            intro1 = R.drawable.intro_az_1;
                    else
                            intro1 = R.drawable.intro_ru_1;

                    return IntroFragment.newInstance(getResources().getString(R.string.intro_message_1_title),
                            getResources().getString(R.string.intro_message_1),
                            intro1, position, runningFrom);
                case 1:

                    int intro2;

                    if(SharedData.getLang().equals("en"))
                        intro2 = R.drawable.intro_az_2;
                    else
                        intro2 = R.drawable.intro_ru_2;

                    return IntroFragment.newInstance(getResources().getString(R.string.intro_message_2_title),
                            getResources().getString(R.string.intro_message_2),
                            intro2, position, runningFrom);
                case 2:

                    int intro3;

                    if(SharedData.getLang().equals("en"))
                        intro3 = R.drawable.intro_az_3;
                    else
                        intro3 = R.drawable.intro_ru_3;

                    return IntroFragment.newInstance(getResources().getString(R.string.intro_message_3_title),
                            getResources().getString(R.string.intro_message_3),
                            intro3, position, runningFrom);
                case 3:

                    int intro4;

                    if(SharedData.getLang().equals("en"))
                        intro4 = R.drawable.intro_az_4;
                    else
                        intro4 = R.drawable.intro_ru_4;

                    return IntroFragment.newInstance(getResources().getString(R.string.intro_message_4_title),
                            getResources().getString(R.string.intro_message_4),
                            intro4, position, runningFrom);
                case 4:

                    int intro5;

                    if(SharedData.getLang().equals("en"))
                        intro5 = R.drawable.intro_az_5;
                    else
                        intro5 = R.drawable.intro_ru_5;

                    return IntroFragment.newInstance(getResources().getString(R.string.intro_message_5_title),
                            getResources().getString(R.string.intro_message_5),
                            intro5, position, runningFrom);
            }


            int intro1;

            if(SharedData.getLang().equals("en"))
                intro1 = R.drawable.intro_az_1;
            else
                intro1 = R.drawable.intro_ru_1;

            return IntroFragment.newInstance(getResources().getString(R.string.intro_message_1_title),
                    getResources().getString(R.string.intro_message_1),
                   intro1, position, runningFrom);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
