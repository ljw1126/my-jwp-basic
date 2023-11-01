package next.utils;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

public class MyCalendarTest {

    @Test
    void getHour() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 11);
        MyCalendar myCalendar = new MyCalendar(now);
        assertThat(myCalendar.getHour().getMessage()).isEqualTo("오전");
    }
}
