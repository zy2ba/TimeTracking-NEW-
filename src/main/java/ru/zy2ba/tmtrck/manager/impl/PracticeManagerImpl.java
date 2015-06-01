package ru.zy2ba.tmtrck.manager.impl;

import org.hibernate.HibernateException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.PracticeDao;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Practice;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.ActivityManager;
import ru.zy2ba.tmtrck.manager.PracticeManager;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public class PracticeManagerImpl implements PracticeManager,ActivityManager<Practice> {
    @Autowired
    private PracticeDao practiceDao;

    @Override
    public Practice create(Practice entity) throws HibernateJdbcException {
        return practiceDao.saveAndFlush(entity);
    }

    @Override
    public Practice update(Practice entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Practice delete(Practice entity) throws HibernateJdbcException {
        return null;
    }

    @Override
    public Practice get(long entityId) throws HibernateJdbcException {
        return null;
    }

    @Override
    public double getNumberOf(Prepod prepod) throws HibernateException {
        double sumOfHours=0;
        for (Practice practice:practiceDao.findByPrepod(prepod)){
            sumOfHours+=practice.getHours();
        }
        return sumOfHours;
    }

    @Override
    public ArrayList<Practice> getByPrepodAndDate(Prepod prepod, LocalDate date) {
        return practiceDao.findByPrepodAndDate(prepod, date);
    }

    @Override
    public ArrayList<Practice> getByPrepodAndDate(Prepod prepod, LocalDate startDate, LocalDate finishDate) {
        ArrayList<Practice> practices = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Practice> practiceArrayList = practiceDao.findByPrepodAndDate(prepod, d);
            for (Practice practice:practiceArrayList){
                practices.add(practice);
            }
        }
        return practices;
    }

    @Override
    public ArrayList<Practice> getByPrepod(Prepod prepod) {
        return practiceDao.findByPrepod(prepod);
    }

    @Override
    public ArrayList<Practice> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate startDate, LocalDate finishDate, TypeOfLoad typeOfLoad) {
        ArrayList<Practice> practices = new ArrayList<>();

        for(LocalDate d= startDate; !d.equals(finishDate.plusDays(1));d=d.plusDays(1) ){
            ArrayList<Practice> practiceArrayList = practiceDao.findByPrepodAndDateAndTypeOfLoad(prepod, d,typeOfLoad);
            for (Practice practice:practiceArrayList){
                practices.add(practice);
            }
        }
        return practices;
    }

    @Override
    public ArrayList<Practice> getByPrepodDateTypeOfLoad(Prepod prepod, LocalDate date, TypeOfLoad typeOfLoad) {
        return practiceDao.findByPrepodAndDateAndTypeOfLoad(prepod,date,typeOfLoad);
    }
}
