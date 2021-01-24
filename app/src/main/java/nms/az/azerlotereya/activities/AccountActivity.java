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
import nms.az.azerlotereya.fragments.IncreaseBalanceFragment;
import nms.az.azerlotereya.fragments.WithdrawBalanceFragment;

/**
 * Created by anar on 3/17/16.
 */
public class AccountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initToolbar();
        initViewPagerAndTabs();

    }

    private void initToolbar() {
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
        setTitle(R.string.account);
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(pagerAdapter);

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
                    return new IncreaseBalanceFragment();
                case 1:
                    return new WithdrawBalanceFragment();
                default:
                    return new IncreaseBalanceFragment();
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.increase_balance);
                case 1:
                    return getString(R.string.withdraw_balance);
            }
            return getString(R.string.increase_balance);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



}
