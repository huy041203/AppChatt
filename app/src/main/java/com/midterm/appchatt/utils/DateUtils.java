package com.midterm.appchatt.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String getTimeAgo(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - timestamp;
        
        // Chuyển đổi sang giây
        long diffSeconds = timeDiff / 1000;
        
        if (diffSeconds < 60) {
            return "Vừa xong";
        }
        
        // Chuyển đổi sang phút
        long diffMinutes = diffSeconds / 60;
        if (diffMinutes < 60) {
            return diffMinutes + " phút trước";
        }
        
        // Chuyển đổi sang giờ
        long diffHours = diffMinutes / 60;
        if (diffHours < 24) {
            return diffHours + " giờ trước";
        }
        
        // Kiểm tra xem có phải hôm qua không
        Calendar now = Calendar.getInstance();
        Calendar messageTime = Calendar.getInstance();
        messageTime.setTime(new Date(timestamp));
        
        if (now.get(Calendar.DATE) - messageTime.get(Calendar.DATE) == 1) {
            return "Hôm qua";
        }
        
        // Nếu quá 1 ngày, hiển thị ngày/tháng
        return String.format("%d/%d", 
            messageTime.get(Calendar.DATE),
            messageTime.get(Calendar.MONTH) + 1);
    }
} 