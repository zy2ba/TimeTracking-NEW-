package ru.zy2ba.tmtrck.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.Group2Dao;
import ru.zy2ba.tmtrck.entity.Group2;
import ru.zy2ba.tmtrck.manager.GroupManager;

/**
 * @author Zy2ba
 * @since 05.05.2015
 */
public class GroupManagerImpl implements GroupManager{

    @Autowired
    private
    Group2Dao group2Dao;

    @Override
    public Group2 findByName(String name)throws HibernateJdbcException {
        return group2Dao.findByName(name);
    }

    @Override
    public Group2 create(Group2 entity) throws HibernateJdbcException{
        Group2 persistGroup = group2Dao.findByName(entity.getName());
        if (persistGroup!=null && persistGroup.getId()>-1){
            return persistGroup;
            //entity.setId(persistGroup.getId());
            //return entity;
        }
     //   Group2 group = group2Dao.saveAndFlush(entity);
        return group2Dao.saveAndFlush(entity);
    }

    @Override
    public Group2 update(Group2 entity)throws HibernateJdbcException {
        return group2Dao.saveAndFlush(entity);
    }

    @Override
    public Group2 delete(Group2 entity)throws HibernateJdbcException {
        return null;
    }

    @Override
    public Group2 get(long entityId)throws HibernateJdbcException {
        return group2Dao.findOne(entityId);
    }
}
