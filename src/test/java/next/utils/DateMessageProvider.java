package next.utils;

import java.util.Calendar;

public class DateMessageProvider {
    public String getDateMessage(Calendar now) {
        int hour = now.get(Calendar.HOUR_OF_DAY);

        return hour < 12 ? "오전" : "오후";
    }
}
