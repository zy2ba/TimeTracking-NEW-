package ru.zy2ba.tmtrck.manager;

import org.joda.time.LocalDate;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.entity.*;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 05.05.05
 */
public interface PairManager extends EntityManager<Pair> {
    void create(ArrayList<Pair> enitys);
    ArrayList<Pair> findByPrepod(Prepod prepod)throws HibernateJdbcException;
    boolean findPairDublicate(Pair entity);
    ArrayList<Pair> findDatesPrepod(Prepod prepod, ArrayList<PairDate> pairDates);
    ArrayList<Pair> findCarriedPairsForPrepodByDate(Prepod prepod,PairDate pairDate);
    ArrayList<Pair> searchCustom(String prepodName,String prepodLastName,LocalDate startDate, LocalDate finishDate,int isCarrieadOutStatus,int isOnHolidayStatus,int separateVia);
    ArrayList<Pair> searchCustom(String prepodName,String prepodLastName,int isCarrieadOutStatus,int isOnHolidayStatus,int separateVia);
}
