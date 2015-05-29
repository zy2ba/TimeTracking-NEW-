package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Zachet;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 29.05.2015.
 */
public interface ZachetDao extends JpaRepository<Zachet,Long> {
    ArrayList<Zachet> findByPrepod(Prepod prepod);
    ArrayList<Zachet> findByPrepodAndDate(Prepod prepod,LocalDate date);
    ArrayList<Zachet> findByPrepodAndDateAndTypeOfLoad(Prepod prepod,LocalDate date,TypeOfLoad typeOfLoad);
}

