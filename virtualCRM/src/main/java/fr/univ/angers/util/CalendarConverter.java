package fr.univ.angers.util;

import java.util.Calendar;

public class CalendarConverter {

    // Private for static class
    private CalendarConverter() {
    }

    public static int getMonth(int month) {
        switch (month) {
            case 1 :
                return Calendar.JANUARY;
            case 2 :
                return Calendar.FEBRUARY;
            case 3 :
                return Calendar.MARCH;
            case 4 :
                return Calendar.APRIL;
            case 5 :
                return Calendar.MAY;
            case 6 :
                return Calendar.JUNE;
            case 7 :
                return Calendar.JULY;
            case 8 :
                return Calendar.AUGUST;
            case 9 :
                return Calendar.SEPTEMBER;
            case 10 :
                return Calendar.OCTOBER;
            case 11 :
                return Calendar.NOVEMBER;
            case 12 :
                return Calendar.DECEMBER;
            default :
                return 13;
        }
    }
}
