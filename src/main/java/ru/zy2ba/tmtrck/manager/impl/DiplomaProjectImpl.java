package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.DiplomaProjectDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.DiplomaProject;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.DiplomaProjectManager;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 21.05.2015
 */
public class DiplomaProjectImpl implements DiplomaProjectManager,ActivityManager<DiplomaProject> {

    @Autowired
    private DiplomaProjectDao diplomaProjectDao;

    @Override
    public DiplomaProject create(DiplomaProject entity) throws HibernateJdbcException {
        return diplomaProjectDao.saveAndFlush(entity);
    }

    @Override
    public DiplomaProject update(DiplomaProject entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public DiplomaProject delete(DiplomaProject entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public DiplomaProject get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {
        double sumOfHours=0;
        for (DiplomaProject diplomaProject:diplomaProjectDao.findByPrepod(prepod)){
            sumOfHours+=diplomaProject.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<DiplomaProject> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return diplomaProjectDao.findByPrepodAndDate(prepod,date);
    }

    @Override
    public ArrayList<DiplomaProject> getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate) {
        ArrayList<DiplomaProject> findDiplomaProjects = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<DiplomaProject> diplomaProjectsOnDate = diplomaProjectDao.findByPrepodAndDate(prepod, d);
            for (DiplomaProject diplomaProject:diplomaProjectsOnDate){
                findDiplomaProjects.add(diplomaProject);
            }
        }
        return findDiplomaProjects;
    }

    @Override
    public ArrayList<DiplomaProject> getByPrepod(Prepod prepod) {
        return diplomaProjectDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<DiplomaProject> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<DiplomaProject> findDiplomaProjects = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<DiplomaProject> diplomaProjectsOnDate = diplomaProjectDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (DiplomaProject diplomaProject:diplomaProjectsOnDate){
                findDiplomaProjects.add(diplomaProject);
            }
        }
        return findDiplomaProjects;
    }

    @Override
    public ArrayList<DiplomaProject> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return diplomaProjectDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
