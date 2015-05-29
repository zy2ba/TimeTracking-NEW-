package ru.zy2ba.tmtrck.manager.impl;

import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.PrepodDao;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.exception.AlreadyInUseException;
import ru.zy2ba.tmtrck.exception.ValidationFailException;
import ru.zy2ba.tmtrck.manager.PrepodManager;
import ru.zy2ba.tmtrck.util.PrepodBuilder;
import ru.zy2ba.tmtrck.util.ResourceLocator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zy2ba
 * @since 05.05.2015
 */
public class PrepodManagerImpl implements PrepodManager {

    private static final Sort SORT_LASTNAME_NAME = new Sort("faculty","lastName","name");

    @Autowired(required = false)
    private
    PrepodDao prepodDao;

    @Override
    public Prepod create(Prepod entity) throws HibernateJdbcException {
        Prepod persistPrepod = prepodDao.findByNameAndLastName(entity.getName(), entity.getLastName());
        if(persistPrepod !=null && persistPrepod.getId()>-1){
            return persistPrepod;
           // entity.setId(persistPrepod.getId());
        } else
        entity = prepodDao.saveAndFlush(entity);
        return entity;
    }

    @Override
         public Prepod update(Prepod entity) throws HibernateJdbcException{
        return prepodDao.saveAndFlush(entity);
    }

    @Override
    public Prepod delete(Prepod entity) throws HibernateJdbcException {
        Prepod toBeDeletedPrepod = prepodDao. findByNameAndLastName(entity.getName(), entity.getLastName());
        if(toBeDeletedPrepod!=null && toBeDeletedPrepod.getId()>-1){
            prepodDao.delete(toBeDeletedPrepod);
        }else throw new ValidationFailException(ResourceLocator.getI18NMessage("noPrepodToDelete"));
        if(toBeDeletedPrepod != null && prepodDao.exists(toBeDeletedPrepod.getId()))
            return entity;
        return null;
    }

    @Override
    public Prepod get(long entityId) throws HibernateJdbcException{
        return prepodDao.findOne(entityId);
    }

    @Override
    public Prepod findByNameAndLastName(String name, String lastName)throws HibernateJdbcException {
        return prepodDao. findByNameAndLastName(name, lastName);
    }

    @Override
    public Prepod checkPassword(String name, String lastName, String password) throws AlreadyInUseException{
        Prepod prepod = prepodDao.findByNameAndLastName(name, lastName);
        if (prepod!=null){
            if (prepod.getPassword()==password.hashCode()){
                if(prepod.getIsWorking() && prepod.getLastActivity().plusMinutes(15).isAfter(new LocalTime())){
                    throw new AlreadyInUseException();
                }
                prepod.setIsWorking(true);
                prepod.setLastActivity();
                prepod.setPasskey();
                prepodDao.saveAndFlush(prepod);
                return prepod;
            }
        }else{
            List<Prepod> prepods = prepodDao.findAll();
            if (prepods.size()==0){
                PrepodBuilder prepodBuilder = PrepodBuilder.getPrepodBuilder();
                Prepod prepodRoot = prepodBuilder.withName("root").withLastName("root").withPassword("root").withFaculty("root").withMiddleName("root").build();
                JOptionPane.showMessageDialog(null,"Обнаружено подключение к базе данных не содержащей аккаунтов пользователей. Создан аккаунт администратора и совершён вход под его именем. Имя - root, фамилия - root, пароль - root");

                Prepod prepodGuest = prepodBuilder.withName("guest").withLastName("guest").withPassword("guest").withFaculty("guest").withMiddleName("guest").build();
                prepodRoot.setIsWorking(true);
                prepodRoot.setLastActivity();
                prepodRoot.setPasskey();
                prepodDao.saveAndFlush(prepodGuest);
                return prepodDao.saveAndFlush(prepodRoot);
            }
        }
        prepod = prepodDao.findByNameAndLastName("guest","guest");
        if (prepod==null){
            PrepodBuilder prepodBuilder = PrepodBuilder.getPrepodBuilder();
            JOptionPane.showMessageDialog(null,"В базе отсутствовал гостевой аккаунт. Данная оплошность исправлена.");
            Prepod prepodGuest = prepodBuilder.withName("guest").withLastName("guest").withPassword("guest").withFaculty("guest").withMiddleName("guest").build();
            prepodDao.saveAndFlush(prepodGuest);
        }else{
            JOptionPane.showMessageDialog(null,"Хоть искомый аккаунт и не был найден, вы всегда можете использовать гостевой аккаунт. Параметры для входа с гостевым аккаунтом: Имя - guest, фамилия - guest, пароль - guest");
        }
        return null;
    }

    @Override
    public ArrayList<Prepod> findByFaculty(String faculty) {
        return prepodDao.findByFaculty(faculty);
    }

    @Override
    public List<Prepod> getAll(){
        return prepodDao.findAll(SORT_LASTNAME_NAME);
    }
}
