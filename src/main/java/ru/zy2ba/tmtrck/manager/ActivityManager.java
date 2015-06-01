package ru.zy2ba.tmtrck.manager;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 21.05.2015
 */
public interface ActivityManager<T> extends EntityManager<T>{
    double getNumberOf(Prepod prepod) throws HibernateException;
    ArrayList<T> getByPrepodAndDate(Prepod prepod, LocalDate date);
    ArrayList<T> getByPrepodAndDate(Prepod prepod,LocalDate startDate,LocalDate finishDate);
    ArrayList<T> getByPrepod(Prepod prepod);

    ArrayList<T> getByPrepodDateTypeOfLoad(Prepod prepod,LocalDate startDate,LocalDate finishDate,TypeOfLoad typeOfLoad);
    ArrayList<T> getByPrepodDateTypeOfLoad(Prepod prepod,LocalDate date, @SuppressWarnings("SameParameterValue") TypeOfLoad typeOfLoad);
}
