package ru.zy2ba.tmtrck.entity.ActivityTypes;

import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

/**
 * Created by Zy2ba on 22.05.2015.
 */
public interface SettableActivity<T> {
    void setNum(int Num);
    void setPrepod(Prepod prepod);
    void setDate(LocalDate date) ;
    void setTypeOfLoad(TypeOfLoad typeOfLoad);
    double getHours();
    //public String getNameOfClass();
    TypeOfLoad getTypeOfLoad();
}
