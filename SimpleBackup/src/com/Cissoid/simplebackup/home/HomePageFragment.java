/**
 * 
 */
package com.Cissoid.simplebackup.home;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wxhcn.simplebackup.R;

/**
 * @author Wxh
 * 
 */
public class HomePageFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    public HomePageFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.homepage, container, false);
        TextView dummyTextView = (TextView) rootView
                .findViewById(R.id.textView);
        dummyTextView.setText("homepage");
        setHasOptionsMenu(true);
        return rootView;
    }

    // @Override
    // public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // // TODO Auto-generated method stub
    // super.onCreateOptionsMenu(menu, inflater);
    // menu.clear();
    // getActivity().getMenuInflater().inflate(R.menu.main, menu);
    // }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_multi_select).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_multi_select)
            Toast.makeText(getActivity(), "h", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
