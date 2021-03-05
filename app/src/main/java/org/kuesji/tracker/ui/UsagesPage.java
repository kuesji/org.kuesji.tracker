package org.kuesji.tracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.kuesji.tracker.Constants;
import org.kuesji.tracker.utils.UsageInfo;
import org.kuesji.tracker.utils.Usages;

import java.util.List;

public class UsagesPage extends LinearLayout {

	TextView txt_title;
	ScrollView scroller_content;
	LinearLayout layout_content;

	public UsagesPage(Context context) {
		super(context);

		setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		setBackgroundColor(Constants.color_background);
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		setPadding(dp2px(32), dp2px(32), dp2px(32), dp2px(32));
		setOnClickListener((v) -> {
		});

		txt_title = new TextView(context);
		txt_title.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		txt_title.setTextColor(Constants.color_foreground);
		txt_title.setGravity(Gravity.CENTER);
		txt_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		txt_title.setPadding(0,0,0,dp2px(32));
		addView(txt_title);

		scroller_content = new ScrollView(context);
		scroller_content.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		scroller_content.setFillViewport(true);
		scroller_content.setVerticalScrollBarEnabled(false);
		scroller_content.setHorizontalScrollBarEnabled(false);
		addView(scroller_content);

		layout_content = new LinearLayout(context);
		layout_content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		layout_content.setOrientation(VERTICAL);
		scroller_content.addView(layout_content);
	}

	public void load(int interval){
		layout_content.removeAllViews();

		TextView waiting_text = new TextView(getContext());
		waiting_text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		waiting_text.setText("loading, please wait");
		waiting_text.setGravity(Gravity.CENTER);
		waiting_text.setBackgroundColor(Constants.color_background);
		waiting_text.setTextColor(Constants.color_foreground);

		layout_content.addView(waiting_text);

		new Thread(()->{
			List<UsageInfo> usages = Usages.query(getContext(),interval);

			Handler handler = getHandler();

			if( handler != null ) {

				handler.postDelayed(() -> {
					layout_content.removeAllViews();

					if( usages.size() < 1 ){
						TextView error_text = new TextView(getContext());
						error_text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						error_text.setText("no data, check permission before trying again");
						error_text.setGravity(Gravity.CENTER);
						error_text.setBackgroundColor(Constants.color_background);
						error_text.setTextColor(Constants.color_foreground);
						layout_content.addView(error_text);
					} else {
						for (UsageInfo info : usages) {
							UsageView v = new UsageView(getContext());
							v.load(info);
							layout_content.addView(v);
						}
					}

				}, 16);

			}
		}).start();
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	private int dp2px(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}
}
