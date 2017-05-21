package com.example.android.notepad;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by i on 2017/5/21.
 */

public class DateUtil {

    /**
     * 将时间戳转换为日期
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if(format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(Long.valueOf(seconds)));
    }
}

