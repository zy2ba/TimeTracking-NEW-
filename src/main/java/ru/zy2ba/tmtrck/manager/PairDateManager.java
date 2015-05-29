package ru.zy2ba.tmtrck.manager;

import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.PairDate;

import org.springframework.orm.hibernate3.HibernateJdbcException;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 07.05.2015
 * Created by Zy2ba on 07.05.2015.
 */
public interface PairDateManager extends EntityManager<PairDate>{
    PairDate findByDate(LocalDate dateTime) throws HibernateJdbcException;
    ArrayList findByDate(LocalDate startDate,LocalDate finishDate);
    PairDate findByDateAndHoliday(LocalDate date,boolean holiday);
    ArrayList<PairDate> findByDateAndHoliday(LocalDate startDate,LocalDate finishDate, boolean holiday);
    void setHoliday(LocalDate date,boolean holiday)throws HibernateJdbcException;
}
