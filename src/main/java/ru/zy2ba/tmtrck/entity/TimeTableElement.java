package ru.zy2ba.tmtrck.entity;

import org.joda.time.LocalDate;

/**
 * Created by Zy2ba on 22.05.2015.
 */
public interface TimeTableElement {
    LocalDate getDate();
    String getDayOfWeekString();
    int getNum();
    String getTime();
    String getLocation();
    String getGroupSpacer(int length);
    String getName();
    String getStringIsCarriedOut(boolean lowCaseFactor);
    boolean getIsCarriedOut();
    boolean getIsOnHoliday();
}
