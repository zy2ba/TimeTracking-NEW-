package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Practice;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public interface PracticeDao  extends JpaRepository<Practice,Long> {
    ArrayList<Practice> findByPrepod(Prepod prepod);
    ArrayList<Practice> findByPrepodAndDate(Prepod prepod,LocalDate date);
    ArrayList<Practice> findByPrepodAndDateAndTypeOfLoad(Prepod prepod,LocalDate date,TypeOfLoad typeOfLoad);
}
