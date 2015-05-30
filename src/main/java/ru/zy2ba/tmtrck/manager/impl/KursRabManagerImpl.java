package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.KursRabDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.DiplomaProject;
import ru.zy2ba.tmtrck.entity.ActivityTypes.KursRab;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.KursRabManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public class KursRabManagerImpl implements KursRabManager ,ActivityManager<KursRab> {
    @Autowired
    private KursRabDao kursRabDao;

    @Override
    public KursRab create(KursRab entity) throws HibernateJdbcException {
        return kursRabDao.saveAndFlush(entity);
    }

    @Override
    public KursRab update(KursRab entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public KursRab delete(KursRab entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public KursRab get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {
        double sumOfHours=0;
        for (KursRab kursRab:kursRabDao.findByPrepod(prepod)){
            sumOfHours+=kursRab.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<KursRab> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return kursRabDao.findByPrepodAndDate(prepod, date);
    }

    @Override
    public ArrayList<KursRab> getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate) {
        ArrayList<KursRab> kursRabs = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<KursRab> kursRabArrayList = kursRabDao.findByPrepodAndDate(prepod, d);
            for (KursRab kursRab:kursRabArrayList){
                kursRabs.add(kursRab);
            }
        }
        return kursRabs;
    }

    @Override
    public ArrayList<KursRab> getByPrepod(Prepod prepod) {
        return kursRabDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<KursRab> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<KursRab> kursRabs = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<KursRab> kursRabArrayList = kursRabDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (KursRab kursRab:kursRabArrayList){
                kursRabs.add(kursRab);
            }
        }
        return kursRabs;
    }

    @Override
    public ArrayList<KursRab> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return kursRabDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
