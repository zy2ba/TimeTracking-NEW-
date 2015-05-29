package ru.zy2ba.tmtrck.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.PlanTableDao;
import ru.zy2ba.tmtrck.entity.PlanTable;
import ru.zy2ba.tmtrck.manager.PlanTableManager;

/**
 * Created by Zy2ba on 27.05.2015.
 */
public class PlanTableManagerImpl implements PlanTableManager {
    @Autowired
    private PlanTableDao planTableDao;

    @Override
    public PlanTable create(PlanTable entity) throws HibernateJdbcException {
        return planTableDao.saveAndFlush(entity);
    }

    @Override
    public PlanTable update(PlanTable entity) throws HibernateJdbcException {
        return planTableDao.saveAndFlush(entity);
    }

    @Override
    public PlanTable delete(PlanTable entity) throws HibernateJdbcException {
        planTableDao.delete(entity.getId());
        return null;
    }

    @Override
    public PlanTable get(long entityId) throws HibernateJdbcException {
        return planTableDao.findOne(entityId);
    }
}
