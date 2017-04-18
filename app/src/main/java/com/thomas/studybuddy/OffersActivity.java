package com.thomas.studybuddy;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

public class OffersActivity extends MainActivity implements OfferFragment.OnListFragmentInteractionListener{
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        setUp();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Your Offers"));
        tabLayout.addTab(tabLayout.newTab().setText("Offers Made"));
        OfferPager offerPager = new OfferPager(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(offerPager);
    }

    @Override
    public void onListFragmentInteraction(ClassModel item) {
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                break;
            case 1:
        }
    }

    public class OfferPager extends FragmentStatePagerAdapter {
        public OfferPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return OfferFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

}