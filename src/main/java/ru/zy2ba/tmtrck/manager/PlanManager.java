package ru.zy2ba.tmtrck.manager;

import ru.zy2ba.tmtrck.entity.Plan;
import ru.zy2ba.tmtrck.entity.Prepod;

/**
 * Created by Zy2ba on 26.05.2015.
 */
public interface PlanManager extends EntityManager<Plan> {
Plan getByPrepodAndStartYear(Prepod prepod, int startYear);
}
