package ru.zy2ba.tmtrck.manager;

import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.entity.PairName;
import ru.zy2ba.tmtrck.entity.enums.PairType;

/**
 * @author Zy2ba
 * @since 07.05.15
 */
public interface PairNameManager extends EntityManager<PairName> {
    PairName findByNameAndPairType(String name,PairType pairType)throws HibernateJdbcException;
}
