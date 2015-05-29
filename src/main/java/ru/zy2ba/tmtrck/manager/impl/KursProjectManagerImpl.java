package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.KursProjectDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.KursProject;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.KursProjectManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public class KursProjectManagerImpl implements KursProjectManager ,ActivityManager<KursProject> {
    @Autowired
    private KursProjectDao kursProjectDao;

    @Override
    public KursProject create(KursProject entity) throws HibernateJdbcException {
        return kursProjectDao.saveAndFlush(entity);
    }

    @Override
    public KursProject update(KursProject entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public KursProject delete(KursProject entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public KursProject get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {
        double sumOfHours=0;
        for (KursProject kursProject:kursProjectDao.findByPrepod(prepod)){
            sumOfHours+=kursProject.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<KursProject> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return kursProjectDao.findByPrepodAndDate(prepod, date);
    }

    @Override
    public ArrayList getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate) {
        ArrayList<KursProject> kursProjects = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<KursProject> kursProjectArrayList = kursProjectDao.findByPrepodAndDate(prepod, d);
            for (KursProject kursProject:kursProjectArrayList){
                kursProjects.add(kursProject);
            }
        }
        return kursProjects;
    }

    @Override
    public ArrayList<KursProject> getByPrepod(Prepod prepod) {
        return kursProjectDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<KursProject> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<KursProject> kursProjects = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<KursProject> kursProjectArrayList = kursProjectDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (KursProject kursProject:kursProjectArrayList){
                kursProjects.add(kursProject);
            }
        }
        return kursProjects;
    }

    @Override
    public ArrayList<KursProject> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return kursProjectDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }

}
