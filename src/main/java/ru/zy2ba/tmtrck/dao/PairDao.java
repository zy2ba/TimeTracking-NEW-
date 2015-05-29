package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.zy2ba.tmtrck.entity.*;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Обеспечивает работу с сущностью пара
 * @link Pair
 * @See PairManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author zy2ba.
 * @since 05.05.2015
 *
 * Data access operations related to {@link Pair}.
 */
public interface PairDao extends JpaRepository<Pair,Long>{
    /**
     * поиск препода по проведшему её преподавателю
     * @param prepod
     * @return список пар пеподавателя
     */
    ArrayList<Pair> findByPrepod(Prepod prepod);

    /**
     * Поиск дубликата пары
     * проведённость не учитывается как не ключевая
     * поскольку аудитория, закреплённая за парой может измениться
     * от расписания к расписанию, то не учитывается
     * @param group
     * @param pairDate
     * @param pairName
     * @param pairNum
     * @param prepod
     * @return возвращает пару, подходящую под ориентировку
     * если нашла, то вторую такую же пожалуй создавать не стоит
     * лучше обновить поля имеющейся
     */
    Pair findByGroup2AndPairDateAndPairNameAndPairNumAndPrepod(Group2 group, PairDate pairDate, PairName pairName, PairNum pairNum, Prepod prepod);

    /**
     * Поиск пар преподавателя на определённую дату
     * @param prepod
     * @param pairDate
     * @return список найденных пар
     */
    ArrayList<Pair> findByPrepodAndPairDate(Prepod prepod,PairDate pairDate);

    /**
     * возвращает список проведённых/не проведёенных пар
     * @param prepod
     * @param pairDate
     * @param isCarriedOut
     * @return список подходящих пар
     */
    ArrayList<Pair> findByPrepodAndPairDateAndIsCarriedOut(Prepod prepod,PairDate pairDate, boolean isCarriedOut);

    /**
     *
     * @param prepod
     * @param isCarriedOut
     * @return
     */
    ArrayList<Pair> findByPrepodAndIsCarriedOut(Prepod prepod, boolean isCarriedOut);

}
