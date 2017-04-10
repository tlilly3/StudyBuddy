package com.thomas.studybuddy;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.thomas.studybuddy.dummy.DummyContent;

public class HomeActivity extends MainActivity implements ClassFragment.OnListFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Studys"));
        tabLayout.addTab(tabLayout.newTab().setText("Homeworks"));
        ClassPager classPager =
                new ClassPager(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(classPager);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.d("MAIN", "Interacted with The fragment");
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class ClassPager extends FragmentStatePagerAdapter {
        public ClassPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return ClassFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


}
