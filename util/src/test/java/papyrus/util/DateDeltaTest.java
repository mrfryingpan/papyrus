package papyrus.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DateDeltaTest {
    private static SimpleDateFormat testFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Date dateFor(String dateString) {
        try {
            return testFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    @Test
    public void basicUsage() {
        DateDelta delta = DateDelta.create(dateFor("2018-12-08 12:00:00"), dateFor("2019-12-08 12:00:00"));
        assertEquals(delta.years, 1);
        assertEquals(delta.months, 0);
        assertEquals(delta.days, 0);
        assertEquals(delta.hours, 0);
        assertEquals(delta.minutes, 0);
        assertEquals(delta.seconds, 0);

        delta = DateDelta.create(dateFor("2010-12-08 12:00:00"), dateFor("2019-12-08 12:00:00"));
        assertEquals(delta.years, 9);
        assertEquals(delta.months, 0);
        assertEquals(delta.days, 0);
        assertEquals(delta.hours, 0);
        assertEquals(delta.minutes, 0);
        assertEquals(delta.seconds, 0);

        delta = DateDelta.create(dateFor("2018-12-08 10:44:30"), dateFor("2018-12-18 12:00:00"));
        assertEquals(delta.years, 0);
        assertEquals(delta.months, 0);
        assertEquals(delta.days, 10);
        assertEquals(delta.hours, 1);
        assertEquals(delta.minutes, 15);
        assertEquals(delta.seconds, 30);

        delta = DateDelta.create(dateFor("2016-11-08 10:44:30"), dateFor("2018-12-18 12:00:00"));
        assertEquals(delta.years, 2);
        assertEquals(delta.months, 1);
        assertEquals(delta.days, 10);
        assertEquals(delta.hours, 1);
        assertEquals(delta.minutes, 15);
        assertEquals(delta.seconds, 30);

        delta = DateDelta.create(dateFor("1000-11-08 10:44:30"), dateFor("2018-12-18 12:00:00"));
        assertEquals(delta.years, 1018);
        assertEquals(delta.months, 1);
        assertEquals(delta.days, 10);
        assertEquals(delta.hours, 1);
        assertEquals(delta.minutes, 15);
        assertEquals(delta.seconds, 30);

        delta = DateDelta.create(dateFor("2018-12-18 12:00:00"), dateFor("2016-11-08 10:44:30"));
        assertTrue(delta.isFuture);
        assertEquals(delta.years, 2);
        assertEquals(delta.months, 1);
        assertEquals(delta.days, 10);
        assertEquals(delta.hours, 1);
        assertEquals(delta.minutes, 15);
        assertEquals(delta.seconds, 30);
    }
}