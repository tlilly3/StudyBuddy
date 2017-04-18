package com.thomas.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            final int full = 255;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private final int REQUEST_CODE = 2;
    private String myclientToken;
    @Override
    public void onListFragmentInteraction(ClassModel item) {
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://studdybuddy3.herokuapp.com/", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("Braintree Token", "Failed token request");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String clientToken) {
                        myclientToken = clientToken;
                        Log.d("Braintree Token", myclientToken);
                        DropInRequest dropInRequest = new DropInRequest()
                                .clientToken(myclientToken);
                        startActivityForResult(dropInRequest.getIntent(OffersActivity.this), REQUEST_CODE);
                    }
                });

                break;
            case 1:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Activity Result", "Made it here");
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("payment_method_nonce", "fake-valid-nonce");
                params.put("amount", 15);
                client.post("https://studdybuddy3.herokuapp.com/checkouts", params,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Log.d("Braintree response", "Response success");
                                Snackbar.make(mViewPager, "Success!", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.d("Braintree response", "Response Fail");
                                try {
                                    Log.d("Response", new String(responseBody, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            // Your implementation here
                        }
                );
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
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