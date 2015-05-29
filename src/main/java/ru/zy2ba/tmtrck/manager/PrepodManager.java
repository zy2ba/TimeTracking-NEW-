package ru.zy2ba.tmtrck.manager;

import ru.zy2ba.tmtrck.entity.Prepod;

import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.exception.AlreadyInUseException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zy2ba
 * @since 05.05.2015
 */
public interface PrepodManager extends EntityManager<Prepod> {
    Prepod findByNameAndLastName(String name, String lastName) throws HibernateJdbcException ;
    Prepod checkPassword(String name,String lastName,String password)throws AlreadyInUseException;
    ArrayList<Prepod> findByFaculty(String faculty);
    List<Prepod> getAll();
}
