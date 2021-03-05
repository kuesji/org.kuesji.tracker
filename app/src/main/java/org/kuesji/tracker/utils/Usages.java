package org.kuesji.tracker.utils;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.*;
import java.util.stream.Collectors;

public class Usages {
	private static List<UsageInfo> query(Context context, int interval, long begin, long end) {
		UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
		List<UsageStats> stats = usm.queryUsageStats(interval, begin, end);

		return unique(stats).stream().filter((x) -> x.time > 60 * 1000).sorted(new Comparator<UsageInfo>() {
			public int compare(UsageInfo u1, UsageInfo u2) {
				return u1.time < u2.time ? 1 : -1;
			}
		}).collect(Collectors.toList());
	}

	private static List<UsageInfo> unique(List<UsageStats> input) {
		HashMap<String, UsageInfo> map = new HashMap();

		for (UsageStats stats : input) {
			if (map.containsKey(stats.getPackageName())) {
				UsageInfo usage = map.get(stats.getPackageName());
				usage.time += stats.getTotalTimeInForeground();
				map.put(stats.getPackageName(), usage);
			} else {
				UsageInfo usage = new UsageInfo(stats.getPackageName(), stats.getTotalTimeInForeground());
				map.put(stats.getPackageName(), usage);
			}
		}

		return new ArrayList(map.values());
	}

	public static List<UsageInfo> query(Context context, int interval) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		if( interval == UsageStatsManager.INTERVAL_WEEKLY ){
			calendar.set(Calendar.DAY_OF_YEAR, -7);
		} else if( interval == UsageStatsManager.INTERVAL_MONTHLY ){
			calendar.set(Calendar.MONTH, -1);
		} else if( interval == UsageStatsManager.INTERVAL_YEARLY ){
			calendar.set(Calendar.YEAR, -1);
		}

		return query(context, interval, calendar.getTimeInMillis(), System.currentTimeMillis());
	}
}
