//package com.wxhcn.simplebackup;
//
//import java.util.Locale;
//
//import com.wxhcn.simplebackup.app.AppFragment;
//import com.wxhcn.simplebackup.home.HomePageFragment;
//import com.wxhcn.simplebackup.sms.SmsFragment;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
///**
// * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
// * one of the sections/tabs/pages.
// */
//public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//    public SectionsPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a DummySectionFragment (defined as a static inner class
//        // below) with the page number as its lone argument.
//        Fragment fragment;
//        Bundle args = new Bundle();
//        if (position == 0) {
//            fragment = new HomePageFragment();
//            args.putInt(HomePageFragment.ARG_SECTION_NUMBER, position + 1);
//        } else if (position == 1) {
//            fragment = new AppFragment();
//            args.putInt(AppFragment.ARG_SECTION_NUMBER, position + 1);
//        } else {
//            fragment = new AppFragment();
//            args.putInt(SmsFragment.ARG_SECTION_NUMBER, position + 1);
//        }
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        // Show 3 total pages.
//        return 3;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        Locale l = Locale.getDefault();
//        switch (position) {
//        case 0:
//            return getString(R.string.title_home);
//        case 1:
//            return getString(R.string.title_app);
//        case 2:
//            return getString(R.string.title_sms);
//        }
//        return null;
//    }
//}
