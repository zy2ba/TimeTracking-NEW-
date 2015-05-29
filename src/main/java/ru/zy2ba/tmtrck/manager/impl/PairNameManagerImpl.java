package ru.zy2ba.tmtrck.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.zy2ba.tmtrck.dao.PairNameDao;
import ru.zy2ba.tmtrck.entity.PairName;
import ru.zy2ba.tmtrck.entity.enums.PairType;
import ru.zy2ba.tmtrck.manager.PairNameManager;

import org.springframework.orm.hibernate3.HibernateJdbcException;

/**
 * @author Zy2ba
 * @since 07.05.15
 */
public class PairNameManagerImpl implements PairNameManager {

@Autowired
private
PairNameDao pairNameDao;

    @Override
    public PairName findByNameAndPairType(String name, PairType pairType) throws HibernateJdbcException {
        return pairNameDao.findByNameAndPairType(name,pairType);
    }

    @Override
    public PairName create(PairName entity) throws HibernateJdbcException{
        PairName persistPairName = pairNameDao.findByNameAndPairType(entity.getName(),entity.getPairType());
        if (persistPairName!=null && persistPairName.getId()>-1){
            return persistPairName;
            //entity.setId(persistPairName.getId());
        }
        PairName pairName = pairNameDao.saveAndFlush(entity);
        return pairName;
    }

    @Override
    public PairName update(PairName entity)throws HibernateJdbcException {
        return null;
    }

    @Override
    public PairName delete(PairName entity)throws HibernateJdbcException {
        return null;
    }

    @Override
    public PairName get(long entityId)throws HibernateJdbcException {
        return null;
    }
}
