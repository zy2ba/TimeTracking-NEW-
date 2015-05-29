package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.Plan;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;

import java.util.ArrayList;

/**
 * Created by Zy2ba on 26.05.2015.
 */
public interface PlanDao extends JpaRepository<Plan,Long> {
    Plan findByPrepodAndStartYear(Prepod prepod, int startYear);
}
