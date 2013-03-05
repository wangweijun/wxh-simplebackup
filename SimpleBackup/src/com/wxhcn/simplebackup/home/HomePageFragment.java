/**
 * 
 */
package com.wxhcn.simplebackup.home;

import com.wxhcn.simplebackup.R;
import com.wxhcn.simplebackup.R.id;
import com.wxhcn.simplebackup.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		return rootView;
	}

}
