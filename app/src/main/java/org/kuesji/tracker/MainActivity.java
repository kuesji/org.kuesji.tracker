package org.kuesji.tracker;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;
import org.kuesji.tracker.ui.MainPage;

import java.util.List;

public class MainActivity extends Activity {

	MainPage mainPage;

	protected void onCreate(Bundle savedState){
		super.onCreate(savedState);

		getWindow().setStatusBarColor(Constants.color_background);
		getWindow().setNavigationBarColor(Constants.color_background);
		getWindow().getDecorView().setBackgroundColor(Constants.color_background);

		mainPage = new MainPage(this);
		setContentView(mainPage);
	}

}
