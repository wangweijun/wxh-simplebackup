package com.Cissoid.simplebackup;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Cissoid.simplebackup.app.AppFragment;
import com.Cissoid.simplebackup.home.HomePageFragment;
import com.Cissoid.simplebackup.sms.SmsFragment;
import com.wxhcn.simplebackup.R;

/**
 * 
 * @author Wxh
 * @since 2013.03.23
 * 
 */
public class SectionPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public SectionPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position)
        {
        case 0:
            fragment = new HomePageFragment();
            args.putInt(HomePageFragment.ARG_SECTION_NUMBER, position + 1);
            break;
        case 1:
            fragment = new AppFragment();
            args.putInt(AppFragment.ARG_SECTION_NUMBER, position + 1);
            break;
        case 2:
            fragment = new SmsFragment();
            args.putInt(SmsFragment.ARG_SECTION_NUMBER, position + 1);
            break;
        }
        if (fragment != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        Locale l = Locale.getDefault();
        switch (position)
        {
        case 0:
            return context.getString(R.string.title_home);
        case 1:
            return context.getString(R.string.title_app);
        case 2:
            return context.getString(R.string.title_sms);
        }
        return null;
    }
}