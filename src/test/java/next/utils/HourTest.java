package next.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HourTest {
    @Test
    void 오전() {
        Hour hour = new Hour(11);
        assertThat(hour.getMessage()).isEqualTo("오전");
    }

    @Test
    void 오후() {
        Hour hour = new Hour(23);
        assertThat(hour.getMessage()).isEqualTo("오후");
    }
}
