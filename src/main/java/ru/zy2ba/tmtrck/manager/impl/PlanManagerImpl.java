package ru.zy2ba.tmtrck.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.PlanDao;
import ru.zy2ba.tmtrck.entity.Plan;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.manager.PlanManager;
import ru.zy2ba.tmtrck.manager.PlanTableManager;
import ru.zy2ba.tmtrck.util.ResourceLocator;

/**
 * Created by Zy2ba on 26.05.2015.
 */
public class PlanManagerImpl implements PlanManager {
    @Autowired
    private PlanDao planDao;

    @Override
    public Plan getByPrepodAndStartYear(Prepod prepod, int startYear) throws HibernateJdbcException {
        return planDao.findByPrepodAndStartYear(prepod, startYear);
    }

    @Override
    public Plan create(Plan entity) throws HibernateJdbcException {
        Plan persistPlans = planDao.findByPrepodAndStartYear(entity.getPrepod(), entity.getStartYear());
        if(persistPlans!=null && persistPlans.getId()>-1){
            return persistPlans;
        }
        PlanTableManager planTableManager = (PlanTableManager) ResourceLocator.getBean("planTableManager");
        entity.setPlanTableAutumnBudget(planTableManager.create(entity.getPlanTableAutumnBudget()));
        entity.setPlanTableSpringBudget(planTableManager.create(entity.getPlanTableSpringBudget()));
        entity = planDao.saveAndFlush(entity);
        return entity;
    }

    @Override
    public Plan update(Plan entity) throws HibernateJdbcException {
        PlanTableManager planTableManager = (PlanTableManager) ResourceLocator.getBean("planTableManager");
        planTableManager.update(entity.getPlanTableSpringBudget());
        planTableManager.update(entity.getPlanTableAutumnBudget());
        return planDao.saveAndFlush(entity);
    }

    @Override
    public Plan delete(Plan entity) throws HibernateJdbcException {
        PlanTableManager planTableManager = (PlanTableManager) ResourceLocator.getBean("planTableManager");
        planTableManager.delete(entity.getPlanTableSpringBudget());
        planTableManager.delete(entity.getPlanTableAutumnBudget());
        planDao.delete(entity);
        return null;
    }

    @Override
    public Plan get(long entityId) throws HibernateJdbcException {
        return planDao.findOne(entityId);
    }
}
