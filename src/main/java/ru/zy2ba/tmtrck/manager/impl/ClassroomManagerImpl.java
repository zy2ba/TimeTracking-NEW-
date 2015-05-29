package ru.zy2ba.tmtrck.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.zy2ba.tmtrck.dao.ClassroomDao;
import ru.zy2ba.tmtrck.entity.Classroom;
import ru.zy2ba.tmtrck.exception.ValidationFailException;
import ru.zy2ba.tmtrck.manager.ClassroomManager;
import ru.zy2ba.tmtrck.util.ResourceLocator;

import org.springframework.orm.hibernate3.HibernateJdbcException;

/**
 * @author Zy2ba
 * @since 06.05.15
 */
public class ClassroomManagerImpl implements ClassroomManager {
    @Autowired
    private ClassroomDao classroomDao;

    @Override
    public Classroom create(Classroom entity) throws HibernateJdbcException {//create
        Classroom persistClassroom = classroomDao.findByBuildingAndNum(entity.getBuilding(),entity.getNum());
        if (persistClassroom!=null && persistClassroom.getId()>-1){
            return  persistClassroom;
           // entity.setId(persistClassroom.getId());
        }else {

        Classroom savedClassroom = classroomDao.saveAndFlush(entity);
        return savedClassroom;}
    }

    @Override
    public Classroom get(long entityId) throws HibernateJdbcException{//read
        return classroomDao.findOne(entityId);
    }

    @Override
    public Classroom update(Classroom entity) throws HibernateJdbcException{//update
        return classroomDao.saveAndFlush(entity);
    }

    @Override
    public Classroom delete(Classroom entity) throws HibernateJdbcException{//delete
        Classroom toBeDeletedClassroom = classroomDao.findByBuildingAndNum(entity.getBuilding(),entity.getNum());
        if(toBeDeletedClassroom!=null && toBeDeletedClassroom.getId()>-1){
            classroomDao.delete(toBeDeletedClassroom);
        }else throw new ValidationFailException(ResourceLocator.getI18NMessage("noPrepodToDelete"));
        if (toBeDeletedClassroom !=null && classroomDao.exists(toBeDeletedClassroom.getId()))
            return entity;
        return null;
    }

    @Override
    public Classroom findByBuildingAndNum(Integer building, Integer num) throws HibernateJdbcException{
        return classroomDao.findByBuildingAndNum(building, num);
    }
}
