package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.ExamDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.DiplomaProject;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Exam;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.ExamManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public class ExamManagerImpl implements ExamManager,ActivityManager<Exam> {
    @Autowired
    private ExamDao examDao;

    @Override
    public Exam create(Exam entity) throws HibernateJdbcException {
        return examDao.saveAndFlush(entity);
    }

    @Override
    public Exam update(Exam entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Exam delete(Exam entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Exam get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {

        double sumOfHours=0;
        for (Exam exam:examDao.findByPrepod(prepod)){
            sumOfHours+=exam.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<Exam> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return examDao.findByPrepodAndDate(prepod,date);
    }

    @Override
    public ArrayList getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate) {
        ArrayList<Exam> findexamArrayList = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Exam> exams = examDao.findByPrepodAndDate(prepod, d);
            for (Exam exam:exams){
                findexamArrayList.add(exam);
            }
        }
        return findexamArrayList;
    }

    @Override
    public ArrayList<Exam> getByPrepod(Prepod prepod) {
        return examDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<Exam> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<Exam> findexamArrayList = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Exam> exams = examDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (Exam exam:exams){
                findexamArrayList.add(exam);
            }
        }
        return findexamArrayList;
    }

    @Override
    public ArrayList<Exam> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return examDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
