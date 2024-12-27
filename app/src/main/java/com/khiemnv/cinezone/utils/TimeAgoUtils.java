package com.khiemnv.cinezone.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class TimeAgoUtils {

    // Hàm trả về thời gian theo kiểu "hôm qua", "1 tuần trước", "3 ngày trước", v.v.
    public static String getTimeAgo(long timestamp) {
        PrettyTime prettyTime = new PrettyTime();
        Date date = new Date(timestamp);
        return prettyTime.format(date);
    }

    // Hàm định dạng thời gian watchedAt thành chuỗi "hôm qua", "1 tuần trước", v.v.
    public static String formatTimestamp(long timestamp) {
        return getTimeAgo(timestamp);
    }
}
