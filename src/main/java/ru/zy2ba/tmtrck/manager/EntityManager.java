package ru.zy2ba.tmtrck.manager;

import org.springframework.orm.hibernate3.HibernateJdbcException;

/**
 * 
 * @author Vijitha Epa.
 * @since 26-09-12.
 * @param <T> generic Type.
 * 
 * Generic entity manager which gives generic CRUD operations.
 */
public interface EntityManager<T> {

    /**
     * сохраняет сущность в бд
     * @param entity
     * @return если сущность уже есть, вернёт сохранённые в БД данные, если нет, вернёт присланные на сохранение с проставленным айдишником
     * @throws HibernateJdbcException
     */
    T create(T entity) throws HibernateJdbcException;

    T update(T entity) throws HibernateJdbcException;

    T delete(T entity) throws HibernateJdbcException;

    T get(long entityId) throws HibernateJdbcException;
}
