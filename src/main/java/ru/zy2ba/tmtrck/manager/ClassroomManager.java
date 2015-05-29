package ru.zy2ba.tmtrck.manager;

import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.entity.Classroom;

/**
 * @author Zy2ba
 * @since 05.05.15
 */
public interface ClassroomManager extends EntityManager<Classroom> {

    Classroom findByBuildingAndNum(Integer building,Integer num)throws HibernateJdbcException;
}
