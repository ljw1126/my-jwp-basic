package next.utils;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

public class DateMessageProviderTest {

    @Test
    void 오전() {
        DateMessageProvider dp = new DateMessageProvider();
        assertThat(dp.getDateMessage(createCurrentDate(11))).isEqualTo("오전");
    }

    @Test
    void 오후() {
        DateMessageProvider dp = new DateMessageProvider();
        assertThat(dp.getDateMessage(createCurrentDate(23))).isEqualTo("오후");
    }

    private Calendar createCurrentDate(int hourOfDay) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        return now;
    }

}
