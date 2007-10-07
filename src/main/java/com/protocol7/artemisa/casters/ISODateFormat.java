package com.protocol7.artemisa.casters;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
 * Limitations: century set as year, e.g. 20 => 2000 
 */
public class ISODateFormat extends DateFormat {

    private static final long serialVersionUID = 3860657714974299382L;

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo,
            FieldPosition fieldPosition) {
        throw new UnsupportedOperationException("format not supported");
    }

    public Calendar parseCalendar(String source) throws ParseException {
        source = source.trim();
        
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        
        int index = parseDate(source, calendar);
        
        parseTime(source, calendar, index);
        
        return calendar;
    }

    @Override
    public Date parse(String source) throws ParseException {
        return parseCalendar(source).getTime();
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        try {
            return parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private int parseDate(String source, Calendar calendar) {
        int index = 0;
        
        // check if we got a date
        if(hasDate(source)) {
            index = parseYear(calendar, source, index);
            
            int weekIndex = parseWeek(calendar, source, index);
            
            if(weekIndex == index) {
                // no week was found
                
                int dayIndex = parseOrdinalDay(calendar, source, index);
                
                if(dayIndex == index) {
                    // no ordinal day found
                    index = parseMonth(calendar, source, index);
                    index = parseDay(calendar, source, index);
                } else {
                    index = dayIndex;
                }
            } else {
                index = weekIndex;
                index = parseDayInWeek(calendar, source, index);
            }
        }
        return index;
    }

    private boolean hasDate(String source) {
        if(source.charAt(0) == 'T') {
            // time only
            return false;
        } else if(source.length() > 2 && source.charAt(2) == ':') {
            // time only
            return false;
        } else {
            return true;
        }
    }

    private int parseYear(Calendar calendar, String source, int startIndex) {
        int yearSign = +1;
        boolean hasSign = false;
        char sign = source.charAt(startIndex); 
        if(sign == '+') {
            startIndex++;
            hasSign = true;
        } else if(sign == '-') {
            startIndex++;
            yearSign = -1;
            hasSign = true;
        }
        
        
        int year;
        int endIndex;
        int leading = checkNumberOfLeadingDigits(source, startIndex); 
        if(leading == 2) {
            // we got a century
            endIndex = startIndex + 2;
            year = parseInt(source, startIndex, endIndex) * 100;
            
        } else if(leading >= 4) {
            if(hasSign) {
                int dashIndex = source.indexOf('-', startIndex);
                
                if(dashIndex > -1) {
                    endIndex = dashIndex; 
                } else {
                    endIndex = startIndex + 4;
                }
                
            } else {
                endIndex = startIndex + 4;
            }
            
            year = parseInt(source, startIndex, endIndex);
        } else {
            throw new InvalidDateException("Insufficient number of digits", startIndex);
        }
        
        calendar.set(Calendar.YEAR, year * yearSign);
        return endIndex;
    }

    private int parseMonth(Calendar calendar, String source, int startIndex) {
        if(source.length() == startIndex) {
            return startIndex;
        }
        
        if(source.charAt(startIndex) == '-') {
            startIndex ++;
        }
        
        int endIndex = startIndex + 2;
        calendar.set(Calendar.MONTH, parseInt(source, startIndex, endIndex) - 1);
        
        return endIndex;
    }

    private int parseWeek(Calendar calendar, String source, int startIndex) {
        int orgStartIndex = startIndex;
        
        if(source.length() == startIndex) {
            return startIndex;
        }
        
        if(source.charAt(startIndex) == '-') {
            startIndex ++;
        }
    
        if(source.charAt(startIndex) == 'W') {
            startIndex ++;
            int endIndex = startIndex + 2;
            calendar.set(Calendar.WEEK_OF_YEAR, parseInt(source, startIndex, endIndex));
            return endIndex;
        } else {
            return orgStartIndex;
        }
        
        
    }

    private int parseDayInWeek(Calendar calendar, String source, int startIndex) {
        if(source.length() == startIndex) {
            return startIndex;
        }
        
        if(source.charAt(startIndex) == '-') {
            startIndex ++;
        }
        
        if(Character.isDigit(source.charAt(startIndex))) {
            int endIndex = startIndex + 1;
            calendar.set(Calendar.DAY_OF_WEEK, parseInt(source, startIndex, endIndex));
            return endIndex;
        } else {
            return startIndex;
        }
        
    }

    private int parseOrdinalDay(Calendar calendar, String source, int startIndex) {
        int orgStartIndex = startIndex;
        
        if(source.length() == startIndex) {
            return startIndex;
        }
        
        if(source.charAt(startIndex) == '-') {
            startIndex ++;
        }
        
        // check if we got exactly three digits
        if(source.length() < startIndex + 3) {
            return orgStartIndex;
        }
        
        if(checkNumberOfLeadingDigits(source, startIndex) == 3) {
            int endIndex = startIndex + 3;
            calendar.set(Calendar.DAY_OF_YEAR, parseInt(source, startIndex, endIndex));
            return endIndex;
        } else {
            return orgStartIndex;
        }
    }

    private int parseDay(Calendar calendar, String source, int startIndex) {
        if(source.length() == startIndex) {
            return startIndex;
        }
        
        if(source.charAt(startIndex) == '-') {
            startIndex ++;
        }
        
        int endIndex = startIndex + 2;
        calendar.set(Calendar.DATE, parseInt(source, startIndex, endIndex));
        
        return endIndex;
    }

    private void parseTime(String source, Calendar calendar, int index) {
        if(source.length() > index && (source.charAt(index) == 'T' || index == 0)) {
            if(source.charAt(index) == 'T') {
                index ++;
            }
            index = parseHours(calendar, source, index);
            index = parseMinutes(calendar, source, index);
            index = parseSeconds(calendar, source, index);
            
            TimeZone tz = parseTimeZone(source, index);
            calendar.setTimeZone(tz);
        } else {
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
    }

    private int parseHours(Calendar calendar, String source, int startIndex) {
        return parseTimeToken(calendar, source, startIndex, Calendar.HOUR_OF_DAY);
    }
    
    private int parseMinutes(Calendar calendar, String source, int startIndex) {
        return parseTimeToken(calendar, source, startIndex, Calendar.MINUTE);
    }
    
    private int parseSeconds(Calendar calendar, String source, int startIndex) {
        return parseTimeToken(calendar, source, startIndex, Calendar.SECOND);
    }
    
    private int parseTimeToken(Calendar calendar, String source, int startIndex, int field) {
        if(source.length() == startIndex) {
            return startIndex;
        }
        
        if(source.charAt(startIndex) == ':') {
            startIndex ++;
        }
        
        int endIndex;
        if(Character.isDigit(source.charAt(startIndex))) {
            endIndex = startIndex + 2;
            calendar.set(field, parseInt(source, startIndex, endIndex));
        } else {
            endIndex = startIndex;
        }
        
        startIndex = endIndex;
        
        if(source.length() > startIndex && isCharInArray(source.charAt(startIndex), new char[]{'.', ','})) {
            // we got decimals
            startIndex++;
            int length = checkNumberOfLeadingDigits(source, startIndex);
            
            endIndex = startIndex + length;
            int dec = parseInt(source, startIndex, endIndex);
            double decimals = Double.parseDouble("0." + dec);
            
            if(field == Calendar.HOUR_OF_DAY) {
                int minutes = (int) (decimals * 60);
                calendar.set(Calendar.MINUTE, minutes);
    
                decimals -= (double)minutes / 60d;
                int seconds = (int) (decimals * 3600);
                calendar.set(Calendar.SECOND, seconds);
    
                decimals -=  (double)seconds / 3600d;
                calendar.set(Calendar.MILLISECOND, (int) Math.round(decimals * 10000000));
            } else if(field == Calendar.MINUTE) {
                int seconds = (int) (decimals * 60);
                calendar.set(Calendar.SECOND, seconds);
    
                decimals -= (double)seconds / 60d;
                calendar.set(Calendar.MILLISECOND, (int) Math.round(decimals * 100000));
            } else if(field == Calendar.SECOND) {
                calendar.set(Calendar.MILLISECOND, (int) Math.round(decimals * 1000));
                
            }
        }
        return endIndex;
    }

    private TimeZone parseTimeZone(String source, int startIndex) {
        TimeZone tz;
        if(source.length() == startIndex) {
            tz = TimeZone.getDefault();
        } else {
            tz = TimeZone.getTimeZone("UTC");
            
            char tzSign = source.charAt(startIndex);
            
            if(tzSign == 'Z') {
                // UTC
            } else if(tzSign == '+' || tzSign == '-'){
                startIndex++;
                int endIndex = startIndex + 2;
                int tzHours = parseInt(source, startIndex, endIndex);
                int tzMinutes = 0;
                int tzSeconds = 0;
                startIndex = endIndex;
                
                if(source.length() > startIndex) {
                    // we got TZ minutes
                    if(source.charAt(startIndex) == ':') {
                        startIndex++;
                    }
                    endIndex = startIndex + 2;
                    tzMinutes = parseInt(source, startIndex, endIndex);
                    startIndex = endIndex;

                    if(source.length() > startIndex) {
                        // we got TZ seconds
                        if(source.charAt(startIndex) == ':') {
                            startIndex++;
                        }
                        endIndex = startIndex + 2;
                        tzSeconds = parseInt(source, startIndex, endIndex);
                        startIndex = endIndex;
                    }
                }
                
                
                int tzMillis = ((tzHours * 60 + tzMinutes) * 60 + tzSeconds) * 1000;
                if(tzSign == '-') {
                    tzMillis = -tzMillis;
                }
                
                tz.setRawOffset(tzMillis);
            } else {
                throw new IllegalArgumentException("Invalid timezone");
            }
        }

        return tz;
    }

    private int parseInt(String source, int startIndex, int endIndex) {
        try {
            return Integer.parseInt(source.substring(startIndex, endIndex));
        } catch(NumberFormatException e) {
            throw new InvalidDateException("Integer expected at " + startIndex, startIndex, e);
        }
    }
    
    private int checkNumberOfLeadingDigits(String source, int startIndex) {
        int noOfDigits = 0;
        for(int i = startIndex; i<source.length(); i++) {
            if(Character.isDigit(source.charAt(i))) {
                noOfDigits++;
            } else {
                break;
            }
        }
        
        return noOfDigits;
    }

    private boolean isCharInArray(char c, char[] chars) {
        for (char d : chars) {
            if(c == d) {
                return true;
            }
        }
        return false;
    }

    
}
