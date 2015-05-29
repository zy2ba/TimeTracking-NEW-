package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.GAKDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.GAK;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.GAKManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 29.05.2015.
 */
public class GAKManagerImpl implements GAKManager,ActivityManager<GAK> {
    @Autowired
    private GAKDao gakDao;

    @Override
    public GAK create(GAK entity) throws HibernateJdbcException {
        return gakDao.saveAndFlush(entity);
    }

    @Override
    public GAK update(GAK entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public GAK delete(GAK entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public GAK get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {
        double sumOfHours=0;
        for (GAK consult:gakDao.findByPrepod(prepod)){
            sumOfHours+=consult.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<GAK> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return gakDao.findByPrepodAndDate(prepod,date);
    }

    @Override
    public ArrayList<GAK> getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate)  {
        ArrayList<GAK> findConsults = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<GAK> gaks = gakDao.findByPrepodAndDate(prepod, d);
            for (GAK gak:gaks){
                findConsults.add(gak);
            }
        }
        return findConsults;
    }

    @Override
    public ArrayList<GAK> getByPrepod(Prepod prepod) {
        return gakDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<GAK> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<GAK> findConsults = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<GAK> consultsOnDate = gakDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (GAK consult:consultsOnDate){
                findConsults.add(consult);
            }
        }
        return findConsults;
    }

    @Override
    public ArrayList<GAK> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return gakDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
