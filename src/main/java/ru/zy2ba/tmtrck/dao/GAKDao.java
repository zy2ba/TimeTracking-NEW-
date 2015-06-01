package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.ActivityTypes.GAK;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 29.05.2015
 */
public interface GAKDao extends JpaRepository<GAK,Long> {
    ArrayList<GAK> findByPrepod(Prepod prepod);
    ArrayList<GAK> findByPrepodAndDate(Prepod prepod,LocalDate date);
    ArrayList<GAK> findByPrepodAndDateAndTypeOfLoad(Prepod prepod,LocalDate date,TypeOfLoad typeOfLoad);


}
