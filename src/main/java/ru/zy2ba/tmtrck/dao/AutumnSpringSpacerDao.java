package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.AutumnSpringSpacer;

/**
 * @author Zy2ba
 * @since 27.05.2015
 */
public interface AutumnSpringSpacerDao extends JpaRepository<AutumnSpringSpacer,Long> {
    AutumnSpringSpacer findByStartYear(int startYear);
}
