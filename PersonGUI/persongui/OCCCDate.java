package persongui;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.text.DateFormatSymbols;

public class OCCCDate implements Serializable
{

    // Constants
    public static final boolean FORMAT_US = true;
    public static final boolean FORMAT_EURO = false;
    public static final boolean STYLE_NUMBERS = false;
    public static final boolean STYLE_NAMES = true;
    public static final boolean SHOW_DAY_NAME = true;
    public static final boolean HIDE_DAY_NAME = false;

    // Instance variables
    private int dayOfMonth;
    private int monthOfYear;
    private int year;
    private GregorianCalendar gc;
    private boolean dateFormat = FORMAT_US;
    private boolean dateStyle = STYLE_NAMES;
    private boolean dateDayName = SHOW_DAY_NAME;

    // Default constructor: current date
    public OCCCDate() {
        gc = new GregorianCalendar();
        updateFields();
    }

    // Constructor with day, month, year + validation
    public OCCCDate(int day, int month, int year) {
        if (!isValidDate(day, month, year)) {
            throw new InvalidOCCCDateException("Invalid date: " + month + "/" + day + "/" + year);
        }
        gc = new GregorianCalendar(year, month - 1, day);
        updateFields();
    }

    // Copy constructor
    public OCCCDate(OCCCDate d) {
        this(d.dayOfMonth, d.monthOfYear, d.year);
        this.dateFormat = d.dateFormat;
        this.dateStyle = d.dateStyle;
        this.dateDayName = d.dateDayName;
    }
    
    // From GregorianCalendar
    public OCCCDate(GregorianCalendar gc) {
        this.gc = (GregorianCalendar) gc.clone();
        updateFields();
    }

    // Helper to refresh fields
    private void updateFields() {
        this.dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
        this.monthOfYear = gc.get(Calendar.MONTH) + 1;
        this.year = gc.get(Calendar.YEAR);
    }

    // Validation using GregorianCalendar rollover check
    private boolean isValidDate(int day, int month, int year) {
        GregorianCalendar temp = new GregorianCalendar(year, month - 1, day);
        int testDay = temp.get(Calendar.DAY_OF_MONTH);
        int testMonth = temp.get(Calendar.MONTH) + 1;
        int testYear = temp.get(Calendar.YEAR);
        return (day == testDay && month == testMonth && year == testYear);
    }

    // Getters
    public int getDayofMonth() { return dayOfMonth; }
    public int getMonthNumber() { return monthOfYear; }
    public int getYear() { return year; }
    public String getDayName() {
        return new DateFormatSymbols().getWeekdays()[gc.get(Calendar.DAY_OF_WEEK)];
    }
    public String getMonthName() {
        return new DateFormatSymbols().getMonths()[monthOfYear - 1];
    }

    // toString: US format, Month names, Day name shown
    @Override
    public String toString() {
        String dateStr = String.format("%s %02d, %04d", getMonthName(), dayOfMonth, year);
        dateStr = getDayName() + ", " + dateStr;
        return dateStr;
    }

    // Setters
    public void setDateFormat(boolean df) {
        this.dateFormat = df;
    }

    public void setStyleFormat(boolean sf) {
        this.dateStyle = sf;
    }

    public void setDayName(boolean nf) {
        this.dateDayName = nf;
    }

    // Difference in years from now
    public int getDifferenceInYears() {
        GregorianCalendar now = new GregorianCalendar();
        int diff = now.get(Calendar.YEAR) - this.year;
        if (now.get(Calendar.DAY_OF_YEAR) < gc.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }

    // Difference in years between this and another OCCCDate
    public int getDifferenceInYears(OCCCDate d) {
        int diff = this.year - d.year;
        if (this.gc.get(Calendar.DAY_OF_YEAR) < d.gc.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }

    // Equals comparison
    public boolean equals(OCCCDate dob) {
        return (this.dayOfMonth == dob.dayOfMonth
                && this.monthOfYear == dob.monthOfYear
                && this.year == dob.year);
    }
}
