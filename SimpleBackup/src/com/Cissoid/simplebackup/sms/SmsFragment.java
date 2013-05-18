package com.Cissoid.simplebackup.sms;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.Cissoid.simplebackup.MainActivity;
import com.wxhcn.simplebackup.R;

public class SmsFragment extends ListFragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";

    private Smslist smslist;
    private SmsAdapter smsAdapter = null;
    private Menu menu;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        smslist = new Smslist((MainActivity) getActivity());
        smsAdapter = new SmsAdapter(this, smslist);
        this.setListAdapter(smsAdapter);
    }

    @Override
    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState )
    {
        // setEmptyText(getActivity().getString(R.string.sms_list_no_message));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // @Override
    // public void onCreateOptionsMenu( Menu menu , MenuInflater inflater )
    // {
    // menu.clear();
    // getActivity().getMenuInflater().inflate(R.menu.main, menu);
    // menu.findItem(R.id.menu_multi_select).setVisible(true);
    // this.menu = menu;
    // // super.onCreateOptionsMenu(menu, inflater);
    // // }
    //
    // @Override
    // public boolean onContextItemSelected( MenuItem item )
    // {
    // switch ( item.getItemId() )
    // {
    // case R.id.menu_multi_select :
    // enterMultiMode();
    // break;
    // case R.id.menu_select_all :
    // selectAll(true);
    // break;
    // case R.id.menu_accept :
    //
    // break;
    // case R.id.menu_cancel :
    // selectAll(false);
    // break;
    // }
    // return super.onContextItemSelected(item);
    // }
    //
    // /**
    // * 进入多选模式
    // */
    // private void enterMultiMode()
    // {
    // menu.setGroupVisible(R.id.MENU_GROUP_MAIN, false);
    // menu.setGroupVisible(R.id.MENU_GROUP_MULTI, true);
    // }
    //
    // /**
    // * 全选
    // *
    // * @param flag
    // */
    // private void selectAll( boolean flag )
    // {
    // for ( int i = 0 ; i < getListView().getChildCount() ; i++ )
    // {
    // View view = (View) getListView().getChildAt(i);
    // CheckBox checkBox = (CheckBox) view.findViewById(R.id.sms_check);
    // checkBox.setChecked(flag);
    // smsAdapter.setSelectAll(flag);
    // }
    // }
}
