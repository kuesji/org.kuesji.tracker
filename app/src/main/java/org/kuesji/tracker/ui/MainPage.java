package org.kuesji.tracker.ui;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.kuesji.tracker.Constants;

import java.util.Arrays;
import java.util.List;

public class MainPage extends LinearLayout {

	String[] selections = new String[]{"check permission", "daily", "weekly", "monthly", "yearly"};

	OnClickListener selection_listener = (View v) -> {
		String title = ((Button)v).getText().toString();
		if( title.equals(selections[0]) ){
			checkPermission();
		} else {
			UsagesPage page = new UsagesPage(getContext());
			page.txt_title.setText(title);

			AlertDialog result = new AlertDialog.Builder(getContext())
			.setCancelable(true)
			.setView(page)
			.create();

			result.show();
			if( title.equals(selections[1]) ) {
				page.load(UsageStatsManager.INTERVAL_DAILY);
			} else if( title.equals(selections[2])){
				page.load(UsageStatsManager.INTERVAL_WEEKLY);
			} else if( title.equals(selections[3])){
				page.load(UsageStatsManager.INTERVAL_MONTHLY);
			} else if( title.equals(selections[4])){
				page.load(UsageStatsManager.INTERVAL_YEARLY);
			}
		}
	};

	public MainPage(Context context) {
		super(context);

		setBackgroundColor(Constants.color_background);
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		setPadding(dp2px(32), dp2px(32), dp2px(32), dp2px(32));
		setOnClickListener((v) -> { });

		for (String selection : selections) {
			Button button = new Button(context);
			button.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			button.setBackgroundColor(Constants.color_foreground);
			button.setTextColor(Constants.color_background);
			button.setText(selection);
			button.setAllCaps(false);
			button.setOnClickListener(selection_listener);
			addView(button);

			View x = new View(context);
			x.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(16)));
			addView(x);
		}

		removeView(getChildAt(getChildCount() - 1));
	}

	private void checkPermission(){
		UsageStatsManager usm = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
		List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,0,System.currentTimeMillis());

		if( stats.size() < 1 ){
			Toast.makeText(getContext(), "you need to give usage stats permission to app tracker", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
			getContext().startActivity(intent);
		} else {
			Toast.makeText(getContext(),"looks like you gave required permission already",Toast.LENGTH_SHORT).show();
		}
	}

	private int dp2px(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}
}
