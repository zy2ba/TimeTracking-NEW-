package ru.zy2ba.tmtrck.entity;

import org.joda.time.LocalDate;

/**
 * Created by Zy2ba on 22.05.2015.
 */
public interface TimeTableElement {
    public LocalDate getDate();
    public String getDayOfWeekString();
    public int getNum();
    public String getTime();
    public String getLocation();
    public String getGroupSpacer(int length);
    public String getName();
    public String getStringIsCarriedOut(boolean lowCaseFactor);
    public boolean getIsCarriedOut();
    public boolean getIsOnHoliday();
}
