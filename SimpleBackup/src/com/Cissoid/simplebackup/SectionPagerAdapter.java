package com.cissoid.simplebackup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.cissoid.simplebackup.app.AppFragment;
import com.cissoid.simplebackup.home.HomePageFragment;
import com.cissoid.simplebackup.sms.SmsFragment;

/**
 * 
 * @author Wxh
 * @since 2013.03.23
 * 
 */
public class SectionPagerAdapter extends FragmentPagerAdapter
{
    private MainActivity activity;

    public SectionPagerAdapter( FragmentManager fm , MainActivity activity )
    {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem( int position )
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putBoolean("sdcard", activity.getStatus().isSdcard());
        args.putBoolean("root", activity.getStatus().isRoot());
        args.putBoolean("busybox", activity.getStatus().isBusybox());
        args.putBoolean("bae", activity.getStatus().isBae());
        switch ( position )
        {
        case 0 :
            fragment = new HomePageFragment();

            args.putInt(HomePageFragment.ARG_SECTION_NUMBER, position + 1);
            break;
        case 1 :
            fragment = new AppFragment();
            activity.setAppFragment((AppFragment) fragment);
            args.putInt(AppFragment.ARG_SECTION_NUMBER, position + 1);
            break;
        case 2 :
            fragment = new SmsFragment();
            activity.setSmsFragment((SmsFragment) fragment);
            args.putInt(SmsFragment.ARG_SECTION_NUMBER, position + 1);
            break;
        }
        if ( fragment != null )
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle( int position )
    {
        switch ( position )
        {
        case 0 :
            return activity.getString(R.string.title_home);
        case 1 :
            return activity.getString(R.string.title_app);
        case 2 :
            return activity.getString(R.string.title_sms);
        }
        return null;
    }

    @Override
    public Object instantiateItem( View container , int position )
    {
        // TODO Auto-generated method stub
        return super.instantiateItem(container, position);
    }
}