package ru.zy2ba.tmtrck.manager;

import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.entity.Group2;


/**
 * @author Zy2ba
 * @since 07.05.15
 */
public interface GroupManager extends EntityManager<Group2> {
    Group2 findByName(String name)throws HibernateJdbcException;
}
