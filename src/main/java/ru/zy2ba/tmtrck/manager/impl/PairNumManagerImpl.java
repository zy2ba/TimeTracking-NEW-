package ru.zy2ba.tmtrck.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.PairNumDao;
import ru.zy2ba.tmtrck.entity.PairNum;
import ru.zy2ba.tmtrck.entity.enums.Week;
import ru.zy2ba.tmtrck.manager.PairNumManager;

/**
 * @author Zy2ba
 * @since 08.05.15
 */
public class PairNumManagerImpl implements PairNumManager {

    @Autowired
    private
    PairNumDao pairNumDao;

    @Override
    public PairNum findByNumAndWeek(int num, Week week) throws HibernateJdbcException {
        return pairNumDao.findByNumAndWeek(num,week);
    }

    @Override
    public PairNum create(PairNum entity) throws HibernateJdbcException{
        PairNum persistPairNum = pairNumDao.findByNumAndWeek(entity.getNum(),entity.getWeek());
        if(persistPairNum!=null && persistPairNum.getId()>-1){
            return persistPairNum;
           // entity.setId(persistPairNum.getId());
        }
        entity = pairNumDao.saveAndFlush(entity);
        return entity;
    }

    @Override
    public PairNum update(PairNum entity) throws HibernateJdbcException{
        return null;
    }

    @Override
    public PairNum delete(PairNum entity) throws HibernateJdbcException{
        return null;
    }

    @Override
    public PairNum get(long entityId)throws HibernateJdbcException {
        return null;
    }
}
