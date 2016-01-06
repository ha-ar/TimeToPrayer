package com.algorepublic.cityhistory.prayertimings;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by waqas on 8/13/15.
 */
public class PagerFragment extends Fragment {


    private static final String POSITION = "position";
    Canvas canvas;
    private int height = 15 ;
    private final Handler handler = new Handler();
    private PagerSlidingTabStrip tabs;


    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, container, false);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pagerForList);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_main);
        tabs.setShouldExpand(true);
        tabs.setBackgroundColor(getResources().getColor(R.color.brown));
        tabs.setIndicatorColor(getResources().getColor(R.color.white));
        tabs.setTextColor(getResources().getColor(R.color.black));
        tabs.setIndicatorHeight(height);
        tabs.setDividerColor(getResources().getColor(R.color.black));
        tabs.setViewPager(pager);

//        tabs.setTextSize(45);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Prayers","Qibla"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
//            context=c;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return AlarmFragment.newInstance();
                case 1:
//                    Intent intent = new Intent(context,QiblaActivity.class);
//                   context.startActivity(intent);
                    return QiblaFragment.newInstance();

            }
            return null;
        }

    }
//    public void showAlertDialog(Context context, String title, String message, Boolean status) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//        // Setting Dialog Title
//        alertDialog.setTitle(title);
//
//        // Setting Dialog Message
//        alertDialog.setMessage(message);
//
//        // Setting alert dialog icon
//        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
//
//        // Setting OK Button
//        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }


}
