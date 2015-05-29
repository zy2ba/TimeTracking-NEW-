package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.ConsultDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Consult;
import ru.zy2ba.tmtrck.entity.PairDate;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.ConsultManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public class ConsultManagerImpl implements ConsultManager,ActivityManager<Consult>{
    @Autowired
    private ConsultDao consultDao;

    @Override
    public Consult create(Consult entity) throws HibernateJdbcException {
        return consultDao.saveAndFlush(entity);
    }

    @Override
    public Consult update(Consult entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Consult delete(Consult entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Consult get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {
        double sumOfHours=0;
        for (Consult consult:consultDao.findByPrepod(prepod)){
            sumOfHours+=consult.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<Consult> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return consultDao.findByPrepodAndDate(prepod,date);
    }

    @Override
    public ArrayList<Consult> getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate)  {
        ArrayList<Consult> findConsults = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Consult> consultsOnDate = consultDao.findByPrepodAndDate(prepod, d);
            for (Consult consult:consultsOnDate){
                findConsults.add(consult);
            }
        }
        return findConsults;
    }

    @Override
    public ArrayList<Consult> getByPrepod(Prepod prepod) {
        return consultDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<Consult> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<Consult> findConsults = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Consult> consultsOnDate = consultDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (Consult consult:consultsOnDate){
                findConsults.add(consult);
            }
        }
        return findConsults;
    }

    @Override
    public ArrayList<Consult> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return consultDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
