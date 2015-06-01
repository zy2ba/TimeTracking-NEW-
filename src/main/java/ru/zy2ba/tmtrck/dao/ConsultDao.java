package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Consult;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;


/**
 * @author Zy2ba
 * @since 21.05.2015
 */
public interface ConsultDao extends JpaRepository<Consult,Long> {
    ArrayList<Consult> findByPrepod(Prepod prepod);
    ArrayList<Consult> findByPrepodAndDate(Prepod prepod,LocalDate date);
    ArrayList<Consult> findByPrepodAndDateAndTypeOfLoad(Prepod prepod,LocalDate date,TypeOfLoad typeOfLoad);
}
