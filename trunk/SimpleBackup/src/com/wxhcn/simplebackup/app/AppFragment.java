/**
 * 
 */
package com.wxhcn.simplebackup.app;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wxhcn.simplebackup.R;

/**
 * @author Wxh
 * 
 */
public class AppFragment extends ListFragment {
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppAdapter adapter = new AppAdapter(getActivity(), getData());
		setListAdapter(adapter);
	}

//	public void onListItemClick(ListView parent, View v, int position, long id) {
//		Map<String, Object> map = (Map<String, Object>) parent
//				.getItemAtPosition(position);
//		String str = (String) map.get("app_title");
//		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//	}

	// public List<Map<String, Object>> getData() {
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// List<PackageInfo> packages = getActivity().getPackageManager()
	// .getInstalledPackages(0);
	// Map<String, Object> map = new HashMap<String, Object>();
	//
	// for (int i = 0; i < packages.size(); i++) {
	// PackageInfo packageInfo = packages.get(i);
	// if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==
	// 0) {
	// map = new HashMap<String, Object>();
	// map.put("app_title",
	// packageInfo.applicationInfo.loadLabel(
	// getActivity().getPackageManager()).toString());
	// map.put("app_info", packageInfo.versionName);
	// map.put("app_icon", packageInfo.applicationInfo
	// .loadIcon(getActivity().getPackageManager()));
	// list.add(map);
	// Log.i("test",
	// packageInfo.applicationInfo.loadLabel(
	// getActivity().getPackageManager()).toString());
	// Log.i("test", i + "");
	// }
	// }
	//
	// return list;
	// }

	public ArrayList<AppInfo> getData() {
		ArrayList<AppInfo> arrayList = new ArrayList<AppInfo>();
		AppInfo appInfo;
		List<PackageInfo> packages = getActivity().getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				appInfo = new AppInfo();
				appInfo.appName = packageInfo.applicationInfo.loadLabel(
						getActivity().getPackageManager()).toString();
				appInfo.versionName = packageInfo.versionName;
				appInfo.appIcon = packageInfo.applicationInfo
						.loadIcon(getActivity().getPackageManager());
				arrayList.add(appInfo);
			}
		}
		return arrayList;
	}
}
