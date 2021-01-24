package nms.az.azerlotereya.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.fragments.GameRulesFragment;
import nms.az.azerlotereya.fragments.GeneralRulesFragment;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_rules);

        initToolbar();
        initViewPagerAndTabs();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.rules));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.slide_out_bottom);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private FragmentManager fm;
        private FragmentTransaction ft;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new GameRulesFragment();
                case 1:
                    return new GeneralRulesFragment();
                default:
                    return new GameRulesFragment();
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.game_rules);
                case 1:
                    return getString(R.string.general_rules);
            }
            return getString(R.string.game_rules);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }




}