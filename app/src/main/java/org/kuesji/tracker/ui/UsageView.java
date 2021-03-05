package org.kuesji.tracker.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.kuesji.tracker.Constants;
import org.kuesji.tracker.utils.UsageInfo;

class UsageView extends LinearLayout {
	UsageInfo usage;

	ImageView logo;
	LinearLayout container_header;
	TextView label;
	TextView pkg;
	TextView time;


	public UsageView(Context ctx) {
		super(ctx);

		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
		setPadding(0,0,0,dp2px(16));

		logo = new ImageView(ctx);
		logo.setLayoutParams(new ViewGroup.LayoutParams(dp2px(64), dp2px(64)));
		logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		addView(logo);

		container_header = new LinearLayout(ctx);
		container_header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		container_header.setOrientation(LinearLayout.VERTICAL);
		addView(container_header);

		label = new TextView(ctx);
		label.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		label.setTextColor(Constants.color_foreground);
		label.setText("app name");
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		container_header.addView(label);

		pkg = new TextView(ctx);
		pkg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		pkg.setTextColor(Constants.color_foreground);
		pkg.setText("package");
		pkg.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		container_header.addView(pkg);

		time = new TextView(ctx);
		time.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		time.setTextColor(Constants.color_foreground);
		time.setText("time");
		time.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		container_header.addView(time);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (changed) {
			ViewGroup.LayoutParams params = logo.getLayoutParams();
			params.width = getWidth() / 10;
			params.height = getWidth() / 10;
			logo.setLayoutParams(params);

			int container_pad = getWidth() / 20;
			container_header.setPadding(container_pad, 0, 0, 0);
		}
	}

	public void load(UsageInfo usage) {
		this.usage = usage;

		pkg.setText(usage.packageName);
		time.setText(formatTime(usage.time));

		PackageManager pm = getContext().getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(usage.packageName, PackageManager.GET_META_DATA);
			label.setText(info.loadLabel(pm));
			logo.setImageDrawable(info.loadIcon(pm));
		} catch (PackageManager.NameNotFoundException e) {
			label.setText("deleted application");
			logo.setImageResource(android.R.drawable.sym_def_app_icon);
		}
	}

	String formatTime(long ms) {
		long chour = 3600;
		long cminute = 60;

		ms = ms / 1000;
		long hour = ms / chour;
		ms = ms % chour;
		long minute = ms / cminute;
		ms = ms % cminute;
		long second = ms;

		String result = "";

		if ( hour > 0 ){
			result += hour+" hours ";
		}

		result += minute + " minutes";

		return result;

	}

	private int dp2px(int dp){
		return (int)(dp*getResources().getDisplayMetrics().density);
	}
}
