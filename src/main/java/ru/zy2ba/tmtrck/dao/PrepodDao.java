package ru.zy2ba.tmtrck.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.zy2ba.tmtrck.entity.Prepod;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Обеспечивает работу с сущностью преподаватель
 * @link Prepod
 * @See PrepodManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author Zy2ba
 *
 */

public interface PrepodDao extends JpaRepository<Prepod,Long> {
    /**
     * выводит список всех преподавателей в системе
     * @param sort /параметр по которому будут отсортированны значения
     * @return
     */
    List<Prepod> findAll(Sort sort);


    /**
     * Returns the {@link Prepod} type instance by the parameter name and lastName.
     *
     * @param name as fetching criteria.
     * @param lastName  as fetching criteria.
     * @return {@link Prepod} instance of results.
     */ 

    Prepod findByNameAndLastName(String name, String lastName);
    ArrayList<Prepod> findByFaculty(String faculty);

}