package ru.zy2ba.tmtrck.manager;

import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.entity.PairNum;
import ru.zy2ba.tmtrck.entity.enums.Week;

/**
 * @author Zy2ba
 * @since 07.05.15
 */
public interface PairNumManager extends EntityManager<PairNum>{
    PairNum findByNumAndWeek(int num, Week week) throws HibernateJdbcException;
}
