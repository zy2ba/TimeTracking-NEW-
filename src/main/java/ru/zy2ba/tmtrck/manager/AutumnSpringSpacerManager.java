package ru.zy2ba.tmtrck.manager;

import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.AutumnSpringSpacer;

/**
 * @author Zy2ba
 * @since 27.05.2015
 */
public interface AutumnSpringSpacerManager extends EntityManager<AutumnSpringSpacer> {
    AutumnSpringSpacer getByStartYear(int startYear);
    AutumnSpringSpacer getSpacerForDate(LocalDate date);


}
