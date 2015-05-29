package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.ZachetDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Zachet;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.ZachetManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 29.05.2015.
 */
public class ZachetManagerImpl implements ActivityManager<Zachet>,ZachetManager {
    @Autowired
    private ZachetDao zachetDao;

    @Override
    public Zachet create(Zachet entity) throws HibernateJdbcException {
        return zachetDao.saveAndFlush(entity);
    }

    @Override
    public Zachet update(Zachet entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Zachet delete(Zachet entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Zachet get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {

        double sumOfHours=0;
        for (Zachet zachet:zachetDao.findByPrepod(prepod)){
            sumOfHours+=zachet.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<Zachet> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return zachetDao.findByPrepodAndDate(prepod,date);
    }

    @Override
    public ArrayList getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate) {
        ArrayList<Zachet> findexamArrayList = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Zachet> zachets = zachetDao.findByPrepodAndDate(prepod, d);
            for (Zachet zachet:zachets){
                findexamArrayList.add(zachet);
            }
        }
        return findexamArrayList;
    }

    @Override
    public ArrayList<Zachet> getByPrepod(Prepod prepod) {
        return zachetDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<Zachet> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<Zachet> findexamArrayList = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Zachet> zachets = zachetDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (Zachet zachet:zachets){
                findexamArrayList.add(zachet);
            }
        }
        return findexamArrayList;
    }

    @Override
    public ArrayList<Zachet> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return zachetDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
