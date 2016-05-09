package com.lile.dl.test.main;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;

import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.utils.DLConstants;
import com.ryg.utils.DLUtils;

public class MainActivity extends Activity {

	private ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mPluginItems.size() > 0) {
				Intent it = new Intent(
						"com.ryg.dynamicload.proxy.activity");
				it.putExtra(DLConstants.EXTRA_PACKAGE,
						mPluginItems.get(0).packageInfo.packageName).putExtra(
						DLConstants.EXTRA_CLASS,
						mPluginItems.get(0).packageInfo.activities[0].name);

				startActivity(it);
			}
		}
		return super.onTouchEvent(event);
	}

	private void init() {
		String pluginFolder = Environment.getExternalStorageDirectory()
				+ "/DynamicAct";
		File file = new File(pluginFolder);
		File[] plugins = file.listFiles();
		if (plugins == null || plugins.length == 0) {
			return;
		}
		for (File plugin : plugins) {
			PluginItem item = new PluginItem();
			item.pluginPath = plugin.getAbsolutePath();
			item.packageInfo = DLUtils.getPackageInfo(this, item.pluginPath);
			if (item.packageInfo.activities != null
					&& item.packageInfo.activities.length > 0) {
				item.launcherActivityName = item.packageInfo.activities[0].name;
			}
			if (item.packageInfo.services != null
					&& item.packageInfo.services.length > 0) {
				item.launcherServiceName = item.packageInfo.services[0].name;
			}
			mPluginItems.add(item);
			DLPluginManager.getInstance(this).loadApk(plugin.getAbsolutePath());
		}
	}

	public static class PluginItem {
		public PackageInfo packageInfo;
		public String pluginPath;
		public String launcherActivityName;
		public String launcherServiceName;

		public PluginItem() {
		}
	}
}
