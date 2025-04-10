package org.example.uthteammatching.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component("timeAgoUtil")
public class TimeAgoUtil {
    public String toRelative(Date date) {
        if (date == null) return "";

        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");

        LocalDateTime postTime = date.toInstant().atZone(zone).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now(zone);
        System.out.println("Thoi gian dang" + postTime);
        Duration duration = Duration.between(postTime, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "Vừa xong";
        } else if (seconds < 3600) {
            return (seconds / 60) + " phút trước";
        } else if (seconds < 86400) {
            return (seconds / 3600) + " giờ trước";
        } else {
            return (seconds / 86400) + " ngày trước";
        }
    }
}
