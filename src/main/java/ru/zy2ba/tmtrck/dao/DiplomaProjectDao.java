package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.ActivityTypes.DiplomaProject;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 21.05.2015.
 */
public interface DiplomaProjectDao extends JpaRepository<DiplomaProject,Long> {
    ArrayList<DiplomaProject> findByPrepod(Prepod prepod);

    ArrayList<DiplomaProject> findByPrepodAndDate(Prepod prepod,LocalDate date);
    ArrayList<DiplomaProject> findByPrepodAndDateAndTypeOfLoad(Prepod prepod,LocalDate date,TypeOfLoad typeOfLoad);
}
