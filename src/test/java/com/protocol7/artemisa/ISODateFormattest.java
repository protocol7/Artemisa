package com.protocol7.artemisa;

import java.util.Calendar;
import java.util.TimeZone;

import com.protocol7.artemisa.casters.ISODateFormat;
import com.protocol7.artemisa.casters.InvalidDateException;

import junit.framework.TestCase;

public class ISODateFormattest extends TestCase {

    public void testFull() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14.123+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullTrim() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("   2007-07-05T11:12:14.123+01:30\t");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testnNull() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar(null);
            fail("Must throw NPE");
        } catch(NullPointerException e) { 
            // ok
        }
    }

    public void testTimeOnly() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("11:12:14.123+01:30");
        
        assertDateTime(-1, -1, -1, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullWithWeekAndDay() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-W27-4T11:12:14.123+01:30");
        
        assertDateTimeWithWeek(2007, 27, 4, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullWithWeek() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-W27T11:12:14.123+01:30");
        
        assertDateTimeWithWeek(2007, 27, -1, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullOrdinalDay() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-234T11:12:14.123+01:30");
        
        assertDateTimeWithDay(2007, 234, 11, 12, 14, 123, 5400, calendar);
    }
    
    public void testFullWithMillisComma() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14,123+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullWithMillisTooManyDigits() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14,12312+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testNoMillis() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, -1, 5400, calendar);
    }
    
    public void testHoursDecimalsMillis() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11.505012+01:30");
        
        assertDateTime(2007, 7, 5, 11, 30, 18, 120, 5400, calendar);
    }

    public void testMinutsDecimalsMillis() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12.5012+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 30, 120, 5400, calendar);
    }
    
    public void testNoSeconds() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, -1, -1, 5400, calendar);
    }
    
    public void testNoMinutes() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11+01:30");
        
        assertDateTime(2007, 7, 5, 11, -1, -1, -1, 5400, calendar);
    }

    public void testDateOnly() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05");
        
        assertDateTime(2007, 7, 5, -1, -1, -1, -1, 0, calendar);
    }

    public void testNoDate() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07");
        
        assertDateTime(2007, 7, -1, -1, -1, -1, -1, 0, calendar);
    }

    public void testYearOnly() throws Exception {
        // must be parsed as a date, not a time
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007");
        
        assertDateTime(2007, -1, -1, -1, -1, -1, -1, 0, calendar);
    }

    public void testCentruryOnly() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("20");
        
        assertDateTime(2000, -1, -1, -1, -1, -1, -1, 0, calendar);
    }

    public void testHourAndMinutesOnly() throws Exception {
        // must be parsed as a time, not a date
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("T2007");
        
        assertDateTime(-1, -1, -1, 20, 7, -1, -1, -1, calendar);
    }

    public void testFullWithoutDashes() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("20070705T11:12:14.123+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }
    

    public void testZeroZeroTimes() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("20070705T00:00:00Z");
        
        assertDateTime(2007, 7, 5, 0, 0, 0, 0, 0, calendar);
    }
    
    public void testMidnight() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("20070705T24:00:00Z");
        
        assertDateTime(2007, 7, 5, 24, 0, 0, 0, 0, calendar);
        assertDateTime(2007, 7, 6, 0, 0, 0, 0, 0, calendar);
    }

    public void testFullWithoutDashesWithYearSign() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("+20070705T11:12:14.123+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullWithoutColons() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T111214.123+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullWithPlusSignInyear() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("+2007-07-05T11:12:14.123+01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testFullWithMinusSignInyear() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("-2007-07-05T11:12:14.123+01:30");
        
        assertDateTime(-2007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testY10KYear() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("+20007-07-05T11:12:14.123+01:30");
        
        assertDateTime(20007, 7, 5, 11, 12, 14, 123, 5400, calendar);
    }

    public void testNegativeTz() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14.123-01:30");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, -5400, calendar);
    }

    public void testTzWithZ() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14.123Z");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 0, calendar);
    }

    public void testTzWithSeconds() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14.123+01:30:34");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5434, calendar);
    }

    public void testTzWithSecondsNoColons() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14.123+013034");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, 5434, calendar);
    }
    
    public void testNoTz() throws Exception {
        ISODateFormat df = new ISODateFormat();
        Calendar calendar = df.parseCalendar("2007-07-05T11:12:14.123");
        
        assertDateTime(2007, 7, 5, 11, 12, 14, 123, -1, calendar);
    }
    
    private void assertDateTime(int expectedYear, int expectedMonth, int expectedDay, int expectedHour, int expectedMinute, int expectedSecond, int expectedMillis, int expectedTzSeconds, Calendar actual) {
        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        expected.clear();

        if(expectedYear != -1) {
            expected.set(Calendar.YEAR, expectedYear);
        }
        if(expectedMonth != -1) {
            expected.set(Calendar.MONTH, expectedMonth - 1);
        }
        if(expectedDay != -1) {
            expected.set(Calendar.DATE, expectedDay);
        }
        setAssertTime(expectedHour, expectedMinute, expectedSecond,
                expectedMillis, expectedTzSeconds, expected);
            
        assertEquals(expected, actual);
    }

    private void setAssertTime(int expectedHour, int expectedMinute,
            int expectedSecond, int expectedMillis, int expectedTzSeconds,
            Calendar expected) {
        if(expectedHour != -1) {
            expected.set(Calendar.HOUR_OF_DAY, expectedHour);
        }
        if(expectedMinute != -1) {
            expected.set(Calendar.MINUTE, expectedMinute);
        }
        if(expectedSecond != -1) {
            expected.set(Calendar.SECOND, expectedSecond);
        }
        if(expectedMillis != -1) {
            expected.set(Calendar.MILLISECOND, expectedMillis);
        }
        
        TimeZone tz = TimeZone.getTimeZone("UTC");
        if(expectedTzSeconds == -1) {
            tz = TimeZone.getDefault();
        } else {
            tz.setRawOffset(expectedTzSeconds * 1000);
        }
        expected.setTimeZone(tz);
    }

    private void assertDateTimeWithWeek(int expectedYear, int expectedWeek, int expectedDay, int expectedHour, int expectedMinute, int expectedSecond, int expectedMillis, int expectedTzSeconds, Calendar actual) {
        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        expected.clear();
        
        if(expectedYear != -1) {
            expected.set(Calendar.YEAR, expectedYear);
        }
        if(expectedWeek != -1) {
            expected.set(Calendar.WEEK_OF_YEAR, expectedWeek);
        }
        if(expectedDay != -1) {
            expected.set(Calendar.DAY_OF_WEEK, expectedDay);
        }
        setAssertTime(expectedHour, expectedMinute, expectedSecond,
                expectedMillis, expectedTzSeconds, expected);
        
        assertEquals(expected, actual);
    }

    private void assertDateTimeWithDay(int expectedYear, int expectedDay, int expectedHour, int expectedMinute, int expectedSecond, int expectedMillis, int expectedTzSeconds, Calendar actual) {
        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        expected.clear();
        
        if(expectedYear != -1) {
            expected.set(Calendar.YEAR, expectedYear);
        }
        if(expectedDay != -1) {
            expected.set(Calendar.DAY_OF_YEAR, expectedDay);
        }
        setAssertTime(expectedHour, expectedMinute, expectedSecond,
                expectedMillis, expectedTzSeconds, expected);
        
        assertEquals(expected, actual);
    }

    public void testInvalidYearWithLetters() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("20bb-bb-05T11:12:14.123+01:30");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }
    
    public void testInvalidMonthWithLetters() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("2007-bb-05T11:12:14.123+01:30");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }

    public void testInvalidDayWithLetters() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("2007-07-ddT11:12:14.123+01:30");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }

    public void testInvalidHourWithLetters() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("2007-07-ddTbb:12:14.123+01:30");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }
    
    public void testInvalidMinutesWithLetters() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("2007-07-ddT11:bb:14.123+01:30");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }
    
    public void testInvalidSecondsWithLetters() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("2007-07-ddT11:12:bb.123+01:30");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }
    
    public void testThreeDigits() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("200");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }
    
    public void testYearWithInvalidSign() throws Exception {
        ISODateFormat df = new ISODateFormat();
        
        try {
            df.parseCalendar("/2007");
            fail("Must throw InvalidDateException");
        } catch(InvalidDateException e) {
            // ok
        }
    }
    

    
}
