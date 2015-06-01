package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.ActivityTypes.Exam;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 21.05.2015
 */
public interface ExamDao extends JpaRepository<Exam,Long> {
    ArrayList<Exam> findByPrepod(Prepod prepod);

    ArrayList<Exam> findByPrepodAndDate(Prepod prepod,LocalDate date);
    ArrayList<Exam> findByPrepodAndDateAndTypeOfLoad(Prepod prepod,LocalDate date,TypeOfLoad typeOfLoad);
}
