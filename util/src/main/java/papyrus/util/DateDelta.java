package papyrus.util;

import java.util.Calendar;
import java.util.Date;

public class DateDelta {
    public boolean isFuture;
    public int years;
    public int months;
    public int days;
    public int hours;
    public int minutes;
    public int seconds;

    public static DateDelta create(Calendar from, Calendar to) {
        DateDelta delta = new DateDelta();
        int comparison = to.compareTo(from);
        if (comparison == 0) {
            return delta;
        } else if (comparison < 0) {
            Calendar temp = from;
            from = to;
            to = temp;
            delta.isFuture = true;
        }
        delta.years = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
        delta.months = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        delta.days = to.get(Calendar.DAY_OF_MONTH) - from.get(Calendar.DAY_OF_MONTH);
        delta.hours = to.get(Calendar.HOUR_OF_DAY) - from.get(Calendar.HOUR_OF_DAY);
        delta.minutes = to.get(Calendar.MINUTE) - from.get(Calendar.MINUTE);
        delta.seconds = to.get(Calendar.SECOND) - from.get(Calendar.SECOND);


        if (delta.seconds < 0) {
            delta.minutes--;
            delta.seconds += from.getActualMaximum(Calendar.SECOND);
        }
        if (delta.minutes < 0) {
            delta.hours--;
            delta.minutes += from.getActualMaximum(Calendar.MINUTE);
        }
        if (delta.hours < 0) {
            delta.days--;
            delta.hours += from.getActualMaximum(Calendar.HOUR);
        }
        if (delta.days < 0) {
            delta.months--;
            delta.days += from.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (delta.months < 0) {
            delta.years--;
            delta.months += from.getActualMaximum(Calendar.MONTH);
        }

        return delta;
    }

    public static DateDelta create(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        from.setTime(fromDate);
        to.setTime(toDate);
        return create(from, to);
    }
}
