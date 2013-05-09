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
import android.widget.CheckBox;

import com.wxhcn.simplebackup.R;

/**
 * @author Wxh
 * 
 */
public class AppFragment extends ListFragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Applist applist;
    private AppAdapter appAdapter = null;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        applist = new Applist(getActivity());
        appAdapter = new AppAdapter(this, applist);
        setListAdapter(appAdapter);
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
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
        case R.id.action_multi_select:
            applist.setMultiSelect(!applist.isMultiSelect());
            enterMultiMode();
            refresh();
            break;
        case R.id.action_select_all:
            selectAll();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enterMultiMode()
    {
        menu.setGroupVisible(R.id.MENU_GROUP_MAIN, false);
        menu.setGroupVisible(R.id.MENU_GROUP_MULTI, true);
    }

    public void refresh()
    {
        appAdapter.notifyDataSetChanged();
    }

    public void selectAll()
    {
        for (int i = 0; i < getListView().getChildCount(); i++)
        {
            View view = (View) getListView().getChildAt(i);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.app_check);
            checkBox.setChecked(true);
            appAdapter.setSelectAll(true);
        }
    }

}
