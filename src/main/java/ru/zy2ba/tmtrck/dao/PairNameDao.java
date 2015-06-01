package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.PairName;
import ru.zy2ba.tmtrck.entity.enums.PairType;

import java.util.List;

/**
 * Обеспечивает работу с сущностью название предмета
 * @link PairName
 * @See PairNameManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author Zy2ba
 * @since 05.05.15
 */
public interface PairNameDao extends JpaRepository<PairName,Long> {
    /**
     * ищет пару по названию предмета
     * @param name
     * @return список пар данного предмета
     */
    List<PairName> findByName(String name);

    /**
     * позволяет найти нужную запись PairName для подстановки
     * @param name
     * @param pairType
     * @return одну пару
     */
    PairName findByNameAndPairType(String name, PairType pairType);
}
