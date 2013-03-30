/**
 * 
 */
package com.wxhcn.simplebackup.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wxhcn.simplebackup.R;

/**
 * @author Wxh
 * 
 */
public class AppFragment extends ListFragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Applist            applist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        applist = new Applist(getActivity());
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        AppAdapter adapter = new AppAdapter(getActivity(), applist);
        setListAdapter(adapter);
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_multi_select).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}
