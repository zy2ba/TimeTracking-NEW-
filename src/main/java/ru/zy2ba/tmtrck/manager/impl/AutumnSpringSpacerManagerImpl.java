package ru.zy2ba.tmtrck.manager.impl;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.AutumnSpringSpacerDao;
import ru.zy2ba.tmtrck.entity.AutumnSpringSpacer;
import ru.zy2ba.tmtrck.manager.AutumnSpringSpacerManager;

/**
 * @author Zy2ba
 * @since 27.05.2015
 */
public class AutumnSpringSpacerManagerImpl implements AutumnSpringSpacerManager {
    @Autowired
    private AutumnSpringSpacerDao autumnSpringSpacerDao;

    @Override
    public AutumnSpringSpacer create(AutumnSpringSpacer entity) throws HibernateJdbcException {
        AutumnSpringSpacer persistSpacer = autumnSpringSpacerDao.findByStartYear(entity.getStartYear());
        if(persistSpacer!=null && persistSpacer.getId()>-1){
            entity.setId(persistSpacer.getId());
            autumnSpringSpacerDao.saveAndFlush(entity);
        }

        return autumnSpringSpacerDao.saveAndFlush(entity);
    }

    @Override
    public AutumnSpringSpacer update(AutumnSpringSpacer entity) throws HibernateJdbcException {
        return autumnSpringSpacerDao.saveAndFlush(entity);
    }

    @Override
    public AutumnSpringSpacer delete(AutumnSpringSpacer entity) throws HibernateJdbcException {
        autumnSpringSpacerDao.delete(entity);
        return null;
    }

    @Override
    public AutumnSpringSpacer get(long entityId) throws HibernateJdbcException {
        return autumnSpringSpacerDao.findOne(entityId);
    }

    @Override
    public AutumnSpringSpacer getByStartYear(int startYear) {
        return autumnSpringSpacerDao.findByStartYear(startYear);
    }

    @Override
    public AutumnSpringSpacer getSpacerForDate(LocalDate date) {
        int year = date.getYear();
        int lastAugustDateNumber = 243;
        if ((year%4==0&& year%100!=0)||(year%400==0) ){
                lastAugustDateNumber = 244;
        } else lastAugustDateNumber = 243;

        if (date.getDayOfYear()> lastAugustDateNumber){
            return autumnSpringSpacerDao.findByStartYear(year);
        }else return autumnSpringSpacerDao.findByStartYear(year-1);
    }
}
