/**
 * 
 */
package com.Cissoid.simplebackup.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.wxhcn.simplebackup.R;

/**
 * @author Wxh
 * 
 */
public class AppFragment extends ListFragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Applist applist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        applist = new Applist(getActivity());
        AppAdapter adapter = new AppAdapter(applist);
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment, container, false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_multi_select).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}
