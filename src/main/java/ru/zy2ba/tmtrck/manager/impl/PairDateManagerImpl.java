package ru.zy2ba.tmtrck.manager.impl;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ru.zy2ba.tmtrck.dao.PairDateDao;
import ru.zy2ba.tmtrck.entity.Pair;
import ru.zy2ba.tmtrck.entity.PairDate;
import ru.zy2ba.tmtrck.manager.PairDateManager;

import org.springframework.orm.hibernate3.HibernateJdbcException;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 07.05.2015
 * Created by Zy2ba on 07.05.2015.
 */
public class PairDateManagerImpl implements PairDateManager {
    private static final Sort SORT_DATE = new Sort("date");
    @Autowired
    private
    PairDateDao pairDateDao;

    @Override
    public PairDate findByDate(LocalDate dateTime) throws HibernateJdbcException {
        return pairDateDao.findByDate(dateTime);
    }

    @Override
    public ArrayList findByDate(LocalDate startDate, LocalDate finishDate) {
        ArrayList<PairDate> findDates = new ArrayList<>();
        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            PairDate date= pairDateDao.findByDate(d);
            if(date!=null) findDates.add(date);
        }
        return findDates;
    }

    @Override
    public PairDate findByDateAndHoliday(LocalDate date, boolean holiday) {

        return pairDateDao.findByDateAndHoliday(date, holiday);
    }

    @Override
    public ArrayList<PairDate> findByDateAndHoliday(LocalDate startDate, LocalDate finishDate, boolean holiday) {
        ArrayList<PairDate> findDates = new ArrayList<>();
        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            PairDate date = pairDateDao.findByDateAndHoliday(d, holiday);
            if(date!=null) findDates.add(pairDateDao.findByDateAndHoliday(d, holiday));
        }
        return findDates;
    }

    @Override
    public void setHoliday(LocalDate date, boolean holiday) throws HibernateJdbcException{
        PairDate persistDate = pairDateDao.findByDate(date);
        if(persistDate != null && persistDate.getId()>-1){
            persistDate.setHoliday(holiday);
            pairDateDao.saveAndFlush(persistDate);
        }
    }

    @Override
    public PairDate create(PairDate entity) throws HibernateJdbcException{
        PairDate persistDate = pairDateDao.findByDate(entity.getDate());
        if(persistDate != null && persistDate.getId()>-1){
            return persistDate;
            //entity.setId(persistDate.getId());
        }
        entity = pairDateDao.saveAndFlush(entity);

        return entity;
    }

    @Override
    public PairDate update(PairDate entity) throws HibernateJdbcException{
        return pairDateDao.saveAndFlush(entity);
    }

    @Override
    public PairDate delete(PairDate entity) throws HibernateJdbcException{
        return null;
    }

    @Override
    public PairDate get(long entityId) throws HibernateJdbcException{
        return pairDateDao.findOne(entityId);
    }


}
